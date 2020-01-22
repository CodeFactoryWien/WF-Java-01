package com.blackwhite.ctrl;

import com.blackwhite.db.*;
import javafx.event.ActionEvent;
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
    private ChoiceBox<Payment> paymentAvailable;
    @FXML
    private ChoiceBox<Guest> mainGuest;
    @FXML
    private TextField bookedRoom;
    @FXML
    private ListView<Room> availableRoomsList;
    @FXML
    private ListView<Payment> paymentlist;
    @FXML
    private TextField amountid;
    @FXML
    private ChoiceBox<PaymentMethod> methodid;
    @FXML
    private ChoiceBox<PaymentStatus> statusid;
    @FXML
    private TextField systemid;
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
    private PaymentDAO paymentDB;
    private CheckinDAO checkinDB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            roomDB = new RoomDAO();
            guestDB = new GuestDAO();
            paymentDB = new PaymentDAO();
            checkinDB = new CheckinDAO();
            typeList.setItems(roomDB.getRoomTypes());
            roomList.setItems(roomDB.getAllRooms());
            guestlist.setItems(guestDB.getAllGuests());
            methodid.setItems(paymentDB.getMethod());
            statusid.setItems(paymentDB.getStatus());
            paymentlist.setItems(paymentDB.getAllPayments());
            availableRoomsList.setItems(checkinDB.getAvailableRooms());
            mainGuest.setItems(checkinDB.getAvailableGuests());
            paymentAvailable.setItems(checkinDB.getPaymentAvailable());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        roomList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            roomNumberContent.setText(Integer.toString(newValue.getRoomNumber()));
            sizeContent.setText(Integer.toString(newValue.getSize()));
            typeList.valueProperty().setValue(newValue.getType());
        });

        guestlist.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            firstnameid.setText(newValue.getFirstName());
            lastnameid.setText(newValue.getLastName());
            emailid.setText(newValue.getEmail());
            addressid.setText(newValue.getAddress());
            documentid.setText(Integer.toString(newValue.getDocNumber()));
        });
        paymentlist.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            amountid.setText(Integer.toString(newValue.getAmount()));
            systemid.setText(newValue.getSystemId());
            methodid.valueProperty().setValue(newValue.getPayMethod());
            statusid.valueProperty().setValue(newValue.getPayStatus());
        }));
        availableRoomsList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            bookedRoom.setText(Integer.toString(newValue.getRoomNumber()));
        } );
        WebEngine webEngine = webview.getEngine();
        webEngine.loadContent("<iframe width=\"350\" height=\"240\" src=\"https://www.youtube.com/embed/XyNlqQId-nk\" " +
                "frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\"></iframe>");
    }

    @FXML
    private void createRoom(){
        roomList.getItems().add(roomDB.addRoom(Integer.parseInt(roomNumberContent.getText()), typeList.getValue().getTypeID(), Integer.parseInt(sizeContent.getText())));
        availableRoomsList.refresh();
    }

    @FXML
    private void deleteRoom(){
        if(roomDB.deleteRoom(roomList.getSelectionModel().getSelectedItem())){
            roomList.getItems().remove(roomList.getSelectionModel().getSelectedItem());
            availableRoomsList.refresh();
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
            selectedRoom.setType(typeList.getValue());
            selectedRoom.setSize(roomSize);
            roomList.refresh();
            availableRoomsList.refresh();
        }
    }
    @FXML
    private void createPayment(){
        paymentlist.getItems().add(paymentDB.addPayment(methodid.getValue().getId(), statusid.getValue().getId(), Integer.parseInt(amountid.getText()), systemid.getText()));
        try {
            paymentAvailable.getItems().removeAll();
            paymentAvailable.setItems(checkinDB.getPaymentAvailable());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void updatePayment(){
        Payment selectedPayment = paymentlist.getSelectionModel().getSelectedItem();
        int amount = Integer.parseInt(amountid.getText());
        int methodId = methodid.getValue().getId();
        int statusId = statusid.getValue().getId();
        if(paymentDB.updatePayment(methodId, statusId, amount, selectedPayment.getSystemId())){
            selectedPayment.setPaymentMethod(methodId);
            selectedPayment.setPaymentStatus(statusId);
            selectedPayment.setAmount(amount);
            selectedPayment.setPayStatus(statusid.getValue());
            selectedPayment.setPayMethod(methodid.getValue());
            paymentlist.refresh();
            try {
                paymentAvailable.getItems().removeAll();
                paymentAvailable.setItems(checkinDB.getPaymentAvailable());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void deletePayment(){
        if(paymentDB.deletePayment(paymentlist.getSelectionModel().getSelectedItem())){
            paymentlist.getItems().remove(paymentlist.getSelectionModel().getSelectedItem());
            try {
                paymentAvailable.getItems().removeAll();
                paymentAvailable.setItems(checkinDB.getPaymentAvailable());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addGuest(){
        guestlist.getItems().add(guestDB.addGuest(firstnameid.getText(), lastnameid.getText(), emailid.getText(), addressid.getText(), Integer.parseInt(documentid.getText())));
        try {
            mainGuest.getItems().removeAll();
            mainGuest.setItems(checkinDB.getAvailableGuests());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteGuest(){
        if(guestDB.deleteGuest(guestlist.getSelectionModel().getSelectedItem())){
            guestlist.getItems().remove(guestlist.getSelectionModel().getSelectedItem());
            try {
                mainGuest.getItems().removeAll();
                mainGuest.setItems(checkinDB.getAvailableGuests());
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            try {
                mainGuest.getItems().removeAll();
                mainGuest.setItems(checkinDB.getAvailableGuests());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public void closeConnection() throws SQLException {
        roomDB.closeConnection();
        guestDB.closeConnection();
        paymentDB.closeConnection();
    }

    public void checkGuestsIn(ActionEvent actionEvent) {
    }
}
