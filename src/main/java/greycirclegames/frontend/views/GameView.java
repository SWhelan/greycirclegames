package greycirclegames.frontend.views;

import greycirclegames.Player;

public class GameView {
	public Integer refreshRate;
	
	public GameView(Player viewingPlayer) {
		refreshRate = viewingPlayer.getRefreshRateForViewing();
	}
}
