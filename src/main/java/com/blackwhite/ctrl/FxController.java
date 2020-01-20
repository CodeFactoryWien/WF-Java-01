package com.blackwhite.ctrl;

import com.blackwhite.db.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FxController implements Initializable {
    @FXML
    private TextField documentid;
    @FXML
    private TextField addressid;
    @FXML
    private TextField emailid;
    @FXML
    private TextField lastnameid;
    @FXML
    private TextField firstnameid;
    @FXML
    private ListView<Guest> guestlist;
    @FXML
    private ChoiceBox<RoomType> typeList;
    @FXML
    private TextField sizeContent;
    @FXML
    private TextField roomNumberContent;
    @FXML
    private ListView<Room> roomList;
    @FXML
    private WebView webview;
    private RoomDAO roomDB;
    private GuestDAO guestDB;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            roomDB = new RoomDAO();
            guestDB = new GuestDAO();
            typeList.setItems(roomDB.getRoomTypes());
            roomList.setItems(roomDB.getAllRooms());
            guestlist.setItems(guestDB.getAllGuests());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        roomList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            roomNumberContent.setText(Integer.toString(newValue.getRoomNumber()));
            sizeContent.setText(Integer.toString(newValue.getSize()));
        });

        guestlist.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            firstnameid.setText(newValue.getFirstName());
            lastnameid.setText(newValue.getLastName());
            emailid.setText(newValue.getEmail());
            addressid.setText(newValue.getAddress());
            documentid.setText(Integer.toString(newValue.getDocNumber()));

        });
        WebEngine webEngine = webview.getEngine();
        webEngine.loadContent("<iframe width=\"350\" height=\"240\" src=\"https://www.youtube.com/embed/XyNlqQId-nk\" " +
                "frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\"></iframe>");
    }

    @FXML
    private void createRoom(){
        roomList.getItems().add(roomDB.addRoom(Integer.parseInt(roomNumberContent.getText()), typeList.getValue().getTypeID(), Integer.parseInt(sizeContent.getText())));
    }

    @FXML
    private void deleteRoom(){
        if(roomDB.deleteRoom(roomList.getSelectionModel().getSelectedItem())){
            roomList.getItems().remove(roomList.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void updateRoom(){
        Room selectedRoom = roomList.getSelectionModel().getSelectedItem();
        int newRoomNumber = Integer.parseInt(roomNumberContent.getText());
        int roomType = typeList.getValue().getTypeID();
        int roomSize = Integer.parseInt(sizeContent.getText());
        if(roomDB.updateRoom(selectedRoom.getRoomNumber(), newRoomNumber, roomType, roomSize)){
            selectedRoom.setRoomNumber(newRoomNumber);
            selectedRoom.setTypeId(roomType);
            selectedRoom.setTypeDescription(typeList.getValue().getDescription());
            selectedRoom.setSize(roomSize);
            roomList.refresh();
        }
    }

    public void closeConnection() throws SQLException {
        roomDB.closeConnection();
        guestDB.closeConnection();
    }

    @FXML
    private void addGuest(){
        guestlist.getItems().add(guestDB.addGuest(firstnameid.getText(), lastnameid.getText(), emailid.getText(), addressid.getText(), Integer.parseInt(documentid.getText())));
    }

    @FXML
    private void deleteGuest(){
        if(guestDB.deleteGuest(guestlist.getSelectionModel().getSelectedItem())){
            guestlist.getItems().remove(guestlist.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void updateGuest(){
        Guest selectedGuest = guestlist.getSelectionModel().getSelectedItem();
        String firstName = firstnameid.getText();
        String lastName = lastnameid.getText();
        String email = emailid.getText();
        String address = addressid.getText();
        int docNumber = Integer.parseInt(documentid.getText());
        if(guestDB.updateGuest(selectedGuest.getId(), firstName, lastName, email, address, docNumber)){
            selectedGuest.setFirstName(firstName);
            selectedGuest.setLastName(lastName);
            selectedGuest.setEmail(email);
            selectedGuest.setAddress(address);
            selectedGuest.setDocNumber(docNumber);
            guestlist.refresh();
        }
    }
}
