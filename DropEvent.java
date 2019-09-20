/**
 *A <tt>DropEvent</tt> is an event which will drop an item from the adventurers inventory 
 *if present.
 *@author Jordan Reiser
 */
public class DropEvent extends Event {
	
	String itemName;

	DropEvent(Event e,String item) {
		super(e);
		this.itemName = item;
	}
	
	//Call GameState.instance() to drop an Item
	String  execute() {
		//removeFromInventory() uses the Item returned from using 
		//getItemFromInventory() which uses the
		//Item this verb comes froms primaryName
		try{
			GameState.instance().removeFromInventory(
				GameState.instance().getItemFromInventoryNamed(itemName));
		}
		catch(Item.NoItemException e){/*Do nothing, it means it is not in the inventory*/}

		return wrappedEvent.execute();
	}
}
