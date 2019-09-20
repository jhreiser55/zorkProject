import java.util.Hashtable;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 *The dungeon acts as an environment for the user to play in. It contains all {@link Room}s, {@link Item}s, and {@link Monster}s and is either instantiated from a .zork/.sav file. 
 *@author The-Onion-Knight
 *@version 2019
 */
public class Dungeon {

	/**
	 * An <tt>Exception</tt> thrown when a passed zork file is not formatted correctly.
	 * This could be for a variety of reasons such as a misspelling, missing a delimiter,
	 * missing markers, or other format errors.
	 *@author Kanghuaroo
	 */
	public static class IllegalDungeonFormatException extends Exception {
		public IllegalDungeonFormatException(String e) {
			super(e);
		}
	}

	// Variables relating to both dungeon file and game state storage.
	public static String TOP_LEVEL_DELIM = "===";
	public static String SECOND_LEVEL_DELIM = "---";
	// Variables relating to dungeon file (.zork) storage.
	public static String ROOMS_MARKER = "Rooms:";
	public static String EXITS_MARKER = "Exits:";
	public static String ITEMS_MARKER = "Items:";
	public static String INFINITE_ROOMS_MARKER = "Infinite Rooms:";
	public static String MONSTERS_MARKER = "Monsters:";
	public static String WEAPON_MARKER = "Weapons:";
	public static String ARMORSET_MARKER = "Armor:";
	// Variables relating to game state (.sav) storage.
	static String FILENAME_LEADER = "Dungeon file: ";
	static String ROOM_STATES_MARKER = "Room states:";

	private String name;
	private Room entry;
	private Hashtable<String,Room> rooms;
	private String filename;
	private Hashtable<String,Item> items;
	private Hashtable<String,Weapon> weapon;	
	private Hashtable<String,ArmorSet> armor;		
	private Hashtable<String, Monster> monsters;
	private Hashtable<String, Achievement> achievements;
	/**
	 *Constructor for <tt>Dungeon</tt> objects that are not being hydrated.
	 *@param name a <tt>String</tt> object that contains the name of the <tt>Dungeon</tt>.
	 */
	Dungeon(String name, Room entry) {
		init();
		this.filename = null;    // null indicates not hydrated from file.
		this.name = name;
		this.entry = entry;
	}

	/**
	 * Constructor for a <tt>Dungeon</tt> object that is being hydrated from a file
	 * Read from the .zork filename passed, and instantiate a Dungeon object
	 * based on it.
	 *@param filename a <tt>String</tt> that contains the name of the file.
	 *@param initState a <tt>boolean</tt> that determines what type of file to hydrate from. false=.sav, true=.zork
	 *@throws FileNotFoundException if a file is not found with a corresponding filename, it informs user and close	*s program.
	 */
	public Dungeon(String filename, boolean initState) throws FileNotFoundException,
	       IllegalDungeonFormatException {

		       init();
		       this.filename = filename;

		       Scanner s = new Scanner(new FileReader(filename));
		       name = s.nextLine();

		       s.nextLine();   // Throw away version indicator.

		       // Throw away delimiter.
		       if (!s.nextLine().equals(TOP_LEVEL_DELIM)) {
			       throw new IllegalDungeonFormatException("No '" +
					       TOP_LEVEL_DELIM + "' after version indicator.");
		       }
			//throw away Monster Starter
		       if (!s.nextLine().equals(MONSTERS_MARKER)) {
			       throw new IllegalDungeonFormatException("No '" +
					       MONSTERS_MARKER + "' line where expected.");
		       }
		       try {
			       while(true){
				       Monster e = new Monster(s);
				       monsters.put(e.getName(),e);
			       }
		       }	catch(Monster.NoMonsterException e) { /*end of monsters */ }
		       //Throw away Weapon starter
		       if (!s.nextLine().equals(WEAPON_MARKER)) {
			       throw new IllegalDungeonFormatException("No '" +
					       WEAPON_MARKER + "' line where expected.");
		       }
		       try {
			       // Instantiate and add weapons.
			       while (true) {
				       Weapon e = new Weapon(s);
				       weapon.put(e.getPrimaryName(), e);
			       }
		       } catch (Weapon.NoWeaponException e) {  /* end of Weapons */ }
		       //Throw away Armor starter
		       if (!s.nextLine().equals(ARMORSET_MARKER)) {
			       throw new IllegalDungeonFormatException("No '" +
					       ARMORSET_MARKER + "' line where expected.");
		       }
		       try {
			       // Instantiate and add weapons.
			       while (true) {
				       ArmorSet e = new ArmorSet(s);
				       armor.put(e.getPrimaryName(), e);
			       }
		       } catch (ArmorSet.NoArmorSetException e) {  /* end of Armor */ }
		       //Throw away Item starter
		       if (!s.nextLine().equals(ITEMS_MARKER)) {
			       throw new IllegalDungeonFormatException("No '" +
					       ITEMS_MARKER + "' line where expected.");
		       }
		       try {
			       // Instantiate and add items.
			       while (true) {
				       add(new Item(s));
			       }
		       } catch (Item.NoItemException e) {  /* end of Items */ }

		       // Throw away Rooms starter.
		       if (!s.nextLine().equals(ROOMS_MARKER)) {
			       throw new IllegalDungeonFormatException("No '" +
					       ROOMS_MARKER + "' line where expected.");
		       }
		       try {
			       // Instantiate and add first room (the entry).
			       entry = new Room(s, this, initState);
			       add(entry);
			       // Instantiate and add other rooms.
			       while (true) {
				       add(new Room(s, this, initState));
			       }
		       } catch (Room.NoRoomException e) {  /* end of rooms */ }
			
		       //Infinate Room Creation
		       // Throw away Infinate  Rooms starter.
		       //System.out.println(s.nextLine());
		       if (!s.nextLine().equals(INFINITE_ROOMS_MARKER)) {
			       throw new IllegalDungeonFormatException("No '" +
					       INFINITE_ROOMS_MARKER + "' line where expected.");
		       }
		       try {
			       // Instantiate and add Infinate Rooms.
			       while (true) {
				       add(new InfiniteRoom(s, this, initState));
			       }

		       } catch (Room.NoRoomException e) { /* end of Infinate Rooms */}
		       
		       // Throw away Exits starter.
		       if (!s.nextLine().equals(EXITS_MARKER)) {
			       throw new IllegalDungeonFormatException("No '" +
					       EXITS_MARKER + "' line where expected.");
		       }
		       try {
			       // Instantiate exits.
			       while (true) {
				       // (Note that the Exit constructor takes care of adding itself
				       // to its source room.)
				       Exit exit = new Exit(s, this);
			       }
		       } catch (Exit.NoExitException e) {  /* end of exits */ }

		       s.close();
	       }

