import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
/**
 *The <tt>CommandFactory</tt> parses the user input and then returns the appropriate command based on the verb entered.
 *@author The-Onion-Knight
 */
public class CommandFactory {

	private static CommandFactory theInstance;
	public static List<String> MOVEMENT_COMMANDS = 
		Arrays.asList("n","w","e","s","u","d" );
	
	public static synchronized CommandFactory instance() {
		if (theInstance == null) {
			theInstance = new CommandFactory();
		}
		return theInstance;
	}

	private CommandFactory() {
	}
	/**
	 *Parses the user input, and returns the appropriate <tt>Command</tt>. I.E. if user enters save,
	 *a <tt>SaveCommand</tt> object will be generated.
	 *@param command A <tt>String</tt> that will be parsed to determine what <tt>Command</tt> should be created.	    *@return <tt>Command</tt> The appropriate <tt>Command</tt> that will be instantiated.
	 */
	public Command parse(String command) throws Exception{
		try{

			command.toLowerCase();
			String parsed [];
			parsed = command.split(" ");
			if (MOVEMENT_COMMANDS.contains(parsed[0])) {
				GameState.instance().addAchievement("move");
				return new MovementCommand(command);
			}
			else if (parsed[0]==null){
				throw new Exception();
			}
			else if(parsed[0].equals("attack")||parsed[0].equals("hit")||parsed[0].equals("kick")){
				return new AttackCommand();
			}
			else if (parsed[0].equals("save")){
				System.out.println("What would you like to call your save file?");
				Scanner input = new Scanner(System.in);
				String a = input.nextLine();
				// For now, only one type of command object, to move and to save.
				return new SaveCommand(a);
			}
			else if (parsed[0].equals("take")){
				return new TakeCommand(command);
			}
			else if (parsed[0].equals("drop")){
				GameState.instance().addAchievement("kill");
				return new DropCommand(command);
			}
			else if (parsed[0].equals("i")||parsed[0].equals("inventory")){
				GameState.instance().addAchievement("inventory");
				return new InventoryCommand();
			}
			else if (parsed[0].equals("look")){
				GameState.instance().addAchievement("look");
				return new LookCommand();
			}
			else if (parsed[0].equals("health")){
				return new HealthCommand();
			}
			else if (parsed[0].equals("score")){
				return new ScoreCommand();
			}
			else{
				for (String s: parsed){
					try{

						if(GameState.instance().getItemFromInventoryNamed(s).goesBy(s)){
							return new ItemSpecificCommand(parsed[0],s);
						}

					}
					catch(Item.NoItemException e) {}
				}

				for (String s: parsed){
					try{

						if(GameState.instance().getItemInVicinityNamed(s).goesBy(s)){
							return new ItemSpecificCommand(parsed[0],s);
						}

					}
					catch(Item.NoItemException e) {}
				}
			}

			return new UnknownCommand(" ");
		}

		catch(Exception e){
			return new UnknownCommand(" ");
		}

	}
}
