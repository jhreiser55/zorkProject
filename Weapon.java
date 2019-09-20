import java.util.Scanner;
import java.util.ArrayList;

/**
 *A <tt>Weapon</tt> is of type Item which is sspecifically used for the {@link AttackCommand} 
 *in the Combat System to simulate fighting monsters. A weapon takes in everything that a 
 *normal Item does but has a specific combat value which is used to determine the 
 *winner of a fight. 
 *@author Jordan Reiser
 */
public class Weapon{
    
static class NoWeaponException extends Exception {}
	
	private String primaryName;
	private int combatVal;
	ArrayList<String> aliases;
	private int weight;

	/**
	 *Given a Scanner object to read from an 'Item' file entry, it will read and return 
	 *a Weapon object representing it.
	 *@param s A scanner used to read Weapons from the given file.
	 *@throws NoItemException where the Item is not positioned at correct section 
	 *of an Item entry. The result of this is the file reader moving to the next line 
	 *in the file.   
	 *@throws IllegalDungeonFormatException where the Dungeon format file was made 
	 *incorrectly and was unable to read the Items within the Room.
	 */
	public Weapon(Scanner s) throws NoWeaponException, Dungeon.IllegalDungeonFormatException {
		aliases = new ArrayList<String>();		
		primaryName = s.nextLine();
		aliases.add(primaryName);
		if(primaryName.equals(Dungeon.TOP_LEVEL_DELIM))
			throw new NoWeaponException();
		//seperate primaryName from Aliases
		String[] temp = primaryName.split(",");
		primaryName = temp[0];
		for(int i = 0; i<temp.length;i++){
			aliases.add(temp[i]);
		}

		weight = s.nextInt();
		s.nextLine();
		combatVal = s.nextInt();
		s.nextLine();

		String delimLine = s.nextLine();

		// throw away delimiter
		if (!delimLine.equals(Dungeon.SECOND_LEVEL_DELIM)) {
			throw new Dungeon.IllegalDungeonFormatException("No '" +
					Dungeon.SECOND_LEVEL_DELIM + "' after room.");
		}
	}

	/**
	 *Returns the combat value of the weapon.
	 */
	public int getCombatVal() {
		return combatVal;
	}

	public Weapon(){
		combatVal = 2;
		weight = 2;
		aliases = new ArrayList<String>();
		primaryName = "fake name";
	}
	public String getPrimaryName(){
		return primaryName;
	}
	public int getWeight(){
		return weight;
	}
}
