package greycirclegames.frontend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import greycirclegames.DBHandler;
import greycirclegames.EmailHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.frontend.views.KingsCornerView;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Card.Suit;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCArtificialPlayer;
import greycirclegames.games.card.kingscorner.KCMove;
import greycirclegames.games.card.kingscorner.KingsCorner;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class KingsCornerHandler extends TemplateHandler{
	protected static ModelAndView renderGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();		
		int gameId = getGameId(rq);
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
		List<Player> players = new LinkedList<Player>();
		
		// Add the user that created the game as the first player
		players.add(getUserFromCookies(rq));
		
		// Add the selected friends to the player list
		if(rq.queryMap("friends").hasValue()){
			Arrays.stream(rq.queryMap("friends").values())
					.map(e -> DBHandler.getUser(Integer.parseInt(e)))
					.forEach(e -> players.add(e));
		}
		
		if(players.size() + numAiPlayers > GlobalConstants.MAX_PLAYERS){
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "The game may not have more than " + Integer.toString(GlobalConstants.MAX_PLAYERS) + " players.");
			rs.redirect(CREATE_GAME_ROUTE);
			return getModelAndView(null, CREATE_GAME_TEMPLATE, rq, rs);
		}
		
		// Add the specified number of AI players to the list
		for(int i = 1; i < numAiPlayers + 1; i++){
			players.add(new KCArtificialPlayer(i * -1));
		}
		
		// Create the game
		KingsCorner game = new KingsCorner(DBHandler.getNextGameID(), players);
		DBHandler.createKCGame(game);
		EmailHandler.sendNewGameIfWanted(players, game.getGameTypeIdentifier(), KINGS_CORNER_ROUTE + "/" + Integer.toString(game.get_id()), getUserFromCookies(rq));
		rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "The game was created. It is your move first.");
		rs.redirect(KINGS_CORNER_ROUTE + "/" + Integer.toString(game.get_id()));
		return getModelAndView(null, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postMove(Request rq, Response rs) {
		String pile = rq.queryParams("pile");
		String gameIdString = rq.queryParams("gameId");
		Integer gameId = Integer.parseInt(gameIdString);
		KingsCorner game = DBHandler.getKCGame(gameId);
		Player player = getUserFromCookies(rq);
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
			origin = game.getGameState().userHands.get(Integer.toString(player.get_id()));
		} else {
			// We are moving a game pile to another game pile
			Pile pile1 = game.getGameState().piles.get(getPileKeyFromString(pile));
			String pile2 = rq.queryParams("pile2");
			destination = game.getGameState().piles.get(getPileKeyFromString(pile2));
			origin = pile1;
			moving.addAll(pile1);
		}
		KCMove move = new KCMove(player, origin, moving, destination);
		if(game.applyMove(move)){
			if(game.getIsActive()){
				rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Move was valid and applied successfully.");
			}
		} else {
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "Move was invalid and not applied.");
		}
		
		if(game.getIsActive() && KingsCorner.isAI(game.getCurrentPlayerObject())){
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
		if(!game.gameIsOver()){
			EmailHandler.sendTurnMailIfWanted(game.getPlayers(), game.getCurrentPlayerObject(), game.getGameTypeIdentifier(), KINGS_CORNER_ROUTE + "/" + game.get_id());
		} else {
			EmailHandler.sendGameOverMailIfWanted(game.getPlayers(), game.getGameTypeIdentifier(), KINGS_CORNER_ROUTE + "/" + game.get_id(), game.getWinner());
		}
		rs.redirect(KINGS_CORNER_ROUTE + "/" + gameIdString);
		return getModelAndView(null, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
}
