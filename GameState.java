
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *This singleton class represents the current state of the Game. It keeps track of a player's
 * current room, inventory, and stores the current dungeon being played.
 *
 *@author Kanghuaroo
 */
public class GameState {

	/**
	 *<tt>Exception</tt> thrown when a passed save file is not formatted correctly.
	 * This could be for a variety of reasons such as a misspelling, missing a delimiter,
	 * or other format errors.
	 *@author Kanghuaroo
	 */
	public static class IllegalSaveFormatException extends Exception {
		public IllegalSaveFormatException(String e) {
			super(e);
		}
	}

	static String DEFAULT_SAVE_FILE = "zork_save";
	static String SAVE_FILE_EXTENSION = ".sav";
	static String SAVE_FILE_VERSION = "Zork III save data";

	static String CURRENT_ROOM_LEADER = "Current room: ";
	static String SAVED_INVENTORY = "Inventory: ";

	private static GameState theInstance;
	private Dungeon dungeon;
	private Room adventurersCurrentRoom;
	private ArrayList<Item> inventory;
	private int health;
	private int score;
	private int combatVal;
	private Weapon weapon;
	private ArmorSet armor;
	private ArrayList<Monster> monsters=new ArrayList<Monster>();
	private ArrayList<Achievement> achievements=new ArrayList<Achievement>();
	/**
	 *Returns the GameStateObject class. The GameState class is a singleton and must be accessed 
	 * through this method.
	 *@return the singleton GameState class
	 */
	static synchronized GameState instance() {
		if (theInstance == null) {
			theInstance = new GameState();
		}
		return theInstance;
	}

	private GameState() {
		inventory = new ArrayList<Item>();
	}

	/**
	 *The restore method takes in a filename and will read the file with that name and then
	 *adjust the GameState and Dungeon to replicate that file.
	 *@param filename A String that is the full name of the file that the GameState is reading.
	 * It needs to contain the .sav extension to indicate it is a save file.
	 *@throws FileNotFoundException Thrown when whe given filename is not a name of a 
	 *readable file.
	 */
	public void restore(String filename) throws FileNotFoundException,
	       IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {

		       Scanner s = new Scanner(new FileReader(filename));

		       if (!s.nextLine().equals(SAVE_FILE_VERSION)) {
			       throw new IllegalSaveFormatException("Save file not compatible.");
		       }

		       String dungeonFileLine = s.nextLine();

		       if (!dungeonFileLine.startsWith(Dungeon.FILENAME_LEADER)) {
			       throw new IllegalSaveFormatException("No '" +
					       Dungeon.FILENAME_LEADER + 
					       "' after version indicator.");
		       }

		       dungeon = new Dungeon(dungeonFileLine.substring(
					       Dungeon.FILENAME_LEADER.length()), false);
		       dungeon.restoreState(s);

		       s.nextLine();  // remove Adventurer: line
		       String currentRoomLine = s.nextLine();  
		       adventurersCurrentRoom = dungeon.getRoom(
				       currentRoomLine.substring(CURRENT_ROOM_LEADER.length()));

		       String temp;
		       //restores score
		       temp = s.nextLine();
		       this.score = Integer.parseInt(temp.substring(temp.indexOf('=')+1));
		       temp=s.nextLine();
		       if(temp.startsWith("Achievements")){
			       temp=temp.substring(temp.indexOf('=')+1);


			       String[] split= temp.split(" ");
			       for(String v:split){
				       achievements.add(Achievement.unlock(v));
			       }
			       temp=s.nextLine();
		       }
		       //restores health
		       this.health = Integer.parseInt(temp.substring(temp.indexOf('=')+1));

		       if(s.hasNext()){
			       String savInv = s.nextLine().substring(SAVED_INVENTORY.length());
			       String[] parsedSavInv = savInv.split(",");
			       for(String i : parsedSavInv){
				       addToInventory(this.dungeon.getItem(i));
			       }
		       }


	       }

	/**
	 *The store method stores the current state of the game to a given file
	 * with a .sav file so you are able to return to your game at another time.
	 *@throws IOException Thrown if the given file cannot be used.
	 */
	public void store() throws IOException {
		store(DEFAULT_SAVE_FILE);
	}

	/**
	 *The store method stores the current state of the game to a given file
	 * with a .sav file so you are able to return to your game at another time.
	 *@param saveName String containing the name of the .sav file without a .sav at the end
	 *@throws IOException Thrown if the given file cannot be used.
	 */
	public void store(String saveName) throws IOException {
		String filename = saveName + SAVE_FILE_EXTENSION;
		PrintWriter w = new PrintWriter(new FileWriter(filename));
		w.println(SAVE_FILE_VERSION);
		dungeon.storeState(w);

		//bottom of the save file
		w.println("Adventurer:");
		w.println(CURRENT_ROOM_LEADER + 
				getAdventurersCurrentRoom().getTitle());
		//saves score
		w.println("Score="+this.score);
		if(achievements.size()>0){
			w.print("Achievements=");
			for(Achievement a:achievements){
				w.print(a.getName()+" ");
			}
			w.println();
		}
		//saves health
		w.println("Health="+this.health);
		//Saves inventory IF there is something in it
		if(inventory.size() > 0){
			w.print("Inventory: " + inventory.get(0).getPrimaryName());
			for(int i = 1; i < inventory.size(); i++)
				w.print("," + inventory.get(i).getPrimaryName());
			w.println();
		}
		w.close();
	}


	void initialize(Dungeon dungeon) {
		this.dungeon = dungeon;
		adventurersCurrentRoom = dungeon.getEntry();
		health = 10;
		score = 0;


	}

