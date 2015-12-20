package greycirclegames.games.card;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

/**
 * A class that abstracts a pile of cards.
 * A pile maintains an order, but the order does not always matter.
 * For instance, the order of a user's hand pile, or the draw pile, does not mean anything.
 * However, the order of a game pile is important.
 * 
 * This is more or less a contained class for a Linked List of cards.
 * @author George
 *
 */
public class Pile extends ReflectionDBObject {
	private LinkedList<Card> cards;
	public String name;
	
	public Pile(String n){
		name = n;
		cards = new LinkedList<Card>();
	}
	
	public Pile(String n, List<Card> c){
		name = n;
		cards = (LinkedList<Card>)c;
	}
	
	public Pile(BasicDBObject obj) {
		BasicDBList cards = (BasicDBList)obj.get("Cards");
		this.cards = new LinkedList<Card>();
		for (Object card : cards) {
			this.cards.add(new Card((BasicDBObject)card));
		}
		this.name = (String)obj.get("Name");
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCards(LinkedList<Card> cards) {
		this.cards = cards;
	}
	public List<Card> getCards(){
		List<Card> cardsInPile = new LinkedList<Card>();
		cardsInPile.addAll(cards);
		return cardsInPile;
	}

	public boolean removeAll(Pile toRemove) {
		return cards.removeAll(toRemove.cards);
	}
	
	public Card get(int index){
		return cards.get(index);
	}

	public Card getTop() {
		return cards.getLast();
	}

	public Card getBottom() {
		return cards.getFirst();
	}
	
	public Card removeTop(){
		return cards.removeLast();
	}

	public void addOn(Card e) {
		cards.addLast(e);
	}

	public boolean contains(Object o) {
		return cards.contains(o);
	}
	
	public boolean containsAll(Pile p){
		return cards.containsAll(p.cards);
	}

	public int size() {
		return cards.size();
	}

	public boolean add(Card e) {
		return cards.add(e);
	}

	public boolean addAll(Pile c) {
		return cards.addAll(c.cards);
	}
	
	public boolean isEmpty(){
		return cards.isEmpty();
	}
	
	/**
	 * Makes a complete deck named n.
	 * @param n	The name of the deck.
	 * @return	The new deck.
	 */
	public static Pile makeDeck(String n){
		Pile p = new Pile(n);
		for(int i = 1; i < 14; i++){
			for(Card.Suit s : Card.Suit.values()){
				p.add(Card.make(i, s));
			}
		}
		
		return p;
	}
	
	/**
	 * Shuffles a given pile.
	 * All possible combinations have a roughly uniform probability of being output.
	 * @param p
	 */
	public static void shuffle(Pile p){
		Collections.shuffle(p.cards);
	}
	
	public boolean equals(Object o){
		if(o instanceof Pile){
			Pile p = (Pile) o;
			return this.cardsAreEqual(p);
		}
		return false;
	}
	
	public boolean cardsAreEqual(Pile p){
		return p.cards.equals((this.cards));
	}
	
	public boolean namesAreEqual(Pile p){
		return p.getName().equals(this.getName());
	}
	
}
