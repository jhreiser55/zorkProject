/**
 *A <tt>ScoreEvent</tt> is an event which when created will add a number of points
 *to the adventurer's current Score.
 *@author Jordan Reiser
 */
public class ScoreEvent extends Event {
	int val;

	ScoreEvent(Event e, String stringVal) {
		super(e);
		this.val = Integer.parseInt(stringVal);
	}

	String  execute() {
		GameState.instance().changeScore(val);
		return wrappedEvent.execute();
	}
}
