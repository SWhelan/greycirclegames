package cardswithfriends;

import java.util.List;

public class KingsCorner extends Game{
	private static final long serialVersionUID = 4654549203834433105L;
	public final Player[] turnOrder;
	
	public KingsCorner(int gameId, List<Player> players){
		super(gameId, players);
		turnOrder = new Player[players.size()];
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
	protected final GameState newGameState(List<Player> players) {
		GameState game = new KCGameState();
		return game;
	}
	
	public static class KingsCornerGenerator{
		public int gameId;
		public GameState gs;
		public List<Move> moves;
		public List<Player> players;
		public Player[] getTurnOrder(){
			return null;
		}
	}

	@Override
	protected boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

}
