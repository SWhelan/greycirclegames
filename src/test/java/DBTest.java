import java.util.ArrayList;
import java.util.LinkedList;

import com.mongodb.BasicDBList;

import spark.utils.Assert;
import cardswithfriends.DBHandler;
import cardswithfriends.KCMove;
import cardswithfriends.KingsCorner;
import cardswithfriends.Leaderboard;
import cardswithfriends.Pile;
import cardswithfriends.Player;
import cardswithfriends.User;


public class DBTest {

	//test CRUD operations for a USER
	public void testUser() {

		int id1 = DBHandler.getNextUserID();
		int id2 = DBHandler.getNextUserID();
		int id3 = DBHandler.getNextUserID();

		BasicDBList frands = new BasicDBList();
		frands.add(id1);
		frands.add(id2);
		frands.add(id3);

		User user = new User(id1,"goduser", "word", "salt", "email@gmail.com", frands);
		User user2 = new User(id2,"goduser2", "word2", "salt2", "2email@gmail.com", frands);
		User user3 = new User(id3,"goduser3", "word3", "salt3", "3email@gmail.com", frands);
		DBHandler.createUser(user);
		DBHandler.createUser(user2);
		DBHandler.createUser(user3);

		User u1 = DBHandler.getUser(id1);
		u1.setUserName("newName");
		DBHandler.updateUser(u1);

		u1 = DBHandler.getUser(id1);
		User u2 = DBHandler.getUser(id2);
		User u3 = DBHandler.getUser(id3);

		Assert.isTrue(u1.getUserName().equals("newName"), "UNEXPECTED userName!!!!");
		Assert.isTrue(u2.getUserName().equals("goduser2"), "UNEXPECTED userName!!!!");
		Assert.isTrue(u3.getUserName().equals("goduser3"), "UNEXPECTED userName!!!!");

		DBHandler.deleteUser(id1);
		DBHandler.deleteUser(id2);
		DBHandler.deleteUser(id3);
	}

	//test CRUD operations for a KCGame
	public void testKCGame() {

		ArrayList<Player> playerList = new ArrayList<Player>();
		BasicDBList frands = new BasicDBList();
		frands.add(6);
		frands.add(7);
		frands.add(8);

		Player p1 = new User(43,"username", "word", "salt", "email@gmail.com", new BasicDBList());    	

		playerList.add(p1);
		playerList.add(new User(44,"username", "word", "salt", "mail@gmail.com", frands));
		playerList.add(new User(45,"username", "word", "salt", "ail@gmail.com", frands));

		Pile pile1 = new Pile("pile one");
		Pile pile2 = new Pile("pile two");
		Pile pile3 = new Pile("pile three");


		int gameID = DBHandler.getNextKCGameID();
		KingsCorner game = new KingsCorner(gameID, playerList);
		game.addMove(new KCMove(p1, pile1, pile2, pile3));

		DBHandler.createKCGame(game);

		LinkedList<KingsCorner> l = (LinkedList<KingsCorner>) DBHandler.getKCGamesforUser(45);

		KingsCorner kc = DBHandler.getKCGame(gameID);
		Assert.isTrue(kc.getIsActive(), "Demo game should be active!");

		kc.setIsActive(false);
		DBHandler.updateKCGame(kc);
		kc = DBHandler.getKCGame(gameID);
		Assert.isTrue(!kc.getIsActive(), "Demo game should be inactive!");

		DBHandler.deleteKCGame(gameID);
	}

	//test CRUD operations for a KCGame
	public void testLeaderboard() {
				
		int id1 = DBHandler.getNextUserID();
		BasicDBList frands = new BasicDBList();
		frands.add(id1);
		User user = new User(id1,"goduser", "word", "salt", "email@gmail.com", frands);
		DBHandler.createUser(user);

		Leaderboard l = DBHandler.getLeaderboard();
		l.addUser(user);
		DBHandler.updateLeaderboard(l); 
		
		l = DBHandler.getLeaderboard();
		
		Assert.isTrue(l.getGameStats() != null, "gameStats were null!!!");
		DBHandler.deleteUser(id1);
	}
}