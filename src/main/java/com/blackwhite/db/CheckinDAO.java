package com.blackwhite.db;

import javafx.collections.ObservableList;
import com.blackwhite.ctrl.DbController;
import javafx.collections.FXCollections;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.sql.*;

public class CheckinDAO {
    private DbController db;
    private Statement statement;
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<RoomType> types = new ArrayList<>();
    private ArrayList<Guest> guests = new ArrayList<>();
    private ArrayList<GuestBooking> mainBookings = new ArrayList<>();
    private ArrayList<Payment> payments = new ArrayList<>();

    public CheckinDAO() throws SQLException, ClassNotFoundException {
        init();
    }

    private void init() throws ClassNotFoundException, SQLException {
        if (db == null) {
            db = DbController.getInstance();
        }
        statement = db.getConnection().createStatement();
    }

    public void closeConnection() throws SQLException {
        statement.close();
    }

    public ObservableList<Room> getAvailableRooms() throws SQLException {
        String sql1 = "SELECT * FROM type;";
        ResultSet data1 = statement.executeQuery(sql1);
        types.clear();
        RoomDAO.createRooms(data1, types);

        String sql ="SELECT * from Room WHERE isAvailable = 1;";
        ResultSet data = statement.executeQuery(sql);
        rooms.clear();
        while (data.next()) {
            Room room = newRoom(data);
            rooms.add(room);
        }
        return FXCollections.observableArrayList(rooms);
    }

    public ObservableList<Guest> getAvailableGuests() throws SQLException {
        String sql = "SELECT * FROM guest g WHERE g.id NOT IN (SELECT guest.id FROM guest JOIN guest_booking " +
                "ON guest_booking.guest_id=guest.id JOIN booking ON booking.id=guest_booking.booking_id " +
                "WHERE booking.checkout_date IS NULL) ORDER BY g.last_name;";
        ResultSet data = statement.executeQuery(sql);
        guests.clear();
        while (data.next()) {
            Guest guest = new Guest();
            guest.setFirstName(data.getString("first_name"));
            guest.setLastName(data.getString("last_name"));
            guest.setId(data.getInt("id"));
            guests.add(guest);
        }
        return FXCollections.observableArrayList(guests);
    }

    public ObservableList<Payment> getPaymentAvailable() throws SQLException {
        String sql = "SELECT * from payment JOIN payment_status ON payment.payment_status_id = payment_status.id " +
                "WHERE payment_status.name<>'paid' ORDER BY payment.id DESC";
        ResultSet data = statement.executeQuery(sql);
        payments.clear();
        while (data.next()) {
            Payment payment = new Payment();
            payment.setId(data.getInt("id"));
            payment.setSystemId(data.getString("payment_system_id"));
            payment.setAmount(data.getInt("amount"));
            payments.add(payment);
        }
        return FXCollections.observableArrayList(payments);
    }

