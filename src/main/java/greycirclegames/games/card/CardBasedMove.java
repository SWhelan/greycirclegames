package greycirclegames.games.card;

import greycirclegames.ArtificialPlayer;
import greycirclegames.DBHandler;
import greycirclegames.games.Move;
import greycirclegames.games.card.kingscorner.KCPile;

/**
 * A Move encapsulates a move of a card game.
 * @author George
 *
 */
public abstract class CardBasedMove extends Move {
	//The pile where the moved cards are coming from.
	protected Pile origin;
	//The pile which is moving (often may be a single card.
	protected Pile moving;
	//The pile to which we are moving the moving cards to.
	protected Pile destination;

	public CardBasedMove(){
	
	}
	
	/**
	 * Constructor.
	 * @param user	The player who is proposing this move
	 * @param origin the pile that the card or entire pile was moved from
	 * @param moving the card or pile being moved
	 * @param destination the pile to place the card or pile onto
	 */
	public CardBasedMove(Integer playerId, Pile origin, Pile moving, Pile destination){
		this.playerId = playerId;
		this.origin = origin;
		this.moving = moving;
		this.destination = destination;
	}
	
	/**
	 * Gets the origin pile.
	 * @return
	 */
	public Pile getOrigin() {
		return origin;
	}
	
	/**
	 * Sets the origin pile.
	 * @param origin
	 */
	public void setOrigin(Pile origin) {
		this.origin = origin;
	}
	
	/**
	 * Gets the moving pile.
	 * @return
	 */
	public Pile getMoving() {
		return moving;
	}
	
	/**
	 * Sets the moving pile.
	 * @param moving
	 */
	public void setMoving(Pile moving) {
		this.moving = moving;
	}
	
	/**
	 * Gets the destination pile.
	 * @return
	 */
	public Pile getDestination() {
		return destination;
	}
	
	/**
	 * Sets the destination pile.
	 * @param destination
	 */
	public void setDestination(Pile destination) {
		this.destination = destination;
	}

	/**
	 * Gets the name of the origin pile.
	 * @return
	 */
	public String getOriginName() {
		return origin.getName();
	}

	/**
	 * Gets the name of the moving pile.
	 * @return
	 */
	public String getMovingName() {
		return moving.getName();
	}

	/**
	 * Gets the name of the destination pile.
	 * @return
	 */
	public String getDestinationName() {
		return destination.getName();
	}
	
	/**
	 * Generates a string describing the move.
	 */
	@Override
	public String toString(){
		//"Player name" placed card on pile X or Moved Pile X onto Pile Y
		StringBuilder builder = new StringBuilder();
		if(playerId < 0){
			builder.append(ArtificialPlayer.getDefaultUsername(playerId));
		} else {
			builder.append(DBHandler.getUser(playerId).getUsername());
		}
		builder.append(" moved ");
		if(origin.getName().equals(KCPile.DRAW_PILE.getPrettyName())){
			builder.append("a card ");
		}else{
			int count = 0;
			for(Card e : this.moving.getCards()) {
				builder.append(e.toString());
				if(this.moving.size() <= 1 || count == this.moving.size() - 1){
					builder.append(" ");
				} else if(count == this.moving.size() - 2){
					if(this.moving.size() != 2){
						builder.append(",");
					}
					builder.append(" and ");
				} else if(count != this.moving.size() - 1){
					builder.append(", ");
				}
				count++;
			};
		}
		
		builder.append("from ");
		builder.append(this.origin.getName());
		builder.append(" onto ");
		builder.append(this.destination.getName());

		return builder.toString();
	}
	
	/**
	 * An inner test class for testing.
	 * @author George
	 *
	 */
	public class Test{
		public boolean namesEqual(String username, String originName, String movingName, String destinationName){
			return	origin.getName().equals(originName) &&
					moving.getName().equals(movingName) &&
					destination.getName().equals(movingName);
		}
 	}
}
