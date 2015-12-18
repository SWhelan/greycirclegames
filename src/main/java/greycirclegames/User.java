package greycirclegames;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;

import greycirclegames.games.Game;
	
public class User extends ReflectionDBObject implements Player {
	private static final int NUM_BITS = 128;
	private static final int RADIX = 32;
	private static final String ALGORITHM = "SHA-256";
	private int _id;
	private String userName;
	private String password;

	private String salt;
	private String email;
	//List of ids of friends
	private BasicDBList friends;
	//Maps Game type ids to wins and losses
	private BasicDBObject winsAndLosses;
	
	public User(int _id, String userName, String password, String salt,
			String email, BasicDBList friends){
		this._id = _id;
		this.userName = userName;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.friends = friends;
		intializeWinsAndLosses();
	}
	
	public User(int _id, String userName, String password, String salt,
			String email, BasicDBList friends, BasicDBObject wlmap){
		this._id = _id;
		this.userName = userName;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.friends = friends;
		this.winsAndLosses = wlmap;
	}

	public User(int _id, String email) {
		this(_id, email, null, null, email, new BasicDBList());
	}

	public User(DBObject obj) {
		this((Integer)obj.get("_id"),
				(String)obj.get("UserName"),
				(String)obj.get("Password"),
				(String)obj.get("Salt"),
				(String)obj.get("Email"),
				(BasicDBList)obj.get("Friends"),
				(BasicDBObject)obj.get("WinsAndLosses"));
	}
	
	
	private void intializeWinsAndLosses() {
		winsAndLosses = new BasicDBObject();
		BasicDBList winLossList = new BasicDBList();
		winLossList.add(0);
		winLossList.add(0);
		winsAndLosses.put(GlobalConstants.KINGS_CORNER, winLossList);
	}

	public Integer get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BasicDBList getFriends(){
		return friends;
	}
	
	public void setFriends(BasicDBList friends) {
		this.friends = friends;
	}
	
	public Leaderboard getLeaderboard(){
		return null;
	}
	public List<Game> getCurrentGames(){
		return null;
	}
	
	public void addFriend(int friendID) {
		this.friends.add(friendID);
	}
	
	public void destroyFriendship(Integer friendID) {
		this.friends.remove(friendID);
	}

	public BasicDBObject getWinsAndLosses() {
		return winsAndLosses;
	}

	public void setWinsAndLosses(BasicDBObject winsAndLosses) {
		this.winsAndLosses = winsAndLosses;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _id;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (_id != other._id)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	public boolean sendFriendRequest(User user){
		return false;
	}
	
	public boolean acceptFriendRequest(User user){
		return false;
	}
	
	public static User make(String userName, String email, String password){
		return null;
	}
	
	public static User login(String userName, String email, String password){
		return null;
	}
	
	public static String generateSalt(){
		SecureRandom random = new SecureRandom();
		return new BigInteger(NUM_BITS, random).toString(RADIX);
	}
	
	public static String hashPassword(String salt, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update((password + salt).getBytes());
			return new String(md.digest());
		} catch (NoSuchAlgorithmException e){
			//Shouldn't reach here as SHA-256 is required to be built in.
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean passwordMatches(String password) {
		return this.password.equals(hashPassword(this.salt, password));
	}

	@Override
	public void updateWin(String game) {
		BasicDBList winLossList = ((BasicDBList)winsAndLosses.get(game));
		int numWins = (Integer)winLossList.get(0) + 1;
		winLossList.put(0, numWins);
		winsAndLosses.put(game, winLossList);
		DBHandler.updateUser(this);
		
		Leaderboard lb = DBHandler.getLeaderboard();
		lb.addUser(this);
		DBHandler.updateLeaderboard(lb);
	}

	@Override
	public void updateLoss(String game) {
		BasicDBList winLossList = ((BasicDBList)winsAndLosses.get(game));
		int numLosses = (Integer)winLossList.get(1) + 1;
		winLossList.put(1, numLosses);
		winsAndLosses.put(game, winLossList);
		DBHandler.updateUser(this);
		
		Leaderboard lb = DBHandler.getLeaderboard();
		lb.addUser(this);
		DBHandler.updateLeaderboard(lb);
	}

	public List<User> getFriendsList() {
		List<User> toReturn = new LinkedList<User>();
		for(Object o : friends){
			Integer friend_id = (Integer)o;
			User f = DBHandler.getUser(friend_id);
			toReturn.add(f);
		}
		return toReturn;
	}
}