    public boolean checkIn(int roomNumber, Guest mainGuest, Payment bookingPayment) {
        try {
            String sql = "INSERT INTO booking (room_id, payment_id, checkin_date)" +
                    "VALUES (?, ?, ?);";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, roomNumber);
            pstm.setInt(2, bookingPayment.getId());
            long now = System.currentTimeMillis();
            pstm.setDate(3, new Date(now));
            pstm.executeUpdate();
            ResultSet result = pstm.getGeneratedKeys();
            if(result.next()) {
                int bookingID = result.getInt(1);
                String sql2 = "INSERT INTO guest_booking (booking_id, guest_id, iscontactperson)" +
                        "VALUES (?,?,?);";
                PreparedStatement pstm2 = db.getConnection().prepareStatement(sql2);
                pstm2.setInt(1, bookingID);
                pstm2.setInt(2, mainGuest.getId());
                pstm2.setBoolean(3, true);
                pstm2.executeUpdate();

                String sql3 = "UPDATE room SET isAvailable = '0' WHERE room_number = ?;";
                PreparedStatement pstm3 = db.getConnection().prepareStatement(sql3);
                pstm3.setInt(1, roomNumber);
                pstm3.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<GuestBooking> getOccupiedRooms() throws SQLException {
        String sql = "SELECT * FROM guest_booking JOIN booking ON booking_id=booking.id " +
                "JOIN guest ON guest_booking.guest_id = guest.id JOIN room ON room_number=room_id " +
                "WHERE iscontactperson=true AND checkout_date IS NULL;";
        ResultSet data = statement.executeQuery(sql);
        mainBookings.clear();
        while (data.next()) {
            GuestBooking mainBooking = new GuestBooking();
            mainBooking.setBookingID(data.getInt("booking_id"));
            mainBooking.setGuestID(data.getInt("guest_id"));
            mainBooking.setContactPerson(true);
            Guest guest = new Guest();
            guest.setFirstName(data.getString("first_name"));
            guest.setLastName(data.getString("last_name"));
            mainBooking.setGuest(guest);
            Booking booking = new Booking();
            booking.setId(data.getInt("booking.id"));
            booking.setRoom_id(data.getInt("room_id"));
            booking.setPayment_id(data.getInt("payment_id"));
            booking.setPrice(data.getInt("price"));
            booking.setCheckin(data.getDate("checkin_date"));
            mainBooking.setBooking(booking);
            Room room = newRoom(data);
            mainBooking.getBooking().setRoomBooked(room);
            for (Payment payment: payments) {
                if(payment.getId()==booking.getPayment_id()){
                    booking.setPaymentBooked(payment);
                }
            }
            mainBookings.add(mainBooking);
        }
        return FXCollections.observableArrayList(mainBookings);
    }

    public ObservableList<Guest> getRoomGuests(int bookingID) throws SQLException {
        ArrayList<Guest> roomGuests = new ArrayList<>();
        String sql = "SELECT * FROM guest_booking JOIN guest g on guest_booking.guest_id = g.id " +
                "WHERE booking_id=?;";
        PreparedStatement pstm = db.getConnection().prepareStatement(sql);
        pstm.setInt(1, bookingID);
        ResultSet data = pstm.executeQuery();
        while (data.next()){
            Guest guest = new Guest();
            guest.setId(data.getInt("guest_id"));
            guest.setFirstName(data.getString("first_name"));
            guest.setLastName(data.getString("last_name"));
            roomGuests.add(guest);
        }
        return FXCollections.observableArrayList(roomGuests);
    }

    public boolean addRoomGuest(GuestBooking selectedBooking, Guest newGuest) {
        try {
            String sql = "INSERT INTO guest_booking (booking_id, guest_id, iscontactperson)" +
                    "VALUES (?,?,?);";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql);
            pstm.setInt(1, selectedBooking.getBookingID());
            pstm.setInt(2, newGuest.getId());
            pstm.setBoolean(3, false);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int checkout (GuestBooking booking, Date checkoutDate, int breakfast, boolean wlan, boolean extrabed){
        int roomCosts = calcRoomCosts(booking, checkoutDate);
        int finalPrice = calcPrice(roomCosts, breakfast, wlan, extrabed);
        long now = System.currentTimeMillis();
        Date checkout = new Date(now);
        int payID = booking.getBooking().getPayment_id();
        try {
            String sql = "UPDATE room SET isAvailable = '1' WHERE room_number = ?;";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql);
            pstm.setInt(1, booking.getBooking().getRoom_id());
            pstm.executeUpdate();
            String sql2 = "UPDATE booking SET checkout_date = ?, price = ? WHERE id = ?;";
            PreparedStatement pstm2 = db.getConnection().prepareStatement(sql2);
            pstm2.setDate(1, checkout);
            pstm2.setInt(2, finalPrice);
            pstm2.setInt(3, booking.getBookingID());
            pstm2.executeUpdate();
            String sql3 = "UPDATE payment SET amount = ? WHERE id = ?;";
            PreparedStatement pstm3 = db.getConnection().prepareStatement(sql3);
            pstm3.setInt(1, finalPrice+booking.getBooking().getPaymentBooked().getAmount());
            pstm3.setInt(2, payID);
            pstm3.executeUpdate();
            String sql4 = "SELECT COUNT(*) as bookings FROM booking WHERE payment_id =? AND checkout_date IS NULL;";
            PreparedStatement pstm4 = db.getConnection().prepareStatement(sql4);
            pstm4.setInt(1, payID);
            ResultSet data = pstm4.executeQuery();
            while (data.next()){
                if(data.getInt("bookings")==0){
                    String sql5 = "UPDATE payment SET payment_status_id=0 WHERE id=?;";
                    PreparedStatement pstm5 = db.getConnection().prepareStatement(sql5);
                    pstm5.setInt(1, payID);
                    pstm5.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finalPrice;
    }

    private int calcPrice(int roomCosts, int breakfast, boolean wlan, boolean extrabed){
        int finalCost = roomCosts;
        try {
            if(breakfast>0){
                String sql = "SELECT * FROM services WHERE name='Breakfast';";
                ResultSet data = statement.executeQuery(sql);
                if (data.next()) {
                    finalCost+=data.getInt("price")*breakfast;
                }
            }
            if(wlan){
                String sql = "SELECT * FROM services WHERE name='WIFI';";
                ResultSet data = statement.executeQuery(sql);
                if (data.next()) {
                    finalCost+=data.getInt("price");
                }
            }
            if(extrabed){
                String sql = "SELECT * FROM services WHERE name='Extra Bed';";
                ResultSet data = statement.executeQuery(sql);
                if (data.next()) {
                    finalCost+=data.getInt("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finalCost;
    }

    private int calcRoomCosts(GuestBooking booking, Date checkoutDate){
        long nights = booking.getBooking().getCheckin().toLocalDate().until(checkoutDate.toLocalDate(), ChronoUnit.DAYS);
        return (int) (nights*booking.getBooking().getRoomBooked().getType().getPrice());
    }

    private Room newRoom (ResultSet data) throws SQLException {
        Room room = new Room();
        room.setRoomNumber(data.getInt("room_number"));
        room.setTypeId(data.getInt("type_id"));
        room.setAvailable(data.getBoolean("isAvailable"));
        room.setSize(data.getInt("size"));
        for (RoomType type: types){
            if(type.getTypeID() == room.getTypeId()){
                room.setType(type);
            }
        }
        return room;
    }
}
