package cardswithfriends;

import java.io.Serializable;

public interface Player extends Serializable{
	public Integer getPlayerId();
	public abstract int hashCode();
	public abstract boolean equals(Object o);
	public abstract String getUserName();
}