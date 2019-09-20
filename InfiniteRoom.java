import java.util.Scanner;

/**
 *An <tt>InfiniteRoom</tt> is of type {@link Room} which will have one of the exits cause the player to keep returning to the <tt>InfiniteRoom</tt> they are trying to leave.
 *@author Jordan Reiser
 */
public class InfiniteRoom extends Room {
	private int Xdepth;
	private int Ydepth;
	private int Zdepth;
	private String infiniteExit;
	
	/**
	 *A constructor which will create a new object of type <tt>InfiniteRoom</tt> with a Scanner, Dungeon, and initState, copies constructor from super class {@link Room}.
	 *
	 *@param s A Scanner which will read the given file to find if an <tt>InfiniteRoom</tt> will be created.
	 *@param d A dungeon in which the <tt>InfiniteRoom</tt> will be added to.
	 *@param initState A boolean which determines if the <tt>InfiniteRooms</tt>'s description will be printed.
	 *@param dir A String which will be the direction of the infinite exit.
	 * @throws NoRoomException The Scanner is not positioned at the correct place in the file and will result in the cursor being moved to the next line. 
	 *@throws IllegalDungeonFormatException Will be thrown if there is a structural problem with the Dungeon file and the Dungeon could not be created.
	 */
	public InfiniteRoom(Scanner s, Dungeon d, boolean initState) throws NoRoomException, Dungeon.IllegalDungeonFormatException {
		super(s,d,initState);

		Xdepth = 0;
		Ydepth = 0;
		Zdepth = 0;
	}

	/**
	 *The leaveBy method will cause the player to enter a new room based off their inputed direction if it is in the direction of a valid exit.
	 *@param dir A String which is the direction that the player would like to leave the current room.
	 */
	public Room leaveBy(String dir) {
		Room temp =  super.leaveBy(dir);
		
		if(temp == null) {return null;}

		//case for when you leave in infinate direction
		if(temp.getTitle().equals(this.getTitle()))
		{
			incrementDepth(dir);
			return this;
		}
		
		//if depth ==0
		if( getDepth() == 0 ){return temp;}
		
		//base case
		return null;
	}

	//changes depth for axis depending on dir
	private void incrementDepth(String dir) {
		if(dir.equals("n")){ Xdepth++; }
		else if (dir.equals("s")){ Xdepth--; }
		else if (dir.equals("e")){ Zdepth++; }
		else if (dir.equals("w")){ Zdepth--; }
		else if (dir.equals("u")){ Ydepth++; }
		else if (dir.equals("d")){ Ydepth--; }
	}

	int getDepth(){
		return Xdepth+Ydepth+Zdepth;
	}

	//describe
	public String describe() {
		//if depth is 0 all over then print normal desc
		if(getDepth() == 0){ return super.describe(); }


		String description;
		if (beenHere) {	description = title; } 
		else { description = title + "\n" + desc; }
		
		for (Exit exit : exits) {
			//only print exits to the same place
			//Room titles ==
			if(exit.getSrc().getTitle().equals(exit.getDest().getTitle())) {
			       	description += "\n" + exit.describe();
		       	}
		}
		
		for (Item item : contents) {
			try{
				description += "\n" + "There is a "
					+ item.getPrimaryName() + " here";
			}
			catch(Exception e){}
		}


		beenHere = true;
		return description;
	}
}
