import java.util.ArrayList;
import java.util.Random;
class AttackCommand extends Command {
	//Make it easier and limit it to 1 monster per room
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<Monster> ph=GameState.instance().getAdventurersCurrentRoom().getMonsters();
	AttackCommand(){
		for(Monster m:ph){
			monsters.add(m);
		}
	}
	//implement the combat value from the weapons/armor to attack.
	String execute() {
		int a=0;
		int i=0;
		if (GameState.instance().getAdventurersCurrentRoom().hasMonsters()){
			Random r = new Random();
			for(Monster m:monsters){
				i=r.nextInt(10);
				a=i;
				GameState.instance().getAdventurersCurrentRoom().getMonster(m.getName()).changeHealth(i);
			}
			return "You have attacked the foe for "+a+" damage!\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
		}
		return "\nThere is nothing in the vicinity to attack.\n"+GameState.instance().getAdventurersCurrentRoom().describe()+"\n";
	}
}
