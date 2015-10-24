package cardswithfriends;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Pile implements Serializable{
	private LinkedList<Card> cards;
	public final String name;
	
	public Pile(String n){
		name = n;
		cards = new LinkedList<Card>();
	}

	public boolean removeAll(Pile toRemove) {
		return cards.removeAll(toRemove.cards);
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
	
	public List<Card> getCards(){
		List<Card> cardsInPile = new LinkedList<Card>();
		cardsInPile.addAll(cards);
		return cardsInPile;
	}
	
	public static Pile makeDeck(String n){
		Pile p = new Pile(n);
		for(int i = 1; i < 14; i++){
			for(Card.Suit s : Card.Suit.values()){
				p.add(Card.make(i, s));
			}
		}
		
		return p;
	}
	
	public static void shuffle(Pile p){
		Collections.shuffle(p.cards);
	}
	
	public boolean equals(Object o){
		if(o instanceof Pile){
			Pile p = (Pile) o;
			return p.cards.equals(this.cards);
		}
		return false;
	}
	
}
