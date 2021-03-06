package code;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextField;

import code.Location;
import code.CodenamesList;
import code.PersonAssignments;
import code.InvalidCountException;
/**
 * 
 * @author allllll of us
 * encompasses all function of the code name game
 *
 */
//Have to add notifyObserver to all methods that need to run update on GUI
public class assign2 {
	/** 
	 * the arraylist of observers
	 */
	private ArrayList<Observer> _observers;
	/**
	 * the font of the words in GUI
	 */
	public int font =20; 
	/**
	 * the arraylist of codenames to be assigned to a location instance
	 */
	private ArrayList<String>codenames;
	/**
	 * the arraylist of people assignments to be assigned to a location instance
	 */
	private ArrayList<String>person;
	/**
	 * hashmap with codenames as keys which are paired to a person assignment
	 */
	private HashMap<String,String>assignedCodeName;
	/**
	 * hashmap with codenames as keys paired to a boolean value to determine if revealed or not
	 */
	private HashMap<String,Boolean> Reveal;
	/**
	 * int to keep track of which team's turn it is
	 */
	private int turnCount;
	/**
	 *  placeholder int used to check if there was a change in number of red agents revealed
	 */
	private int count2red;
	/**
	 *  placeholder int used to check if there was a change in number of blue agents revealed
	 */
	private int count2blue;
	
	private int count2green;
	/**
	 * total number of red agents left to be revealed
	 */
	public int redTotal;
	/**
	 * total number of blue agents left to be revealed
	 */
	public int blueTotal;
	
	public int greenTotal; 
	public int assassinTotal;
	public boolean RRA=false;//red reveal assassin
	public boolean BRA=false;//blue
	public boolean GRA=false;//green
	/**
	 * creates a matrix for all the codenames
	 */
	private Location[][] board;
	/**
	 * keeps track of the number of guesses a team has after their spymaster has given a clue
	 */
	public int count=0;
	
	public assign2() {
		redTotal = 6;
		blueTotal = 5;
		greenTotal=5;
		codenames =new ArrayList<String>();
		person =  new ArrayList<String>();
		turnCount =  0;
		count2red = 0;
		count2blue = 0;
		count2green = 0;
		assassinTotal=2;
		board = new Location[5][5];
		_observers=new ArrayList<>();
	}
	
	/**
	 * sets the parameter to the instance variable
	 * @param codename arraylist being passed in
	 */
	private void setCodenames(ArrayList<String>codename){
		this.codenames=codename;
	}
	
	/**
	 * sets the parameter to the instance variable
	 * @param person arraylist being passed in
	 */
	private void setPerson(ArrayList<String>person) {
		this.person=person;
	}
	/**
	 * makes it so one can access the private codenames variable
	 * @return the codenames arraylist
	 */
	public ArrayList<String> getcodename(){
		return codenames;
	}
	/**
	 * makes it so one can access the private person variable
	 * @return the person arraylist
	 */
	public ArrayList<String> getperson(){
		return person;
	}
	
	/**
	 * this method does the initialization that needed at the start of 
	 * each new game:
	 *  -setting red team to go first
	 *  -create new instance of CodenameList and PersonAssignments
	 *  -setting the ArrayList<String> instance variables codenames and person
	 *  -mapping each codenames and person in Hashmap, and mapping each codenames to not revealed.
	 *  -create a board with 25 Locaton instance variable and assign to different codenames.
	 *  @param CodenamesList cod - an instance of the CodenamesList class
	 *  @param PersonAssignments per - an instance of the PersonAssignments class
	 */
	public void gameStarted() throws FileNotFoundException, IOException{//set up for the game.
		CodenamesList cod = new CodenamesList();
		PersonAssignments2 per=new PersonAssignments2();
		turnCount=0;//red team's turn
		redTotal = 6;
		blueTotal = 5;
		greenTotal = 5;
		RRA=false;
		BRA=false;
		GRA=false;
		assassinTotal=2;
		assignedCodeName=new HashMap<String,String>();
		Reveal=new HashMap<String,Boolean>();// boolean with be true if is revealed, false will be not reveals
		setCodenames(cod.getList());
		setPerson(per.getList());
		assignCodeName(codenames,person);
		board=namesOnBoard(codenames);
	}
	
