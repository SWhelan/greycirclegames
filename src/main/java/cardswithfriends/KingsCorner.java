package cardswithfriends;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class KingsCorner extends Game{
	/**
	 * The turn order of the game in the current state.
	 * Order does matter here.
	 */
	public List<Player> turnOrder;
	// The index of turn order, not the current Id of the player.
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
		//We have to consider if a player is an Artificial Player or user when
		//constructing from the database
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
		//We have to consider if a player is an Artificial Player or user when
		//constructing from the database
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
		//If the game is not over and the move is valid.
		if(!gameIsOver() && move.isValid()){
			//Apply the move and save it to the list of moves
			move.apply();
			addMove(move);
			//Check if the move has made a player win
			if(gameIsOver()){
				setToWinState();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Ends the turn of the current user.
	 * In King's Corner, a turn end is marked by drawing a card,
	 * so in this method, a card is drawn for the player.
	 * A card does not need to be drawn in addition to calling endTurn.
	 * @return	True if the tun was successfully ended.
	 */
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
	
	/**
	 * If the current move is an AI, we can call this method to play the AI's moves.
	 * Also, if the next player(s) is also an AI, this method will play all those
	 * AI's moves.
	 * @return	True if any AI moves were made.
	 */
	public boolean applyAIMoves(){
		boolean result = false;
		Player cur = getCurrentPlayerObject();
		Pile aiHand;
		Map<Integer,Pile> visiblePiles = getGameState().getVisiblePiles();
		Move m = null;
		//While the game is still active and the current player is an AI
		while(this.isActive && isAI(cur)){
			result = true;
			aiHand = getGameState().userHands.get(Integer.toString(cur.get_id()));

			ArtificialPlayer ai = (ArtificialPlayer) cur;
			boolean hasMove = true;
			//Get as many moves from the player as we can (until null)
			while(this.isActive && hasMove){
				m = ai.createMove(aiHand, visiblePiles);
				if(m != null){
					this.applyMove(m);
				}else{
					hasMove = false;
				}				
			}
			//End the turn
			this.endTurn();
			//Get next player
			cur = getCurrentPlayerObject();
		}
		return result;
	}
	
	/**
	 * Checks if th player is an AI for the King's Corner game.
	 * @param p	A player
	 * @return	True if the player is an AI.
	 */
	public static boolean isAI(Player p){
		int id = p.get_id();
		return id < 0;
	}
	
	/**
	 * Gets the Player object of the current player.
	 * @return	The current Player
	 */
	public Player getCurrentPlayerObject(){
		return turnOrder.get(currentPlayer);
	}
	
	/**
	 * Returns the GameState as a KCGameState object.
	 */
	public KCGameState getGameState(){
		return (KCGameState) super.getGameState();
	}
	
	@Override
	protected final GameState newGameState(List<Player> players) {
		GameState game = new KCGameState();
		game.initializeToNewGameState(players);
		return game;
	}
	
	//Checks if any of the player hands are empty.
	@Override
	public boolean gameIsOver(){
		for(Pile p : getGameState().userHands.values()){
			if(p.isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the winning player of the game, if there is one.
	 * @return	The winning Player, or null if there is none.
	 */
	public Player getWinner(){
		if(gameIsOver()){
			return getCurrentPlayerObject();
		}
		return null;
	}
	
	/**
	 * Sets the game to a win state - meaning someone has won.
	 */
	private void setToWinState(){
		isActive = false;
		winner_id = getCurrentPlayerObject().get_id();
		this.updateLeaderboard();
	}
	
	/**
	 * Sets the game to a non-winning end state.
	 * For example, if a player quits the game, the whole game might end.
	 * Currently, this is not implemented on the front-end.
	 */
	private void setToNonWinningEndState(){
		isActive = false;
		winner_id = null;
	}

	//The string identifier for King's Corner
	@Override
	protected String getGameTypeIdentifier() {
		return GlobalConstants.KINGS_CORNER;
	}
}
