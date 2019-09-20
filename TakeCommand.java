import java.util.ArrayList;
/**
 *<tt>TakeCommand</tt> checks surrounding area to see if specified item is present, if so it adds item to inventory
 *"Take all" adds all items in vicinity to inventory.(So long as weight cap is not exceeded.)
 *@author The-Onion-Knight
 */
public class TakeCommand extends Command {
	private String itemName;
	private ArrayList<Object> inven;
	private ArrayList<Item> inv;
	private ArrayList<Item> iv=new ArrayList<Item>();
	private String[] array;
	private String s = "";
	private Item a;
	TakeCommand(String s){
		this.itemName=s;
		array=s.split(" ");
		inv = GameState.instance().getAdventurersCurrentRoom().getContents();
	}
	/**
	 *Method checks items in vicinity to see if user specified item is present, if so, it adds item
	 *to inventory. If user types "take all", all items in vicinity are added to inventory.
	 *@return <tt>String</tt> informs user that item is either added or unfound.
	 *@throws Item.NoItemException throws exception if item is not found in area.
	 */
	public String execute() throws Exception{

		array=itemName.split(" ");
		inv = GameState.instance().getAdventurersCurrentRoom().getContents();
		int size = GameState.instance().getAdventurersCurrentRoom().contentsSize();
		try{    //Add room contents to 2nd ArrayList to avoid altering list that is being iterated through.
			for(Item p:inv){
				iv.add(p);	
			}
			//Check if there are contents in room, if so return that there are none.
			if (size == 0){
				return "\n" + "There are no items, weapons or armor  present.\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
			}
			//If command is take all, take all items...
			if(itemName.toLowerCase().contains("take all")){
				//making an array list not including the weapon if there is one
				if(GameState.instance().getAdventurersCurrentRoom().hasArmor() == true){
					ArmorSet e = GameState.instance().getAdventurersCurrentRoom().getArmor();
					GameState.instance().setArmor(e);
				}
				if(GameState.instance().getAdventurersCurrentRoom().hasWeapon() == true){				
					for (int i=0;i<size;i++){
						if(GameState.instance().getWeight(iv.get(i))==true){
							GameState.instance().addToInventory(iv.get(i));
							s+=iv.get(i).getPrimaryName()+" taken."+"\n";
						}
						else{
							s+=iv.get(i).getPrimaryName()+ " would make you over encumbered.\n";
						}
					}
					if(GameState.instance().getWeight(GameState.instance().getAdventurersCurrentRoom().getWeapon())==true){
						Weapon e = GameState.instance().getAdventurersCurrentRoom().getWeapon();
						GameState.instance().setWeapon(e);
					}
					else {
						s+=GameState.instance().getAdventurersCurrentRoom().getWeapon().getPrimaryName() + " would make you over encumbered. \n";
					}
				}
				else{
					for (int i=0;i<size;i++){
						if(GameState.instance().getWeight(iv.get(i))==true){
							GameState.instance().addToInventory(iv.get(i));
							s+=iv.get(i).getPrimaryName()+" taken.\n";
						}
						else{
							s+=iv.get(i).getPrimaryName()+ " would make you over encumbered.\n";
						}
					}
				}

				return "\n" + s+"\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
			}
			//Else, add the specified item to inventory.
			for (int i=0;i<array.length;i++){
				String in = array[i];
				for (Item item:iv){
					if (item.goesBy(in)){
						if(GameState.instance().getWeight(item)==true){
							GameState.instance().addToInventory(item);
							return "\n" + item.getPrimaryName()+" taken.\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
						}
						else{return "\n" + "You are fully encumbered\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
						}
					}
				}
			}
			if(GameState.instance().getAdventurersCurrentRoom().hasWeapon() == true) {
				String name = GameState.instance().getAdventurersCurrentRoom().getWeapon().getPrimaryName();
				String in = array[1];
				if(in.equals(name)){
					Weapon e = GameState.instance().getAdventurersCurrentRoom().getWeapon();
					GameState.instance().setCombatVal(e.getCombatVal());
					GameState.instance().setWeapon(e);
					GameState.instance().getAdventurersCurrentRoom().setWeapon(null);
					return name + " taken, Your combat value is: " + GameState.instance().getCombatVal() + " \n" + GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
				}
			}
			if(GameState.instance().getAdventurersCurrentRoom().hasArmor() == true){
				String name = GameState.instance().getAdventurersCurrentRoom().getArmor().getPrimaryName();
				String in = array[1];
				if(in.equals(name)){				
					ArmorSet e = GameState.instance().getAdventurersCurrentRoom().getArmor();
					GameState.instance().setCombatVal(e.getCombatVal());
					GameState.instance().setArmor(e);
					GameState.instance().getAdventurersCurrentRoom().setArmor(null);
					return name + " taken, Your combat value is: " + GameState.instance().getCombatVal() +" \n" + GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
				}		
			}
			//Tell user if item is not in room.
			return "\n" + "Item not found\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		}


		catch(Exception e){
			e.printStackTrace();
			return "\n" + "Item not found\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		}
	}

}


