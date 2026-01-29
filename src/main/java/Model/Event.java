package Model;

public class Event {
    private int eventId;          
    private String title;        
    private String category;      
    private String date;          
    private String location;      
    private int capacity;         
    private int seatsAvailable;   

    public Event(int eventId, String title, String category, String date,
                 String location, int capacity, int seatsAvailable) {
        this.eventId = eventId;
        this.title = title;
        this.category = category;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.seatsAvailable = seatsAvailable;
    }
    
    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public int getSeatsAvailable() { return seatsAvailable; }
}
