package greycirclegames.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.frontend.views.GameListView;
import greycirclegames.frontend.views.UserView;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ApplicationHandler extends TemplateHandler {
	
	protected static ModelAndView renderHome(Request rq, Response rs) {
		if(rq.cookie(GlobalConstants.USER_COOKIE_KEY) != null){
			return renderGameList(rq, rs);
		}
		return getModelAndView(new HashMap<String, Object>(), HOME_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderRegister(Request rq, Response rs) {
		return getModelAndView(new HashMap<String, Object>(), REGISTER_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderLogin(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		String email = rq.queryParams("email");
		String password = rq.queryParams("password");
		info.put("email", email);
		info.put("password", password);
		return getModelAndView(info, LOGIN_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderTutorial(Request rq, Response rs) {
		return getModelAndView(new HashMap<String, Object>(), TUTORIAL_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView logout(Request rq, Response rs) {
		rs.removeCookie(GlobalConstants.USER_COOKIE_KEY);
		rs.redirect(HOME_ROUTE);
		return getModelAndView(null, HOME_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderGameList(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		int userId = getUserIdFromCookies(rq); 
		info.put("kcgames", new GameListView(DBHandler.getKCGamesforUser(userId), userId));
		info.put("circlesgames", new GameListView(DBHandler.getCirclesGamesforUser(userId), userId));
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
		rs.cookie(GlobalConstants.USER_COOKIE_KEY, Integer.toString(newUser.get_id()));
		rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "New user successfully created. You have been logged in. You should create a game or add friends to get started.");
		rs.redirect(CREATE_GAME_ROUTE);
		return getModelAndView(null, REGISTER_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postLogin(Request rq, Response rs) {
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

	protected static ModelAndView renderEditUser(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("user", new UserView(getUserFromCookies(rq)));
		return getModelAndView(info, EDIT_USER_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postEditUser(Request rq, Response rs) {		
		User user = getUserFromCookies(rq);
		List<String> errorMessage = new ArrayList<String>();
		String userName = rq.queryParams("userName");
		String email = rq.queryParams("email");
		String currentPassword = rq.queryParams("currentPassword");
		String newPassword = rq.queryParams("newPassword");
		String newPasswordAgain = rq.queryParams("newPasswordAgain");
		boolean emailForNewFriends = rq.queryParams("emailForNewFriends").equals("true")?true:false;
		boolean emailForNewGames = rq.queryParams("emailForNewGames").equals("true")?true:false;
		boolean emailForTurn = rq.queryParams("emailForTurn").equals("true")?true:false;
		boolean emailForGameOver = rq.queryParams("emailForGameOver").equals("true")?true:false;
		boolean noError = true;
		if(DBHandler.getUserByUserName(userName) == null){
			user.setUserName(userName);
		} else {
			noError = false;
			errorMessage.add("That username is already taken.");
		}
		if(email.equals("") || DBHandler.getUserByEmail(email) == null){
			user.setEmail(email);
		} else {
			noError = false;
			errorMessage.add("That email has already been registered.");
		}
		
		if(!currentPassword.equals("") || !newPassword.equals("") || !newPasswordAgain.equals("")){
			if(user.passwordMatches(currentPassword)){
				if(newPassword.equals(newPasswordAgain)){
					String newSalt = User.generateSalt();
					String hashedNewPassword = User.hashPassword(newSalt, newPassword);
					user.setSalt(newSalt);
					user.setPassword(hashedNewPassword);
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
			DBHandler.updateUser(user);
			rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "User was updated successfully.");
		} else {
			rs.cookie(GlobalConstants.DISPLAY_ERROR, errorMessage.toString());
		}
		rs.redirect(EDIT_USER_ROUTE);
		return getModelAndView(null, EDIT_USER_TEMPLATE, rq, rs);
	}

}
