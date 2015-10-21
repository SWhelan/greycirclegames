package cardswithfriends;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
	
public class User implements Player,Serializable {
	private static final long serialVersionUID = -8255733091222689114L;
	private static final int NUM_BITS = 128;
	private static final int RADIX = 32;
	private static final String ALGORITHM = "SHA-256";
	private int userId;
	private String userName;	
	private String password;
	private String salt;
	
	public User(int userId, String userName){
		this.userId = userId;
		this.userName = userName;
	}
	
	@Override
	public Integer getPlayerId() {
		return userId;
	}
	
	@Override
	public String getUserName() {
		return userName;
	}

	public Set<User> getFriends(){
		return null;
	}
	
	public Leaderboard getLeaderboard(){
		return null;
	}
	
	public boolean sendFriendRequest(User user){
		return false;
	}
	
	public boolean acceptFriendRequest(User user){
		return false;
	}
	
	public List<Game> getCurrentGames(){
		return null;
	}
	
	public static User make(String userName, String email, String password){
		return null;
	}
	
	public static User login(String userName, String email, String password){
		return null;
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
