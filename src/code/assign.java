package code;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import code.Location;
import code.CodenamesList;
import code.PersonAssignments;
import code.InvalidCountException;

public class assign {
	private ArrayList<String>codenames=new ArrayList<String>();
	private ArrayList<String>person=new ArrayList<String>();
	private HashMap<String,String>assginedCodeName;
	private HashMap<String,Boolean> Reveal;
	private int turnCount = 1;
	private int count2red=0;
	private int count2blue=0;
	private int redTotal;
	private int blueTotal;
	private Location[][] board=new Location[5][5];
	public assign() {
		redTotal = 9;
		blueTotal = 8;
	}
	
	private void setCodenames(ArrayList<String>codename){
		this.codenames=codename;
	}
	private void setPerson(ArrayList<String>person) {
		this.person=person;
	}
	public ArrayList<String> getcodename(){
		return codenames;
	}
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
	 */
	public void gameStarted(CodenamesList cod,PersonAssignments per) throws FileNotFoundException, IOException{//set up for the game.
		turnCount=1;//red team's turn
		assginedCodeName=new HashMap<String,String>();
		Reveal=new HashMap<String,Boolean>();// boolean with be true if is revealed, false will be not reveals
		setCodenames(cod.getList());
		setPerson(per.getList());
		assignCodeName(codenames,person);
		board=namesOnBoard(codenames);
	}
	
	/**
	 * Mapping each codenames to a person and save in a HashMap.
	 * Mapping each codenames to the state of not revealed and save in a HashMap.
	 */
	private void  assignCodeName(ArrayList<String>codename,ArrayList<String>agents){//map each codenames to the person.
		HashMap<String, String> assgin=new HashMap<String,String>();
		for(int i=0;i<codename.size();i++) {
			assgin.put(codename.get(i),agents.get(i));
		}
		assginedCodeName=assgin;
		
		for(String y:codename) {
			Reveal.put(y, false); //false means that the codenames is not reveal.
		}	
	}
	
	public boolean updateLocation(Location theLocation) {
		Reveal.put(theLocation.getName(), true); //set the code name related to the location to revealed
		if(turnCount%2!= 0) {//if it is reds turn
		if(assginedCodeName.get(theLocation.getName())=="red agent") {
			redTotal--;//decrement total red agents
			return true;
		}
		else if(assginedCodeName.get(theLocation.getName())=="blue agent") {
			blueTotal--;//decrement total blue agents
			return false;
		}
		}
		if(turnCount%2== 0) {
			if(assginedCodeName.get(theLocation.getName())=="blue agent") {
				redTotal--;
				return true;
			}
			else if(assginedCodeName.get(theLocation.getName())=="red agent") {
				blueTotal--;
				return false;
			}
		}
		return false;
		
	}
	
	/**
	 * Take in a ArrayList of strings of random codenames and create a new Location array 
	 * base on the arraylist.
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
	
	public int turn() {//showing who's turn
		int num=turnCount%2;
		return num;
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
	public boolean clue(String aClue) throws InvalidCountException {//passing in one word and one number for a clue, separate by comma.
		String[] x= aClue.split(",");//try to separate the sentence into words.
		boolean legal=true;
		int count = Integer.parseInt(x[1]);
		if(count<0||count==0) {
			throw new InvalidCountException();
		}
		for(String s:x) {
			for(String c:Reveal.keySet()) {
				if(s.equals(c)&&Reveal.get(c)) {//if the clue has words that same as the code name that is revealed then is fine.
					legal=true;
				}
				if(s.equals(c)&&Reveal.get(c)==false) {//if clue has words same as codename that is not revealed if illegal.
					if(turnCount%2==1) {//if clue is illegal then the team's turn is forfeit
						turnCount=2;
						}
					else if(turnCount%2==0){//should remember to call to update the frame and call observer()
						turnCount=1;
						}
					return false;
				}
				if(s.equals(c)==false) {//if clue doesn't have words same as codename, then it is legal.
					legal=true;
				}
			}
		}
		return legal;
	}
	
	public Location[][] getBoard(){
		return board;
	}
	
	public HashMap<String,String> getAssignedCodeNameandValues(){
	return assginedCodeName;
	}
	
	public HashMap<String,Boolean> getReveal(){
		return Reveal;
	}
	public int getRedTotal() {
		return redTotal;
	}
	public int getBlueTotal() {
		return blueTotal;
	}
	public int winningState() {
		int playerTurn = turnCount%2;// if player turn equals 1 it is red's turn. if player turn equals 0 it is blue's turn
		int count1blue = 0;
		int count1red = 0;
		for(String code: Reveal.keySet()) {
			if(Reveal.get(code)) {//iteration through hashmap to see which code names are associated with a true value
				if(assginedCodeName.get(code)=="assassin") { // iteration through hashmap to see which role is associated with revealed code name 
					return 0; //return 0 means turn needs to be checked to find winner
				}
				if(assginedCodeName.get(code)=="blue agent") {
					count1blue++;
					if(count1blue==8) {
						return 1;//return 1 means game is in winning state and blue wins
					}
				}
				if(assginedCodeName.get(code)=="red agent") {
					count1red++;
					if(count1red==9) {
						return -1;// return -1 means game is in winning state and red wins
					}
				}
			}
		}
		if(playerTurn!=0 && count1red==count2red) {//checks if when it is red teams turn if 1 more red agent was revealed. If so the turn count is not changed
				turnCount++;
			}
			if(playerTurn==0 && count1blue==count2blue) {// checks if when it is blue teams turn if 1 more blue agent was revealed. If so the turn count is not changed
				turnCount++;
			}
			count2red = count1red;
			count2blue = count1blue;
		return 10;//means board not in game winning state
	}
	
	/**
	 * Returns the name of the team who won the game. Returns null if the game hasn't started or 
	 * the game is not ended yet.
	 * @return the name of the team won the game.
	 */
	public String teamWon() {
		if(winningState()==0) {
			if(turnCount%2!=0) {
				return "blue team";
			}
			if(turnCount%2==0) {
				return "red team";
			}
		}
		return null;
	}
	
	public void setTurns(int turn) {//makes it easier to test
		turnCount=turn;
	}
	
	}
	
