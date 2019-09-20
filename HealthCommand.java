
class HealthCommand extends Command{
	/**
	 *<tt>HealthCommand</tt> retrieves health from {@link GameState} and returns an appropriate message
	 *depending on how much health that the character has left.
	 *@author The-Onion-Knight
	 */
	private static GameState state;
	private Room current; 
	HealthCommand(){
		state=GameState.instance();
		current=state.getAdventurersCurrentRoom();
	}	

	String execute(){
		if (state.getHealth() <= 3){
			return "You're not feeling too good bud, perhaps you should take a rest.\n"+current.describe()+"\n";
		}
		else if(state.getHealth() <= 5){
			return "You're not in great shape but you've still got some fight left, be wary.\n"+current.describe()+"\n";
		}
		else if(state.getHealth() <=9){
			return "You've taken some damage but you're still doing well.\n"+current.describe()+"\n";
		}
		else {
			return "You are at peak human condition!\n"+current.describe()+"\n";
		}
	}

}
