import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *<tt>Item</tt> represents an Item in the {@link Dungeon} that the player
 *can interact with.
 *@author Kanghuaroo
 */
public class Item{
	
    /**
     *An <tt>Exception</tt> that is thrown when there is no <tt>Exit</tt> to be found from a 
     *method or be initialized from a <tt>Scanner</tt>.
     *@author Kanghuaroo
     */
    static class NoItemException extends Exception {}
	
	String primaryName;
	int weight;
	Hashtable<String,Event> messages;
	ArrayList<String> aliases;

	/**
	 * Instantiates a new <tt>Item</tt> Object using the passed input paramaters.
	 * @param s a <tt>Scanner</tt> Object that is positioned at the beginning of an
	 * <tt>Item</tt>  file entry.
	 * @throws NoItemException The reader is not postitioned at the start of an <tt>Item</tt>
	 * entry.  The reader's cursor will still be advanced by one line .
	 * @throws Dungeon.IllegalDugeonFormatException There is a problem with the dungeon
	 * file that the reader was made from that is detected when reading this <tt>Item</tt>
	 */
	public Item(Scanner s) throws NoItemException,
	       Dungeon.IllegalDungeonFormatException{
		       init();
		       //check first line
		       primaryName = s.nextLine();
		       aliases.add(primaryName);
		       if(primaryName.equals(Dungeon.TOP_LEVEL_DELIM))
			       throw new NoItemException();
		       //seperate primaryName from Aliases
		       String[] temp = primaryName.split(",");
		       primaryName = temp[0];
		       for(int i = 0; i<temp.length;i++){
			       aliases.add(temp[i]);
		       }

		weight = s.nextInt();
		s.nextLine();
		
		//creates messages
 	        String lineOfDesc = s.nextLine();
		while (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM) &&
        	       !lineOfDesc.equals(Dungeon.TOP_LEVEL_DELIM)) {
           		 
			//temp = lineOfDesc.split(":");
			
			//if there is an event in brackets []
			if(lineOfDesc.contains("[")){
	   		 	messages.put(lineOfDesc.substring(0, lineOfDesc.indexOf('[')),
			     		EventFactory.instance().parse(lineOfDesc, this.primaryName));
		        }
			else{
				messages.put(lineOfDesc.substring(0, lineOfDesc.indexOf(':')),
					EventFactory.instance().parse(lineOfDesc,false));
			}
			lineOfDesc = s.nextLine();
       		 }

	       	// throw away delimiter
       		 if (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM)) {
        	    throw new Dungeon.IllegalDungeonFormatException("No '" +
        	        Dungeon.SECOND_LEVEL_DELIM + "' after room.");
       		 }
	}

	/**
	 * Returns the <tt>primaryName String</tt>.The <tt>primaryName</tt> of a String
	 * may or may not be unique in a {@link Dungeon}.
	 *@return the <tt>primaryName String</tt> that this <tt>Item</tt> goes by
	 */
	public String getPrimaryName(){ return primaryName; }
	
	/**
	 *Returns a boolean that represents if this Item goes by the given String.
	 * Returns true if it does and false if it does not.
	 *@param name the <tt>String</tt> to be checked if this <tt>Item</tt> goes by it
	 *@return a boolean that represents if this <tt>Item</tt> goes by the passed name
	 */
	public boolean goesBy(String name) {
		for(int i = 0; i < aliases.size(); i++) {
			if(name.equalsIgnoreCase(aliases.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 *Returns the message for a given verb.  This also has the effect of triggering all the
	 *{@link Event}s associated with a verb.
	 *@param verb the verb <tt>String</tt> to get a message for
	 *@return the message <tt>String</tt> that matches the passed verb.Reurns <tt>null</tt>
	* if there is no eassage for te passed verb.
	 */
	public String getMessageForVerb(String verb) throws NoItemException {
		try{	
			return messages.get(verb).execute();
		}catch(NullPointerException e){ throw new NoItemException(); }
	}
	
	/**
	 *Returns the <tt>String</tt> representaion of an <tt>Item</tt> Object. The format of the 
	 *returned <tt>String</tt> is the "<tt>'primaryName'</tt> weights: <tt>'weight'</tt>, and also goes by:
	 *<tt>'aliases'</tt>."  The words in single quotes are instance variables of an <tt>Item</tt> Object.
	 * In the output, all <tt>aliases</tt> are printed out and seperated by spaces.
	 *@return the <tt>String</tt> representaion of an <tt>Item</tt> Object
	 */
	public String toString() {
		String desc = primaryName + " weighs: " + String.valueOf(weight) + ", and also goes by: ";
		for(int i = 0; i < aliases.size(); i++) {
			desc += aliases.get(i) + " ";
		}
		//removes extra space at the end
		String ret = desc.substring(0,desc.length()-1) + ".";
		return ret;
	}
	
	/**
	 *Returns the weight of an <tt>Item</tt>
	 *@return the <tt>weight</tt> variable of an <tt>Item</tt>
	 */
	public int getWeight() {return weight;}
	
	/**
	 *Does basic initialization of an <tt>Item</tt> Object
	 */
	private void init(){
		aliases = new ArrayList<String>();
		messages = new Hashtable<String,Event>();
	}

	/**
	 * Returns the <tt>alias ArrayList</tt> that contains the alternate names of an <tt>Item</tt>
	 *@return the <tt>ArrayList</tt> of <tt>aliases</tt> this <tt>Item</tt> goes by
	 */
	public ArrayList<String> getAliases() {
		return aliases;
	}
}
