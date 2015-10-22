package cardswithfriends;

import java.util.List;

public class KingsCorner extends Game{
	private static final long serialVersionUID = 4654549203834433105L;
	public final Player[] turnOrder;
	private int currentPlayer;
	
	public KingsCorner(int gameId, List<Player> players){
		super(gameId, players);
		turnOrder = (Player[]) players.toArray();
		currentPlayer = 0;
	}
	
	private KingsCorner(KingsCornerGenerator kc){
		super(kc.gameId, kc.gs, kc.moves, kc.players);
		turnOrder = kc.getTurnOrder();
		currentPlayer = kc.currentPlayer;
	}

	@Override
	public boolean applyMove(Move move) {
		if(move.isValid()){
			move.apply();
			addMove(move);
			return true;
		}
		return false;
	}
	
	public boolean endTurn(){
		KCGameState gs = getKCGameState();
		Pile curUserHand = gs.userHands.get(getCurrentPlayer());
		Pile drawPile = gs.piles.get(Constants.DRAW_PILE);
		curUserHand.add(drawPile.removeTop());
		currentPlayer = (currentPlayer + 1) % turnOrder.length;
		return save();
	}

	public Player getCurrentPlayer(){
		return turnOrder[currentPlayer];
	}
	
	private KCGameState getKCGameState(){
		return (KCGameState) getGameState();
	}
	
	@Override
	protected final GameState newGameState(List<Player> players) {
		GameState game = new KCGameState();
		game.initializeToNewGameState(players);
		return game;
	}
	
	public static class KingsCornerGenerator{
		public int gameId;
		public GameState gs;
		public List<Move> moves;
		public List<Player> players;
		public int currentPlayer;
		public Player[] getTurnOrder(){
			return null;
		}
	}

	@Override
	protected boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

}
