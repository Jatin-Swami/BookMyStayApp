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
    public SingleRoom(int numberOfBeds, int squareFeet, double pricePerNight) {
        super(1, 250, 1500.00);
    }
}


class DoubleRoom extends Room {
    public DoubleRoom(int numberOfBeds, int squareFeet, double pricePerNight) {
        super(2, 400, 2500.00);
    }
}


class SuiteRoom extends Room {
    public SuiteRoom(int numberOfBeds, int squareFeet, double pricePerNight) {
        super(3, 750, 5000.00);
    }
}


public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Hotel Room Initialization\n");
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;
        Room singleRoom = new SingleRoom(singleRoomAvailable, 250, 1500.00);
        Room doubleRoom = new DoubleRoom(doubleRoomAvailable, 2500, 5000.00);
        Room suiteRoom = new SuiteRoom(suiteRoomAvailable, 2500, 5000.00);
        System.out.println("Single Room: ");
        singleRoom.displayRoomDetails();
        System.out.println("Available: " +  singleRoomAvailable + "\n");
        System.out.println("Double Room: ");
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " +  doubleRoomAvailable + "\n");
        System.out.println("Suite Room: ");
        suiteRoom.displayRoomDetails();
        System.out.println("Available: " +  suiteRoomAvailable);
    }
}
