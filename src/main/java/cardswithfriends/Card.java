package cardswithfriends;

import com.mongodb.ReflectionDBObject;

public class Card extends ReflectionDBObject {
	private int number;
	
	//The ordinal vlaue of the suit (index, e.g. Spade is 1, Diamond is 2 ...)
	//need to do this for enum to go into the db, mongo sucks with enums
	//just use the decodeSuit/encodeSuit to access the enum
	int suitOrdinal;	
	
	private Card(int number, Suit suit) {
		super();
		this.number = number;
		this.suitOrdinal = suit.ordinal();
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
	public void setNumber(int number) {
		this.number = number;
	}
	public Suit decodeSuit() {
		return Suit.values()[suitOrdinal];
	}
	public void encodeSuit(Suit suit) {
		this.suitOrdinal = suit.ordinal();
	};
	public int getSuitOrdinal() {
		return suitOrdinal;
	}
	public void setSuitOrdinal(int suitOrdinal) {
		this.suitOrdinal = suitOrdinal;
	}

	public boolean isRed(){
		return decodeSuit() == Suit.DIAMOND || decodeSuit() == Suit.HEART;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Card){
			Card other = (Card) o;
			return other.getNumber() == this.getNumber() && other.decodeSuit() == this.decodeSuit();
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
		
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public void setHtml(String html) {
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
