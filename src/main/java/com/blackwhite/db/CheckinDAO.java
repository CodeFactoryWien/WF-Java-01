package com.blackwhite.db;

import com.blackwhite.ctrl.DbController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class CheckinDAO {
    private DbController db;
    private Statement statement;
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Guest> guests = new ArrayList<>();

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
        ArrayList<RoomType> types = new ArrayList<>();
        String sql1 = "SELECT * FROM type;";
        ResultSet data1 = statement.executeQuery(sql1);

        while (data1.next()) {
            RoomType type = new RoomType();
            type.setTypeID(data1.getInt("id"));
            type.setCapacity(data1.getInt("capacity"));
            type.setPrice(data1.getInt("price"));
            type.setEquipment(data1.getString("equipment"));
            type.setDescription(data1.getString("description"));
            types.add(type);
        }

        String sql ="SELECT * from Room WHERE isAvailable = 1;";
        ResultSet data = statement.executeQuery(sql);
        while (data.next()) {
            Room room = new Room();
            room.setRoomNumber(data.getInt("room_number"));
            room.setTypeId(data.getInt("type_id"));
            room.setSize(data.getInt("size"));
            room.setAvailable(data.getBoolean("isAvailable"));
            for (RoomType roomType: types) {
                if(roomType.getTypeID()==room.getTypeId()) {
                    room.setType(roomType);
                }
            }
            rooms.add(room);
        }
        return FXCollections.observableArrayList(rooms);
    }

    public ObservableList<Guest> getAvailableGuests() throws SQLException {
        String sql = "SELECT * FROM guest g WHERE g.id NOT IN (SELECT guest.id FROM guest JOIN guest_booking " +
                "ON guest_booking.guest_id=guest.id JOIN booking ON booking.id=guest_booking.booking_id " +
                "WHERE booking.checkout_date IS NULL) ORDER BY g.last_name;";
        ResultSet data = statement.executeQuery(sql);

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
                "WHERE payment_status.name<>'payed' ORDER BY payment.id DESC";
        ResultSet data = statement.executeQuery(sql);
        ArrayList<Payment> payments = new ArrayList<>();

        while (data.next()) {
            Payment payment = new Payment();
            payment.setId(data.getInt("id"));
            payment.setSystemId(data.getString("payment_system_id"));
            payments.add(payment);
        }
        return FXCollections.observableArrayList(payments);
    }

    public boolean checkIn(int roomNumber, Guest mainGuest, Payment bookingPayment) {
        try {
            String sql = "INSERT INTO booking (room_id, payment_id, number_guests, checkin_date)" +
                    "VALUES (?, ?, ?, ?);";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, roomNumber);
            pstm.setInt(2, bookingPayment.getId());
            pstm.setInt(3, 1);
            long now = System.currentTimeMillis();
            pstm.setDate(4, new Date(now));
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
}
