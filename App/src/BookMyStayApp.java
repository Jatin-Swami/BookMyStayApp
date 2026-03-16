import java.util.*;

class RoomSearchService {
    public void searchAvailableRooms(RoomInventory inventory, Room singleRoom, Room doubleRoom, Room suiteRoom) {
        Map<String, Integer> availability = inventory.getRoomAvailability();
        if(availability.get("Single") > 0) {
            System.out.println("Single Room: ");
            singleRoom.displayRoomDetails();
            System.out.println("Available Rooms: " + availability.get("Single") + "\n");
        }
        if(availability.get("Double") > 0) {
            System.out.println("Double Room: ");
            doubleRoom.displayRoomDetails();
            System.out.println("Available Rooms: " + availability.get("Double") + "\n");
        }
        if(availability.get("Suite") > 0) {
            System.out.println("Suite Room: ");
            suiteRoom.displayRoomDetails();
            System.out.println("Available Rooms: " + availability.get("Suite") + "\n");
        }
    }
}


class RoomInventory {
    private Map<String, Integer> roomAvailability =  new HashMap<>();
    public RoomInventory() {
        roomAvailability.put("Single", 0);
        roomAvailability.put("Double", 0);
        roomAvailability.put("Suite", 0);
    }
    public void initializeInventory(String roomType, int available) {
        roomAvailability.put(roomType, available);
    }
    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
    public void updateRoomAvailability(Map<String, Integer> roomAvailability) {
        this.roomAvailability = roomAvailability;
    }
}


abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;
    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }
    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}


class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.00);
    }
}


class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.00);
    }
}


class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.00);
    }
}


public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Hotel Room Initialization\n");
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();
        RoomInventory roomInventory = new RoomInventory();
        roomInventory.initializeInventory("Single", 5);
        roomInventory.initializeInventory("Double", 3);
        roomInventory.initializeInventory("Suite", 2);
        RoomSearchService roomSearchService = new RoomSearchService();
        roomSearchService.searchAvailableRooms(roomInventory, singleRoom, doubleRoom, suiteRoom);
    }
}
