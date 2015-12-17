package greycirclegames.views;

import java.util.LinkedList;
import java.util.List;

import greycirclegames.DBHandler;

public class KingsCornerListView {
	public List<KingsCornerEntryView> currentTurn = new LinkedList<KingsCornerEntryView>();
	public List<KingsCornerEntryView> othersTurn = new LinkedList<KingsCornerEntryView>();
	public List<KingsCornerEntryView> ended = new LinkedList<KingsCornerEntryView>();
	
	public KingsCornerListView(int userId) {
		DBHandler.getKCGamesforUser(userId)
		.stream()
		.map(e -> new KingsCornerEntryView(e))
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