	/**
	 * Mapping each codenames to a person and save in a HashMap.
	 * Mapping each codenames to the state of not revealed and save in a HashMap.
	 * @param ArrayList<String> codename - the codename variable
	 * @param ArrayList<String agents - the person variable
	 */
	private void  assignCodeName(ArrayList<String> codename,ArrayList<String>person){//map each codenames to the person
		HashMap<String, String> assign=new HashMap<String,String>();
		for(int i=0;i<codename.size();i++) {
			assign.put(codename.get(i),person.get(i));
		}
		assignedCodeName=assign;

		for(String y:codename) {
			Reveal.put(y, false); //false means that the codenames is not reveal.
		}	
	}
	
	/**
	 * method that decrements the agent count and ends when a wrong agent, an innocent bystander, or an assasin is revealed
	 * @param theLocation - an instance of the Location class
	 * @return the boolean value true or false - false meaning the turn ends and the other team goes, true meaning the turn continues and the count is decremented
	 */
	public boolean updateLocation(Location theLocation) {
		if(RRA) {redTotal=-1;}
		if(BRA) {blueTotal=-1;}
		if(GRA) {greenTotal=-1;}
			
		
		Reveal.put(theLocation.getName(), true); //set the code name related to the location to revealed
		if(count>0) {
		if(turn()==0) {//if it is reds turn
		if(assignedCodeName.get(theLocation.getName())=="red agent") {
			count--;
			redTotal--;//decrement total red agents
			return true;
		}
		else if(assignedCodeName.get(theLocation.getName())=="blue agent") {
			blueTotal--;//decrement total blue agents
			//turn count should be changed here probably 
			changeTurn();
			return false;
		}
		else if(assignedCodeName.get(theLocation.getName())=="green agent") {
			greenTotal--;
			changeTurn();
			return false;
		}
		}
		if(turn()==1) {//if its blue turn
			
			if(assignedCodeName.get(theLocation.getName())=="blue agent") {
				count--;
				blueTotal--;
				return true;
			}
			else if(assignedCodeName.get(theLocation.getName())=="red agent") {
				
				redTotal--;
				//turn count should be changed here probably 
				changeTurn();
				
				return false;
			}
			else if(assignedCodeName.get(theLocation.getName())=="green agent") {
				greenTotal--;
				changeTurn();
				return false;
			}
		}
		if(turn()==2) {
			if(assignedCodeName.get(theLocation.getName())=="green agent") {
				count--;
				greenTotal--;
				return true;
			}
			else if(assignedCodeName.get(theLocation.getName())=="red agent") {
				redTotal--;
				//turn count should be changed here probably 
				changeTurn();
				return false;
			}
			else if(assignedCodeName.get(theLocation.getName())=="blue agent") {
				blueTotal--;
				changeTurn();
				return false;
			}
			
		}
		if(assignedCodeName.get(theLocation.getName())=="innocent bystander") {
			changeTurn();
			return false;
		}
		if(assignedCodeName.get(theLocation.getName())=="assassin") {
			if(turn()==0) {
				RRA=true;
			}else if(turn()==1) {
				BRA=true;
			}else if(turn()==2) {
				GRA=true;
			}
			changeTurn();
			return false;
		}	
		}
		changeTurn();
		return false;
		
	}
	
	/**
	 * Take in a ArrayList of strings of random codenames and create a new Location array 
	 * base on the arraylist.
	 * @param ArrayList<String> codename - an arraylist of codenames
	 * @return Location array with codenames store.
	 */
	private Location[][] namesOnBoard(ArrayList<String>codename){
		
		Location[][] l=new Location[5][5];
		int indexInCode=0;//index in the arraylist of the hashmap keset which are the unreveal codenames.
		for(int i=0;i<l.length;i++) {//loop and get the codename to show on the board.
			for(int j=0;j<l[0].length;j++) {
				if(indexInCode<25) {
					l[i][j]=new Location(codename.get(indexInCode));
					indexInCode=indexInCode+1;
				}
			}
		}
		return l; //return the board with string on each index
	}
	/**
	 * if turn returns 1 it is red turn if turn return 0 it is blue turn
	 * @return return an int of either 1 or 0
	 */
	public int turn() {//showing who's turn
		return turnCount;
	}
	public void changeTurn() {
			turnCount++;
			if(turnCount==3) {
				turnCount=0;}
			
		if(BRA==true) {
			if(turnCount==1) {
				turnCount=2;
			}
		}
		if(RRA==true) {
			if(turnCount==0) {
				turnCount=1;
			}
			
		}
		if(GRA==true) {
			if(turnCount==2) {
				turnCount=0;
			}
		}
	}
	/**
	 * Passing in a clue as string, which in clue one word and a number that separated by comma.
	 * Split the string by comma. The clue will be legal if the clue does not include word same as 
	 * one of the codenames that is not reveal. If the clue contain codename that is reveal, the clue 
	 * is also legal. If clue contains word that same as codename that is not revealed, then the clue is illegal.
	 * If the count number given by the clue is equal or less than zero, then throw the InvalidCountException.
	 * @return boolean value that indicate whether clue is legal or illegal.
	 * @throws InvalidCountException 
	 */
	public boolean clue(String aClue) {//passing in one word  for a clue.
		if(aClue=="") {
			return false;
		}
		aClue.toUpperCase();
		boolean legal=true;
			for(String c:Reveal.keySet()) {
				if(aClue.equals(c)&&Reveal.get(c)) {//if the clue has words that same as the code name that is revealed then is fine.
					legal=true;
				}
				if(aClue.equals(c)&&Reveal.get(c)==false) {//if clue has words same as codename that is not revealed if illegal.			
					this.count=-1;
					return false;
				}
				if(aClue.contains(c)) {
					return false;
				}
		}
		return legal;
	}
	
