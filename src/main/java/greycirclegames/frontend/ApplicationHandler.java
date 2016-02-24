package greycirclegames.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import greycirclegames.ArtificialPlayer;
import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.NotificationAndEmailHandler;
import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.frontend.views.FriendView;
import greycirclegames.frontend.views.GameListView;
import greycirclegames.frontend.views.UserView;
import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.card.kingscorner.KingsCorner;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ApplicationHandler extends TemplateHandler {
	
	private static final int MAX_AGE = 3600 * 24 * 365 * 10;

	protected static ModelAndView renderHome(Request rq, Response rs) {
		if(TemplateHandler.isLoggedIn(rq)){
			return renderGameList(rq, rs);
		}
		return getModelAndView(new HashMap<String, Object>(), HOME_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderRegister(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		String username = rq.queryParams("username");
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		String passwordAgain = rq.queryParams("password-again");
		info.put("username", username);
		info.put("email", email);
		info.put("password", password);
		info.put("passwordAgain", passwordAgain);
		return getModelAndView(info, REGISTER_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderLogin(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		String username = rq.queryParams("username");
		String password = rq.queryParams("password");
		info.put("username", username);
		info.put("password", password);
		return getModelAndView(info, LOGIN_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderTutorial(Request rq, Response rs) {
		return getModelAndView(new HashMap<String, Object>(), TUTORIAL_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView logout(Request rq, Response rs) {
		rs.removeCookie(GlobalConstants.USER_COOKIE_KEY);
		rs.removeCookie(GlobalConstants.VERIFY_COOKIE_KEY);
		rs.redirect(HOME_ROUTE);
		return getModelAndView(null, HOME_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderGameList(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		int userId = getUserIdFromCookies(rq);
		info.put("kcgames", new GameListView(DBHandler.getKCGamesforUser(userId), userId, KINGS_CORNER_ROUTE));
		info.put("circlesgames", new GameListView(DBHandler.getCirclesGamesforUser(userId), userId, CIRCLES_ROUTE));
		return getModelAndView(info, GAME_LIST_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderCreateGame(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		List<Player> friends = getFriendsFromDB(getUserIdFromCookies(rq));
		info.put("friends", friends);
		info.put("hasFriends", !friends.isEmpty());
		return getModelAndView(info, CREATE_GAME_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postRegister(Request rq, Response rs) {
		boolean guest = Boolean.parseBoolean(rq.queryParams("guest"));
		String username = "";
		String email = "";
		String password = "";
		String passwordAgain = "";
		if(!guest){	
			username = rq.queryParams("username");
			email = rq.queryParams("email").toLowerCase();
			password = rq.queryParams("password");
			passwordAgain = rq.queryParams("password-again");
		
			if(username.equals("")) {
				rs.header(GlobalConstants.DISPLAY_ERROR, "Username is required.");
				return renderRegister(rq, rs);
			}
			User user = DBHandler.getUserByUsername(username);
			if(user != null || username.toLowerCase().startsWith(ArtificialPlayer.USERNAME_PREFIX.toLowerCase())){
				rs.header(GlobalConstants.DISPLAY_ERROR, "Username already in use.");
				return renderRegister(rq, rs);
			}
			user = DBHandler.getUserByEmail(email);
			if(user != null && !email.equals("")){
				rs.header(GlobalConstants.DISPLAY_ERROR, "Email already in use.");
				return renderRegister(rq, rs);
			}
				
			if(password.equals("") || passwordAgain.equals("")){
				rs.header(GlobalConstants.DISPLAY_ERROR, "Password can not be empty.");
				return renderRegister(rq, rs);
			}
			
			if(!password.equals(passwordAgain)){
				rs.header(GlobalConstants.DISPLAY_ERROR, "Passwords do not match.");
				return renderRegister(rq, rs);
			}
		}
		User newUser = new User(username, email, password);
		if(guest){
			newUser.setUsername("Guest " + Integer.toString(newUser.get_id()));
			newUser.setPassword(User.hashPassword(newUser.getSalt(), newUser.getUsername()));
			DBHandler.updateUser(newUser);
		}
		rs.cookie(GlobalConstants.USER_COOKIE_KEY, Integer.toString(newUser.get_id()), MAX_AGE);
		rs.cookie(GlobalConstants.VERIFY_COOKIE_KEY, newUser.getCookieValue());
		if(guest){
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "A guest account has been created for you. Your username/password is \"" + newUser.getUsername() + "\" (no quotes). You have been logged in. You should create a game or add friends to get started. If you change your mind about forms in the top right there is a drop down to edit settings and you can change your username and other settings.");
		} else {
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "New user successfully created. You have been logged in. You should create a game or add friends to get started.");
		}
		rs.redirect(CREATE_GAME_ROUTE);
		return getModelAndView(null, REGISTER_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postLogin(Request rq, Response rs) {
		String username = rq.queryParams("username");
		String password = rq.queryParams("password");
		User user = DBHandler.getUserByUsername(username);
		if(checkLogin(user, password)){
			rs.cookie(GlobalConstants.USER_COOKIE_KEY, Integer.toString(user.get_id()), MAX_AGE);
			rs.cookie(GlobalConstants.VERIFY_COOKIE_KEY, user.getCookieValue(), MAX_AGE);
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "Logged in successfully.");
			rs.redirect(GAMES_ROUTE);
		}
		rs.header(GlobalConstants.DISPLAY_ERROR, "Your username or password is incorrect.");
		return renderLogin(rq, rs);
	}

	protected static ModelAndView renderEditUser(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("user", new UserView(getUserFromCookies(rq)));
		return getModelAndView(info, EDIT_USER_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postEditUser(Request rq, Response rs) {
		User user = getUserFromCookies(rq);
		List<String> errorMessage = new ArrayList<String>();
		String username = rq.queryParams("username");
		String email = rq.queryParams("email");
		String currentPassword = rq.queryParams("currentPassword");
		String newPassword = rq.queryParams("newPassword");
		String newPasswordAgain = rq.queryParams("newPasswordAgain");
		boolean emailForNewFriend = rq.queryParams("emailForNewFriend") != null ?true:false;
		boolean emailForNewGame = rq.queryParams("emailForNewGame") != null ?true:false;
		boolean emailForTurn = rq.queryParams("emailForTurn") != null ?true:false;
		boolean emailForGameOver = rq.queryParams("emailForGameOver") != null ?true:false;
		boolean showHelpers = rq.queryParams("showHelpers") != null ?true:false;
		
		if(	user.getEmailForNewFriend() != emailForNewFriend ||
			user.getEmailForNewGame() != emailForNewGame ||
			user.getEmailForTurn() != emailForTurn ||
			user.getEmailForGameOver() != emailForGameOver ||
			user.getShowHelpers() != showHelpers){
				// Only change if there was a change (to conserve DB hits)
				user.setEmailForNewFriend(emailForNewFriend);
				user.setEmailForNewGame(emailForNewGame);
				user.setEmailForTurn(emailForTurn);
				user.setEmailForGameOver(emailForGameOver);
				user.setShowHelpers(showHelpers);
				// Update just the email settings in case there are errors with other sections.
				DBHandler.updateUser(user);
		}
		
		boolean noError = true;
		boolean change = false;
        String currentUsername = user.getUsername();
		if(!username.equals(currentUsername)){
            if(username.toLowerCase().startsWith(ArtificialPlayer.USERNAME_PREFIX.toLowerCase())) {
                noError = false;
                errorMessage.add("Don't steal our artificial players' names!");
            } else if(username.toLowerCase().equals(currentUsername.toLowerCase()) ||
                    DBHandler.getUserByUsername(username) == null){
				user.setUsername(username);
				change = true;
			} else if(username.equals("")){
				noError = false;
				errorMessage.add("A username is required.");
			} else {
				noError = false;
				errorMessage.add("That username is already taken.");
			}
		}
		if(!email.equals(user.getEmail())){
			if((email.equals("") || DBHandler.getUserByEmail(email) == null)){
				user.setEmail(email);
				change = true;
			} else {
				noError = false;
				errorMessage.add("That email has already been registered.");
			}
		}
		if(change){
			// Update after just the username and password in case errors with other section
			DBHandler.updateUser(user);
			change = false;
		}
		
		if(!currentPassword.equals("") || !newPassword.equals("") || !newPasswordAgain.equals("")){
			if(user.passwordMatches(currentPassword)){
				if(newPassword.equals(newPasswordAgain)){
					String newSalt = User.generateSalt();
					String hashedNewPassword = User.hashPassword(newSalt, newPassword);
					user.setSalt(newSalt);
					user.setPassword(hashedNewPassword);
					change = true;
				} else {
					noError = false;
					errorMessage.add("New password entries did not match.");
				}
			} else {
				noError = false;
				errorMessage.add("Current password was not correct.");
			}
		}
		
		if(noError){
			if(change){
				DBHandler.updateUser(user);
			}
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "User was updated successfully.");
		} else {
			StringBuilder builder = new StringBuilder();
			errorMessage.stream().forEach(e -> {builder.append(e); builder.append(" ");});
			rs.cookie(GlobalConstants.DISPLAY_ERROR, builder.toString());
		}
		rs.redirect(EDIT_USER_ROUTE);
		return getModelAndView(null, EDIT_USER_TEMPLATE, rq, rs);
	}

	public static ModelAndView removeNotification(Request rq, Response rs) {
		int notificationIndex = Integer.parseInt(rq.params(":id"));
		User user = getUserFromCookies(rq);
		user.removeNotification(notificationIndex);
		DBHandler.updateUser(user);
		return getModelAndView(null, GAME_LIST_TEMPLATE, rq, rs);
	}

	public static ModelAndView renderRematch(Request rq, Response rs) {
		int gameId = Integer.parseInt(rq.params(":id"));
		int creatorId = getUserIdFromCookies(rq);
		String gameType = rq.params(":gameType");
		List<Integer> players;
		if(("/" + gameType).equals(TemplateHandler.CIRCLES_ROUTE)){
			Circles game = DBHandler.getCirclesGame(gameId);
			players = makeNewPlayersList(game.getPlayers(), creatorId);
			return CirclesHandler.createCirclesGame(players, rq, rs);
		} else if(("/" + gameType).equals(TemplateHandler.KINGS_CORNER_ROUTE)){
			KingsCorner game = DBHandler.getKCGame(gameId);
			players = makeNewPlayersList(game.getPlayers(), creatorId);
			return KingsCornerHandler.createKCGame(players, rq, rs);
		}
		return null; // Compiler can't know we can never get here.
	}

	public static ModelAndView postPoke(Request rq, Response rs){
		String originalUrl = rq.headers("referer");
		int gameId = Integer.parseInt(rq.params(":id"));
		NotificationAndEmailHandler.poke(getUserIdFromCookies(rq), gameId);
		rs.redirect(originalUrl);
		return getModelAndView(new HashMap<String, Object>(), GAME_LIST_TEMPLATE, rq, rs);
	}
	
	private static List<Integer> makeNewPlayersList(List<Integer> players, int creatorId) {
		List<Integer> newPlayerList = new ArrayList<Integer>();
		newPlayerList.add(creatorId);
		newPlayerList.addAll(players.stream().filter(e -> ((int) e) != creatorId).collect(Collectors.toList()));
		return newPlayerList;
	}

	public static ModelAndView renderUserHistory(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("friend", new FriendView(getUserIdFromCookies(rq), "All Artificial Players"));
		return getModelAndView(info, FRIEND_INFO_TEMPLATE, rq, rs);
	}

}
