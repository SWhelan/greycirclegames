package greycirclegames.frontend.views;

import java.util.List;

public class HandView {
	public List<CardView> cards;
	public String username;
	public boolean isCurrentPlayer;
	
	public HandView(List<CardView> cards, String username, boolean isCurrentPlayer){
		this.cards = cards;
		this.username = username;
		this.isCurrentPlayer = isCurrentPlayer;
	}
}
