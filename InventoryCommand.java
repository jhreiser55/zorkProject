import java.util.ArrayList;
/**
 *Method lists inventory contents.
 *@author The-Onion-Knight
 */
class InventoryCommand extends Command{
private ArrayList <Item> inv = new ArrayList <Item>();
private String s;
InventoryCommand(){
	inv=GameState.instance().getInventory();
}

/**
 *Checks if inventory is empty, if so it returns a "You are empty handed" message. Else,
 *it returns the contents of inventory in a list.(You are carrying:....)
 *@return <tt>String</tt> A list of items that are in the player's inventory.
 */
String execute(){
	//Return inventory contents.
   if (inv.size()==0){
	   return "\n" + "You are empty-handed.\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
        }
   else{
	s="You are carrying:"+"\n";
	for (Item item:inv){
	s += " "+item.getPrimaryName()+"\n";
	}
	return "\n" + s+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
   }
 
}
}

