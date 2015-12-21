package greycirclegames.frontend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.frontend.views.CirclesView;
import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.board.circles.CirclesArtificialPlayer;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class CirclesHandler extends TemplateHandler{

	protected static ModelAndView postCreateGame(Request rq, Response rs) {
		List<Player> players = new LinkedList<Player>();
		
		// Add the user that created the game as the first player
		players.add(getUserFromCookies(rq));
		
		// Add the selected friends to the player list
		if(rq.queryMap("friends").hasValue()){
			Arrays.stream(rq.queryMap("friends").values())
					.map(e -> DBHandler.getUser(Integer.parseInt(e)))
					.forEach(e -> players.add(e));
		}
		
		if(rq.queryParams("ai") != null){
			players.add(new CirclesArtificialPlayer(-1));
		}
		
		// Create the game
		Circles game = new Circles(DBHandler.getNextGameID(), players);
		DBHandler.createCirclesGame(game);
		rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "The game was created.");
		rs.redirect(GAMES_ROUTE);
		return getModelAndView(null, GAME_LIST_TEMPLATE, rq, rs);
	}

	protected static ModelAndView renderGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();		
		int gameId = getGameId(rq);
		if(gameId < 0){
			info.put("game", null);
			rs.header(GlobalConstants.DISPLAY_ERROR, "Game not found.");
			return getModelAndView(info, CIRCLES_TEMPLATE, rq, rs);
		}
		CirclesView view = new CirclesView(DBHandler.getCirclesGame(gameId), getUserFromCookies(rq));
		info.put("game", view);
		return getModelAndView(info, CIRCLES_TEMPLATE, rq, rs);
	}

}
