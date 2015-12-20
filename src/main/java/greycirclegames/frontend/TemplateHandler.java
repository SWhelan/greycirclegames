package greycirclegames.frontend;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.NotFoundException;
import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.frontend.views.GameView;
import greycirclegames.frontend.views.KingsCornerListView;
import greycirclegames.frontend.views.LeaderboardView;
import greycirclegames.games.kingscorner.ArtificialPlayer;
import greycirclegames.games.kingscorner.Card;
import greycirclegames.games.kingscorner.Card.Suit;
import greycirclegames.games.kingscorner.KCMove;
import greycirclegames.games.kingscorner.KingsCorner;
import greycirclegames.games.kingscorner.Move;
import greycirclegames.games.kingscorner.Pile;
import greycirclegames.games.kingscorner.PileIds;
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
	public static final String HOME_ROUTE = "/";
	public static final String LOGIN_ROUTE = "/login";
	public static final String REGISTER_ROUTE = "/register";
	public static final String CREATE_GAME_ROUTE = "/new";
	public static final String FRIENDS_ROUTE = "/friends";
	public static final String FRIENDS_ADD_ROUTE = "/addFriend";
	public static final String FRIENDS_REMOVE_ROUTE = "/removeFriend";
	public static final String GAMES_ROUTE = "/games";
	public static final String LEADERBOARD_ROUTE = "/leaderboard";
	public static final String TUTORIAL_ROUTE = "/tutorial";
	public static final String LOGOUT_ROUTE = "/logout";
	public static final String POST_MOVE_ROUTE = "/move";
	public static final String POST_TURN_ROUTE = "/turn";
	
	// Prepend Games
	public static final String CIRCLES_ROUTE = "/circles";
	public static final String KINGS_CORNER_ROUTE = "/kingscorner";
	
	public static final String PUBLIC_ROUTE = "/public";
	
	private static final String HOME_TEMPLATE = "home.mustache";
	private static final String LOGIN_TEMPLATE = "login.mustache";
	private static final String REGISTER_TEMPLATE = "register.mustache";
	private static final String CREATE_GAME_TEMPLATE = "createGame.mustache";
	private static final String FRIENDS_TEMPLATE = "friends.mustache";
	private static final String FRIEND_INFO_TEMPLATE = "friendInfo.mustache";
	public static final String GAME_LIST_TEMPLATE = "gameList.mustache";
	private static final String KINGS_CORNERS_TEMPLATE = "kingscorners.mustache";
	private static final String LEADERBOARD_TEMPLATE = "leaderboard.mustache";
	private static final String TUTORIAL_TEMPLATE = "tutorial.mustache";

	public static void registerTemplates(){
		// Ensure they are logged in or the url is public/doesn't require login
        before((rq, rs) -> {
        	rs.removeCookie(GlobalConstants.DISPLAY_SUCCESS);
        	rs.removeCookie(GlobalConstants.DISPLAY_ERROR);
        	String path = rq.pathInfo();
            if (requiresAuthentication(path) && !isLoggedIn(rq)) {
            	rs.redirect("/");
            }
        });
        
        // Handle the GET requests by rendering the mustache template
        get(HOME_ROUTE, 			(rq, rs) -> renderHome(rq, rs), 		new MustacheTemplateEngine());
        get(REGISTER_ROUTE, 		(rq, rs) -> renderRegister(rq, rs), 	new MustacheTemplateEngine());
        get(LOGIN_ROUTE, 			(rq, rs) -> renderLogin(rq, rs), 		new MustacheTemplateEngine());
        get(GAMES_ROUTE, 			(rq, rs) -> renderGameList(rq, rs), 	new MustacheTemplateEngine());
        get(GAMES_ROUTE + "/:id",	(rq, rs) -> renderGame(rq, rs), 		new MustacheTemplateEngine());
        get(CREATE_GAME_ROUTE, 		(rq, rs) -> renderCreateGame(rq, rs), 	new MustacheTemplateEngine());
        get(FRIENDS_ROUTE, 			(rq, rs) -> renderFriends(rq, rs), 		new MustacheTemplateEngine());
        get(FRIENDS_ROUTE + "/:id", (rq, rs) -> renderFriendInfo(rq, rs), 	new MustacheTemplateEngine());
        get(TUTORIAL_ROUTE, 		(rq, rs) -> renderTutorial(rq, rs), 	new MustacheTemplateEngine());
        get(LEADERBOARD_ROUTE, 		(rq, rs) -> renderLeaderboard(rq, rs), 	new MustacheTemplateEngine());
        get(LOGOUT_ROUTE, 			(rq, rs) -> logout(rq, rs));
        
        // Handle the POST requests
        post(REGISTER_ROUTE, 		(rq, rs) -> postRegister(rq, rs), 		new MustacheTemplateEngine());
        post(LOGIN_ROUTE, 			(rq, rs) -> postLogin(rq, rs), 			new MustacheTemplateEngine());
        post(CREATE_GAME_ROUTE, 	(rq, rs) -> postCreateGame(rq, rs), 	new MustacheTemplateEngine());        
        post(POST_MOVE_ROUTE, 		(rq, rs) -> postMove(rq, rs), 			new MustacheTemplateEngine());
        post(POST_TURN_ROUTE, 		(rq, rs) -> postTurn(rq, rs), 			new MustacheTemplateEngine());
        post(FRIENDS_ADD_ROUTE, 	(rq, rs) -> postAddFriend(rq, rs), 		new MustacheTemplateEngine());
        post(FRIENDS_REMOVE_ROUTE, 	(rq, rs) -> postRemoveFriend(rq, rs),	new MustacheTemplateEngine());
        
        post(CIRCLES_ROUTE + CREATE_GAME_ROUTE, (rq, rs) -> CirclesHandler.postCreateGame(rq, rs), new MustacheTemplateEngine());
        
        // Throw an error on 404s
        get("/*", (rq, rs) -> {
        	if(!rq.pathInfo().startsWith(PUBLIC_ROUTE)){
        		throw new NotFoundException();
        	} else {
        		return null;
        	}
        });
        
        // Catch the 404 errors and redirect to home page
        exception(NotFoundException.class, (e, rq, rs) -> {
    		rs.status(404);
    		rs.redirect(HOME_ROUTE);
        });
        
        // Catch other errors return an internal server error and redirect
        exception(Exception.class, (e, rq, rs) -> {
        	e.printStackTrace();
        	rs.status(500);
        	rs.redirect(HOME_ROUTE);
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
		info.put("games", new KingsCornerListView(getUserIdFromCookies(rq)));
		return getModelAndView(info, GAME_LIST_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();		
		int gameId;
		try {
			gameId = Integer.parseInt(rq.queryParams("gameId"));
		} catch (NumberFormatException e){
			
		}
		
		try {
			gameId = Integer.parseInt(rq.params(":id"));
		} catch (NumberFormatException e){
			info.put("game", null);
			rs.header(GlobalConstants.DISPLAY_ERROR, "Game not found.");
			return getModelAndView(info, KINGS_CORNERS_TEMPLATE, rq, rs);
		}
		info.put("game", new GameView(DBHandler.getKCGame(gameId), getUserFromCookies(rq)));
		return getModelAndView(info, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView renderCreateGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		List<Player> friends = getFriendsFromDB(getUserIdFromCookies(rq));
		info.put("friends", friends);
		info.put("hasFriends", !friends.isEmpty());
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
		info.put("entries", (new LeaderboardView()).entries);
		return getModelAndView(info, LEADERBOARD_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView logout(Request rq, Response rs) {
		rs.removeCookie(GlobalConstants.USER_COOKIE_KEY);
		rs.redirect(HOME_ROUTE);
		return getModelAndView(null, HOME_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView postRegister(Request rq, Response rs) {
		String email = rq.queryParams("email").toLowerCase();
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
		rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "New user successfully created.");
		rs.redirect(LOGIN_ROUTE);
		return getModelAndView(null, REGISTER_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView postLogin(Request rq, Response rs) {
		String email = rq.queryParams("email").toLowerCase();
		String password = rq.queryParams("password");
		User user = DBHandler.getUserByEmail(email);
		if(checkLogin(user, password)){
			rs.cookie(GlobalConstants.USER_COOKIE_KEY, Integer.toString(user.get_id()));
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Logged in successfully.");
			rs.redirect(GAMES_ROUTE);
		}
		rs.header(GlobalConstants.DISPLAY_ERROR, "Your username or password is incorrect.");
		return renderLogin(rq, rs);
	}
	
	private static ModelAndView postCreateGame(Request rq, Response rs) {
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
			players.add(new ArtificialPlayer(i * -1));
		}
		
		// Create the game
		KingsCorner game = new KingsCorner(DBHandler.getNextGameID(), players);
		DBHandler.createKCGame(game);
		rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "The game was created.");
		rs.redirect(GAMES_ROUTE);
		return getModelAndView(null, GAME_LIST_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView postMove(Request rq, Response rs) {
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
		Move move = new KCMove(player, origin, moving, destination);
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
		rs.redirect(GAMES_ROUTE + "/" + gameIdString);
		return getModelAndView(null, KINGS_CORNERS_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView postTurn(Request rq, Response rs) {
		String gameIdString = rq.queryParams("gameId");
		int gameId = Integer.parseInt(gameIdString);
		KingsCorner game = DBHandler.getKCGame(gameId);
		game.endTurn();
		if(game.applyAIMoves()){
			// It was an AI Player's turn
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Your turn ended. AI Player(s) played.");
		} else {
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Your turn has ended.");
		}
		DBHandler.updateKCGame(game);
		rs.redirect(GAMES_ROUTE + "/" + gameIdString);
		return getModelAndView(null, KINGS_CORNERS_TEMPLATE, rq, rs);
	}

	private static ModelAndView postAddFriend(Request rq, Response rs){
		String searchValue = rq.queryParams("searchValue").toLowerCase();
		Player user2 = DBHandler.getUserByEmail(searchValue);
		if(user2 == null){
			user2 = DBHandler.getUserByUserName(searchValue);
		}
		if(user2 == null){
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "No user was found with that search.");
		} else if (user2.get_id() == getUserIdFromCookies(rq)){
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "You can not add your self as a friend.");
		} else if(alreadyFriends(getUserIdFromCookies(rq), user2.get_id())){
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "You are already friends with " + user2.getUserName());
		} else {
			if(!DBHandler.addFriend(getUserIdFromCookies(rq), user2.get_id())){
				rs.cookie(GlobalConstants.DISPLAY_ERROR, "There was an error adding that friend.");
			} else if(!DBHandler.addFriend(user2.get_id(), getUserIdFromCookies(rq))){
				rs.cookie(GlobalConstants.DISPLAY_ERROR, "There was an error adding that friend.");
			} else {
				rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "The requested friend has been added.");
			}
		}
		rs.redirect(FRIENDS_ROUTE);
		return getModelAndView(null, FRIENDS_TEMPLATE, rq, rs);
	}
	
	private static ModelAndView postRemoveFriend(Request rq, Response rs){
		Integer friendToRemoveId = Integer.parseInt(rq.queryParams("friendId"));
		Player user2 = DBHandler.getUser(friendToRemoveId);
		if(user2 == null){
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "No user was found with that id to remove.");
		} else {
			if(!DBHandler.removeFriend(getUserIdFromCookies(rq), user2.get_id())){
				rs.cookie(GlobalConstants.DISPLAY_ERROR, "There was an error removing that friend.");
			} else if (!DBHandler.removeFriend(user2.get_id(), getUserIdFromCookies(rq))){
				rs.cookie(GlobalConstants.DISPLAY_ERROR, "There was an error removing that friend.");
			} else {
				rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Friend was successfully removed.");
			}
		}
		rs.redirect(FRIENDS_ROUTE);
		return getModelAndView(null, FRIENDS_TEMPLATE, rq, rs);
	}
	
	/* Utilities */
	
	// Does the attempted url require the user to be logged in?
	private static boolean requiresAuthentication(String path) {
		if(	path == null || 
			path.equals(HOME_ROUTE) || 
			path.equals(LOGIN_ROUTE) || 
			path.equals(REGISTER_ROUTE) || 
			path.equals(TUTORIAL_ROUTE)){
			return false;
		}
		return true;
	}
	
	// Is the user logged in?
	private static boolean isLoggedIn(Request rq){
		return rq.cookie(GlobalConstants.USER_COOKIE_KEY) != null;
	}
	
	// Is the login/password valid for the found user?
	private static boolean checkLogin(User user, String password) {
		if(user == null || !user.passwordMatches(password)){
			return false;
		}		
		return true;
	}
	
	/*
	 *  Get the user id from the cookies
	 *  as each url that would use this ensures that the user is logged in
	 *  ie has a cookie this parse int won't fail
	 */
	public static int getUserIdFromCookies(Request rq){
		try {
			return Integer.parseInt(rq.cookie(GlobalConstants.USER_COOKIE_KEY));
		} catch (NumberFormatException e){
			return -1;
		}
	}
	
	/*
	 * Same as get user id but then also get the user with that id
	 * form the database
	 */
	public static User getUserFromCookies(Request rq){
		return DBHandler.getUser(getUserIdFromCookies(rq));
	}
	
	// Is the user with userId already friends with friendId?
	public static boolean alreadyFriends(int userId, int friendId){
		List<Player> friends = getFriendsFromDB(userId);
		return friends.stream().anyMatch(e -> e.get_id() == friendId);
	}
	
	// The database returns a list of ids get the list of Players
	public static List<Player> getFriendsFromDB(int userId){
		return DBHandler.getFriendsForUser(userId)
					.stream()
					.map(e -> DBHandler.getUser((Integer)e))
					.collect(Collectors.toList());
	}
	
	// Get the key for the pile HashMap from the string name of the pile
	public static String getPileKeyFromString(String name){
		return Integer.toString(
				Arrays.stream(PileIds.values())
				.filter(e -> e.name().equals(name))
				.collect(Collectors.toList())
				.get(0)
				.ordinal());
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
	public static ModelAndView getModelAndView(HashMap<String, Object> info, String templateName, Request rq, Response rs){
		if(info == null){
			return new ModelAndView(new HashMap<String, Object>(), templateName);
		}
		if(isLoggedIn(rq)){
			info.put("loggedIn", true);
			info.put("userName", getUserFromCookies(rq).getUserName());
		}
		if(rs.raw().containsHeader(GlobalConstants.DISPLAY_ERROR)){
			info.put(GlobalConstants.DISPLAY_ERROR, rs.raw().getHeader(GlobalConstants.DISPLAY_ERROR));
		}
		if(rs.raw().containsHeader(GlobalConstants.DISPLAY_SUCCESS)){
			info.put(GlobalConstants.DISPLAY_SUCCESS, rs.raw().getHeader(GlobalConstants.DISPLAY_SUCCESS));
		}
		
		if(rq.cookie(GlobalConstants.DISPLAY_ERROR) != null){
			info.put(GlobalConstants.DISPLAY_ERROR, rq.cookie(GlobalConstants.DISPLAY_ERROR));
			rs.removeCookie(GlobalConstants.DISPLAY_ERROR);
		}
		if(rq.cookie(GlobalConstants.DISPLAY_SUCCESS) != null){
			info.put(GlobalConstants.DISPLAY_SUCCESS, rq.cookie(GlobalConstants.DISPLAY_SUCCESS));
			rs.removeCookie(GlobalConstants.DISPLAY_SUCCESS);
		}
		
		info.put("HOME_ROUTE", HOME_ROUTE);
		info.put("LOGIN_ROUTE", LOGIN_ROUTE);
		info.put("REGISTER_ROUTE", REGISTER_ROUTE);
		info.put("CREATE_GAME_ROUTE", CREATE_GAME_ROUTE);
		info.put("FRIENDS_ROUTE", FRIENDS_ROUTE);
		info.put("FRIENDS_ADD_ROUTE", FRIENDS_ADD_ROUTE);
		info.put("FRIENDS_REMOVE_ROUTE", FRIENDS_REMOVE_ROUTE);
		info.put("GAMES_ROUTE", GAMES_ROUTE);
		info.put("LEADERBOARD_ROUTE", LEADERBOARD_ROUTE);
		info.put("TUTORIAL_ROUTE", TUTORIAL_ROUTE);
		info.put("LOGOUT_ROUTE", LOGOUT_ROUTE);
		info.put("POST_MOVE_ROUTE", POST_MOVE_ROUTE);
		info.put("POST_TURN_ROUTE", POST_TURN_ROUTE);
		info.put("CIRCLES_ROUTE", CIRCLES_ROUTE);
		info.put("KINGS_CORNER_ROUTE", KINGS_CORNER_ROUTE);
		
		return new ModelAndView(info, templateName);
	}

}
