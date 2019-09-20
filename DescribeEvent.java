public class DescribeEvent extends Event{
	private String desc;

	DescribeEvent(String desc){
		this.desc = desc;
	}

	String execute(){
		return this.desc;
	}
}
