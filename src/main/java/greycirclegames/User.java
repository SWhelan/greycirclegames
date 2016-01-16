package greycirclegames;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;

public class User extends ReflectionDBObject implements Player {
	private static final int NUM_BITS = 128;
	private static final int RADIX = 32;
	private static final String ALGORITHM = "SHA-256";
	private int _id;
	private String username;
	private String password;

	private String salt;
	private String email;
	// List of ids of friends
	private BasicDBList friends;
	private boolean emailForNewFriend;
	private boolean emailForNewGame;
	private boolean emailForTurn;
	private boolean emailForGameOver;

	public User(int _id, String username, String password, String salt, String email, BasicDBList friends,
			boolean emailForNewFriend, boolean emailForNewGame, boolean emailForTurn, boolean emailForGameOver) {
		this._id = _id;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.email = email;
		this.friends = friends;
		this.emailForNewFriend = emailForNewFriend;
		this.emailForNewGame = emailForNewGame;
		this.emailForTurn = emailForTurn;
		this.emailForGameOver = emailForGameOver;
	}

	public User(int _id, String username, String password, String salt, String email, BasicDBList friends) {
		this(_id, username, password, salt, email, friends, false, false, false, false);
	}

	public User(int _id, String email) {
		this(_id, email, null, null, email, new BasicDBList());
	}

	public User(DBObject obj) {
		this((Integer) obj.get("_id"), (String) obj.get("Username"), (String) obj.get("Password"),
				(String) obj.get("Salt"), (String) obj.get("Email"), (BasicDBList) obj.get("Friends"),
				(boolean) obj.get("EmailForNewFriend"), (boolean) obj.get("EmailForNewGame"),
				(boolean) obj.get("EmailForTurn"), (boolean) obj.get("EmailForGameOver"));
	}

	public Integer get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public BasicDBList getFriends() {
		return friends;
	}
	public void setFriends(BasicDBList friends) {
		this.friends = friends;
	}
	public void addFriend(int friendID) {
		this.friends.add(friendID);
	}
	public void destroyFriendship(Integer friendID) {
		this.friends.remove(friendID);
	}
	public boolean getEmailForNewFriend() {
		return emailForNewFriend;
	}
	public void setEmailForNewFriend(boolean emailForNewFriend) {
		this.emailForNewFriend = emailForNewFriend;
	}
	public boolean getEmailForNewGame() {
		return emailForNewGame;
	}
	public void setEmailForNewGame(boolean emailForNewGame) {
		this.emailForNewGame = emailForNewGame;
	}
	public boolean getEmailForTurn() {
		return emailForTurn;
	}
	public void setEmailForTurn(boolean emailForTurn) {
		this.emailForTurn = emailForTurn;
	}
	public boolean getEmailForGameOver() {
		return emailForGameOver;
	}
	public void setEmailForGameOver(boolean emailForGameOver) {
		this.emailForGameOver = emailForGameOver;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _id;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(NUM_BITS, random).toString(RADIX);
	}

	public static String hashPassword(String salt, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			md.update((password + salt).getBytes());
			return new String(md.digest());
		} catch (NoSuchAlgorithmException e) {
			// Shouldn't reach here as SHA-256 is required to be built in.
			e.printStackTrace();
			return null;
		}
	}

	public boolean passwordMatches(String password) {
		return this.password.equals(hashPassword(this.salt, password));
	}

	public List<User> getFriendsList() {
		List<User> toReturn = new LinkedList<User>();
		for (Object o : friends) {
			Integer friend_id = (Integer) o;
			User f = DBHandler.getUser(friend_id);
			toReturn.add(f);
		}
		return toReturn;
	}
}
