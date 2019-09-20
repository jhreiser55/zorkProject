/**
 *A <tt>TransformEvent</tt> is an event which will cause the current item to
 *be removed and be replaced by another item.
 *@author Jordan Reiser
 */
public class TransformEvent extends Event {
	
	String itemName;
	String itemToBecome;

	TransformEvent(Event e, String itemToBecome, String itemName) {
		super(e);
		this.itemToBecome = itemToBecome;
		this.itemName = itemName;
	}

	//What Happens when the Item is in both Room and Inventory?
	//	Uses primaryName of an Item
	//	Are primaryNames unique?
	//		
	String  execute() {

		Item transformedItem = GameState.instance().getDungeon().getItem(itemToBecome);

		try{
			//disappers the Item if its in the inventory
			GameState.instance().getInventory().remove(
					GameState.instance().getItemFromInventoryNamed(itemName));
			
			//put itemToBecome in the inventory
			GameState.instance().addToInventory(transformedItem);
		}
		catch(Item.NoItemException e){/*Do nothing, Item is not in inventory*/}
		
		try{
			//disappear the Item if its in the adventurersCurrent Room
			GameState.instance().getAdventurersCurrentRoom().getContents().remove(
					GameState.instance().getItemInVicinityNamed(itemName));

			//put itemToBecome in the adventurersCurrentRoom
			GameState.instance().getAdventurersCurrentRoom().add(transformedItem);
		}
		catch(Item.NoItemException e){/*Do nothing, it is not in the Room*/}

		//disappear the old Item  from the Dungeon
		GameState.instance().getDungeon().disappearItem(itemName);
		
		return wrappedEvent.execute();
	}
}
