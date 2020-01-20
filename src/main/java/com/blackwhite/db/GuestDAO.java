package com.blackwhite.db;

import com.blackwhite.ctrl.DbController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GuestDAO {

    private DbController db;
    private Statement statement;


    public GuestDAO () throws SQLException, ClassNotFoundException {
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

    public ObservableList<Guest> getAllGuests() throws SQLException {
        String sql = "SELECT * FROM Guest;";
        ResultSet data = statement.executeQuery(sql);
        ArrayList<Guest> guests = new ArrayList<>();

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
}
