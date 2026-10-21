public class SuiteRoom extends Room {
    public SuiteRoom(int roomNumber, double price) {
        super(roomNumber, price);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Room Number: " + getRoomNumber());
        System.out.println("Price: Rs. " + getPrice());
    }
}
