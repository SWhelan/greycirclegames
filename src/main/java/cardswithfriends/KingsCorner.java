package cardswithfriends;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class KingsCorner extends Game{
	public List<Player> turnOrder;
	// The number of turn order not the current Id of the player.
	private int currentPlayer;

	public KingsCorner(int gameId, List<Player> players){
		super(gameId, players);
		turnOrder = new ArrayList<Player>();
		turnOrder.addAll(players);
		currentPlayer = 0;
	}

	//for creating a new kcgame from the mongo object
	public KingsCorner(DBObject obj) {
		
		this.currentPlayer = (Integer)obj.get("CurrentPlayer");
		
		BasicDBList turnOrder = (BasicDBList)obj.get("TurnOrder");
		this.turnOrder = new LinkedList<Player>();
		for (Object player : turnOrder) {
			int playerId = (Integer)((BasicDBObject) player).get("_id");
			if(playerId < 0){
				this.turnOrder.add(new ArtificialPlayer(playerId));
			} else {
				this.turnOrder.add(new User((BasicDBObject)player));
			}
		}
		
		this._id = (Integer)obj.get("_id");
		this.gameState = new KCGameState((BasicDBObject)obj.get("GameState"));
		
		BasicDBList moves = (BasicDBList)obj.get("Moves");
		this.moves = new LinkedList<Move>();
		for (Object move : moves) {
			this.moves.add(new KCMove((BasicDBObject)move));
		}
		
		BasicDBList players = (BasicDBList)obj.get("Players");
		this.players = new LinkedList<Player>();
		for (Object player : players) {
			int playerId = (Integer)((BasicDBObject) player).get("_id");
			if(playerId < 0){
				this.players.add(new ArtificialPlayer(playerId));
			} else {
				this.players.add(new User((BasicDBObject)player));
			}
		}
		
		this.isActive = (Boolean)obj.get("IsActive");
		this.winner_id = (Integer)obj.get("Winner_id");
		this.updatedLeaderboard = (Boolean)obj.get("UpdatedLeaderboard");
	}

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
			if(gameIsOver()){
				setToWinState();
			}
			return true;
		}
		return false;
	}
	
	public boolean endTurn(){
		if(gameIsOver()){
			return false;
		}
		KCGameState gs = getGameState();
		Pile curUserHand = gs.userHands.get(Integer.toString(getCurrentPlayerObject().get_id()));
		Pile drawPile = gs.piles.get(Integer.toString(PileIds.DRAW_PILE.ordinal()));
		if(!drawPile.isEmpty()){
			Pile topCard = new Pile("Top card");
			topCard.add(drawPile.getTop());
			Move endTurn = new KCMove(getCurrentPlayerObject(), drawPile, topCard, curUserHand);
			endTurn.apply();
			moves.add(endTurn);
		}
		currentPlayer = (currentPlayer + 1) % turnOrder.size();
		
		return true;
	}
	
	public boolean applyAIMoves(){
		boolean result = false;
		Player cur = getCurrentPlayerObject();
		Pile aiHand;
		Map<Integer,Pile> visiblePiles = getGameState().getVisiblePiles();
		Move m = null;
		while(this.isActive && isAI(cur)){
			result = true;
			aiHand = getGameState().userHands.get(Integer.toString(cur.get_id())).copy();

			ArtificialPlayer ai = (ArtificialPlayer) cur;
			boolean hasMove = true;
			while(this.isActive && hasMove){
				m = ai.createMove(aiHand, visiblePiles);
				if(m != null){
					this.applyMove(m);
				}else{
					hasMove = false;
				}				
			}
			this.endTurn();
			
			cur = getCurrentPlayerObject();
		}
		return result;
	}
	
	public static boolean isAI(Player p){
		int id = p.get_id();
		return id < 0;
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
	
	@Override
	public boolean gameIsOver(){
		for(Pile p : getGameState().userHands.values()){
			if(p.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public Player getWinner(){
		if(gameIsOver()){
			return getCurrentPlayerObject();
		}
		return null;
	}
	
	private void setToWinState(){
		isActive = false;
		winner_id = getCurrentPlayerObject().get_id();
		this.updateLeaderboard();
	}
	
	private void setToNonWinningEndState(){
		isActive = false;
		winner_id = null;
	}

	@Override
	protected String getGameTypeIdentifier() {
		return GlobalConstants.KINGS_CORNER;
	}
}