	// Common object initialization tasks, regardless of which constructor
	// is used.
	private void init() {
		rooms = new Hashtable<String,Room>();
		items = new Hashtable<String,Item>();
		monsters = new Hashtable<String,Monster>();
		weapon = new Hashtable<String,Weapon>();
		armor = new Hashtable<String,ArmorSet>();
	}

	/**
	 * Stores the current (changeable) state of this dungeon to the writer
	 * passed.
	 *@param w a <tt>PrintWriter</tt> object to allow program to write down its current state.
	 *
	 */
	void storeState(PrintWriter w) throws IOException {
		w.println(FILENAME_LEADER + getFilename());
		w.println(ROOM_STATES_MARKER);
		for (Room room : rooms.values()) {
			room.storeState(w);
		}

		w.println(TOP_LEVEL_DELIM);
	}

	/**
	 * Restore the (changeable) state of this dungeon to that reflected in the
	 * reader passed.
	 *@param s a <tt>Scanner</tt> object that allows program to read in the save file.
	 *@throws IllegalSaveFormatException if save file is incorrectly formatted, it informs user and ends program.
	 */
	void restoreState(Scanner s) throws GameState.IllegalSaveFormatException {

		// Note: the filename has already been read at this point.

		if (!s.nextLine().equals(ROOM_STATES_MARKER)) {
			throw new GameState.IllegalSaveFormatException("No '" +
					ROOM_STATES_MARKER + "' after dungeon filename in save file.");
		}

		String roomName = s.nextLine();
		while (!roomName.equals(TOP_LEVEL_DELIM)) {
			getRoom(roomName.substring(0,roomName.length()-1)).restoreState(s, this);
			roomName = s.nextLine();
		}
	}
	/**
	 *Retrieves the entry room of the dungeon.
	 *@return <tt>Room</tt> the entry room of the dungeon.
	 */
	public Room getEntry() { return entry; }
	/**
	 *Retrieves the name of the dungeon.
	 *@return <tt>String</tt> the name of the dungeon.
	 */
	public String getName() { return name; }
	/**
	 *Retrieves the filename of the file being hydrated from.
	 *@return <tt>String</tt> the name of the file being hydrated from.
	 */
	public String getFilename() { return filename; }
	/**
	 *Adds a room to the dungeon.
	 *@param room a <tt>Room</tt> object that is being added to the dungeon
	 */
	public void add(Room room) { rooms.put(room.getTitle(),room); }
	/**
	 *Adds an <tt>Item</tt> to the dungeon.
	 *@param item the <tt>Item</tt> being added to the dungeon.
	 */
	public void add(Item item) { items.put(item.getPrimaryName(),item); }

	public void add(Monster monster) {monsters.put(monster.getName(),monster); }
	/**
	 *@param roomTitle a <tt>String</tt> object that contains the title of the room being accessed.
	 *@return <tt>Room</tt> the room that corresponds to the title.
	 */
	public Room getRoom(String roomTitle) {
		return rooms.get(roomTitle);
	}
	/**
	 *Retrieves the item that corresponds to the item name.
	 *@param primaryName a <tt>String</tt> object that contains the name of the item being accessed.
	 *@return <tt>Item</tt> the item being retrieved by name.
	 *
	 */
	public Item getItem(String primaryName){
		return this.items.get(primaryName);
	}

	public Weapon getWeapon(String primaryName){
		return this.weapon.get(primaryName);
	}

	public ArmorSet getArmor(String primaryName){
		return this.armor.get(primaryName);
	}
	/**
	 *Adds <tt>Monster</tt> object to dungeon.
	 *@param monster the <tt>Monster</tt> object being added to the dungeon.
	 *
	 */
	public Monster getMonster(String name) {
		return this.monsters.get(name);
	}
	/**
	 *Adds <tt>NPC</tt> object to the dungeon.
	 *@param npc the <tt>NPC</tt> object being added to the dungeon.
	 */
	public void addNPC(Monster npc){
		//this.npcs.put(npc.getPrimaryName(),npc);
	}
	/**
	 *Removes <tt>Monster</tt> object from the dungeon.
	 *@param monster the <tt>Monster</tt> object being removed from the dungeon.
	 */
	public void removeMonster(Monster monster){
		monsters.remove(monster.getName());
		if(monsters.isEmpty()){
			GameState.instance().addAchievement("kill");
		}
	}
	/**
	 *Removes an {@link Item} from the items Hashtable.  Part of the
	 * disappearEvent.
	 * @param primaryName the primaryName for an {@link Item}
	 */
	public void disappearItem(String primaryName){
		this.items.remove(primaryName);
	}
	public Hashtable<String,Room> getRooms() {
		return rooms;
	}
}
