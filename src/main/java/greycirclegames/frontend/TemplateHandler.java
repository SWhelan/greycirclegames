package greycirclegames.frontend;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.before;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.NotFoundException;
import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.games.card.kingscorner.KCPile;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
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
    public static final String FAVICON_ROUTE = "/favicon.ico";
    public static final String HOME_ROUTE = "/";
    public static final String LOGIN_ROUTE = "/login";
    public static final String REGISTER_ROUTE = "/register";
    public static final String EDIT_USER_ROUTE = "/editUser";
    public static final String CREATE_GAME_ROUTE = "/new";
    public static final String FRIENDS_ROUTE = "/friends";
    public static final String FRIENDS_ADD_ROUTE = "/addFriend";
    public static final String FRIENDS_REMOVE_ROUTE = "/removeFriend";
    public static final String GAMES_ROUTE = "/games";
    public static final String TUTORIAL_ROUTE = "/tutorial";
    public static final String LOGOUT_ROUTE = "/logout";
    public static final String POST_MOVE_ROUTE = "/move";
    public static final String POST_TURN_ROUTE = "/turn";
    public static final String REMOVE_NOTIFICATION_ROUTE = "/removeNotification";
    public static final String REMATCH_ROUTE= "/rematch";
    public static final String POKE_ROUTE = "/poke";
    public static final String USER_HISTORY_ROUTE = "/statistics";
    
    // Prepend Games
    public static final String CIRCLES_ROUTE = "/circles";
    public static final String KINGS_CORNER_ROUTE = "/kingscorner";

    public static final String PUBLIC_ROUTE = "/public";

    public static final String HOME_TEMPLATE = "home.mustache";
    public static final String LOGIN_TEMPLATE = "login.mustache";
    public static final String REGISTER_TEMPLATE = "register.mustache";
    public static final String EDIT_USER_TEMPLATE = "editUser.mustache";
    public static final String CREATE_GAME_TEMPLATE = "createGame.mustache";
    public static final String FRIENDS_TEMPLATE = "friends.mustache";
    public static final String FRIEND_INFO_TEMPLATE = "friendInfo.mustache";
    public static final String GAME_LIST_TEMPLATE = "gameList.mustache";
    public static final String KINGS_CORNERS_TEMPLATE = "kingscorners.mustache";
    public static final String TUTORIAL_TEMPLATE = "tutorial.mustache";
    public static final String CIRCLES_TEMPLATE = "circles.mustache";

    public static void registerTemplates() {
        // Ensure they are logged in or the url is public/doesn't require login
        Spark.before((rq, rs) -> {
            rs.removeCookie(GlobalConstants.DISPLAY_SUCCESS);
            rs.removeCookie(GlobalConstants.DISPLAY_ERROR);
            String path = rq.pathInfo();
            if (requiresAuthentication(path) && !isLoggedIn(rq)) {
                rs.redirect("/");
            }
        });

        // Handle the GET requests by rendering the mustache template
        get(HOME_ROUTE, (rq, rs) -> ApplicationHandler.renderHome(rq, rs), new MustacheTemplateEngine());
        get(REGISTER_ROUTE, (rq, rs) -> ApplicationHandler.renderRegister(rq, rs), new MustacheTemplateEngine());
        get(LOGIN_ROUTE, (rq, rs) -> ApplicationHandler.renderLogin(rq, rs), new MustacheTemplateEngine());
        get(EDIT_USER_ROUTE, (rq, rs) -> ApplicationHandler.renderEditUser(rq, rs), new MustacheTemplateEngine());
        get(USER_HISTORY_ROUTE, (rq, rs) -> ApplicationHandler.renderUserHistory(rq, rs), new MustacheTemplateEngine());
        get(TUTORIAL_ROUTE, (rq, rs) -> ApplicationHandler.renderTutorial(rq, rs), new MustacheTemplateEngine());
        get(LOGOUT_ROUTE, (rq, rs) -> ApplicationHandler.logout(rq, rs));
        get(GAMES_ROUTE, (rq, rs) -> ApplicationHandler.renderGameList(rq, rs), new MustacheTemplateEngine());
        get(CREATE_GAME_ROUTE, (rq, rs) -> ApplicationHandler.renderCreateGame(rq, rs), new MustacheTemplateEngine());
        get(REMOVE_NOTIFICATION_ROUTE + "/:id", (rq, rs) -> ApplicationHandler.removeNotification(rq, rs), new MustacheTemplateEngine());
        post(REGISTER_ROUTE, (rq, rs) -> ApplicationHandler.postRegister(rq, rs), new MustacheTemplateEngine());
        post(LOGIN_ROUTE, (rq, rs) -> ApplicationHandler.postLogin(rq, rs), new MustacheTemplateEngine());
        post(EDIT_USER_ROUTE, (rq, rs) -> ApplicationHandler.postEditUser(rq, rs), new MustacheTemplateEngine());
        get(POKE_ROUTE + "/:gameTypeId" + "/:id", (rq, rs) -> ApplicationHandler.postPoke(rq, rs), new MustacheTemplateEngine());

        // Routes involving adding, removing, or viewing friends
        get(FRIENDS_ROUTE, (rq, rs) -> FriendsHandler.renderFriends(rq, rs), new MustacheTemplateEngine());
        get(FRIENDS_ROUTE + "/:id", (rq, rs) -> FriendsHandler.renderFriendInfo(rq, rs), new MustacheTemplateEngine());
        post(FRIENDS_ADD_ROUTE, (rq, rs) -> FriendsHandler.postAddFriend(rq, rs), new MustacheTemplateEngine());
        post(FRIENDS_REMOVE_ROUTE, (rq, rs) -> FriendsHandler.postRemoveFriend(rq, rs), new MustacheTemplateEngine());

        // Kings Corner Specific Routes
        get(KINGS_CORNER_ROUTE + "/:id", (rq, rs) -> KingsCornerHandler.renderGame(rq, rs),
                new MustacheTemplateEngine());
        post(KINGS_CORNER_ROUTE + CREATE_GAME_ROUTE, (rq, rs) -> KingsCornerHandler.postCreateGame(rq, rs),
                new MustacheTemplateEngine());
        post(KINGS_CORNER_ROUTE + POST_MOVE_ROUTE, (rq, rs) -> KingsCornerHandler.postMove(rq, rs),
                new MustacheTemplateEngine());
        post(KINGS_CORNER_ROUTE + POST_TURN_ROUTE, (rq, rs) -> KingsCornerHandler.postTurn(rq, rs),
                new MustacheTemplateEngine());

        // Circles Specific Routes
        get(CIRCLES_ROUTE + "/:id", (rq, rs) -> CirclesHandler.renderGame(rq, rs), new MustacheTemplateEngine());
        post(CIRCLES_ROUTE + CREATE_GAME_ROUTE, (rq, rs) -> CirclesHandler.postCreateGame(rq, rs),
                new MustacheTemplateEngine());
        post(CIRCLES_ROUTE + POST_TURN_ROUTE, (rq, rs) -> CirclesHandler.postTurn(rq, rs),
                new MustacheTemplateEngine());

        get(REMATCH_ROUTE + "/:gameType" + "/:id", (rq, rs) -> ApplicationHandler.renderRematch(rq, rs), new MustacheTemplateEngine());
        // Throw an error on 404s
        get("/*", (rq, rs) -> {
            if (!rq.pathInfo().startsWith(PUBLIC_ROUTE)) {
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

    /**
     * 
     * @param rq
     * @return -1 if game id parameter not valid or paramter value
     */

    protected static int getGameId(Request rq) {
        int gameId;
        try {
            gameId = Integer.parseInt(rq.queryParams("gameId"));
        } catch (NumberFormatException e) {

        }

        try {
            gameId = Integer.parseInt(rq.params(":id"));
        } catch (NumberFormatException e) {
            return -1;
        }
        return gameId;
    }

    // Does the attempted url require the user to be logged in?
    protected static boolean requiresAuthentication(String path) {
        return!(path == null || path.equals(HOME_ROUTE) || path.equals(LOGIN_ROUTE) || path.equals(REGISTER_ROUTE)
                || path.equals(TUTORIAL_ROUTE) || path.startsWith(PUBLIC_ROUTE) || path.equals(FAVICON_ROUTE));
    }

    // Is the user logged in?
    protected static boolean isLoggedIn(Request rq) {
        if(rq.cookie(GlobalConstants.USER_COOKIE_KEY) == null || rq.cookie(GlobalConstants.VERIFY_COOKIE_KEY) == null){
        	return false;
        } else {
        	User user = getUserFromCookies(rq);
        	if(user != null){
	        	String correctCookieValue = user.getCookieValue();
	        	String actualCookieValue = rq.cookie(GlobalConstants.VERIFY_COOKIE_KEY);
	        	return correctCookieValue.equals(actualCookieValue);
        	} else {
        		return false;
        	}
        }
    }

    // Is the login/password valid for the found user?
    protected static boolean checkLogin(User user, String password) {
        if (user == null || !user.passwordMatches(password)) {
            return false;
        }
        return true;
    }

    /**
     * Get the user id from the cookies as each url that would use this ensures
     * that the user is logged in ie has a cookie this parse int won't fail
     */
    protected static int getUserIdFromCookies(Request rq) {
        try {
            return Integer.parseInt(rq.cookie(GlobalConstants.USER_COOKIE_KEY));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Same as get user id but then also get the user with that id form the
     * database
     */
    protected static User getUserFromCookies(Request rq) {
        return DBHandler.getUser(getUserIdFromCookies(rq));
    }

    /**
     *  Is the user with userId already friends with friendId?
     * @param userId
     * @param friendId
     * @return true if friends false if not friends
     */
    protected static boolean alreadyFriends(int userId, int friendId) {
        List<Player> friends = getFriendsFromDB(userId);
        return friends.stream().anyMatch(e -> e.get_id() == friendId);
    }

    /**
     *  The database returns a list of ids get the list of Players
     * @param userId
     * @return List<Player> from a user that is the friends of that user.
     */
    protected static List<Player> getFriendsFromDB(int userId) {
        return DBHandler.getFriendsForUser(userId).stream().map(e -> DBHandler.getUser((Integer) e))
                .collect(Collectors.toList());
    }

    /**
     * Get the key for the pile HashMap from the string name of the pile
     * @param name
     * @return string to use as the key in the hashmap of the piles in a kcgame
     */
    protected static String getPileKeyFromString(String name) {
        return Integer.toString(Arrays.stream(KCPile.values()).filter(e -> e.name().equals(name))
                .collect(Collectors.toList()).get(0).ordinal());
    }
    
    /**
     * 
     * A way to display success and failure messages to the user without
     * repeating this code in every above method.
     * 
     * To set a success message add a header to the response:
     * rs.header(GlobalConstants.DISPLAY_SUCCESS, "Success!");
     * 
     * where the second param is the message same for errors/failures:
     * rs.header(GlobalConstants.DISPLAY_ERROR, "Error!");
     * 
     * @param info
     *            the HashMap of string to object that should be passed to the
     *            UI
     * @param templateName
     *            file name of template to render
     * @param rq
     *            HTTP request
     * @param rs
     *            HTTP response with headers set for success/failure messages
     * @return a ModelAndView for the specified template and HashMap with the
     *         messages added. A ModelAndView is passed to the Mustache
     *         Rendering Engine to render the response via the template.
     */
    protected static ModelAndView getModelAndView(HashMap<String, Object> info, String templateName, Request rq,
            Response rs) {
        if (info == null) {
            return new ModelAndView(new HashMap<String, Object>(), templateName);
        }
        if (isLoggedIn(rq)) {
        	User user = getUserFromCookies(rq);
            info.put("loggedIn", true);
            info.put("username", user.getUsername());
            info.put("notifications", user.getNotifications());
            int count = user.getNotifications().size();
            info.put("showNotificationCount", count > 0);
            info.put("notificationCount", Integer.toString(count));
        }
        if (rq.host().startsWith("localhost")) {
        	info.put(GlobalConstants.DEV_MODE, "Development: ");
        }
        if (rs.raw().containsHeader(GlobalConstants.DISPLAY_ERROR)) {
            info.put(GlobalConstants.DISPLAY_ERROR, rs.raw().getHeader(GlobalConstants.DISPLAY_ERROR));
        }
        if (rs.raw().containsHeader(GlobalConstants.DISPLAY_SUCCESS)) {
            info.put(GlobalConstants.DISPLAY_SUCCESS, rs.raw().getHeader(GlobalConstants.DISPLAY_SUCCESS));
        }

        if (rq.cookie(GlobalConstants.DISPLAY_ERROR) != null) {
            info.put(GlobalConstants.DISPLAY_ERROR, rq.cookie(GlobalConstants.DISPLAY_ERROR));
            rs.removeCookie(GlobalConstants.DISPLAY_ERROR);
        }
        if (rq.cookie(GlobalConstants.DISPLAY_SUCCESS) != null) {
            info.put(GlobalConstants.DISPLAY_SUCCESS, rq.cookie(GlobalConstants.DISPLAY_SUCCESS));
            rs.removeCookie(GlobalConstants.DISPLAY_SUCCESS);
        }

        info.put("HOME_ROUTE", HOME_ROUTE);
        info.put("LOGIN_ROUTE", LOGIN_ROUTE);
        info.put("REGISTER_ROUTE", REGISTER_ROUTE);
        info.put("EDIT_USER_ROUTE", EDIT_USER_ROUTE);
        info.put("CREATE_GAME_ROUTE", CREATE_GAME_ROUTE);
        info.put("FRIENDS_ROUTE", FRIENDS_ROUTE);
        info.put("FRIENDS_ADD_ROUTE", FRIENDS_ADD_ROUTE);
        info.put("FRIENDS_REMOVE_ROUTE", FRIENDS_REMOVE_ROUTE);
        info.put("GAMES_ROUTE", GAMES_ROUTE);
        info.put("TUTORIAL_ROUTE", TUTORIAL_ROUTE);
        info.put("LOGOUT_ROUTE", LOGOUT_ROUTE);
        info.put("POST_MOVE_ROUTE", POST_MOVE_ROUTE);
        info.put("POST_TURN_ROUTE", POST_TURN_ROUTE);
        info.put("CIRCLES_ROUTE", CIRCLES_ROUTE);
        info.put("KINGS_CORNER_ROUTE", KINGS_CORNER_ROUTE);
        info.put("REMOVE_NOTIFICATION_ROUTE", REMOVE_NOTIFICATION_ROUTE);
        info.put("REMATCH_ROUTE", REMATCH_ROUTE);
        info.put("USER_HISTORY_ROUTE", USER_HISTORY_ROUTE);

        return new ModelAndView(info, templateName);
    }

}
