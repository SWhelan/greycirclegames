package greycirclegames;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;

public class User extends ReflectionDBObject implements Player {
	private static final int NUM_BITS = 128;
	private static final int RADIX = 32;
	private static final String ALGORITHM = "SHA-256";
	
	private int _id;
	private String username;
	private String email;
	private String password;
	private String salt;
	private String cookieValue;
	
	private BasicDBList friendIds;
	
	private Integer nextNotificationId;
	private List<Notification> notifications = new ArrayList<Notification>();
	
	private boolean emailForNewFriend;
	private boolean emailForNewGame;
	private boolean emailForTurn;
	private boolean emailForGameOver;
	private boolean emailForPoke;
	
	private boolean showHelpers;

	/**
	 * Does absolutely nothing for you used mostly for testing purposes.
	 * 
	 * Okay well to be fair I guess you get a shiny new User object but nothing else.
	 */
	public User(){
		
	}
	
	/**
	 * This saves a new User to the database and should be used for creating a User for the first time.
	 * 
	 * This sets the ID from the Database, hashes the password, sets all the 
	 * default values, and most importantly saves the User to the database.
	 * 
	 * @param username
	 * @param email
	 * @param password should be the plain text password will be hashed in this method
	 */
	public User(String username, String email, String password) {
		this(DBHandler.getNextUserID(), username, email, "", User.generateSalt(), User.generateCookieValue(),
				new BasicDBList(), 0, null, 
				false, false, false, false, false,
				true
				);
		this.password = User.hashPassword(this.salt, password);
		DBHandler.createUser(this);
	}
	
	/**
	 * Use this to specify every possible parameter of a User
	 * 
	 * @param _id
	 * @param username
	 * @param email
	 * @param password
	 * @param salt use User.generateSalt
	 * @param cookieValue use User.generateCookieValue
	 * @param friendIds
	 * @param nextNotificationId
	 * @param notifications
	 * @param emailForNewFriend
	 * @param emailForNewGame
	 * @param emailForTurn
	 * @param emailForGameOver
	 * @param emailForPoke
	 * @param showHelpers
	 */
	public User(int _id, String username, String email, String password, String salt, String cookieValue,
			BasicDBList friendIds, int nextNotificationId, List<Notification> notifications,
			boolean emailForNewFriend, boolean emailForNewGame, boolean emailForTurn, boolean emailForGameOver, boolean emailForPoke, 
			boolean showHelpers) {
		this._id = _id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.salt = salt;
		this.cookieValue = cookieValue;
		
		this.friendIds = friendIds;
		
		this.nextNotificationId = nextNotificationId;
		if(notifications == null){
			this.notifications = new ArrayList<Notification>();
		} else {
			this.notifications = notifications;
		}
		
		this.emailForNewFriend = emailForNewFriend;
		this.emailForNewGame = emailForNewGame;
		this.emailForTurn = emailForTurn;
		this.emailForGameOver = emailForGameOver;
		this.emailForPoke = emailForPoke;
		
		this.showHelpers = showHelpers;
	}

	public User(DBObject obj) {
		this((Integer) obj.get("_id"), (String) obj.get("Username"), (String) obj.get("Email"),
				(String) obj.get("Password"), (String) obj.get("Salt"), (String) obj.get("CookieValue"),
				(BasicDBList) obj.get("FriendIds"), (Integer) obj.get("NextNotificationId"),
				((BasicDBList) obj.get("Notifications")).stream().map(e -> new Notification((BasicDBObject) e)).collect(Collectors.toList()),
				(boolean) obj.get("EmailForNewFriend"), (boolean) obj.get("EmailForNewGame"), 
				(boolean) obj.get("EmailForTurn"), (boolean) obj.get("EmailForGameOver"), (boolean) obj.get("EmailForPoke"), 
				(boolean) obj.get("ShowHelpers"));
	}
	
	/**
	 * Getters and Setters must start with get/set and must both exist publicly 
	 * for Mongo DB to access and set fields. Even for booleans the getters/setters
	 * must start with "get/set" not "is"
	 */

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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public BasicDBList getFriendIds() {
		return friendIds;
	}
	public void setFriendIds(BasicDBList friendIds) {
		this.friendIds = friendIds;
	}
	public Integer getNextNotificationId() {
		return nextNotificationId;
	}
	public void setNextNotificationId(int nextNotificationId) {
		this.nextNotificationId = nextNotificationId;
	}
	public List<Notification> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	public String getCookieValue() {
		return cookieValue;
	}
	public void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
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
	public boolean getEmailForPoke(){
		return emailForPoke;
	}
	public void setEmailForPoke(boolean emailForPoke){
		this.emailForPoke = emailForPoke;
	}
	public boolean getShowHelpers() {
		return showHelpers;
	}
	public void setShowHelpers(boolean showHelpers) {
		this.showHelpers = showHelpers;
	}
	
	/**
	 * End of getter/setter section
	 */
	
	public void addFriend(int friendID) {
		this.friendIds.add(friendID);
	}
	
	public void destroyFriendship(Integer friendID) {
		this.friendIds.remove(friendID);
	}
	
	public void addNotification(String text, String url, int gameId, boolean friends){
		this.notifications.add(new Notification(this.nextNotificationId, text, url, gameId, friends));
		this.nextNotificationId = this.nextNotificationId + 1;
	}

	public void removeNotification(int id){
		int index = 0;
		boolean found = false;
		for(int i = 0; i < this.notifications.size(); i++){
			if(this.notifications.get(i).getUid() == id){
				found = true;
				index = i;
			}
		}
		if(found){
			this.notifications.remove(index);
			this.nextNotificationId = this.nextNotificationId - 1;
		}
	}

	public void removeGameNotifications(int gameId){
		int i = 0;
		while(i < this.notifications.size()){
			if(this.notifications.get(i).getGameId() == gameId){
				this.notifications.remove(i);
				this.nextNotificationId = this.nextNotificationId - 1;
			} else {
				i++;
			}
		}
	}

	public void removeFriendNotifications(){
		int i = 0;
		while(i < this.notifications.size()){
			if(this.notifications.get(i).getFriends()){
				this.notifications.remove(i);
				this.nextNotificationId = this.nextNotificationId - 1;
			} else {
				i++;
			}
		}
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
		for (Object o : friendIds) {
			Integer friend_id = (Integer) o;
			User f = DBHandler.getUser(friend_id);
			toReturn.add(f);
		}
		return toReturn;
	}
	
	public static String generateCookieValue(){
		return User.generateSalt() + User.generateSalt();
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
}
