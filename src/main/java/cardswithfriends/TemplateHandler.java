package cardswithfriends;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cardswithfriends.views.GameView;
import cardswithfriends.views.KingsCornerView;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

public class TemplateHandler {
	private static final String HOME_TEMPLATE = "home.mustache";
	private static final String LOGIN_TEMPLATE = "login.mustache";
	private static final String REGISTER_TEMPLATE = "register.mustache";
	private static final String CREATE_GAME_TEMPLATE = "createGame.mustache";
	private static final String FRIENDS_TEMPLATE = "friends.mustache";
	private static final String FRIEND_INFO_TEMPLATE = "friendInfo.mustache";
	private static final String GAME_LIST_TEMPLATE = "gameList.mustache";
	private static final String KINGS_CORNERS_TEMPLATE = "kingsCorners.mustache";
	private static final String LEADERBOARD_TEMPLATE = "leaderboard.mustache";
	private static final String TUTORIAL_TEMPLATE = "tutorial.mustache";

	public static void registerTemplates(){
		get("/", 			(rq, rs) -> renderHome(rq, rs), 		new MustacheTemplateEngine());
        get("/login", 		(rq, rs) -> renderLogin(rq, rs), 		new MustacheTemplateEngine());
        get("/logout", 		(rq, rs) -> logout(rq, rs));
        get("/register", 	(rq, rs) -> renderRegister(rq, rs), 	new MustacheTemplateEngine());
        get("/new", 		(rq, rs) -> renderCreateGame(rq, rs), 	new MustacheTemplateEngine());
        get("/game/:id",	(rq, rs) -> renderGame(rq, rs), 		new MustacheTemplateEngine());
        get("/friends", 	(rq, rs) -> renderFriends(rq, rs), 		new MustacheTemplateEngine());
        get("/games", 		(rq, rs) -> renderGameList(rq, rs), 	new MustacheTemplateEngine());
        get("/tutorial", 	(rq, rs) -> renderTutorial(rq, rs), 	new MustacheTemplateEngine());
        get("/leaderboard", (rq, rs) -> renderLeaderboard(rq, rs), 	new MustacheTemplateEngine());
        before((rq, rs) -> {
        	String path = rq.pathInfo();
            if (requiresAuthentication(path) && !isLoggedIn(rq)) {
            	rs.redirect("/");
            }
        });
        post("/login", (rq, rs) -> postLogin(rq, rs), 	new MustacheTemplateEngine());
        post("/register", (rq, rs) -> postRegister(rq, rs), new MustacheTemplateEngine());
        post("/makeMove", (rq, rs) -> postMove(rq, rs), new MustacheTemplateEngine());
        post("/new", (rq, rs) -> postCreateGame(rq, rs), new MustacheTemplateEngine());
        post("/addFriend", (rq, rs) -> postAddFriend(rq, rs), new MustacheTemplateEngine());
        post("/removeFriend", (rq, rs) -> postRemoveFriend(rq, rs), new MustacheTemplateEngine());
        get("/friends/:id", (rq, rs) -> renderFriendInfo(rq, rs), new MustacheTemplateEngine());
        post("/turn", (rq, rs) -> postTurn(rq, rs), new MustacheTemplateEngine());
        get("/*", (rq, rs) -> {
        	if(!rq.pathInfo().contains("main.css")){
        		throw new Exception();
        	} else {
        		return null;
        	}
        });
        exception(Exception.class, (e, rq, rs) -> {
            rs.status(404);
            rs.redirect("/");
        });
	}
	
	private static ModelAndView postTurn(Request rq, Response rs) {
		// TODO Auto-generated method stub
		//DBHandler.getKCGame(gameID);
		rs.header(GlobalConstants.DISPLAY_SUCCESS, "Your turn has ended.");
		return renderGame(rq, rs);
	}

	private static ModelAndView renderFriendInfo(Request rq, Response rs) {
		/*List<Player> friends = DBHandler.getFriendsForUser(getUserIdFromCookies(rq));
		Player friend = null;
		for(Player p : friends){
			if(p.get_id() == Integer.parseInt(rq.params(":id"))){
				friend = p;
			}
		}
		if(friend == null){
			rs.header(GlobalConstants.DISPLAY_ERROR, "You are not friends with the requested user or the requested user does not exist. If you have just sent them a friend request they may have not accepted yet.");
		}*/
		Player friend = new User(4, "Friend 4");
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("friend", friend);
		return getModelAndView(info, FRIEND_INFO_TEMPLATE, rq, rs);
	}

