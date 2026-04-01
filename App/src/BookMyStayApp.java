import java.util.*;

class BookingHistory {
    private static List<Reservation> confirmedReservations = new ArrayList<>();
    public static void addReservation(Reservation reservation){
        confirmedReservations.add(reservation);
    }
    public static List<Reservation> getConfirmedReservations(){ return confirmedReservations; }
}

class BookingReportService {
    public void generateReport(){
        System.out.println("Booking History Report");
        for(Reservation reservation : BookingHistory.getConfirmedReservations()){
            System.out.println("Guest: " + reservation.getGuestName() + ", Room Type: " + reservation.getRoomType());
        }
    }
}

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
        } else {
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
            BookingHistory.addReservation(reservation);
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
    public Reservation getNextRequest() {
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
    private Map<String, Integer> roomAvailability = new HashMap<>();

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

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) { super(message); }
}

class ReservationValidator {
    public void validate(String guestName, String roomType, RoomInventory inventory) throws InvalidBookingException {
        if(!(roomType.equals("Single") || roomType.equals("Double") || roomType.equals("Suite"))) {
            throw new InvalidBookingException("Invalid room type selected");
        }
        if(inventory.getRoomAvailability().get(roomType) < 1) {
            throw new InvalidBookingException("Room unavailable");
        }
    }
}

class CancellationService {
    private Stack<String> releasedRoomIds;
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        this.releasedRoomIds = new Stack<>();
        this.reservationRoomTypeMap = new HashMap<>();
    }

    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (reservationRoomTypeMap.containsKey(reservationId)) {
            String roomType = reservationRoomTypeMap.get(reservationId);

            Map<String, Integer> availability = inventory.getRoomAvailability();
            availability.put(roomType, availability.getOrDefault(roomType, 0) + 1);
            inventory.updateRoomAvailability(availability);

            releasedRoomIds.push(reservationId);
            reservationRoomTypeMap.remove(reservationId);

            System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
        }
    }

    public void showRollbackHistory() {
        System.out.println("Rollback History (Most Recent First):");
        for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Booking Cancellation");

        RoomInventory inventory = new RoomInventory();
        inventory.initializeInventory("Single", 5);
        inventory.initializeInventory("Double", 0);
        inventory.initializeInventory("Suite", 1);

        CancellationService cancellationService = new CancellationService();

        cancellationService.registerBooking("Single-1", "Single");

        cancellationService.cancelBooking("Single-1", inventory);
        System.out.println();

        cancellationService.showRollbackHistory();
        System.out.println();

        System.out.println("Updated Single Room Availability: " + inventory.getRoomAvailability().get("Single"));
    }
}