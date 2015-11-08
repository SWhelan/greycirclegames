package cardswithfriends;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class Card extends ReflectionDBObject {
	private int number;
	
	//The ordinal vlaue of the suit (index, e.g. Spade is 1, Diamond is 2 ...)
	//need to do this for enum to go into the db, mongo sucks with enums
	//just use the decodeSuit/encodeSuit to access the enum
	private int suitOrdinal;
	
	//Still need to keep the suit on the class to access from mustache templates
	private Suit suit;
	
	public Card(int number, Suit suit) {
		super();
		this.number = number;
		this.suitOrdinal = suit.ordinal();
		this.setEnumSuit(suit);
	}
	
	public Card(BasicDBObject obj) {
		this.number = (Integer)obj.get("Number");
		this.suitOrdinal = (Integer)obj.get("SuitOrdinal");
		this.suit = decodeSuit();
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
	public Suit decodeSuit() {
		return Suit.values()[suitOrdinal];
	}
	public void encodeSuit(Suit suit) {
		this.suitOrdinal = suit.ordinal();
	}
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

	//The two must have different names since we don't want ReflectionDBObject to try to put in the db.
	public Suit getSuit() {
		return suit;
	}
	public void setEnumSuit(Suit suit) {
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
