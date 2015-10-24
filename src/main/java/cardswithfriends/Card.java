package cardswithfriends;

import java.io.Serializable;

public class Card implements Serializable{
	private final int number;
	private final Suit suit;
	
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
		SPADE("Spade"),
		DIAMOND("Diamond"),
		HEART("Heart"),
		CLUB("Club");
		
		private String displayName;
		
		private Suit(String displayName){
			this.displayName = displayName;
		}
		
		public String getDisplayName(){
			return this.displayName;
		}
	};
}
