package cardswithfriends;

import java.util.LinkedList;
import java.util.List;

public class KingsCorner extends Game{
	public final User[] turnOrder;
	
	public KingsCorner(int gameId, List<User> players){
		super(gameId, players);
		turnOrder = new User[players.size()];
	}
	
	private KingsCorner(KingsCornerGenerator kc){
		super(kc.gameId, kc.gs, kc.moves, kc.players);
		turnOrder = kc.getTurnOrder();
	}

	@Override
	public boolean applyMove(Move move) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected final GameState newGameState(List<User> players) {
		GameState game = new KCGameState();
		return game;
	}
	
	public static class KingsCornerGenerator{
		public int gameId;
		public GameState gs;
		public List<Move> moves;
		public List<User> players;
		public User[] getTurnOrder(){
			return null;
		}
	}

	@Override
	protected String toDBForm() {
		// TODO Auto-generated method stub
		return null;
	}

}
