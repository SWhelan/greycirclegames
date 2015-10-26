package cardswithfriends;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

import com.mongodb.DBObject;
import com.mongodb.ReflectionDBObject;
	
public class User extends ReflectionDBObject implements Player {
	private static final int NUM_BITS = 128;
	private static final int RADIX = 32;
	private static final String ALGORITHM = "SHA-256";
	private int _id;
	private String userName;
	private String password;
	private String salt;
	private String email;
	
	public User(int _id, String userName, String password, String salt, String email){
		this._id = _id;
		this.userName = userName;
		this.password = password;
		this.salt = salt;
		this.email = email;
	}
	
	public User(int _id, String userName) {
		this(_id, userName, null, null, null);
	}

	public User(DBObject obj) {
		this((Integer)obj.get("_id"),
				(String)obj.get("UserName"),
				(String)obj.get("Password"),
				(String)obj.get("Salt"),
				(String)obj.get("Email"));
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

	public Set<User> getFriends(){
		return null;
	}
	
	public Leaderboard getLeaderboard(){
		return null;
	}
	public List<Game> getCurrentGames(){
		return null;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof User){
			User other = (User) o;
			return other._id == this._id;
		}
		return false;
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
	
	public boolean checkPassword(String password) {
		return this.password.equals(hashPassword(this.salt, password));
	}
}
