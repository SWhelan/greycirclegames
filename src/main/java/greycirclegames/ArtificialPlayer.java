package greycirclegames;

import com.mongodb.ReflectionDBObject;

import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;

public abstract class ArtificialPlayer extends ReflectionDBObject implements Player {
	protected int _id;
	protected String username;
	
	public ArtificialPlayer(int id){
		this._id = id;
		this.username = getDefaultUsername(id);
	}
	
	public static String getDefaultUsername(int id){
		return "Computer Player " + Integer.toString(Math.abs(id));
	}
	
	@Override
	public Integer get_id() {
		return _id;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public abstract Move createMove(Game<? extends Move, ? extends  GameState, ? extends ArtificialPlayer> game);
}
