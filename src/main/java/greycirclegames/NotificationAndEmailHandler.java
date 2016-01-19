package greycirclegames;

import java.util.List;

import greycirclegames.frontend.TemplateHandler;

public class NotificationAndEmailHandler {
	public static void sendNewFriendIfWanted(int adderId, int addedId) {
		User added = DBHandler.getUser(addedId);
		if(!added.getEmailForNewFriend()){
			return;
		}
		
		Player adder = DBHandler.getUser(adderId);
		EmailService.sendNewFriendMail(added.getEmail(), adder.getUsername());	
	}
	
	public static void sendNewGameIfWanted(List<Player> players, String gameTypeIdentifier, String url, Player creator) {
		for(Player player : players){
			if(player.get_id() > 0){
				User user = (User)player;
				if(user.getEmailForNewGame() && !player.equals(creator)){
					EmailService.sendNewGameMail(user.getEmail(), gameTypeIdentifier, url);
				}
			}
		}
	}
	
	public static void sendTurnMailIfWanted(List<Player> players, Player player, String gameTypeIdentifier, String url) {
		if(player.get_id() > 0){
			User user = (User)player;
			if(listHasMoreThanOneHuman(players) && user.getEmailForTurn()){
				EmailService.sendTurnMail(user.getEmail(), gameTypeIdentifier, url);
			}
		}
	}
	
	public static void sendGameOverMailIfWanted(List<Player> players, String gameTypeIdentifier, String url, Player ender) {
		if(listHasMoreThanOneHuman(players)){
			for(Player player : players){
				if(player.get_id() > 0){
					User user = (User)player;
					if(user.getEmailForGameOver() && !player.equals(ender)){
						EmailService.sendNewGameMail(user.getEmail(), gameTypeIdentifier, url);
					}
				}
			}
		}
	}
	
	public static boolean listHasMoreThanOneHuman(List<Player> players){
		int count = 0;
		for(Player player : players){
			if(player.get_id() > 0){
				count = count + 1;
			}
		}
		return count > 1;
	}

	public static void newGame(int gameId, List<Player> players, String gameTypeIdentifier, String url, User creator) {
		sendNewGameIfWanted(players, gameTypeIdentifier, url, creator);
		players.stream().forEach((e) -> {
			if(!e.equals(creator)){
				if(e.get_id() > 0){
					User user = (User)e;
					user.addNotification("New: " + gameTypeIdentifier, url, gameId, false);
					DBHandler.updateUser(user);
				}
			}
		});
	}

	public static void turn(int gameId, List<Player> players, Player currentPlayerObject, String gameTypeIdentifier,
			String url) {
		sendTurnMailIfWanted(players, currentPlayerObject, gameTypeIdentifier, url);
		
		if(currentPlayerObject.get_id() < 0){
			return;
		}
		User user = (User)currentPlayerObject;
		user.addNotification("It is your turn!", url, gameId, false);
		DBHandler.updateUser(user);
	}

	public static void gameOver(int gameId, List<Player> players, String gameTypeIdentifier, String url, Player winner, Player ender) {
		sendGameOverMailIfWanted(players, gameTypeIdentifier, url, ender);
		players.stream().forEach(e -> {
			if(!e.equals(ender)){ // They know the game is over already
				if(e.get_id() > 0){
					User user = (User) e;
					String status = "lost";
					if(winner != null && e.equals(winner)){
						status = "won";
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
