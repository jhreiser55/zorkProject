import java.util.Scanner;

/**
 *A <tt>HiddenItemRoom</tt> is a Room that has a hidden {@link Item} that can be revealed(added to available contents).
 *You can reveal the hidden {@link Item} by using a specific Item that triggers the {@link HiddenItemRoom#reveal()} method
 *@author Kanghuaroo
 */
public class HiddenItemRoom extends Event {
	private String hiddenItem;
	private String hiddenRoom;
	private boolean stillHidden;
	public HiddenItemRoom(Event e, String input) {
		super(e);
		stillHidden = true;
		String[] vars = input.split("-");
		this.hiddenRoom = vars[0];
		this.hiddenItem = vars[1];
	}
	
	/**
	 *This reveals the hidden {@link Exit} and adds it to the list of available exits to use.
	 * This will replace the current {@link Exit} if both have use the same direction.
	 */
	public String execute() {
		if(GameState.instance().getAdventurersCurrentRoom().getTitle().equals(hiddenRoom) && stillHidden){
			GameState.instance().getAdventurersCurrentRoom().add(
					GameState.instance().getDungeon().getItem(hiddenItem));
			stillHidden = false;
		}
		
		//since the DescribeEvent is called last, it SHOULD show the added Exit
		return wrappedEvent.execute();
	}	
}

