package cardswithfriends;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;

public class KingsCorner extends Game{
	public List<Player> turnOrder;
	private int currentPlayer;
	
	public KingsCorner(int gameId, List<Player> players){
		super(gameId, players);
		turnOrder = new ArrayList<Player>();
		turnOrder.addAll(players);
		currentPlayer = 0;
	}
	
	private KingsCorner(KingsCornerGenerator kc){
		super(kc.gameId, kc.gs, kc.moves, kc.players);
		turnOrder = kc.getTurnOrder();
		currentPlayer = kc.currentPlayer;
	}

//	public KingsCorner(DBObject obj) {
//		this((Integer)obj.get("_id"),
//				(String)obj.get("UserName"),
//				(String)obj.get("Password"),
//				(String)obj.get("Salt"),
//				(String)obj.get("Email"));	
//		}
	
	public int getCurrentPlayer(){
		return currentPlayer;
	}
	public void setCurrentPlayer(int currentPlayer){
		this.currentPlayer = currentPlayer;
	}
	public List<Player> getTurnOrder(){
		return turnOrder;
	}
	public void setTurnOrder(List<Player> turnOrder){
		this.turnOrder = turnOrder;
	}
	
	@Override
	public boolean applyMove(Move move) {
		if(!gameIsOver() && move.isValid()){
			move.apply();
			addMove(move);
			return true;
		}
		return false;
	}
	
	public boolean endTurn(){
		if(gameIsOver()){
			return false;
		}
		KCGameState gs = getGameState();
		Pile curUserHand = gs.userHands.get(Integer.toString(getCurrentPlayer()));
		Pile drawPile = gs.piles.get(Integer.toString(PileIds.DRAW_PILE.ordinal()));
		if(!drawPile.isEmpty()){
			curUserHand.add(drawPile.removeTop());
		}
		currentPlayer = (currentPlayer + 1) % turnOrder.size();
		
		return save();
	}

	public Player getCurrentPlayerObject(){
		return turnOrder.get(currentPlayer);
	}
	
	public KCGameState getGameState(){
		return (KCGameState) super.getGameState();
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
		public List<Player> getTurnOrder(){
			return null;
		}
	}

	@Override
	protected boolean save() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean gameIsOver(){
		for(Pile p : getGameState().userHands.values()){
			if(p.isEmpty()){
				return true;
			}
		}
		return false;
	}
}