	public boolean count(int countnum) throws InvalidCountException{
		boolean legal=true;
		if(countnum<0||countnum==0||countnum>6) {
			throw new InvalidCountException();
		}
		this.count=countnum;
		notifyObservers();
		return legal;
	}
	
	/**
	 * get's the value of count
	 * @return the value of count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @return The board that has initialize 25 new Location instances variable.
	 */
	public Location[][] getBoard(){
		return board;
	}
	/**
	 * @return The hashmap that mapping the codenames with person.
	 */
	public HashMap<String,String> getAssignedCodeNameandValues(){
	return assignedCodeName;
	}
	/**
	 * @return The hashmap that shows that whether a location is reveal or not.
	 */
	public HashMap<String,Boolean> getReveal(){
		return Reveal;
	}
	/**
	 * @return The total number of red agents remain.
	 */
	public int getRedTotal() {
		return redTotal;
	}
	/**
	 * @return The total number of blue agents remain.
	 */
	public int getBlueTotal() {
		return blueTotal;
	}
	public int getGreenTotal() {
		return greenTotal;
	}
	/**
	 * checkes to see if the board is in a winning state
	 * @return an int that describes the type of winning state the board is in
	 */
	public int winningState() {
		int playerTurn = turn();// if player turn equals 1 it is red's turn. if player turn equals 0 it is blue's turn
		int count1blue = 0;
		int count1red = 0;
		int count1green=0;
		int count1assassin=0;
		for(String code: Reveal.keySet()) {
			if(Reveal.get(code)) {//iteration through hashmap to see which code names are associated with a true value
				if(assignedCodeName.get(code)=="assassin") { // iteration through hashmap to see which role is associated with revealed code name 
					count1assassin++;		
					if(count1assassin==2) {
					return 0; }//return 0 means turn needs to be checked to find winner
				}
				if(assignedCodeName.get(code)=="blue agent") {
					count1blue++;
					if(count1blue==5&&BRA==false) {
						return 1;//return 1 means game is in winning state and blue wins
					}
				}
				if(assignedCodeName.get(code)=="red agent") {
					count1red++;
					if(count1red==6&&RRA==false) {
						return -1;// return -1 means game is in winning state and red wins
					}
				}
				if(assignedCodeName.get(code)=="green agent") {
					count1green++;
					if(count1green==5&&GRA==false) {
						return 2;//return 2 means game is in winning state and green wins
					}
				}
			}
		}
			assassinTotal=count1assassin;
			count2red = count1red;
			count2blue = count1blue;
			count2green=count1green;
		return 10;//means board not in game winning state
	}
	
	/**
	 * Returns the name of the team who won the game. Returns null if the game hasn't started or 
	 * the game is not ended yet.
	 * @return the name of the team won the game.
	 */
	public String teamWon() {
		if(winningState()==0) {
			if(RRA==false) {
				return "RED TEAM";
			}
			if(BRA==false) {
				return "BLUE TEAM";
			}
			if(GRA==false) {
				return "GREEN TEAM";
			}
		}
		return null;
	}
	/**
	 * sets the turn for ease of testing
	 * @param turn
	 */
	public void setTurns(int turn) {//makes it easier to test
		turnCount=turn;
	}
	
	/**
	 * adds GUI as an observer
	 */
	public void addObserver(Observer obs) {
		_observers.add(obs);
		notifyObservers();
	}

	/**
	 * updates the GUI
	 */
	public void notifyObservers() {
		for (Observer obs : _observers) {
			obs.update();
		}
	}

	}
	
