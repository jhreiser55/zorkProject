/**
 *A <tt>ScoreCommand</tt> is of type <tt>Command</tt> which when called will return the Player's current score.
 *@author Jordan Reiser
 */
public class ScoreCommand extends Command {
	private String rank;
	ScoreCommand() {}

	/**
	 *The execute method will return a String consisting of the Player's current score.
	 *@return <tt>String</tt> the player's current score.
	 */
	public String execute() {
		return "Your current score is: "+GameState.instance().getScore()+"\n"+
		"That means that your rank is: "+GameState.instance().getRank()+"\n"+
		GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
	}

}
