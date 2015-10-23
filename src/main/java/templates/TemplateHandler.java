package templates;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cardswithfriends.DBHandler;
import cardswithfriends.KingsCorner;
import cardswithfriends.Player;
import cardswithfriends.User;
import cardswithfriends.views.KingsCornerView;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

public class TemplateHandler {
	private static final String HOME_TEMPLATE = "home.mustache";
	private static final String LOGIN_TEMPLATE = "login.mustache";
	private static final String REGISTER_TEMPLATE = "register.mustache";
	private static final String CREATE_GAME_TEMPLATE = "createGame.mustche";
	private static final String FRIENDS_TEMPLATE = "friends.mustache";
	private static final String FRIEND_INFO_TEMPLATE = "friendInfo.mustache";
	private static final String GAME_LIST_TEMPLATE = "gameList.mustache";
	private static final String KINGS_CORNERS_TEMPLATE = "kingsCorners.mustache";
	private static final String LEADERBOARD_TEMPLATE = "leaderboard.mustache";
	private static final String TUTORIAL_TEMPLATE = "tutorial.mustache";

	public static void registerTemplates(){
		get("/", 			(rq, rs) -> renderHome(rq, rs), 		new MustacheTemplateEngine());
        get("/login", 		(rq, rs) -> renderLogin(rq, rs), 		new MustacheTemplateEngine());
        get("/logout", 		(rq, rs) -> logout(rq, rs), 			new MustacheTemplateEngine());
        get("/register", 	(rq, rs) -> renderRegister(rq, rs), 	new MustacheTemplateEngine());
        get("/new", 		(rq, rs) -> renderCreateGame(rq, rs), 	new MustacheTemplateEngine());
        get("/game/:id", 	(rq, rs) -> renderGame(rq, rs), 		new MustacheTemplateEngine());
        get("/friends", 	(rq, rs) -> renderFriends(rq, rs), 		new MustacheTemplateEngine());
        get("/games", 		(rq, rs) -> renderGameList(rq, rs), 	new MustacheTemplateEngine());
        get("/tutorial", 	(rq, rs) -> renderTutorial(rq, rs), 	new MustacheTemplateEngine());
        get("/leaderboard", (rq, rs) -> renderLeaderboard(rq, rs), 	new MustacheTemplateEngine());
        before((rq, rs) -> {
        	String path = rq.pathInfo();
            if (requiresAuthentication(path) && rq.cookie("id") == null) {
            	rs.redirect("/");
            }
        });
        //TODO
        post("/login", (rq, rs) -> postLogin(rq, rs), 	new MustacheTemplateEngine());
        post("/register", (rq, rs) -> postRegister(rq, rs), new MustacheTemplateEngine());
        /*
        post("/login")
        post("/new")
        post("/makeMove")
        post("/turn")
        
        post("/removeFriend")
        post("/addFriend")
        get("/friends/:name")
        get("/friends/search/:name")
        
        6.6.12 private static ModelAndView postLogin(Request rq, Response rs) 
		Checks the login and if it is valid will redirect to the gameList otherwise it will re-render the login template.
		6.6.13 private static ModelAndView postCreateGame(Request rq, Response rs) 
		Will create a new game with the players/parameters passed into the post.
		6.6.14 private static ModelAndView postMove(Request rq, Response rs)
		Attempt to create and validate a move from the request parameters. Re-renders the game with an update state or an error.
		6.6.15 private static ModelAndView postTurn(Request rq, Response rs)
		Attempt to post an entire turn and move the game forward by switching turns and saving game state. Re-renders the game view.
		6.6.16 private static ModelAndView postRegister(Request rq, Response rs) 
		Attempts to create a new user if the username is already taken will display an error otherwise will redirect to the login page.
		6.6.17 private static ModelAndView postRemoveFriend(Request rq, Response rs) 
		Attempts to remove a friend and will return to the friends management page.
		6.6.18 private static ModelAndView postAddFriend(Request rq, Response rs) 
		Attempts to add a friend and will return to the friends managment page with success/failure information.
		6.6.19 private static ModelAndView renderFriendInfo(Request rq, Response rs) 
		Renders a page of info about a friend containing how many game won against that friend.
        */
	}

