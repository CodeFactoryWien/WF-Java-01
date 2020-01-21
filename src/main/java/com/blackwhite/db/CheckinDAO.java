package com.blackwhite.db;

import com.blackwhite.ctrl.DbController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        db.getConnection().close();
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
                "WHERE booking.checkout_date IS NULL);";
        ResultSet data = statement.executeQuery(sql);

        while (data.next()) {
            Guest guest = new Guest();
            guest.setFirstName(data.getString("first_name"));
            guest.setLastName(data.getString("last_name"));
            guest.setEmail(data.getString("email"));
            guest.setAddress(data.getString("address"));
            guest.setId(data.getInt("id"));
            guest.setDocNumber(data.getInt("doc_number"));
            guests.add(guest);
        }
        return FXCollections.observableArrayList(guests);
    }

    public ObservableList<Payment> getPaymentAvailable() throws SQLException {
        String sql = "SELECT * from payment JOIN payment_status ON payment.payment_status_id = payment_status.id WHERE payment_status.name<>\"payed\"";
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

}
