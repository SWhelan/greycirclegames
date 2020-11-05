import java.util.ArrayList;

import greycirclegames.DBHandler;
import greycirclegames.DatabaseConnector;
import greycirclegames.User;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Card.Suit;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCMove;
import greycirclegames.games.card.kingscorner.KingsCorner;
import spark.utils.Assert;

public class DBTest {
    
	// Test CRUD operations for a USER
	public void testUser() {
    	DatabaseConnector.getInstance().setTestDatabase();
		User user1 = new User("goduser", "word", "email@gmail.com");
		User user2 = new User("goduser2", "word2", "2email@gmail.com");
		User user3 = new User("goduser3", "word3", "3email@gmail.com");
		
		user1 = DBHandler.getUser(user1.get_id());
		user1.setUsername("newName");
		DBHandler.updateUser(user1);

		user1 = DBHandler.getUser(user1.get_id());
		user2 = DBHandler.getUser(user2.get_id());
		user3 = DBHandler.getUser(user3.get_id());

		Assert.isTrue(user1.getUsername().equals("newName"), "UNEXPECTED username 1!!!!");
		Assert.isTrue(user2.getUsername().equals("goduser2"), "UNEXPECTED username 2!!!!");
		Assert.isTrue(user3.getUsername().equals("goduser3"), "UNEXPECTED username 3!!!!");

		DBHandler.deleteUser(user1.get_id());
		DBHandler.deleteUser(user2.get_id());
		DBHandler.deleteUser(user3.get_id());
	}

	// Test CRUD operations for a KCGame
	public void testKCGame() {
    	DatabaseConnector.getInstance().setTestDatabase();

		ArrayList<Integer> playerList = new ArrayList<Integer>();

		User test = new User("Test User", "Test Email", "password");
		playerList.add(test.get_id());
		playerList.add(-1);

		Pile pile1 = new Pile("pile one");
		pile1.add(new Card(2, Suit.CLUB));
		Pile pile2 = new Pile("pile two");
		pile2.add(new Card(2, Suit.HEART));
		Pile pile3 = new Pile("pile three");
		pile3.add(new Card(2, Suit.DIAMOND));


		int gameID = DBHandler.getNextGameID();
		KingsCorner game = new KingsCorner(gameID, playerList);
		game.addMove(new KCMove(43, pile1, pile2, pile3));

		DBHandler.createKCGame(game);

		KingsCorner kc = DBHandler.getKCGame(gameID);
		Assert.isTrue(kc.getIsActive(), "Demo game should be active!");

		kc.setIsActive(false);
		DBHandler.updateKCGame(kc);
		kc = DBHandler.getKCGame(gameID);
		Assert.isTrue(!kc.getIsActive(), "Demo game should be inactive!");

		DBHandler.deleteKCGame(gameID);
		DBHandler.deleteUser(test.get_id());
	}
	
}