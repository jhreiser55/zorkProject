/**
 *A <tt>DisappearEvent</tt> is an event which will cause an Item to removed  within the 
 *Room, Dungeon and Inventory.
 *@author Jordan Reiser
 */
public class DisappearEvent extends Event {
	
	String itemName;

	DisappearEvent(Event e, String item) {
		super(e);
		this.itemName = item;
	}

	String  execute() {
		
		//disappers the Item if its in the inventory
		try{
			GameState.instance().getInventory().remove(
					GameState.instance().getItemFromInventoryNamed(itemName));
		}
		catch(Item.NoItemException e){/*Do nothing, Item is not in inventory*/}
		
		//disappear the Item if its in the adventurersCurrent Room
		try{
			GameState.instance().getAdventurersCurrentRoom().getContents().remove(
					GameState.instance().getItemInVicinityNamed(itemName));
		}
		catch(Item.NoItemException e){}

		//disappear from the Dungeon
		GameState.instance().getDungeon().disappearItem(itemName);
		
		//return the description of the wrappedEvent
		return wrappedEvent.execute();
	}
}
