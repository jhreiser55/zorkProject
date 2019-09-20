import java.util.Scanner;
import java.util.Random;

/**
 *A <tt>Monster</tt> is an object which the player will encounter in Rooms which has 
 *a name, health and a catchphrase and will have the ability to fight.
 *@param name The name of the Monster, helps identify the type of monster in the current room.
 *@param health Used in the {@link AttackCommand} class to help determine the winner 
 *of the fight, the player or monster.
 *@param catchPhrase String which is displayed once the user decides to fight the monster. 
 *differentiates between every Monster object.
 *@author Jordan Reiser
 */

public class Monster {

	/**
	 *Remember to update Dungeon when adding monster hydration.
	 *
	 */
	private String name;
	private int health;
	private String catchPhrase="";
	private Random r = new Random();
	static class NoMonsterException extends Exception{}
	public Monster(Scanner s) throws NoMonsterException,
               Dungeon.IllegalDungeonFormatException{
                       //check first line
                       name = s.nextLine();
                       if(name.equals(Dungeon.TOP_LEVEL_DELIM))
                               throw new NoMonsterException();
                health = s.nextInt();
                //creates catchphrase
                String lineOfDesc = s.nextLine();
                while (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM) &&
                       !lineOfDesc.equals(Dungeon.TOP_LEVEL_DELIM)) {
			catchPhrase+=lineOfDesc;
                        lineOfDesc = s.nextLine();
		       }
	}

	/**
	 *Creates an object of type {@link Monster} with a name, health and catchPhrase.
	 *@param name The name of the Monster, helps identify the type of monster in 
	 *the current room.
	 *@param health Used in the {@link AttackCommand} class to help determine the 
	 *winner of the fight, the player or the monster.
	 *@param catchPhrase String which is displayed once the user decides to fight 
	 *the Monster. differentiates between every Monster object.
	 */
	public Monster(String name, int health, String catchPhrase) {
		this.name = name;
		this.health = health;
		this.catchPhrase = catchPhrase;
	}
	
	/**
	*Method returns the <tt>Monster</tt>'s catch phrase.
	*@return <tt>String</tt> the {@link Monster}'s catch phrase.
	*/
	public String getCatchPhrase() {
		return this.attack()+"\n"+catchPhrase;
	}

	/**
	*Method uses a random number to generate an attack for the <tt>Monster</tt> and 
	*applies it to the user, causing the user to lose the appropriate amount of health.
 	*@return <tt>String</tt> a message that informs the user that they have been 
	*attacked and lost health.
 	*/
	public String attack() {
		int i = r.nextInt(10);
		if (i>6){
		GameState.instance().changeHealth(1);
		return "You've been attacked! Perhaps you should check your health!";
		}
		return "The Monster attacks you, but it misses!";
	}
	/**
	 *Method retuns the {@link Monster}'s name. 
	 *@return <tt>String</tt> the name of the {@link Monster}
	 *@param monster the {@link Monster} that the method is being called on.
	 */
	public String getName(){
		return this.name;
	}
	public void changeHealth(int i){
		health-=i;
		if (this.isDead()){
			System.out.println("The foe has been defeated!");
			GameState.instance().getDungeon().removeMonster(this);
			GameState.instance().getAdventurersCurrentRoom().remove(this);
			}

	}
	public boolean isDead(){
		if (health<=0){
			return true;
		}
		return false;
	}
}
