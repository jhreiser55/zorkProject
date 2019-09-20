
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *A <tt>Room</tt> represents a room of a {@link Dungeon} that the player can enter.A 
 *<tt>Room</tt> can contains zero or  multiple instancs of {@link Item} that the player can 
 *interact with and {@link Exit} that the player can use to change their current <tt>Room</tt>.
 *@author Kanghuaroo
 */
public class Room {

	/**
	 *An <tt>Exception</tt> that is thrown when there is no <tt>Room</tt> to be found from a 
	 *method or be initialized from a <tt>Scanner</tt>.
	 *@author Kanghuaroo
	 */
	class NoRoomException extends Exception {}

	protected String title;
	protected String desc;
	protected boolean beenHere;
	protected ArrayList<Exit> exits;
	protected ArrayList<Item> contents;
	protected ArrayList<Monster> monsters;
	protected Weapon weapon;
	protected ArmorSet armor;	   
	/**
	 *Creates an empty <tt>Room</tt> Object from a the passed title.
	 *@param title the title the <tt>Room</tt> should display upon entering and be reffered as.
	 *@Deprecated Create a <tt>Room</tt> using the constructor below, it takes care of
	 *contents and exits.
	 */
	Room(String title) {
		init();
		this.title = title;
	}

	/** Given a <tt>Scanner</tt> object positioned at the beginning of a "room" file
	  entry, read and return a Room object representing it. 
	  @param s the <tt>Scanner</tt> Object to initialize a Room from
	  @param d the {@link Dungeon} this Room will belong to  
	  @param initState True if we are using a <tt>.sav</tt> file, False otherwise
	  @throws NoRoomException The reader object is not positioned at the
	  start of a room entry. A side effect of this is the reader's cursor
	  is now positioned one line past where it was.
	  @throws IllegalDungeonFormatException A structural problem with the
	  dungeon file itself, detected when trying to read this room.
	 */
	public Room(Scanner s, Dungeon d, boolean initState) throws NoRoomException,
	       Dungeon.IllegalDungeonFormatException {

		       init();
		       title = s.nextLine();
		       desc = "";
		       if (title.equals(Dungeon.TOP_LEVEL_DELIM)) {
			       throw new NoRoomException();
		       }

		       //set weapon in room
		       String lineOfDesc = s.nextLine();
		       if(lineOfDesc.contains("Weapons:")){
			       String temp = lineOfDesc.substring(lineOfDesc.indexOf(' ')+1);
				weapon = d.getWeapon(temp);
			       //Moves to next line IF there was a weapon in the room.
			       lineOfDesc = s.nextLine();
		       }

		       //set Armor in room
		       if(lineOfDesc.contains("Armor:")){
			       String temp = lineOfDesc.substring(lineOfDesc.indexOf(' ')+1);
			       armor = d.getArmor(temp);
			       //Moves to next line IF there was armor in the room.
			       lineOfDesc = s.nextLine();
		       }

		       //set contents of the Room       
		       if(lineOfDesc.contains("Contents:")){
			       String temp = lineOfDesc.substring(lineOfDesc.indexOf(' ')+1);
			       String[] itemsInRoom = temp.split(",");
			       for(String i : itemsInRoom){
				       contents.add(d.getItem(i));	
			       }
			       //moves to next life IF there was contents in the Room
			       lineOfDesc = s.nextLine();
		       }
		       if(lineOfDesc.contains("Monsters:")){
			       String temp = lineOfDesc.substring(lineOfDesc.indexOf(' ')+1);
			       String[] monstersInRoom = temp.split(",");
			       for (String r: monstersInRoom){
				       monsters.add(d.getMonster(r));
			       }

			       lineOfDesc = s.nextLine();
		       }

		       //Desc hydration
		       while (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM) &&
				       !lineOfDesc.equals(Dungeon.TOP_LEVEL_DELIM)) {
			       desc += lineOfDesc + "\n";
			       lineOfDesc = s.nextLine();
		       }

