
package greycirclegames;

import java.util.List;

import greycirclegames.frontend.TemplateHandler;
import greycirclegames.games.Game;

public class NotificationAndEmailHandler {
	public static void sendNewFriendIfWanted(int adderId, int addedId) {
		User added = DBHandler.getUser(addedId);
		if(!added.getEmailForNewFriend()){
			return;
		}
		
		Player adder = DBHandler.getUser(adderId);
		EmailService.sendNewFriendMail(added.getEmail(), adder.getUsername());	
	}
	
	public static void sendNewGameIfWanted(List<Integer> players, String gameTypeIdentifier, String url, Player creator) {
		for(Integer id : players){
			if(id > 0){
				User user = DBHandler.getUser(id);
				if(user.getEmailForNewGame() && !user.equals(creator)){
					EmailService.sendNewGameMail(user.getEmail(), gameTypeIdentifier, url);
				}
			}
		}
	}
	
	public static void sendTurnMailIfWanted(List<Integer> players, Player player, String gameTypeIdentifier, String url) {
		if(player.get_id() > 0){
			User user = (User)player;
			if(Game.listHasMoreThanOneHuman(players) && user.getEmailForTurn()){
				EmailService.sendTurnMail(user.getEmail(), gameTypeIdentifier, url);
			}
		}
	}
	
	public static void sendGameOverMailIfWanted(List<Integer> players, String gameTypeIdentifier, String url, Player ender) {
		if(Game.listHasMoreThanOneHuman(players)){
			for(Integer id : players){
				if(id > 0){
					User user = DBHandler.getUser(id);
					if(user.getEmailForGameOver() && !user.equals(ender)){
						EmailService.sendGameOverMail(user.getEmail(), url);
					}
				}
			}
		}
	}

	public static void newGame(int gameId, List<Integer> players, String gameTypeIdentifier, String url, User creator) {
		sendNewGameIfWanted(players, gameTypeIdentifier, url, creator);
		players.stream().forEach((e) -> {
			if(e > 0){
				User user = DBHandler.getUser(e);
				if(!user.equals(creator)){
					user.addNotification("New: " + gameTypeIdentifier, url, gameId, false);
					DBHandler.updateUser(user);
				}
			}
		});
	}

	public static void turn(int gameId, List<Integer> players, Player currentPlayerObject, String gameTypeIdentifier,
			String url) {
		sendTurnMailIfWanted(players, currentPlayerObject, gameTypeIdentifier, url);
		
		if(currentPlayerObject.get_id() < 0){
			return;
		}
		User user = (User)currentPlayerObject;
		user.addNotification("It is your turn!", url, gameId, false);
		DBHandler.updateUser(user);
	}

	public static void gameOver(int gameId, List<Integer> players, String gameTypeIdentifier, String url, Player winner, Player ender) {
		sendGameOverMailIfWanted(players, gameTypeIdentifier, url, ender);
		players.stream().forEach(e -> {
			if(e > 0){
				User user = DBHandler.getUser(e);
				if(!user.equals(ender)){ // The know the game is over already
					String status = "lost";
					if(winner != null && e.equals(winner)){
						status = "won";
					} else if(winner == null){
						status = "tied";
					}
					user.addNotification("Game is over you " + status + "." , url, gameId, false);
					DBHandler.updateUser(user);
				}
			}
		});
	}

	public static void newFriend(int adderId, Integer addedId) {
		sendNewFriendIfWanted(adderId, addedId);
		User adder = DBHandler.getUser(adderId);
		User added = DBHandler.getUser(addedId);
		added.addNotification(adder.getUsername() + " added you as a friend.", TemplateHandler.FRIENDS_ROUTE, -1, true);
		DBHandler.updateUser(added);
	}
}
