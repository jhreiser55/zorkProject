
/**
 *An <tt>Event</tt> is an abstract class that passes the child classes an execute() 
 *method. The child classes have the ability to modify the <tt>GameState</tt> class using 
 *the execute() method.
 *@author Jordan Reiser
 */
abstract public class Event {
	protected Event wrappedEvent;
	
	Event() {}
	Event(Event e) {wrappedEvent = e;}
	
	abstract String execute();	
}
