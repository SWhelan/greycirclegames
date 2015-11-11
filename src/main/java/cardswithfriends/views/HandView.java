package cardswithfriends.views;

import java.util.List;

public class HandView {
	public List<CardView> cards;
	public String userName;
	
	public HandView(List<CardView> cards, String userName){
		this.cards = cards;
		this.userName = userName;
	}
}
