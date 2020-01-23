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
    ArrayList<Guest> guests = new ArrayList<>();

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
    }

    public ObservableList<Guest> getAllGuests() throws SQLException {
        String sql = "SELECT * FROM Guest;";
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

    public Guest addGuest (String firstName, String lastName, String email, String address, int docNumber) throws SQLException {
        String sql="INSERT INTO guest (id, first_name, last_name, email, address, doc_number) VALUES (?,?,?,?,?,?);";
        PreparedStatement pstm = db.getConnection().prepareStatement(sql);
        pstm.setInt(1, guests.get(guests.size()-1).getId()+1);
        pstm.setString(2, firstName);
        pstm.setString(3, lastName);
        pstm.setString(4, email);
        pstm.setString(5, address);
        pstm.setInt(6, docNumber);
        pstm.executeUpdate();
        Guest guest = new Guest();
        guest.setId(guests.get(guests.size()-1).getId()+1);
        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);
        guest.setAddress(address);
        guest.setDocNumber(docNumber);
        return guest;
    }

    public boolean deleteGuest (Guest guest) {
        try {
            String sql="DELETE FROM guest WHERE id=?;";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql);
            pstm.setInt(1, guest.getId());
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateGuest (int id, String firstName, String lastName, String email, String address, int docNumber) {
        try {
            String sql="UPDATE guest SET first_name=?, last_name=?, email=?, address=?, doc_number=?  WHERE id=?;";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql);
            pstm.setString(1, firstName);
            pstm.setString(2, lastName);
            pstm.setString(3, email);
            pstm.setString(4, address);
            pstm.setInt(5, docNumber);
            pstm.setInt(6, id);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
