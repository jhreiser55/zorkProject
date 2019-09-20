import java.util.Scanner;

/**
 *A <tt>HiddenExitRoom</tt> is a Room that has a hidden {@link Exit} that can be revealed(added to available exits).
 *You can reveal the hidden {@link Exit} by using a specific Item that triggers the {@link HiddenExitRoom#reveal()} method
 *@author Kanghuaroo
 */
public class HiddenExitRoom extends Event {
	private String hiddenExit;
	private String hiddenRoom;
	private boolean stillHidden;
	/**
	 *@param input line containing the {@link Room}s title of the hidden Exit and the {@link Exit}
	 */
	public HiddenExitRoom(Event e, String input) {
		super(e);
		stillHidden = true;
		String[] vars = input.split("-");
		this.hiddenRoom = vars[0];
		this.hiddenExit = vars[1]+"-"+vars[2];
	}
	
	/**
	 *This reveals the hidden {@link Exit} and adds it to the list of available exits to use.
	 * This will replace the current {@link Exit} if both have use the same direction.
	 */
	public String execute() {
		if(GameState.instance().getAdventurersCurrentRoom().getTitle().equals(hiddenRoom) && stillHidden){
			String[] tmp = hiddenExit.split("-");
			Exit temp = new Exit(hiddenRoom, tmp[0], tmp[1]);

			stillHidden = false;
		}
		
		//since the DescribeEvent is called last, it SHOULD show the added Exit
		return wrappedEvent.execute();
	}	
}

