/**
 *A <tt>DieEvent</tt> is an event which will  inform the player that the game is over,
 *the player has lost and will exit the program.
 *@author Jordan Reiser
 */
public class DieEvent extends Event{
	
	DieEvent(Event e) {super(e);}

	String  execute() {
		GameState.instance().changeHealth(100);
		return wrappedEvent.execute();
	}
}
