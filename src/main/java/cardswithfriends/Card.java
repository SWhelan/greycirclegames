package cardswithfriends;

import com.mongodb.ReflectionDBObject;

public class Card extends ReflectionDBObject {
	private int number;
	private Suit suit;
	
	private Card(int number, Suit suit) {
		super();
		this.number = number;
		this.suit = suit;
	}
	
	public static Card make(int number, Suit suit) throws IllegalArgumentException {
		if(number > 0 && number < 14){
			return new Card(number, suit);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public int getNumber() {
		return number;
	}
	public Suit getSuit() {
		return suit;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public boolean isRed(){
		return suit == Suit.DIAMOND || suit == Suit.HEART;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Card){
			Card other = (Card) o;
			return other.getNumber() == this.getNumber() && other.getSuit() == this.getSuit();
		}
		return false;
	}

	public enum Suit {
		SPADE("Spade", "&#9824;"),
		DIAMOND("Diamond", "&#9826;"),
		HEART("Heart", "&#9825;"),
		CLUB("Club", "&#9827;");
		
		private String displayName;
		private String html;
		
		private Suit(String displayName, String html){
			this.displayName = displayName;
			this.html = html;
		}
		
		public String getDisplayName(){
			return this.displayName;
		}
		
		public String getHtml(){
			return this.html;
		}
	};
}
