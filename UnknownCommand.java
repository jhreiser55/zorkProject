import java.util.ArrayList;
import java.util.Arrays;
/**
 *For any unrecognized commands, method returns a "you can't do that" message.
 *@author The-Onion-Knight
 */
public class UnknownCommand extends Command{
	private String [] parsed = new String[2]; 
	private String bogusCommand;
	UnknownCommand(String bogus){
		this.bogusCommand=bogus;
		parsed = bogus.split(" ");
	}
	/**
	 *If user input is less than 2 characters or is equal to null, it says "come again?"
	 *else, it returns a "you cant do that" message.
	 *@return s message, either "you can't do that", ot "come again?"
	 */
	public String execute(){
		//If command is empty or only contains verb, ask user to try again.	
		if(parsed.length<2||bogusCommand==null){
			return "\n" + "Come again?\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";}

		//Else, return "You can't "+  bogus command.	
		return "\n" + "You can't "+bogusCommand+"\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";

	}


}
