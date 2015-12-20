package greycirclegames.games.board.circles;

import greycirclegames.Player;

public class CirclesArtificialPlayer implements Player {
	private int id;

	public CirclesArtificialPlayer(int id){
		this.id = id;
	}
	
	@Override
	public Integer get_id() {
		return id;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateWin(String game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLoss(String game) {
		// TODO Auto-generated method stub
		
	}

}
