package cardswithfriends.views;

import java.util.List;

import cardswithfriends.KingsCorner;
import cardswithfriends.Player;

public class KingsCornerView {
	public int gameId;
	public String currentPlayerName;
	public List<String> players;
	public KingsCornerView(KingsCorner kc){
		gameId = kc.getGameId();
		currentPlayerName = kc.getCurrentPlayer().getUserName();
		for(Player p : kc.turnOrder){
			players.add(p.getUserName());
		}
	}
}
