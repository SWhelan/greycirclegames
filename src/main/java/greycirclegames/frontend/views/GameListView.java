package greycirclegames.frontend.views;

import java.util.LinkedList;
import java.util.List;

import greycirclegames.ArtificialPlayer;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;

public class GameListView {
	public List<GameEntryView> currentTurn = new LinkedList<GameEntryView>();
	public List<GameEntryView> othersTurn = new LinkedList<GameEntryView>();
	public List<GameEntryView> ended = new LinkedList<GameEntryView>();
	
	public GameListView(List<? extends Game<? extends Move, ? extends GameState, ? extends ArtificialPlayer>> games, int userId, String gameRoute) {
		games
		.stream()
		.map(e -> new GameEntryView(e, gameRoute))
		.forEach(e -> {
			if(e.isActive){
				if(e.currentPlayerId == userId){
					currentTurn.add(e);
				} else {
					othersTurn.add(e);
				}
			} else {
				ended.add(e);
			}
		});
	}
	
}