	private static boolean requiresAuthentication(String path) {
		if(	path == null || 
			path.equals("/") || 
			path.equals("/login") || 
			path.equals("/register") || 
			path.equals("/tutorial")){
			return false;
		}
		return true;
	}


	private static ModelAndView postLogin(Request rq, Response rs) {
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		User user = DBHandler.getUserByEmail(email);
		if(checkLogin(user, password)){
			if(user == null){
				//TODO remove once we are saving users.
				rs.cookie("id", "10");
			} else {
				rs.cookie("id", user.getPlayerId().toString());
			}
			return renderHome(rq, rs);
		} else {
			// "There was an error. Try again.";
			return renderLogin(rq, rs);
		}
	}
	
	private static ModelAndView postRegister(Request rq, Response rs) {
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		String passwordAgain = rq.queryParams("password-again");
		
		if(DBHandler.getUserByEmail(email) != null){
			//Error email already in use.
			return renderRegister(rq, rs);
		}
		
		if(!password.equals(passwordAgain)){
			//Error passwords don't match
			return renderRegister(rq, rs);
		}
		
		//Make the new user
		User newUser = new User(0, email);
		DBHandler.createUser(newUser);
		return renderLogin(rq, rs);
	}

	private static boolean checkLogin(User user, String password) {
		if(user == null || user.checkPassword(password)){
			//return false
		}		
		return true;
	}

	private static ModelAndView renderHome(Request rq, Response rs) {
		return new ModelAndView(new HashMap<String, String>(), HOME_TEMPLATE);
	}
	
	private static ModelAndView renderLogin(Request rq, Response rs) {
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("email", email);
		info.put("password", password);
		return new ModelAndView(info, LOGIN_TEMPLATE);
	}
	
	private static ModelAndView logout(Request rq, Response rs) {
		// TODO Auto-generated method stub
		rs.redirect("/");
		return null;
	}

	private static ModelAndView renderRegister(Request rq, Response rs) {
		// TODO Auto-generated method stub
		return new ModelAndView(new HashMap<String, String>(), REGISTER_TEMPLATE);
	}
	
	private static ModelAndView renderTutorial(Request rq, Response rs) {
		// TODO Auto-generated method stub
		return new ModelAndView(new HashMap<String, String>(), TUTORIAL_TEMPLATE);
	}

	private static ModelAndView renderGameList(Request rq, Response rs) {
		// real thing 
		//List<KingsCorner> games = DBHandler.getKCGamesforUser(Integer.parseInt(rq.cookie("id")));
		//testing
		List<Player> players = new LinkedList<Player>();
		players.add(new User(10, "sdlfkjsd"));
		KingsCorner game1 = new KingsCorner(1, players);
		HashMap<String, Object> info = new HashMap<String, Object>();
		List<KingsCornerView> games = new LinkedList<KingsCornerView>();
		games.add(new KingsCornerView(game1));
		info.put("games", games);
		return new ModelAndView(info, GAME_LIST_TEMPLATE);
	}

	private static ModelAndView renderFriends(Request rq, Response rs) {
		// TODO Auto-generated method stub
		return new ModelAndView(new HashMap<String, String>(), FRIENDS_TEMPLATE);
	}

	private static ModelAndView renderGame(Request rq, Response rs) {
		// TODO Auto-generated method stub
		return new ModelAndView(new HashMap<String, String>(), KINGS_CORNERS_TEMPLATE);
	}

	private static ModelAndView renderCreateGame(Request rq, Response rs) {
		// TODO Auto-generated method stub
		return new ModelAndView(new HashMap<String, String>(), CREATE_GAME_TEMPLATE);
	}
	
	private static ModelAndView renderLeaderboard(Request rq, Response rs) {
		// TODO Auto-generated method stub
		return new ModelAndView(new HashMap<String, String>(), LEADERBOARD_TEMPLATE);
	}

}
