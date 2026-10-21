public class DeluxeRoom extends Room {
    public DeluxeRoom(int roomNumber, double price) {
        super(roomNumber, price);
    }

    @Override
    public String getRoomType() {
        return "Deluxe Room";
    }

    @Override
    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Room Number: " + getRoomNumber());
        System.out.println("Price: Rs. " + getPrice());
    }
}
