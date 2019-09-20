//import java.util.Array;
/**
 *A <tt>WoundEvent</tt> is an event which will decrease or increase the amount 
 *of health points the adventurer has by a given Integer. The player will be prompted 
 *with the amount of health they lost or gained and the players current health
 *when the execute method is ran.
 *@author Jordan Reiser
 */
public class WoundEvent extends Event {
	private int val;
	
	WoundEvent(Event e, String stringVal) {
		super(e);
		this.val = Integer.parseInt(stringVal);
	}

	String execute() {	
		GameState.instance().changeHealth(val);
		int newHealth = GameState.instance().getHealth();
		String returnState = "";
		if(val > 0) {
			returnState = "Owie! You just lost " + val + " health. ";	
		}
		else if(val < 0) {
			returnState =  "Powering up! You just gained " + (val*-1) + " health. ";	
		}
		if(newHealth == 1) {
			returnState +=  "Ground control, we are going down.";
		}
		else if(newHealth == 2) {
			returnState += "Need. More. Water. Sandy!";
		}
		else if(newHealth == 3) {
			returnState += "Your health is: ((1 + 2)/(9/3))(2) + (18/18)";
		}
		else if(newHealth == 4) {
			returnState +=  "ffffoooouuuurrrr iiiissss yyyyoooouuuurrrr hhhheeeeaaaalllltttthhhh";
		}
		else if(newHealth == 5) {
			returnState +=  "Were only half way to being dead... or alive!";
		}
		else if(newHealth == 6) {
			returnState += "Say 'six' three times and you'll see something spooky!";
		}
		else if(newHealth == 7) {
			returnState += "If you get a blackjack divide it by 3.";
		}
		else if(newHealth == 8) {
			returnState += "is Eight really that great?";
		}
		else if(newHealth == 9) {
			returnState += "Im living an A- life right now.";
		}
		else if(newHealth >= 10) {
			returnState += "I feel like a new Man!";
		}
		return wrappedEvent.execute() + "\n" + 
			returnState;
	}
}
