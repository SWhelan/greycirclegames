package cardswithfriends;

import java.util.Collection;
import java.util.LinkedList;

public class Pile {
	private LinkedList<Card> cards;
	public String name;

	public boolean removeAll(Pile toRemove) {
		return cards.removeAll(toRemove.cards);
	}

	public Card removeFirst() {
		return cards.removeFirst();
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public Card getTop() {
		return cards.getFirst();
	}

	public Card getBottom() {
		return cards.getLast();
	}

	public void addFirst(Card e) {
		cards.addFirst(e);
	}

	public boolean contains(Object o) {
		return cards.contains(o);
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
	
	
}
