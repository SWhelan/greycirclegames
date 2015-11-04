package cardswithfriends;

import com.mongodb.ReflectionDBObject;

public class Card extends ReflectionDBObject {
	private int number;
	private String displayNumber;
	
	//The ordinal vlaue of the suit (index, e.g. Spade is 1, Diamond is 2 ...)
	//need to do this for enum to go into the db, mongo sucks with enums
	//just use the decodeSuit/encodeSuit to access the enum
	int suitOrdinal;
	
	//Still need to keep the suit on the class to access from mustache templates
	private Suit suit;
	
	public Card(int number, Suit suit) {
		super();
		this.number = number;
		this.suitOrdinal = suit.ordinal();
		this.setSuit(suit);
		setDisplayNumber();
	}
	
	public static Card make(int number, Suit suit) throws IllegalArgumentException {
		if(suit != null && number > 0 && number < 14){
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
	
	private void setDisplayNumber(){
		switch (number){
			case 13: displayNumber = "K";
				break;
			case 12: displayNumber = "Q";
				break;
			case 11: displayNumber = "J";
				break;
			default: displayNumber = Integer.toString(number);
		}
	}
	
	public String getDisplayNumber(){
		return this.displayNumber;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (number != other.number)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
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
