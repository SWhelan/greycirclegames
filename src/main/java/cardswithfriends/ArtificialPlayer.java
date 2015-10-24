package cardswithfriends;
public class ArtificialPlayer implements Player {
	private static final long serialVersionUID = 1L;
	private int playerID;

	public Integer getPlayerID() {
		return playerID;
	}

	public Move createMove(KCGameState gamestate) {
		return new KCMove(null, null, null, null);
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.getPlayerID().hashCode();
	}

	@Override
	public Integer getPlayerId() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public String getUserName() {
		return "Computer Player";
	}
}