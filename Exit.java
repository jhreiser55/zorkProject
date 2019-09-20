
import java.util.Scanner;

/**
 *A link between two {@link Room} Objects through a specified direction.
 *@author Kanhuaroo
 */
public class Exit {
    
     /**
     *An <tt>Exception</tt> that is thrown when there is no <tt>Exit</tt> to be found from a 
     *method or be initialized from a <tt>Scanner</tt>.
     *@author Kanghuaroo
     */
    class NoExitException extends Exception {}

    private String dir;
    private Room src, dest;

    Exit(String dir, Room srcRoom, Room destRoom) {
        init();
        this.dir = dir;
        this.src = srcRoom;
        this.dest = destRoom;
        this.src.addExit(this);
    }

    public Exit(String s, String direction, String des) {
        init();
	Dungeon d = GameState.instance().getDungeon();

        this.src = d.getRoom(s);
        this.dir = direction;
        this.dest = d.getRoom(des);
        
	this.src.addExit(this);
    }

    /** Given a Scanner object positioned at the beginning of an "exit" file
        entry, read and return an Exit object representing it. 
	@param s The Scanner Object of the file, positioned at the top of an "exit" file entry
	@param d The {@link Dungeon} that contains this exit (so that Room objects 
        may be obtained.)
        @throws NoExitException The reader object is not positioned at the
        start of an exit entry. A side effect of this is the reader's cursor
        is now positioned one line past where it was.
        @throws IllegalDungeonFormatException A structural problem with the
        dungeon file itself, detected when trying to read this room.
     */
    public Exit(Scanner s, Dungeon d) throws NoExitException,
        Dungeon.IllegalDungeonFormatException {

        init();
        String srcTitle = s.nextLine();
        if (srcTitle.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoExitException();
        }
        src = d.getRoom(srcTitle);
        dir = s.nextLine();
        dest = d.getRoom(s.nextLine());
        
        // I'm an Exit object. Add me as an exit to my source Room.
	src.addExit(this);

        // throw away delimiter
        if (!s.nextLine().equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                Dungeon.SECOND_LEVEL_DELIM + "' after exit.");
        }
    }

    // Common object initialization tasks.
    private void init() {
    }

    /**
     *Returns a <tt>String</tt> description of this <tt>Exit</tt>.  It displays 
     * the direction you can go in and the title of the {@link Room} in that direction.
     *@return the <tt>String</tt> representation of this <tt>Exit</tt>
     */
    public String describe() {
        return "You can go " + dir + " to " + dest.getTitle() + ".";
    }

    /**
     * Returns the <tt>dir</tt> variable of an <tt>Exit</tt> Object
     *@return the String variable <tt>dir</tt> of an <tt>Exit</tt> Object
     */
    public String getDir() { return dir; }
    
    /**
     * Returns the source (where you came from) {@link Room} of this <tt>Exit</tt> Object
     *@return the {@link Room} Obeject associated with the <tt>src</tt> variable.
     */
    public Room getSrc() { return src; }
    
    /**
     * Returns the destination, where you are going, {@link Room} of this <tt>Exit</tt> Object
     *@return the {@link Room} Obeject associated with the <tt>dest</tt> variable.
     */
    public Room getDest() { return dest; }
}
