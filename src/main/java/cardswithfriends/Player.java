package cardswithfriends;

public interface Player {
	public Integer get_id();
	public abstract int hashCode();
	public abstract boolean equals(Object o);
	public String getUserName();
	public void updateWin(String game);
	public void updateLoss(String game);
}