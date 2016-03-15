
package greycirclegames;

import java.util.List;

import greycirclegames.frontend.TemplateHandler;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;

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
				if(user.getEmailForNewGame() && !user.get_id().equals(creator.get_id())){
					EmailService.sendNewGameMail(user.getEmail(), gameTypeIdentifier, url);
				}
			}
		}
	}
	
	public static void sendTurnMailIfWanted(List<Integer> players, Player player, String gameTypeIdentifier, String url, boolean skipTurn) {
		if(player.get_id() > 0){
			User user = (User)player;
			if(Game.listHasMoreThanOneHuman(players) && user.getEmailForTurn()){
                if(skipTurn) {
                    EmailService.sendSkippedTurnMail(user.getEmail(), gameTypeIdentifier, url);
                } else {
                    EmailService.sendTurnMail(user.getEmail(), gameTypeIdentifier, url);
                }
			}
		}
	}
	
	public static void sendGameOverMailIfWanted(List<Integer> players, String gameTypeIdentifier, String url, Player ender) {
		if(Game.listHasMoreThanOneHuman(players)){
			for(Integer id : players){
				if(id > 0){
					User user = DBHandler.getUser(id);
					if(user.getEmailForGameOver() && !user.get_id().equals(ender.get_id())){
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
				if(!e.equals(creator.get_id())){
					User user = DBHandler.getUser(e);
					user.addNotification("New: " + gameTypeIdentifier, url, gameId, false);
					DBHandler.updateUser(user);
				}
			}
		});
	}
	
	public static void sendPokeMailIfWanted(User sender, User receiver, String url) {
		if(sender.getEmailForPoke() && receiver.getEmailForPoke()){
			EmailService.sendPokeMail(receiver.getEmail(), url);
		}
	}

	public static void turn(int gameId, List<Integer> players, Player currentPlayerObject, String gameTypeIdentifier,
			String url, boolean skipTurn) {
		sendTurnMailIfWanted(players, currentPlayerObject, gameTypeIdentifier, url, skipTurn);
		
		if(currentPlayerObject.get_id() < 0){
			return;
		}
		User user = (User)currentPlayerObject;
        if(skipTurn) {
            user.addNotification("Your turn was skipped!", url, gameId, false);
        } else {
            user.addNotification("It is your turn!", url, gameId, false);
        }
		DBHandler.updateUser(user);
	}

	public static void gameOver(int gameId, List<Integer> players, String gameTypeIdentifier, String url, Player winner, Player ender) {
		sendGameOverMailIfWanted(players, gameTypeIdentifier, url, ender);
		players.stream().forEach(e -> {
			if(e > 0){
				if(!e.equals(ender.get_id())){ // The know the game is over already
					User user = DBHandler.getUser(e);
					String status = "lost";
					if(winner != null && e.equals(winner.get_id())){
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

	public static void poke(int senderId, int gameId, String gameIdentifier) {
		User sender = DBHandler.getUser(senderId);
		Game<? extends Move, ? extends GameState, ? extends ArtificialPlayer> game = null;
		String url = "";
		if(gameIdentifier.equals(GlobalConstants.KINGS_CORNER)){
			game = DBHandler.getKCGame(gameId);
			url = TemplateHandler.KINGS_CORNER_ROUTE;
		} else {
			game = DBHandler.getCirclesGame(gameId);
			url = TemplateHandler.CIRCLES_ROUTE;
		}
		url = url + "/" + Integer.toString(gameId);
		User receiver = DBHandler.getUser(game.getPlayers().get(game.getCurrentPlayerIndex()));
		sendPokeMailIfWanted(sender, receiver, url);
		receiver.addNotification("Reminder - It is your turn!", url, gameId, false);
	}

}
