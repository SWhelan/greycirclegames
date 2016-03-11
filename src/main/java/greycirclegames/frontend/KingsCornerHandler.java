package greycirclegames.frontend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.User;
import greycirclegames.frontend.views.KingsCornerView;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Card.Suit;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCMove;
import greycirclegames.games.card.kingscorner.KingsCorner;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class KingsCornerHandler extends TemplateHandler{
	protected static ModelAndView renderGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();		
		int gameId = getGameId(rq);
		User user = getUserFromCookies(rq);
		user.removeGameNotifications(gameId);
		DBHandler.updateUser(user);
		if(gameId < 0){
			info.put("game", null);
			rs.header(GlobalConstants.DISPLAY_ERROR, "Game not found.");
			return getModelAndView(info, KINGS_CORNERS_TEMPLATE, rq, rs);
		}
		info.put("game", new KingsCornerView(DBHandler.getKCGame(gameId), getUserFromCookies(rq)));
		return getModelAndView(info, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postCreateGame(Request rq, Response rs) {
		Integer numAiPlayers = Integer.parseInt(rq.queryParams("ai"));		
		List<Integer> players = new ArrayList<Integer>();
		
		// Add the user that created the game as the first player
		players.add(getUserIdFromCookies(rq));
		
		// Add the selected friends to the player list
		if(rq.queryMap("friends").hasValue()){
			Arrays.stream(rq.queryMap("friends").values())
					.forEach(e -> players.add(Integer.parseInt(e)));
		}
		
		if(players.size() + numAiPlayers > GlobalConstants.MAX_PLAYERS){
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "The game may not have more than " + Integer.toString(GlobalConstants.MAX_PLAYERS) + " players.");
			rs.redirect(CREATE_GAME_ROUTE);
			return getModelAndView(null, CREATE_GAME_TEMPLATE, rq, rs);
		}
		
		// Add the specified number of AI players to the list
		for(int i = 1; i < numAiPlayers + 1; i++){
			players.add(i * -1);
		}
		
		return createKCGame(players, rq, rs);
	}
	
	protected static ModelAndView postMove(Request rq, Response rs) {
		String pile = rq.queryParams("pile");
		String gameIdString = rq.queryParams("gameId");
		Integer gameId = Integer.parseInt(gameIdString);
		KingsCorner game = DBHandler.getKCGame(gameId);
		Integer playerId = getUserIdFromCookies(rq);
		Pile moving = new Pile("moving");
		Pile destination;
		Pile origin;
		if(rq.queryParams("pile2") == null){
			// We are moving a player's card onto a game pile
			Integer number = Integer.parseInt(rq.queryParams("number"));
			String suit = rq.queryParams("suit");
			moving.add(new Card(number, Arrays.stream(Suit.values())
											.filter(e -> e.getDisplayName().equals(suit))
											.collect(Collectors.toList())
											.get(0)));
			destination = game.getGameState().piles.get(getPileKeyFromString(pile));
			origin = game.getGameState().userHands.get(Integer.toString(playerId));
		} else {
			// We are moving a game pile to another game pile
			Pile pile1 = game.getGameState().piles.get(getPileKeyFromString(pile));
			String pile2 = rq.queryParams("pile2");
			destination = game.getGameState().piles.get(getPileKeyFromString(pile2));
			origin = pile1;
			moving.addAll(pile1);
		}
		KCMove move = new KCMove(playerId, origin, moving, destination);
		if(moving.size() > 0 && game.applyMove(move)){
			if(game.getIsActive()){
				rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Move was valid and applied successfully.");
			}
		} else {
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "Move was invalid and not applied.");
		}
		
		if(game.getIsActive() && game.players.get(game.currentPlayerIndex) < 0){
	        game.applyAIMoves();
		}
		
		DBHandler.updateKCGame(game);
		rs.redirect(KINGS_CORNER_ROUTE + "/" + gameIdString);
		return getModelAndView(null, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postTurn(Request rq, Response rs) {
		String gameIdString = rq.queryParams("gameId");
		int gameId = Integer.parseInt(gameIdString);
		KingsCorner game = DBHandler.getKCGame(gameId);
		game.endTurn();
		if(game.applyAIMoves()){
			// It was an AI Player's turn
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Your turn ended. Computer Player(s) played.");
		} else {
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Your turn has ended.");
		}
		DBHandler.updateKCGame(game);
		rs.redirect(KINGS_CORNER_ROUTE + "/" + gameIdString);
		return getModelAndView(null, KINGS_CORNERS_TEMPLATE, rq, rs);
	}

	protected static ModelAndView createKCGame(List<Integer> players, Request rq, Response rs) {
		KingsCorner game = new KingsCorner(DBHandler.getNextGameID(), players);
		DBHandler.createKCGame(game);
		rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "The game was created. It is your move first.");
		rs.redirect(KINGS_CORNER_ROUTE + "/" + Integer.toString(game.get_id()));
		return getModelAndView(null, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
}