	private static ModelAndView postAddFriend(Request rq, Response rs){
		String searchValue = rq.queryParams("searchValue");
		Player user2 = DBHandler.getUserByEmail(searchValue);
		if(user2 == null){
			rs.header(GlobalConstants.DISPLAY_ERROR, "No user was found with that search.");
		} else {
			//TODO Are we just going to let people add friends or send emails to accept? Probably need an intermediary pending status as well.
			DBHandler.addFriend(getUserFromCookies(rq), user2);
			rs.header(GlobalConstants.DISPLAY_SUCCESS, "A friend request was sent to that user.");
		}
		return renderFriends(rq, rs);
	}
	
	private static ModelAndView postRemoveFriend(Request rq, Response rs){
		Integer friendToRemoveId = Integer.parseInt(rq.queryParams("friendId"));
		Player user2 = DBHandler.getUser(friendToRemoveId);
		if(user2 == null){
			rs.header(GlobalConstants.DISPLAY_ERROR, "No user was found with that id to remove.");
		} else {
			if(DBHandler.removeFriend(getUserFromCookies(rq), user2)){
				rs.header(GlobalConstants.DISPLAY_SUCCESS, "Friend was successfully removed.");
			} else {
				rs.header(GlobalConstants.DISPLAY_ERROR, "There was an error removing that friend.");
			}
		}
		return renderFriends(rq, rs);
	}

	private static ModelAndView postCreateGame(Request rq, Response rs) {
		List<Player> players = new LinkedList<Player>();
		players.add(getUserFromCookies(rq));
		players.add(new ArtificialPlayer(-1));
		KingsCorner game = new KingsCorner(DBHandler.getNextKCGameID(), players);
		DBHandler.createKCGame(game);
		return renderGameList(rq, rs);
	}

