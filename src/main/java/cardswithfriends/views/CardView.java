package cardswithfriends.views;

import cardswithfriends.Card;
import cardswithfriends.Card.Suit;

public class CardView {
	public boolean isFirst;
	public boolean isRed;
	public int number;
	public String displayNumber;
	public int suitOrdinal;
	public Suit suit;
	
	public CardView(Card card){
		this.number = card.getNumber();
		this.suitOrdinal = card.getSuitOrdinal();
		this.displayNumber = card.getDisplayNumber();
		this.suit = card.getSuit();
		this.isRed = card.isRed();
	}
	
	public void setFirst(Boolean isFirst){
		this.isFirst = isFirst;
	}
	
}