	/**
	 *Returns the {@link Room} the player is currently in
	 *@return the current {@link Room} the player is in
	 */
	public Room getAdventurersCurrentRoom() {
		return adventurersCurrentRoom;
	}

	/**
	 *Changes the current {@link Room} the player is in
	 *@param room the {@link Room} to become the current room the player is in
	 */
	public void setAdventurersCurrentRoom(Room room) {
		adventurersCurrentRoom = room;
	}

	/**
	 * Returns the current {@link Dungeon} that the player is in
	 *@return the currently active {@link Dungeon}
	 */
	public Dungeon getDungeon() {
		return dungeon;
	}

	/**
	 *Returns the currently held {@link Item} Objects the player has
	 *@return the <tt>ArrayList</tt> of the {@link Item} Objects the player has
	 */
	public ArrayList <Item> getInventory() {
		return inventory;	
	}

	public Weapon getWeapon(){
		return weapon;
	}

	public ArmorSet getArmor(){
		return armor;
	}

	/**
	 *Adds a desired {@link Item} to the player's inventory and removes 
	 *it from the current {@link Room}.  This method does not check weight
	 *to see if there is enough space in the player's inventory.
	 *@param item to be added to the inventory
	 */
	public void  addToInventory(Item item){
		//itemCounter+=1;
		inventory.add(item);
		//if(itemCounter==0){
		addAchievement("item");
		//}
		adventurersCurrentRoom.remove(item);
	}

	/**
	 *Removes the passed {@link Item} from a player's inventory and puts it into
	 *the current {@link Room}.
	 *@param item the desired item to be removed from inventory
	 */
	public void removeFromInventory(Item item){
		inventory.remove(item);
		adventurersCurrentRoom.add(item);
	}

	/**
	 *Returns the {@link Item} in the current {@link Room} that goes by the passed name.
	 * This will return the first {@link Item} that goes by the given name. 
	 *@param name is the name to check if there is an {@link Item} in the Vicinity
	 *@return the {@link Item} in the current {@link Room} that goes by the passed <tt>String</tt>
	 *@throws Item.NoItemException if there is no {@link Item} Object that goes by <tt>name</tt>
	 */
	public Item getItemInVicinityNamed(String name) throws Item.NoItemException {
		ArrayList<Item> vicinity = adventurersCurrentRoom.getContents();
		for(int i = 0; i < vicinity.size(); i++) {
			if(vicinity.get(i).goesBy(name)) {
				return vicinity.get(i);
			}
		}
		throw new Item.NoItemException();	
	}
	/**
	 *Returns an {@link Item} that goes by the passed <tt>String</tt>.  Throws a 
	 * NoItemException if there are no Items that go by that name in the players inventory.
	 * This will return the first {@link Item} that goes by the given name. 
	 *@param name the <tt>String alias</tt> of an {@link Item} to get
	 *@return an {@link Item} in the player's inventory that goes by the passed name
	 *@throws Item.NoItemException if there are no items that goes by the passed name
	 */
	public Item getItemFromInventoryNamed(String name) throws Item.NoItemException {	
		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).goesBy(name)) {
				return inventory.get(i);
			}
		}
		throw new Item.NoItemException();	
	}
	/**
	 * Takes an {@link Item} parameter and sees if there is space in a player's inventory to carry it.
	 * Returns true if you can carry it, false if you cannot.
	 * @param i is an {@link Item} that you want to carry
	 *@return a boolean that represents if the player can pick up an {@link Item} or ot
	 */
	public boolean  getWeight(Item i){
		int w=0;
		for (Item item:inventory){
			w+=item.getWeight();
		}
		if(weapon != null){	
			w += weapon.getWeight();
		}
		if (w+i.getWeight()<=40){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean getWeight(Weapon i){
		int w=0;
		for (Item item:inventory){
			w+=item.getWeight();
		}
		if (w+i.getWeight()<=40){
			return true;
		}
		else{
			return false;
		}
	}
	public void setWeapon(Weapon e){
		this.weapon = e;
	}
	public void setArmor(ArmorSet e){
		this.armor = e;
	}
	public void setCombatVal(int c){
		combatVal += c;
	}
	public int getCombatVal(){
		return combatVal;
	}
	public int getScore(){
		if(score>=100){
			System.out.println("You have defeated all of the monsters and won the game!");
			System.exit(3);
		}


		if(achievements.isEmpty()){
			return 0;
		}

		return score;
	}
	public int getHealth(){
		return health;
	}
	//REDUCES health by given amount; Negative values will heal fyi
	public void changeHealth(int i){
		this.health -= i;
		if(health<0){
			System.out.println("You have died!");
			System.exit(2);
		}
	}
	public String getRank(){
		int i =0;
		for(Achievement a:achievements){
			i+=a.getValue();
		}
		if(i<50){
			return "Guy who is not even halfway through yet!";
		}
		else if(i<100){
			return "Guy who is more than halfway through!";
		}
		return "Josh Brolin";
	}
	public void changeScore(int i){
		this.score+=i;
	}
	public void addAchievement(String a){
		Achievement e = Achievement.unlock(a);
		if (getAchievementState(e)==false){
			achievements.add(e);
			score+=e.getValue();
			if (score>=100){
				System.out.println("You have defeated all of the monsters and won the game!");
				System.exit(2);
			}
			System.out.println("You've unlocked the achievement "+e.getMessage());
		}

	}
	public boolean getAchievementState(Achievement a){
		for(Achievement b:achievements){
			if(a.getMessage().equals(b.getMessage())){
				return true;
			}
		}
		return false;
	}

}
