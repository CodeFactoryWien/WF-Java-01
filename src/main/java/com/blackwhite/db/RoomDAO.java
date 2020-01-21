package com.blackwhite.db;

import com.blackwhite.ctrl.DbController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoomDAO {
    private DbController db;
    private Statement statement;
    ArrayList<RoomType> types = new ArrayList<>();

    public RoomDAO () throws SQLException, ClassNotFoundException {
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

    public ObservableList<Room> getAllRooms() throws SQLException {
        String sql = "SELECT * FROM ROOM;";
        ResultSet data = statement.executeQuery(sql);
        ArrayList<Room> rooms = new ArrayList<>();

        while (data.next()) {
            Room room = new Room();
            room.setRoomNumber(data.getInt("room_number"));
            room.setTypeId(data.getInt("type_id"));
            room.setSize(data.getInt("size"));
            room.setAvailable(data.getBoolean("isAvailable"));
            for (RoomType roomType: types) {
                if(roomType.getTypeID()==room.getTypeId()) {
                    room.setType(roomType);
                    System.out.println(room.getType().toString());
                }
            }
            rooms.add(room);
        }
        return FXCollections.observableArrayList(rooms);
    }

    public ObservableList<RoomType> getRoomTypes() throws SQLException {
        String sql = "SELECT * FROM type;";
        ResultSet data = statement.executeQuery(sql);

        while (data.next()) {
            RoomType type = new RoomType();
            type.setTypeID(data.getInt("id"));
            type.setCapacity(data.getInt("capacity"));
            type.setPrice(data.getInt("price"));
            type.setEquipment(data.getString("equipment"));
            type.setDescription(data.getString("description"));
            types.add(type);
        }
        return FXCollections.observableList(types);
    }

    public Room addRoom (int roomNumber, int type, int size) {
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
                    room.setType(roomType);
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
    }
}
