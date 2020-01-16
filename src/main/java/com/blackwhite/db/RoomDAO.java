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
            rooms.add(room);
        }
        for (Room room: rooms) {
            String sql2="SELECT `description` from TYPE JOIN ROOM WHERE type.id= ? ";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql2);
            pstm.setInt(1, room.getTypeId());
            ResultSet typeData = pstm.executeQuery();
            while (typeData.next()){
                room.setTypeDescription(typeData.getString("description"));
            }
        }
        return FXCollections.observableArrayList(rooms);
    }

}
