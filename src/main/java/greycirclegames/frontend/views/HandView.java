package greycirclegames.frontend.views;

import java.util.List;

public class HandView {
	public List<CardView> cards;
	public String userName;
	public boolean isCurrentPlayer;
	
	public HandView(List<CardView> cards, String userName, boolean isCurrentPlayer){
		this.cards = cards;
		this.userName = userName;
		this.isCurrentPlayer = isCurrentPlayer;
	}
}
