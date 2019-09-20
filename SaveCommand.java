/**
 *Method saves game by writing user's progress to a .sav file.
 *@author The-Onion-Knight
 */
public class SaveCommand extends Command{
private String saveFilename;

SaveCommand(String s){
this.saveFilename=s;
}
/**
 *Method saves game by writing current progress to a .sav file. If an error is found it informs user
 *that file cannot be saved.
 *@return <tt>String</tt> a message either letting user know that game saved, or that it could not.
 */
public String execute(){

            try {
                GameState.instance().store(saveFilename);
                return "\n" + "Data saved to " + saveFilename +
                    GameState.SAVE_FILE_EXTENSION + ".\n"+
		    GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
            } catch (Exception e) {
                System.err.println("Couldn't save!");
                e.printStackTrace();
                return "\n" + "";
}

}
}
