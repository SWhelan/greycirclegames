package cardswithfriends;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import cardswithfriends.Card.Suit;
import cardswithfriends.views.GameView;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

/**
 * 
 * Registers the urls with a function/method to render the appropriate template.
 * 
 * Handles the Request/Response cycle for all registered urls.
 * 
 * @author Sarah Whelan
 *
 */
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
		// Ensure they are logged in or the url is public/doesn't require login
        before((rq, rs) -> {
        	String path = rq.pathInfo();
            if (requiresAuthentication(path) && !isLoggedIn(rq)) {
            	rs.redirect("/");
            }
        });
        
        // Handle the GET requests by rendering the mustache template
		get("/", 			(rq, rs) -> renderHome(rq, rs), 		new MustacheTemplateEngine());
		get("/register", 	(rq, rs) -> renderRegister(rq, rs), 	new MustacheTemplateEngine());
        get("/login", 		(rq, rs) -> renderLogin(rq, rs), 		new MustacheTemplateEngine());
        get("/games", 		(rq, rs) -> renderGameList(rq, rs), 	new MustacheTemplateEngine());
        get("/game/:id",	(rq, rs) -> renderGame(rq, rs), 		new MustacheTemplateEngine());
        get("/new", 		(rq, rs) -> renderCreateGame(rq, rs), 	new MustacheTemplateEngine());
        get("/friends", 	(rq, rs) -> renderFriends(rq, rs), 		new MustacheTemplateEngine());
        get("/friends/:id", (rq, rs) -> renderFriendInfo(rq, rs), new MustacheTemplateEngine());
        get("/tutorial", 	(rq, rs) -> renderTutorial(rq, rs), 	new MustacheTemplateEngine());
        get("/leaderboard", (rq, rs) -> renderLeaderboard(rq, rs), 	new MustacheTemplateEngine());
        get("/logout", 		(rq, rs) -> logout(rq, rs));
        
        // Handle the POST requests
        post("/register", (rq, rs) -> postRegister(rq, rs), new MustacheTemplateEngine());
        post("/login", (rq, rs) -> postLogin(rq, rs), 	new MustacheTemplateEngine());
        post("/new", (rq, rs) -> postCreateGame(rq, rs), new MustacheTemplateEngine());        
        post("/game/:id/move", (rq, rs) -> postMove(rq, rs), new MustacheTemplateEngine());
        post("/game/:id/turn", (rq, rs) -> postTurn(rq, rs), new MustacheTemplateEngine());
        post("/addFriend", (rq, rs) -> postAddFriend(rq, rs), new MustacheTemplateEngine());
        post("/removeFriend", (rq, rs) -> postRemoveFriend(rq, rs), new MustacheTemplateEngine());
        
        // Throw an error on 404s
        get("/*", (rq, rs) -> {
        	// main.css is the only public static file currently
        	// would need to handle this better once more static files are added
        	if(!rq.pathInfo().contains("main.css")){
        		throw new Exception();
        	} else {
        		return null;
        	}
        });
        
        // Catch the 404 errors and redirect to home page
        exception(Exception.class, (e, rq, rs) -> {
            rs.status(404);
            rs.redirect("/");
        });
	}
	
	private static ModelAndView renderHome(Request rq, Response rs) {
		if(rq.cookie(GlobalConstants.USER_COOKIE_KEY) != null){
			return renderGameList(rq, rs);
		}
		return getModelAndView(new HashMap<String, Object>(), HOME_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderRegister(Request rq, Response rs) {
		return getModelAndView(new HashMap<String, Object>(), REGISTER_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderLogin(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		info.put("email", email);
		info.put("password", password);
		return getModelAndView(info, LOGIN_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderGameList(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("games", DBHandler.getKCGamesforUser(getUserIdFromCookies(rq)));
		return getModelAndView(info, GAME_LIST_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		int gameId = Integer.parseInt(rq.params(":id"));
		info.put("game", new GameView(DBHandler.getKCGame(gameId), getUserFromCookies(rq)));
		return getModelAndView(info, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderCreateGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("friends", getFriendsFromDB(getUserIdFromCookies(rq)));
		return getModelAndView(info, CREATE_GAME_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderFriends(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("friends", getFriendsFromDB(getUserIdFromCookies(rq)));
		return getModelAndView(info, FRIENDS_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderFriendInfo(Request rq, Response rs) {
		List<Player> friends = getFriendsFromDB(getUserIdFromCookies(rq))
								.stream()
								.filter(e -> e.get_id() == Integer.parseInt(rq.params(":id")))
								.collect(Collectors.toList());
		if(friends.isEmpty()){
			rs.header(GlobalConstants.DISPLAY_ERROR, "You are not friends with the requested user or the requested user does not exist. If you have just sent them a friend request they may have not accepted yet.");
			return renderFriends(rq, rs);
		}
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("friend", friends.get(0));
		return getModelAndView(info, FRIEND_INFO_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderTutorial(Request rq, Response rs) {
		return getModelAndView(new HashMap<String, Object>(), TUTORIAL_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderLeaderboard(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		Leaderboard board = DBHandler.getLeadboard();
		info.put("board", board);
		return getModelAndView(info, LEADERBOARD_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView logout(Request rq, Response rs) {
		rs.removeCookie(GlobalConstants.USER_COOKIE_KEY);
		rs.redirect("/");
		return null;
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
	
	private static boolean checkLogin(User user, String password) {
		if(user == null || !user.passwordMatches(password)){
			return false;
		}		
		return true;
	}
	
	private static ModelAndView postCreateGame(Request rq, Response rs) {
		Integer numAiPlayers = Integer.parseInt(rq.queryParams("ai"));		
		List<Player> players = new LinkedList<Player>();
		
		// Add the user that created the game as the first player
		players.add(getUserFromCookies(rq));
		
		// Add the selected friends to the player list
		Arrays.stream(rq.queryMap("friends").values())
				.map(e -> DBHandler.getUser(Integer.parseInt(e)))
				.forEach(e -> players.add(e));
		
		// Add the specified number of AI players to the list
		for(int i = 0; i < numAiPlayers; i++){
			players.add(new ArtificialPlayer(i * -1));
		}
		
		// Create the game
		KingsCorner game = new KingsCorner(DBHandler.getNextKCGameID(), players);
		DBHandler.createKCGame(game);
		return renderGameList(rq, rs);
	}
	
	private static ModelAndView postMove(Request rq, Response rs) {
		String pile = rq.queryParams("pile");
		Integer gameId = Integer.parseInt(rq.queryParams("gameId"));
		KingsCorner game = DBHandler.getKCGame(gameId);
		Player player = getUserFromCookies(rq);
		Pile moving = new Pile("moving");
		Pile destination;
		Pile origin;
		if(rq.queryParams("pile2") == null){
			// We are moving a player's card onto a game pile
			Integer number = Integer.parseInt(rq.queryParams("number"));
			String suit = rq.queryParams("suit");
			moving.add(new Card(number, Suit.valueOf(suit)));
			destination = game.getGameState().piles.get(Integer.parseInt(pile));
			origin = game.getGameState().userHands.get(player.get_id());
		} else {
			// We are moving a game pile to another game pile
			Pile pile1 = game.getGameState().piles.get(Integer.parseInt(pile));
			String pile2 = rq.queryParams("pile2");
			destination = game.getGameState().piles.get(Integer.parseInt(pile2));
			origin = pile1;
			moving.addAll(pile1);
		}
		Move move = new KCMove(player, origin, moving, destination);
		if(game.applyMove(move)){
			rs.header(GlobalConstants.DISPLAY_SUCCESS, "Move was valid and applied succefully.");
		} else {
			rs.header(GlobalConstants.DISPLAY_ERROR, "Move was invalid and and not applied.");
		}
		return renderGame(rq, rs);
	}
	
	private static ModelAndView postTurn(Request rq, Response rs) {
		int gameId = Integer.parseInt(rq.queryParams("gameId"));
		KingsCorner game = DBHandler.getKCGame(gameId);
		game.endTurn();
		rs.header(GlobalConstants.DISPLAY_SUCCESS, "Your turn has ended.");
		DBHandler.updateKCGame(game);
		return renderGame(rq, rs);
	}

	private static ModelAndView postAddFriend(Request rq, Response rs){
		String searchValue = rq.queryParams("searchValue");
		Player user2 = DBHandler.getUserByEmail(searchValue);
		if(user2 == null){
			user2 = DBHandler.getUserByUserName(searchValue);
		}
		if(user2 == null){
			rs.header(GlobalConstants.DISPLAY_ERROR, "No user was found with that search.");
		} else {
			if(!DBHandler.addFriend(getUserIdFromCookies(rq), user2.get_id())){
				rs.header(GlobalConstants.DISPLAY_ERROR, "There was an error adding that friend.");
			} else if(!DBHandler.addFriend(user2.get_id(), getUserIdFromCookies(rq))){
				rs.header(GlobalConstants.DISPLAY_ERROR, "There was an error adding that friend.");
			} else {
				rs.header(GlobalConstants.DISPLAY_SUCCESS, "The requested friend has been added.");
			}
		}
		return renderFriends(rq, rs);
	}
	
	private static ModelAndView postRemoveFriend(Request rq, Response rs){
		Integer friendToRemoveId = Integer.parseInt(rq.queryParams("friendId"));
		Player user2 = DBHandler.getUser(friendToRemoveId);
		if(user2 == null){
			rs.header(GlobalConstants.DISPLAY_ERROR, "No user was found with that id to remove.");
		} else {
			if(DBHandler.removeFriend(getUserFromCookies(rq).get_id(), user2.get_id())){
				rs.header(GlobalConstants.DISPLAY_SUCCESS, "Friend was successfully removed.");
			} else {
				rs.header(GlobalConstants.DISPLAY_ERROR, "There was an error removing that friend.");
			}
		}
		return renderFriends(rq, rs);
	}
	
	/* Utilities */
	
	// Does the attempted url require the user to be logged in?
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
	
	// Is the user logged in?
	private static boolean isLoggedIn(Request rq){
		return rq.cookie(GlobalConstants.USER_COOKIE_KEY) != null;
	}
	
	/*
	 *  Get the user id from the cookies
	 *  as each url that would use this ensures that the user is logged in
	 *  ie has a cookie this parse int won't fail
	 */
	private static int getUserIdFromCookies(Request rq){
		return Integer.parseInt(rq.cookie(GlobalConstants.USER_COOKIE_KEY));
	}
	
	/*
	 * Same as get user id but then also get the user with that id
	 * form the database
	 */
	private static User getUserFromCookies(Request rq){
		return DBHandler.getUser(getUserIdFromCookies(rq));
	}
	
	// The database returns a list of ids get the list of Players
	private static List<Player> getFriendsFromDB(int userId){
		return DBHandler.getFriendsForUser(userId)
					.stream()
					.map(e -> DBHandler.getUser((Integer)e))
					.collect(Collectors.toList());
	}
	
	/**
	 * 
	 * A way to display success and failure messages to the user without
	 * repeating this code in every above method.
	 * 
	 * To set a success message add a header to the response:
	 * rs.header(GlobalConstants.DISPLAY_SUCCESS, "Success!");
	 * 
	 * where the second param is the message
	 * same for errors/failures:
	 * rs.header(GlobalConstants.DISPLAY_ERROR, "Error!");
	 * 
	 * @param info the HashMap of string to object that should be passed to the UI
	 * @param templateName file name of template to render
	 * @param rq HTTP request
	 * @param rs HTTP response with headers set for success/failure messages
	 * @return a ModelAndView for the specified template and HashMap with the messages added.
	 * A ModelAndView is passed to the Mustache Rendering Engine to render the response via
	 * the template.
	 */
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
