/**
 *A <tt>WinEvent</tt> is an event which informs the Player that they have won the game and will exit the program.
 *@author Jordan Reiser
 */
public class WinEvent extends Event {
	
	WinEvent(Event e) {super(e);}

	String  execute() {
		GameState.instance().changeScore(101);
		GameState.instance().getScore();
		return wrappedEvent.execute();
	}
}
