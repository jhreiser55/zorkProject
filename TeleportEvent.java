import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
/**
 *A <tt>Teleport</tt> is an event which will modify the {@link GameState} such that the 
 *adventurers current room is a randomly generated room throughout the dungeon.
 *@author Jordan Reiser
 */
public class TeleportEvent extends Event {
	private Hashtable<String,Room> rooms;
	private ArrayList<Room> randomRoom;
	
	TeleportEvent(Event e) {
		super(e);
		//this.rooms = GameState.instance().getDungeon().getRooms();
		randomRoom = new ArrayList<Room>();
	}
		
	String  execute() {
		this.rooms = GameState.instance().getDungeon().getRooms();
		Enumeration<String> enumeration = GameState.instance().getDungeon().getRooms().keys();
		this.rooms = GameState.instance().getDungeon().getRooms();
		while(enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			randomRoom.add(rooms.get(key));
		}

		int max = randomRoom.size();
		int min = 0;
		int range = max;
		int randomNumber = (int)(Math.random() * range);
		Room random = randomRoom.get(randomNumber);
		random.changeBoolean();
		GameState.instance().setAdventurersCurrentRoom(random);
		return wrappedEvent.execute() + "\n" + 
			random.describe();
	}
}
