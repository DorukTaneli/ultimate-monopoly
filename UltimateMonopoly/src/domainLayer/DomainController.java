package domainLayer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import domainLayer.squares.DeedSquare;
import domainLayer.squares.PropertySquare;
import domainLayer.squares.Square;
import uiLayer.AppWindow; //THIS SHOULDNT BE HERE ************ DOING THIS TO SEE THE PIECES MOVE
import uiLayer.PropertyListener;


/**
 * Domain Controller class is the controller of the Domain.
 * Utilizes Controller Pattern. Delegates Messages from UI to relevant classes in the Domain.
 * @author SAWCON
 */

public final class DomainController {
	private static volatile DomainController instance = null;
	
	private static  int PLAYERS_TOTAL;
	private ArrayList<Player> players = new ArrayList<Player>(PLAYERS_TOTAL);
	private Board board = new Board();
	private Cup cup; //might not be needed in DC, might be needed in order to save the game.
	private int turn = 0;
	private static AppWindow app;
	public boolean playersAreSet=false;
	
	/**
	 * Creates Domain Controller Object.
	 * Initializes Appwindow, Players, Cup, and PropertyListeners
	 * @param Appwindow Object to be set 
	 */
	
	public DomainController(AppWindow aw) {
		Player p;
		this.app=aw;
		PLAYERS_TOTAL = aw.getNumOfPlayers();
		System.out.println("Num of players: "+PLAYERS_TOTAL);
		players = new ArrayList<Player>();
		for(int i=0;i<PLAYERS_TOTAL;i++) {
			p = new Player("Player"+i, board);
			players.add(p);
		}
		
		for(Player player: players) {
				player.addPropertyListener(app);
		}
		playersAreSet=true;
	}
	/**
	 * Returns the singleton instance of Domain Controller
	 * @return the singleton instance of Domain Controller
	 */
	public static DomainController getInstance() {
		if(instance == null) {
					instance = new DomainController(app);
		}
		
		return instance;
		
	}

	//	public void playGame() {
	//		for (int i = 0; i < ROUNDS_TOTAL; i++) {
	//			playRound();
	//		}
	//	}

	//	private void playRound() {
	//	for (Iterator iter = players.iterator(); iter.hasNext();) {
	//		Player player = (Player) iter.next();
	//		player.takeTurn();
	//	}
	//}

	/**
	 * Gets the current PLayer
	 * @return the current Player
	 */
	public Player getCurrentPlayer() {
		return (Player) players.get(turn);
	}
	
	/**
	 * Gets the Players List
	 * @return the Players List
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the Location Index of the Player with given index
	 * @param Index of player
	 * @return LocationIndex of Player
	 */
	public Player getPlayerLocationIndex(int a) {
		return players.get(a);
	}

	/**
	 * Gets the Board
	 * @return the Board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Passes takeTurn message to the current Player
	 * Updates UI location of player's piece
	 */
	public void rollPressed() {
		Player currentPlayer = getCurrentPlayer();
		currentPlayer.takeTurn();
		//TODO make the following observer pattern
		app.updatePieceGUILocation();
	}

	/**
	 * If the location of the current player is a PropertySquare,
	 * The player attempts to purchase that square.
	 */
	public void buyPressed() {
		Player p = getCurrentPlayer();
		Square sq = p.getLocation();
		if(sq instanceof PropertySquare)
			p.attemptPurchase((PropertySquare)sq);
	}

	public void buildPressed() {
		Player p= getCurrentPlayer();
		Square sq=p.getLocation();
		if(sq instanceof DeedSquare) {
			p.attemptBuild((DeedSquare)sq);
		}
	}

	/**
	 * If the player has rolled,
	 * his turn ends and it is the next player's turn.
	 */
	public void endTurnPressed() {
		Player p = getCurrentPlayer();
		if(p.haveRolled()) {
			p.setHaveRolled(false);
			turn = (turn + 1) % PLAYERS_TOTAL;
		}

	}
}

