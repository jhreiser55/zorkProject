/**
 *An <tt>EventFactory</tt> is a factory class which instanciaates events.
 *@author Jordan Reiser
 */
public class EventFactory {
	private	static EventFactory theInstance;

	/**
	 *Instanciates a new <tt>EventFactory</tt> if there is not one already created.
	 */
	public static synchronized EventFactory instance() {
		if(theInstance == null) {
			theInstance = new EventFactory();
		}
	return theInstance;
	}

	private EventFactory() {
	}
	
	//Types of Events - Score, Wound, Die, Win, Drop, Disappear, Transform, Teleport, Describe
	Event parse(String event, String itemName) {
		//Example input for a wand Item -
		//wave[Score(5),Teleport]:A fairy breifly appears then dissapears.
		String onlyEvents = event.substring(event.indexOf('[')+1,event.indexOf(']'));
		String verbName = event.substring(0, event.indexOf('['));
		
		//new DescribeEvent with just the description after the :
		Event decoratedEvent = new DescribeEvent(event.substring(event.indexOf(':')+1));
		
		//Wraps Events with other Events to have multiple Events per Event
		//Score, Wound, and Transform all have extra params
		//Drop needs to know the Item
		if(onlyEvents.contains("Score")){decoratedEvent = new ScoreEvent(decoratedEvent,
										find(onlyEvents, "Score"));}
		if(onlyEvents.contains("Wound")){decoratedEvent = new WoundEvent(decoratedEvent,
										find(onlyEvents, "Wound"));}
		if(onlyEvents.contains("Die")){decoratedEvent = new DieEvent(decoratedEvent);}
		if(onlyEvents.contains("Win")){decoratedEvent = new WinEvent(decoratedEvent);}
		if(onlyEvents.contains("Drop")){decoratedEvent = new DropEvent(decoratedEvent, itemName);}
		if(onlyEvents.contains("Disappear")){decoratedEvent = new DisappearEvent(decoratedEvent, itemName);}
		if(onlyEvents.contains("Transform")){decoratedEvent = new TransformEvent(decoratedEvent, 
									find(onlyEvents, "Transform"), itemName);}
		if(onlyEvents.contains("Teleport")){decoratedEvent = new TeleportEvent(decoratedEvent);}
		if(onlyEvents.contains("HiddenItem")){decoratedEvent = new HiddenItemRoom(decoratedEvent,
									find(onlyEvents, "HiddenItem"));}
		if(onlyEvents.contains("HiddenExit")){decoratedEvent = new HiddenExitRoom(decoratedEvent,
									find(onlyEvents, "HiddenExit"));}
		return decoratedEvent;
	}

	//Creates DescribeEvent when there are no Events in the input
	Event parse(String event, boolean EventState){
		return new DescribeEvent(event.substring(event.indexOf(':')+1));
	}
	
	//Find a Event's value from the input, returns 0 if it cannot be found
	private String find(String input, String s){
		String[] tmp = input.split(",");
		for(String events : tmp){
			if(events.contains(s)){
				return events.substring(events.indexOf('(')+1, events.indexOf(')'));
			}
		}
		return "0";
	}
}
