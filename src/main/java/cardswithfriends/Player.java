package cardswithfriends;
public interface Player {
	public int getPlayerId();
	public abstract int hashCode();
	public abstract boolean equals(Object o);
	public abstract String getUserName();
}