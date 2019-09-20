
import java.util.Scanner;

/**
 *The interpreter reads in a .zork/.sav file, depending on the the user's input. It then instantiates a {@link Dungeon} object from the file. 
 *@author The-Onion-Knight
 *@version 2019
 */
public class Interpreter {

    private static GameState state; // not strictly necessary; GameState is 
                                    // singleton

    public static String USAGE_MSG = 
        "Usage: Interpreter dungeonFile.zork|saveFile.sav.";

    /**
     *Reads user input and determines if file name ends with .zork/.sav. If not it tells user to input a 
     *.zork or .sav file. Creates a command so long as input does not equal 'q'. Q ends program.
     *
     *@param args an array of <tt>String</tt> objects created from the user input. 
     */
    public static void main(String args[]) {

        if (args.length < 1) {
            System.err.println(USAGE_MSG);
            System.exit(1);
        }

        String command;
        Scanner commandLine = new Scanner(System.in);

        try {
            state = GameState.instance();
            if (args[0].endsWith(".zork")) {
                state.initialize(new Dungeon(args[0], true));
                System.out.println("\nWelcome to " + 
                    state.getDungeon().getName() + "!");
            } else if (args[0].endsWith(".sav")) {
                state.restore(args[0]);
                System.out.println("\nWelcome back to " + 
                    state.getDungeon().getName() + "!");
            } else {
                System.err.println(USAGE_MSG);
                System.exit(2);
            }

            System.out.print("\n" + 
                state.getAdventurersCurrentRoom().describe() + "\n");

            command = promptUser(commandLine);

            while (!command.equals("q")) {
		
                System.out.print(
                    CommandFactory.instance().parse(command).execute());

                command = promptUser(commandLine);
            }

            System.out.println("Bye!");
	
	}catch(Exception e) { 
            e.printStackTrace(); 
        } 
    }
    private static String promptUser(Scanner commandLine) {

        System.out.print("> ");
        return commandLine.nextLine();
    }

}
