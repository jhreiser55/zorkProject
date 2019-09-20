import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
/**
 *The item specific command parses the user input, and then determines if any items in the area have a
 *corresponding message for the verb entered, if so it returns the message. Else, it informs user that 
 *they cannot perform the action that they are attempting to.
 *@author The-Onion-Knight
 *@version 2019
 */
public class ItemSpecificCommand extends Command{
	private ArrayList <Item> test;
	private ArrayList <Item> inv;
	private String verb;
	private String noun;
	ItemSpecificCommand(String v, String n){
		this.verb=v;
		this.noun=n;
	}
	/**
	 *Checks an item for the item-specific message that corresponds to the verb entered by user.
	 *@return <tt>String</tt> The item-specific message for the verb the user inputs if one exists.
	 *@throws Item.NoItemException if message is not found, returns a "you can't do that" message to user.
	 */
/*
	public String execute() throws Exception{
			String tmp;
		try {
			ArrayList<Item> inventory = GameState.instance().getInventory();
			tmp = findMessage(inventory);
			if(!tmp.equals("null"))
				return tmp;
		} catch (Exception e) { /* Item doesnt have that command *//* }
		
		try {
			ArrayList<Item> roomItems = GameState.instance().getAdventurersCurrentRoom().getContents();
			tmp = findMessage(roomItems);
			if(!tmp.equals("null"))
				return tmp;
		} catch (Exception e) { /* Item doesnt have that command *//* }
		
		return "\n" + "You can't " + verb +" a "+ noun + ".\n";
	}

	private String findMessage(ArrayList<Item> arr) throws Exception{
		for(Item j : arr) {
			String input = j.getMessageForVerb(verb);
			if(j.goesBy(noun) && !input.equals("null")) {
				return "\n" + input + 
					"\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
			}
		}
		return "null";
	}
}
*/
	public String execute(){
		//item in inv
		try {
			return "\n" + GameState.instance().getItemFromInventoryNamed(noun).getMessageForVerb(verb)
				+'\n'+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		} catch (Item.NoItemException e) { /* Item doesnt have that command */ }
		//item in curr room
		try {
			return "\n" + GameState.instance().getItemInVicinityNamed(noun).getMessageForVerb(verb) 
				+ '\n'+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		} catch (Item.NoItemException e) { /* Item doesnt have that command */ }
		
		return "\n" + "You can't " + verb +" a "+ noun + ".\n";
	}
}
