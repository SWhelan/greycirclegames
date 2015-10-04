package cardswithfriends;

import java.util.Collection;
import java.util.LinkedList;

public class Pile {
	private LinkedList<Card> cards;

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public Card getFirst() {
		return cards.getFirst();
	}

	public Card getLast() {
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

	public boolean addAll(Collection<? extends Card> c) {
		return cards.addAll(c);
	}
	
	
}
