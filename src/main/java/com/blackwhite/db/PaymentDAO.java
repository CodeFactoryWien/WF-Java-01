package com.blackwhite.db;

import com.blackwhite.ctrl.DbController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PaymentDAO {
    private DbController db;
    private Statement statement;
    ArrayList<PaymentMethod> methods = new ArrayList<>();
    ArrayList<PaymentStatus> statuse = new ArrayList<>();

    public PaymentDAO () throws SQLException, ClassNotFoundException {
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

    public ObservableList<Payment> getAllPayments() throws SQLException {
        String sql = "SELECT * FROM payment;";
        ResultSet data = statement.executeQuery(sql);
        ArrayList<Payment> payments = new ArrayList<>();

        while (data.next()) {
            Payment payment = new Payment();
            payment.setPaymentMethod(data.getInt("payment_method_id"));
            payment.setPaymentStatus(data.getInt("payment_status_id"));
            payment.setSystemId(data.getString("payment_system_id"));
            payment.setAmount(data.getInt("amount"));
            payments.add(payment);
        }
        for (Payment payment: payments) {
            String sql2="SELECT `name` from payment_method JOIN payment WHERE payment_method.id= ? ";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql2);
            pstm.setInt(1, payment.getPaymentMethod());
            ResultSet typeData = pstm.executeQuery();
            while (typeData.next()){
                payment.setMethodName(typeData.getString("name"));
            }
        }
        for (Payment payment: payments) {
            String sql2="SELECT `name` from payment_status JOIN payment WHERE payment_status.id= ? ";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql2);
            pstm.setInt(1, payment.getPaymentStatus());
            ResultSet typeData = pstm.executeQuery();
            while (typeData.next()){
                payment.setStatusName(typeData.getString("name"));
            }
        }
        return FXCollections.observableArrayList(payments);
    }

    public ObservableList<PaymentMethod> getMethod() throws SQLException {
        String sql = "SELECT * FROM payment_method;";
        ResultSet data = statement.executeQuery(sql);

        while (data.next()) {
            PaymentMethod method = new PaymentMethod();
            method.setId(data.getInt("id"));
            method.setName(data.getString("name"));
            methods.add(method);
        }
        return FXCollections.observableList(methods);
    }

    public ObservableList<PaymentStatus> getStatus() throws SQLException {
        String sql = "SELECT * FROM payment_status;";
        ResultSet data = statement.executeQuery(sql);

        while (data.next()) {
            PaymentStatus status = new PaymentStatus();
            status.setId(data.getInt("id"));
            status.setName(data.getString("name"));
            statuse.add(status);
        }
        return FXCollections.observableList(statuse);
    }

    /*public Room addPayment (int roomNumber, int type, int size) {
        try {
            String sql3="INSERT INTO room (room_number, type_id, size, isAvailable) VALUES (?,?,?,?);";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql3);
            pstm.setInt(1, roomNumber);
            pstm.setInt(2, type);
            pstm.setInt(3, size);
            pstm.setBoolean(4, true);
            pstm.executeUpdate();
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setTypeId(type);
            room.setSize(size);
            room.setAvailable(true);
            for (RoomType roomType: types) {
                if(roomType.getTypeID()==type){
                    room.setTypeDescription(roomType.getDescription());
                }
            }
            return room;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteRoom (Room room) {
        try {
            String sql4="DELETE FROM room WHERE room_number=?;";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql4);
            pstm.setInt(1, room.getRoomNumber());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRoom (int oldRoomNumber, int newRoomNumber, int type, int size) {
        try {
            String sql5="UPDATE room SET room_number=?, type_id=?, size=? WHERE room_number=?;";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql5);
            pstm.setInt(1, newRoomNumber);
            pstm.setInt(2, type);
            pstm.setInt(3, size);
            pstm.setInt(4, oldRoomNumber);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/
}