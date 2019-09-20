/**
 *Method either moves the user in the direction specified, or informs user that they cannot move 
 *in that direction.
 *@author The-Onion-Knight
 */
public class MovementCommand extends Command{
	private String dir;
	MovementCommand(String s){
		this.dir=s;

	}
	/**
	 *Method checks to see if a valid room object is returned by the {@link Room#leaveBy} method
	 *if so, it moves user into that <tt>Room</tt>, else it informs user that they cannot
	 *travel in that direction.
	 *@return <tt>String</tt> a message that informs user that they either were able to travel 
	 *in the specified direction, or that they were unable to.
	 */
	public String execute(){
		Room currentRoom =
			GameState.instance().getAdventurersCurrentRoom();
		Room nextRoom = currentRoom.leaveBy(dir);
		if (nextRoom != null) {  // could try/catch here.
			GameState.instance().setAdventurersCurrentRoom(nextRoom);
			return "\n" + nextRoom.describe() + "\n";
		} else {
			return "\n" + "Sorry, you can't go " + dir + " from " +
				currentRoom.getTitle() + ".\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		}
	}
}
