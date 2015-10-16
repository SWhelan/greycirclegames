package cardswithfriends;
public interface Player {
	public Integer getPlayerId();
	public abstract int hashCode();
	public abstract boolean equals(Object o);
	public abstract String getUserName();
}