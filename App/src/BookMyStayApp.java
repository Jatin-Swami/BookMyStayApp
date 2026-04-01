import java.awt.print.Book;
import java.util.*;
class Service {
    private String serviceName;
    private double cost;
    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }
    public String getServiceName(){
        return serviceName;
    }
    public double getCost(){
        return cost;
    }
}
class AddOnServiceManager {
    private Map<String, List<Service>> serviceByReservation;
    public AddOnServiceManager() {
        serviceByReservation = new HashMap<>();
    }
    public void addService(String reservationId, Service service) {
        if (serviceByReservation.containsKey(reservationId)) {
            serviceByReservation.get(reservationId).add(service);
        }
        else {
            serviceByReservation.put(reservationId, new ArrayList<>());
            serviceByReservation.get(reservationId).add(service);
        }
    }
    public double calculateTotalServiceCost(String reservationId) {
        double cost = 0;
        for(Service serviceName : serviceByReservation.get(reservationId)) {
            cost += serviceName.getCost();
        }
        return cost;
    }
}
class RoomAllocationService {
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();
        if (availability.getOrDefault(roomType, 0) > 0) {
            String roomId = generateRoomId(roomType);
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);
            availability.put(roomType, availability.get(roomType) - 1);
            inventory.updateRoomAvailability(availability);
            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() + ", Room ID: " + roomId);
        } else {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName() + " - No " + roomType + " rooms available.");
        }
    }
    private String generateRoomId(String roomType) {
        int count = 1;
        if (assignedRoomsByType.containsKey(roomType)) {
            count = assignedRoomsByType.get(roomType).size() + 1;
        }
        String newRoomId = roomType + "-" + count;
        while (allocatedRoomIds.contains(newRoomId)) {
            count++;
            newRoomId = roomType + "-" + count;
        }
        return newRoomId;
    }
}
class Reservation {
    private String guestName;
    private String roomType;
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
    public String getGuestName() {
        return guestName;
    }
    public String getRoomType() {
        return roomType;
    }
}
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;
    public BookingRequestQueue() { requestQueue = new LinkedList<>(); }
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }
    public  Reservation getNextRequest() {
        return requestQueue.poll();
    }
    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}
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
        RoomInventory inventory = new RoomInventory();
        inventory.initializeInventory("Single", 5);
        inventory.initializeInventory("Double", 5);
        inventory.initializeInventory("Suite", 5);

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Single");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        RoomAllocationService allocationService = new RoomAllocationService();

        System.out.println("Room Allocation Processing");

        while(bookingQueue.hasPendingRequests()) {
            Reservation currentRequest = bookingQueue.getNextRequest();
            allocationService.allocateRoom(currentRequest, inventory);
        }

        System.out.println("\nAdd-On Service Selection\nReservation ID: Single-1");
        Service service = new Service("Breakfast", 1500);
        AddOnServiceManager services = new AddOnServiceManager();
        services.addService("Single-1", service);
        System.out.println("Total Add-On Cost: " + services.calculateTotalServiceCost("Single-1"));
    }
}
