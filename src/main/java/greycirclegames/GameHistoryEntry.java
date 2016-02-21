package greycirclegames;

import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class GameHistoryEntry extends ReflectionDBObject {
	private String gameIdentifier;
	private int numGames;
	private int numWon;
	private int numLost;
	private int numTie;
	
	public GameHistoryEntry(){
		// Mongo DB
	}
	
	public GameHistoryEntry(String gameIdentifier, int numGames, int numWon, int numLost, int numTie){
		this.gameIdentifier = gameIdentifier;
		this.numGames = numGames;
		this.numWon = numWon;
		this.numLost = numLost;
		this.numTie = numTie;
	}
	
	public GameHistoryEntry(BasicDBObject obj){
		this.gameIdentifier = (String) obj.get("GameIdentifier");
		this.numGames = (Integer) obj.get("NumGames");
		this.numWon = (Integer) obj.get("NumWon");
		this.numLost = (Integer) obj.get("NumLost");
		this.numTie = (Integer) obj.get("NumTie");
	}
	
	public String getGameIdentifier() {
		return gameIdentifier;
	}
	public void setGameIdentifier(String gameIdentifier) {
		this.gameIdentifier = gameIdentifier;
	}
	public int getNumGames() {
		return numGames;
	}
	public void setNumGames(int numGames) {
		this.numGames = numGames;
	}
	public int getNumWon() {
		return numWon;
	}
	public void setNumWon(int numWon) {
		this.numWon = numWon;
	}
	public int getNumLost() {
		return numLost;
	}
	public void setNumLost(int numLost) {
		this.numLost = numLost;
	}
	public int getNumTie() {
		return numTie;
	}
	public void setNumTie(int numTie) {
		this.numTie = numTie;
	}
	
	public void increaseGameCount(){
		this.numGames++;
	}
	
	public void increaseNumWins(){
		this.numWon++;
	}
	
	public void increaseNumLost(){
		this.numLost++;
	}
	
	public void increaseNumTie(){
		this.numTie++;
	}

}