	private static ModelAndView postMove(Request rq, Response rs) {
	/* This is the real stuff:
	 * 	Integer number = Integer.parseInt(rq.queryParams("number"));
		String suit = rq.queryParams("suit");
		String pile = rq.queryParams("pile");
		Integer gameId = Integer.parseInt(rq.queryParams("gameId"));
		KingsCorner game = DBHandler.getKCGame(gameId);
		Pile destination = game.getGameState().piles.get(Integer.parseInt(pile));
		Pile moving = new Pile("moving");
		moving.add(new Card(number, Suit.valueOf(suit)));
		Player player = getUserFromCookies(rq);
		Pile origin = game.getGameState().userHands.get(player.get_id());
		Move move = new KCMove(player, origin, moving, destination);
		if(game.applyMove(move)){*/
			rs.header(GlobalConstants.DISPLAY_SUCCESS, "Move was valid and applied succefully.");
		//} else {
			//rs.header(GlobalConstants.DISPLAY_ERROR, "Move was invalid and and not applied.");
		//}
		return renderGame(rq, rs);
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
			rs.cookie(GlobalConstants.USER_COOKIE_KEY, Integer.toString(user.get_id()));
			rs.redirect("/games");
		}
		rs.header(GlobalConstants.DISPLAY_ERROR, "Your username or password is incorrect.");
		return renderLogin(rq, rs);
	}
	
	private static boolean isLoggedIn(Request rq){
		return rq.cookie(GlobalConstants.USER_COOKIE_KEY) != null;
	}
	
	private static int getUserIdFromCookies(Request rq){
		return Integer.parseInt(rq.cookie(GlobalConstants.USER_COOKIE_KEY));
	}
	
	private static User getUserFromCookies(Request rq){
		return DBHandler.getUser(getUserIdFromCookies(rq));
	}
	
	private static ModelAndView postRegister(Request rq, Response rs) {
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		String passwordAgain = rq.queryParams("password-again");
		User user = DBHandler.getUserByEmail(email); 
		if(user != null){
			rs.header(GlobalConstants.DISPLAY_ERROR, "Email already in use.");
			return renderRegister(rq, rs);
		}
		
		if(!password.equals(passwordAgain)){
			rs.header(GlobalConstants.DISPLAY_ERROR, "Passwords do not match.");
			return renderRegister(rq, rs);
		}
		
		User newUser = new User(DBHandler.getNextUserID(), email);
		String salt = User.generateSalt();
		newUser.setSalt(salt);
		newUser.setPassword(User.hashPassword(salt, password));
		newUser.setUserName(email);
		DBHandler.createUser(newUser);
		rs.header(GlobalConstants.DISPLAY_SUCCESS, "New user succesfully created.");
		return renderLogin(rq, rs);
	}

	private static boolean checkLogin(User user, String password) {
		if(user == null || !user.passwordMatches(password)){
			return false;
		}		
		return true;
	}

	private static ModelAndView renderHome(Request rq, Response rs) {
		if(rq.cookie(GlobalConstants.USER_COOKIE_KEY) != null){
			return renderGameList(rq, rs);
		}
		return getModelAndView(new HashMap<String, Object>(), HOME_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderLogin(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		info.put("email", email);
		info.put("password", password);
		return getModelAndView(info, LOGIN_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView logout(Request rq, Response rs) {
		rs.removeCookie(GlobalConstants.USER_COOKIE_KEY);
		rs.redirect("/");
		return null;
	}

	private static ModelAndView renderRegister(Request rq, Response rs) {
		return getModelAndView(new HashMap<String, Object>(), REGISTER_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderTutorial(Request rq, Response rs) {
		return getModelAndView(new HashMap<String, Object>(), TUTORIAL_TEMPLATE, rq, rs);
	}

	private static ModelAndView renderGameList(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		// real thing 
		//List<KingsCorner> games = DBHandler.getKCGamesforUser(getUserIdFromCookies(rq));
		// fake thing
		List<Player> players = new LinkedList<Player>();
		players.add(getUserFromCookies(rq));
		players.add(new User(10, "opponent1@test.com"));
		players.add(new User(11, "opponent2@test.com"));
		KingsCorner game1 = new KingsCorner(1, players);
		KingsCorner game2 = new KingsCorner(2, players);
		KingsCorner game3 = new KingsCorner(3, players);
		List<KingsCornerView> games = new LinkedList<KingsCornerView>();
		games.add(new KingsCornerView(game1));
		games.add(new KingsCornerView(game2));
		games.add(new KingsCornerView(game3));
		info.put("games", games);
		return getModelAndView(info, GAME_LIST_TEMPLATE, rq, rs);
	}
	


	private static ModelAndView renderFriends(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		List<Player> players = DBHandler.getFriendsForUser(getUserIdFromCookies(rq));
		players.add(new User(10, "A really good friend"));
		players.add(new User(12, "Also like an okay friend"));
		players.add(new User(3, "Friend 3"));
		players.add(new User(4, "Friend 4"));
		info.put("friends", players);
		return getModelAndView(info, FRIENDS_TEMPLATE, rq, rs);
	}

	private static ModelAndView renderGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		List<Player> players = new LinkedList<Player>();
		/*int gameId;
		try {
			gameId = Integer.parseInt(rq.queryParams("gameId"));
		} catch (NumberFormatException e){
			gameId = Integer.parseInt(rq.params(":id"));
		}*/
		//info.put("game", new GameView(DBHandler.getKCGame(gameId), getUserFromCookies(rq)));
		User user = new User(20, "sdlfkjsd");
		players.add(user);
		players.add(getUserFromCookies(rq));
		players.add(new User(11, "asdfadsfkj"));
		KingsCorner game1 = new KingsCorner(1, players);
		
    	KCGameState gs = (KCGameState) game1.getGameState();
    	
    	Pile spoof = new Pile("Spoof North Pile");
    	spoof.addOn(Card.make(8, Card.Suit.CLUB));
    	gs.piles.put(Integer.toString(PileIds.NORTH_PILE.ordinal()), spoof);
    	Pile user0Hand = gs.userHands.get(Integer.toString(players.get(0).get_id()));
    	Card toMove = Card.make(7, Card.Suit.DIAMOND);
    	if(!user0Hand.contains(toMove)){
    		user0Hand.add(toMove);
    	}
    	
    	Pile moving = new Pile("Moving Pile");
    	moving.add(toMove);
    	
    	Move move = new KCMove(players.get(0), user0Hand, moving, spoof);
    	
    	game1.applyMove(move);
		
		
		
		
		info.put("game", new GameView(game1, getUserFromCookies(rq)));
		return getModelAndView(info, KINGS_CORNERS_TEMPLATE, rq, rs);
	}

	private static ModelAndView renderCreateGame(Request rq, Response rs) {
		//TODO fill out this template
		return getModelAndView(new HashMap<String, Object>(), CREATE_GAME_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderLeaderboard(Request rq, Response rs) {
		// TODO real things
		HashMap<String, Object> info = new HashMap<String, Object>();
		List<Player> players = new LinkedList<Player>();
		players.add(new User(3, "ImNumberOne@test.com"));
		players.add(new User(4, "two@email.com"));
		players.add(new User(5, "thirdistheonewiththetreasurechest@gmail.com"));
		info.put("players", players);
		return getModelAndView(info, LEADERBOARD_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView getModelAndView(HashMap<String, Object> info, String templateName, Request rq, Response rs){
		if(isLoggedIn(rq)){
			info.put("loggedIn", true);
		}
		if(rs.raw().containsHeader(GlobalConstants.DISPLAY_ERROR)){
			info.put(GlobalConstants.DISPLAY_ERROR, rs.raw().getHeader(GlobalConstants.DISPLAY_ERROR));
		}
		if(rs.raw().containsHeader(GlobalConstants.DISPLAY_SUCCESS)){
			info.put(GlobalConstants.DISPLAY_SUCCESS, rs.raw().getHeader(GlobalConstants.DISPLAY_SUCCESS));
		}
		return new ModelAndView(info, templateName);
	}

}
