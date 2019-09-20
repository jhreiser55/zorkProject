import java.util.Scanner;
import java.util.ArrayList;

/**
 * An <tt>ArmorSet</tt> is of type Item which is used in the {@link AttackCommand} 
 * class to calculate the given chance of winning a fight. An ArmorSet takes in 
 * everything an Item does in addition to a Combat Value which is used in the attack 
 * simulation with a Monster.
 *@author Jordan Reiser
 */
public class ArmorSet {
    
static class NoArmorSetException extends Exception {}
	
	private int combatVal;
	ArrayList<String> aliases;
	String primaryName;

	/**
	 *Given a Scanner object to read from an 'Item' file entry, it will read and return 
	 *an ArmorSet object representing it. An Armor set takes in everything that Item 
	 *does in addition to a Combat Value which will help determine the outcome of the 
	 *{@link AttackCommand} class.
	 *@param s A Scanner that is used to read ArmorSets from the given file.
	 *@throws NoItemException where the ArmorSet is not positioned correctly in the entry. 
	 *This will result in the curser moving to the next line of the file.
	 *@throws IllegalDungeonFormatException where the file format is incorrect and 
	 *a dungeon cannot be created wil the given ArmorSet.	 
	 *
	 */
	public ArmorSet(Scanner s) throws NoArmorSetException, Dungeon.IllegalDungeonFormatException {

		aliases = new ArrayList<String>();
		primaryName = s.nextLine();
		aliases.add(primaryName);
		if(primaryName.equals(Dungeon.TOP_LEVEL_DELIM))
			throw new NoArmorSetException();
		//seperate primaryName from Aliases
		String[] temp = primaryName.split(",");
		primaryName = temp[0];
		for(int i = 0; i<temp.length;i++){
			aliases.add(temp[i]);
		}

		combatVal = s.nextInt();
		s.nextLine();

		String delimLine = s.nextLine();

		// throw away delimiter
		if (!delimLine.equals(Dungeon.SECOND_LEVEL_DELIM)) {
			throw new Dungeon.IllegalDungeonFormatException("No '" +
					Dungeon.SECOND_LEVEL_DELIM + "' after Armor.");
		}
	}
	public ArmorSet(){
		combatVal = 2;
		aliases = new ArrayList<String>();
		primaryName = "fake name";
	}
	public int getCombatVal(){
		return combatVal;
	}

	public String getPrimaryName(){
		return primaryName;
	}
}
