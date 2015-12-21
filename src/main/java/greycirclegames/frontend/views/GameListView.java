package greycirclegames.frontend.views;

import java.util.LinkedList;
import java.util.List;

import greycirclegames.games.Game;

public class GameListView {
	public List<GameEntryView> currentTurn = new LinkedList<GameEntryView>();
	public List<GameEntryView> othersTurn = new LinkedList<GameEntryView>();
	public List<GameEntryView> ended = new LinkedList<GameEntryView>();
	
	public GameListView(List<? extends Game> games, int userId) {
		games
		.stream()
		.map(e -> new GameEntryView(e))
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
