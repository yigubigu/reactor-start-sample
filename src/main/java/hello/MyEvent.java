package hello;

public class MyEvent {

	private String eventName = "";
	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public MyEvent(String eventName) {
		this.eventName =  eventName;
	}
	
	
	public void filterInterest1(MyEvent myEvent) {
		
	}
}
