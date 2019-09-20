
/**
 * A <tt>Command</tt> is an abstract class, subclasses of which represent specific 
 * commands the player types as he or she plays the game.
 *@author Jordan Reiser
 */
abstract public class Command {
	Command(){

	}
    abstract String execute() throws Exception;
}
