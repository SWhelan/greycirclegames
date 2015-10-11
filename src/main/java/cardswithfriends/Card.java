package cardswithfriends;

public class Card {
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
