package cardswithfriends.views;

import java.util.LinkedList;
import java.util.List;

import cardswithfriends.DBHandler;
import cardswithfriends.KingsCorner;
import cardswithfriends.Player;

public class KingsCornerEntryView {
	public boolean isActive;
	public String winner;
	public int gameId;
	public String currentPlayerName;
	public List<String> players = new LinkedList<String>();
	public KingsCornerEntryView(KingsCorner kc){
		gameId = kc.get_id();
		currentPlayerName = kc.getCurrentPlayerObject().getUserName();
		for(Player p : kc.turnOrder){
			players.add(p.getUserName());
		}
		this.isActive = kc.getIsActive();
		
		if(!kc.getIsActive()){
			Player user = DBHandler.getUser(kc.getWinner_id());
			if(user != null){
				this.winner = user.getUserName();
			}
		}
	}
}
