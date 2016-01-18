package greycirclegames.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.NotificationAndEmailHandler;
import greycirclegames.Player;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class FriendsHandler extends TemplateHandler {

	protected static ModelAndView renderFriends(Request rq, Response rs) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("friends", getFriendsFromDB(getUserIdFromCookies(rq)));
		return getModelAndView(info, FRIENDS_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView renderFriendInfo(Request rq, Response rs) {
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
	
	protected static ModelAndView postAddFriend(Request rq, Response rs){
		String searchValue = rq.queryParams("searchValue");
		if(searchValue.equals("")) {
			rs.cookie(GlobalConstants.DISPLAY_ERROR, "Please enter an email or username.");
		} else {
			Player user2 = DBHandler.getUserByUsername(searchValue);
			if(user2 == null){
				searchValue = searchValue.toLowerCase();
				user2 = DBHandler.getUserByEmail(searchValue);
			}
			if(user2 == null){
				rs.cookie(GlobalConstants.DISPLAY_ERROR, "No user was found with that search.");
			} else if (user2.get_id() == getUserIdFromCookies(rq)){
				rs.cookie(GlobalConstants.DISPLAY_ERROR, "You cannot add yourself as a friend.");
			} else if(alreadyFriends(getUserIdFromCookies(rq), user2.get_id())){
				rs.cookie(GlobalConstants.DISPLAY_ERROR, "You are already friends with " + user2.getUsername());
			} else {
				if(!DBHandler.addFriend(getUserIdFromCookies(rq), user2.get_id())){
					rs.cookie(GlobalConstants.DISPLAY_ERROR, "There was an error adding that friend.");
				} else if(!DBHandler.addFriend(user2.get_id(), getUserIdFromCookies(rq))){
					rs.cookie(GlobalConstants.DISPLAY_ERROR, "There was an error adding that friend.");
				} else {
					rs.cookie(GlobalConstants.DISPLAY_SUCCESS, "The requested friend has been added.");
					NotificationAndEmailHandler.newFriend(getUserIdFromCookies(rq), user2.get_id());
				}
			}
		}
		rs.redirect(FRIENDS_ROUTE);
		return getModelAndView(null, FRIENDS_TEMPLATE, rq, rs);
	}
	
	protected static ModelAndView postRemoveFriend(Request rq, Response rs){
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
}
