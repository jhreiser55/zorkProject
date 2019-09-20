class Achievement{

private String message;
private int value;
private String name;

Achievement(String message, int value, String name){
this.value=value;
this.message=message;
this.name=name;
}
/**
 *Given the name, the method returns the <tt>Achievement</tt> that has been unlocked.
 *
 *@param name the name of the <tt>Achievement</tt> being unlocked.
 *@return <tt>Achievement</tt> the <tt>Achievement</tt> that has been unlocked.
 */
static Achievement unlock(String name){
name.toLowerCase();
if(name.equals("kill")){
	return new Achievement("Killmonger: All enemies defeated!", 20, "kill");
}
if (name.equals("move")){
	return new Achievement("Mover: You moved, good for you!", 20,"move");
}
if (name.equals("inventory")){
	return new Achievement("Attentive: You've perused your inventory!",20,"inventory");
}
if (name.equals("look")){
	return new Achievement("Looker: You used your eyes to look around!",20,"look");
}
if (name.equals("item")){
	return new Achievement("GoodStart: You have added an item to your inventory!",20,"item");
}
return null;
}
public int getValue(){
return this.value;
}
public String getMessage(){
return this.message;
}
public String getName(){
return this.name;
}
}