		       // throw away delimiter
		       if (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM)) {
			       throw new Dungeon.IllegalDungeonFormatException("No '" +
					       Dungeon.SECOND_LEVEL_DELIM + "' after room.");
		       }
	       }


	// Common object initialization tasks.
	private void init() {
		exits = new ArrayList<Exit>();
		contents = new ArrayList<Item>();
		monsters = new ArrayList<Monster>();
		beenHere = false;
	}

	/**
	 *Returns the title of a Room in String format.
	 * @return the String title of a <tt>Room</tt> Object
	 */
	public String getTitle() { return title; }

	/**
	 * Changes the description of a <tt>Room</tt> Object to the passed String
	 * @param desc the String to become the new description
	 */
	public void setDesc(String desc) { this.desc = desc; }

	/**
	 * Store the current (changeable) state of this room to the writer
	 * passed.
	 * @param w a <tt>PrintWriter</tt> to write the current state to
	 */
	public void storeState(PrintWriter w) {
		// At this point, nothing to save for this room if the user hasn't
		// visited it.
		if (beenHere) {
			w.println(title + ":");
			w.println("beenHere=true");
			//Weapon Saving
			if(weapon != null) {
				w.println(weapon.getPrimaryName());
			}
			//ArmorSet saving
			if(armor != null){
				w.println(armor.getPrimaryName());
			}
			//content saving
			if(contents.size() > 0){
				w.print("Contents: ");
				for(Item i : contents){
					w.print(i.getPrimaryName() + ",");
				}
				w.print("/");
				for(Monster m : monsters){
					w.print(m.getName() + ",");
				}
				w.println();
			}
			w.println(Dungeon.SECOND_LEVEL_DELIM);
		}
	}

	/**
	 * Changes the state of the <tt>beenHere</tt> boolean variable to <tt>false</tt>
	 */
	public void changeBeenHere(){
		this.beenHere=false;
	}
	public int contentsSize(){
		int hold = contents.size();
		if(weapon != null){
			hold++;
		}	
		if(armor != null){
			hold++;
		}
		return 1;
	}

	/**
	 * Restores the state of a room as specified by the file in the <tt>Scanner</tt>.
	 * 	This will replace the normal starting <tt>contents</tt> with what is read 
	 * 	from the passed <tt>Scanner</tt>.
	 * @param s the <tt>Scanner</tt> to restore the state from
	 * @param d the {@link Dungeon} this <tt>Room</tt> is located in
	 * @throws GameState.IllegalSaveFormatException when the scanner is not positioned
	 * at the start of a saved Room entry.
	 */
	public void restoreState(Scanner s, Dungeon d)
		throws GameState.IllegalSaveFormatException {

			String line = s.nextLine();
			//beenHere restoration
			if (!line.startsWith("beenHere")) {
				throw new GameState.IllegalSaveFormatException("No beenHere.");
			}
			beenHere = Boolean.valueOf(line.substring(line.indexOf("=")+1));
			
			String battle = s.nextLine();
			weapon = d.getWeapon(battle);
			armor = d.getArmor(s.nextLine());
			
			//contents restoration of Items in .sav
			contents = new ArrayList<Item>();
			line = s.nextLine();
			if(!line.equals(Dungeon.SECOND_LEVEL_DELIM)){
				line = line.substring(line.indexOf(' ') + 1);
				String[] saveContents = line.split(",");
				for(String i : saveContents){
					if (i.startsWith("/")){
						i=i.substring(i.indexOf('/'));
						monsters.add(d.getMonster(i));
						break;
					}
					contents.add(d.getItem(i));
				}
				
				s.nextLine(); //remove delimiter

			}
			else{
			}
		}

	/**
	 *Returns a String description of a Room.  The output contains the <tt>desc</tt> variable and 
	 * the {@link Exit} Objects that can be used to leave by and <tt>contents</tt> that can be interacted
	 * with in the room.
	 * @return the desciption, where you can exit, and availible items
	 */
	public String describe() {
		String description;
		if (beenHere) {
			description = title;
		} else {
			description = title + "\n" + desc;
		}
		for (Exit exit : exits) {
			description += "\n" + exit.describe();
		}
		for (Item item : contents) {
			try{
				description += "\n" + "There is a "
					+ item.getPrimaryName() + " here";

			}
			catch(Exception e){}
		}
		if(weapon != null){
			description += "\n There is a " + weapon.getPrimaryName() + " Weapon here";
		}
		if(armor != null){
			description += "\n There is a " + armor.getPrimaryName() + " Armor Set here";
		}
		if (monsters.size()!=0){
			try{
				for (Monster m:monsters){
					description+="\nA wild "+m.getName()+" has appeared!\n"+m.getCatchPhrase()+"\n";
				}
			}
			catch(Exception e){}
		}



		beenHere = true;
		return description;
	}

	/**
	 *Returns the room from the <tt>Exit</tt> in the passed direction
	 *@param dir the direction of the desired <tt>Exit</tt>
	 *@return the </tt>Room</tt> of the {@link Exit} associated with <tt>dir</tt>
	 */
	public Room leaveBy(String dir) {
		for (Exit exit : exits) {
			if (exit.getDir().equals(dir)) {
				return exit.getDest();
			}
		}
		return null;
	}

	/**
	 *Adds an {@link Exit} to the <tt>ArrayList</tt> of availible <tt>exits</tt> for this<tt>Room</tt>
	 *@param exit the desired {@link Exit} to be added
	 */
	public void addExit(Exit exit) {
		exits.add(exit);
	}

	/**
	 *Returns the <tt>contents ArrayList</tt> of the <tt>Room</tt>
	 *@return the <tt>ArrayList</tt> of {@link Item} Objects in the <tt>Room</tt>
	 */
	public ArrayList <Item> getContents(){
		return contents;
	}
	public void changeBoolean() {
		beenHere = false;
	}

	/**
	 *Removes an {@link Item} from the <tt>contents ArrayList</tt> of this <tt>Room</tt>
	 *@param item the desired {@link Item} to be removed
	 */

	public void remove(Item item){
		this.contents.remove(item);
	}

	/**
	 *Adds an {@link Item} to the <tt>contents ArrayList</tt> of this <tt>Room</tt>
	 *@param item the desired {@link Item} to be added
	 */
	public void add(Item item){
		this.contents.add(item);
	}
	public void remove(Monster m){
		this.monsters.remove(m);
		GameState.instance().getDungeon().removeMonster(m);

	}
	public boolean hasMonsters(){
		if (monsters.size()>0){
			return true;

		}
		return false;
	}

	public Weapon getWeapon(){
		return weapon;
	}
	public boolean hasWeapon(){
		if(weapon != null){
			return true;
		}
		return false;
	}
	public boolean hasArmor(){
		if(armor != null){
			return true;
		}
		return false;
	}
	public ArmorSet getArmor(){
		return armor;
	}
	public void setWeapon(Weapon a){
		this.weapon = a;
	}
	public void setArmor(ArmorSet a){
		this.armor = a;
	}
	public ArrayList<Monster> getMonsters(){
		return this.monsters;
	}
	public Monster getMonster(String s){
		for(Monster m:monsters){
			if (m.getName().equals(s)){
				return m;
			}
		}
		return null;
	}

}
