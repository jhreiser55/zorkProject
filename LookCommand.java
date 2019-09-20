	/**
	 *Method returns the current <tt>Room</tt>'s description.
	 *@author The-Onion-Knight
	 */
public class LookCommand extends Command{
	LookCommand (){

	}
	/**
	 *Method sets beenHere to false for room, and then returns the description.
	 *@return <tt>String</tt> the description of the current room.
	 */
	public String execute(){
		//Change room state to beenHere=false, return description.
		GameState.instance().getAdventurersCurrentRoom().changeBeenHere();
		return "\n" + GameState.instance().getAdventurersCurrentRoom().describe()+"\n";


	}

}
