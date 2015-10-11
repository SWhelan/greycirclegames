package cardswithfriends;
public class ArtificialPlayer implements Player {
	private int playerID;

	public int getPlayerID() {
		return playerID;
	}

	public Move createMove(KCGameState gamestate) {
		return new Move(null, null, null, null);
	}
}