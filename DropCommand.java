import java.util.ArrayList;
import java.util.Arrays;
/**
 *The <tt>DropCommand</tt> checks the inventory to see if item entered by user is present, if so it drops item in
 *room. Else, it informs user that item cannot be dropped. "Drop all" drops all items in inventory.
 *@author The-Onion-Knight
 */
public class DropCommand extends Command {
	private String[] check;
	private String itemName;
	private ArrayList<Item> inv;
	private ArrayList<Item> dup = new ArrayList<Item>();
	DropCommand(String s){
		inv=GameState.instance().getInventory();
		check = s.split(" ");
		this.itemName=s;
	}
	/**
	 *Method checks to see if item is present in inventory and if so, it drops item in the room.
	 *@return <tt>String</tt> informs user that item is dropped, if item is not found, throws Exception.
	 *@throws Item.NoItemException if item is not found, method throws a no item exception.
	 */
	
	public String execute() throws Item.NoItemException{
		
		try{	
			for(Item itm:inv){
				dup.add(itm);
			
			}
			int size=inv.size();
			String s="";
			//If user types drop all, drop all items, if inventory is empty let user know.
			if(itemName.toLowerCase().contains("drop all")){
				if (size==0){
					return "\n" + "You are empty-handed.\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
				}
				for(int i=0;i<size;i++){
					GameState.instance().removeFromInventory(dup.get(i));
					s+= dup.get(i).getPrimaryName()+" dropped."+"\n";
					}
				return "\n" + s+"\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
				}
			//Else drop item if it is in inventory.
			for (int i =0;i<check.length;i++){
				itemName=check[i];
				for (Item e:dup){
					if (e.goesBy(itemName)){
						GameState.instance().removeFromInventory(e);
						return "\n" + itemName+" dropped.\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
					}
				}
			}
			//Let user know that item is not in inventory.
			return "\n" + "Drop what?\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		}

		catch(Exception e){
			return "\n" + "Item not Found\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		}


	


}
}
