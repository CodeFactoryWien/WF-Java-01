package com.blackwhite.ctrl;

import com.blackwhite.db.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FxController implements Initializable {
    @FXML
    private Button checkoutButton;
    @FXML
    private TextField breakfastService;
    @FXML
    private CheckBox wlanService;
    @FXML
    private CheckBox bedService;
    @FXML
    private DatePicker datepicker;
    @FXML
    private TextField checkoutRoom;
    @FXML
    private ListView<GuestBooking> checkoutList;
    @FXML
    private ListView<GuestBooking> checkedInList;
    @FXML
    private TextField checkedRoom;
    @FXML
    private TextField checkedDate;
    @FXML
    private TextField checkedGuest;
    @FXML
    private ChoiceBox<Guest> additionalGuests;
    @FXML
    private ListView<Guest> addGuestsList;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab checkinGuestsTab;
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
            updateLists();
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
            documentid.setText(newValue.getDocNumber());
        });
        paymentlist.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            amountid.setText(Integer.toString(newValue.getAmount()));
            systemid.setText(newValue.getSystemId());
            methodid.valueProperty().setValue(newValue.getPayMethod());
            statusid.valueProperty().setValue(newValue.getPayStatus());
            systemid.setDisable(true);
        }));
        availableRoomsList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->
                bookedRoom.setText(Integer.toString(newValue.getRoomNumber())));

        checkedInList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            checkedRoom.setText(Integer.toString(newValue.getBooking().getRoom_id()));
            checkedDate.setText(newValue.getBooking().getCheckin().toString());
            checkedGuest.setText(newValue.getGuest().toString());
            try {
                addGuestsList.setItems(checkinDB.getRoomGuests(newValue.getBookingID()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        checkoutButton.setDisable(true);
        checkoutList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            checkoutRoom.setText(Integer.toString(newValue.getBooking().getRoom_id()));
            checkoutButton.setDisable(true);
            datepicker.valueProperty().setValue(null);
        }));
        datepicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (datepicker.getValue()!=null){
                if (checkoutRoom.getText().isEmpty()){
                    System.out.println("Select Room");
                } else {
                    checkoutButton.setDisable(false);
                }
            }
        }));
        checkedRoom.setDisable(true);
        checkedDate.setDisable(true);
        checkedGuest.setDisable(true);
        WebEngine webEngine = webview.getEngine();
        webEngine.loadContent("<iframe width=\"350\" height=\"240\" src=\"https://www.youtube.com/embed/XyNlqQId-nk\" " +
                "frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\"></iframe>");
    }

    @FXML
    private void createRoom(){
        try {
            Room room = roomDB.addRoom(Integer.parseInt(roomNumberContent.getText()),
                    typeList.getValue().getTypeID(), Integer.parseInt(sizeContent.getText()));
            if (room!=null){
                roomList.getItems().add(room);
                availableRoomsList.getItems().add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert();
        }
    }

    @FXML
    private void deleteRoom() {
        if(roomDB.deleteRoom(roomList.getSelectionModel().getSelectedItem())){
            roomList.getItems().remove(roomList.getSelectionModel().getSelectedItem());
            try {
                availableRoomsList.setItems(checkinDB.getAvailableRooms());
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
            }
        }
    }

    @FXML
    private void updateRoom() {
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
            try {
                availableRoomsList.setItems(checkinDB.getAvailableRooms());
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
            }
        }
    }

    @FXML
    private void createPayment(){
        try {
            paymentlist.getItems().add(paymentDB.addPayment(methodid.getValue().getId(),
                    statusid.getValue().getId(), Integer.parseInt(amountid.getText()), systemid.getText()));
            paymentAvailable.setItems(checkinDB.getPaymentAvailable());
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert();
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
                paymentAvailable.setItems(checkinDB.getPaymentAvailable());
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
            }
        }
        systemid.setDisable(false);
    }

    @FXML
    private void deletePayment(){
        if(paymentDB.deletePayment(paymentlist.getSelectionModel().getSelectedItem())){
            paymentlist.getItems().remove(paymentlist.getSelectionModel().getSelectedItem());
            systemid.setDisable(false);
            try {
                paymentAvailable.setItems(checkinDB.getPaymentAvailable());
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
            }
        }
    }

    @FXML
    private void addGuest(){
        try {
            guestlist.getItems().add(guestDB.addGuest(firstnameid.getText(), lastnameid.getText(),
                    emailid.getText(), addressid.getText(), documentid.getText()));
            mainGuest.setItems(checkinDB.getAvailableGuests());
            additionalGuests.setItems(mainGuest.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert();
        }
    }

    @FXML
    private void deleteGuest(){
        if(guestDB.deleteGuest(guestlist.getSelectionModel().getSelectedItem())){
            guestlist.getItems().remove(guestlist.getSelectionModel().getSelectedItem());
            try {
                mainGuest.setItems(checkinDB.getAvailableGuests());
                additionalGuests.setItems(mainGuest.getItems());
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
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
        String docNumber = documentid.getText();
        if(guestDB.updateGuest(selectedGuest.getId(), firstName, lastName, email, address, docNumber)){
            selectedGuest.setFirstName(firstName);
            selectedGuest.setLastName(lastName);
            selectedGuest.setEmail(email);
            selectedGuest.setAddress(address);
            selectedGuest.setDocNumber(docNumber);
            guestlist.refresh();
            try {
                mainGuest.setItems(checkinDB.getAvailableGuests());
                additionalGuests.setItems(mainGuest.getItems());
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
            }
        }
    }

    @FXML
    private void checkGuestsIn() {
        if (checkinDB.checkIn(Integer.parseInt(bookedRoom.getText()), mainGuest.getValue(),
                paymentAvailable.getValue())) {
            availableRoomsList.getItems().remove(availableRoomsList.getSelectionModel().getSelectedItem());
            bookedRoom.clear();
            additionalGuests.getItems().remove(mainGuest.getValue());
            mainGuest.getItems().remove(mainGuest.getValue());
            paymentAvailable.getSelectionModel().clearSelection();
            try {
                checkedInList.setItems(checkinDB.getOccupiedRooms());
                checkoutList.setItems(checkinDB.getOccupiedRooms());
                tabPane.getSelectionModel().select(checkinGuestsTab);
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
            }
        }
    }

    @FXML
    private void addFurtherGuests() {
        if(checkinDB.addRoomGuest(checkedInList.getSelectionModel().getSelectedItem(), additionalGuests.getValue())){
            mainGuest.getItems().remove(additionalGuests.getValue());
            additionalGuests.getItems().remove(additionalGuests.getValue());
            additionalGuests.getSelectionModel().clearSelection();
            try {
                addGuestsList.setItems(checkinDB.getRoomGuests(checkedInList.getSelectionModel().getSelectedItem().getBookingID()));
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert();
            }
        }
    }

    public void checkGuestsOut() {
        GuestBooking booking = checkoutList.getSelectionModel().getSelectedItem();
        Date checkoutDay = java.sql.Date.valueOf(datepicker.getValue());
        boolean wlan = wlanService.isSelected();
        boolean bed = bedService.isSelected();
        int breakfast = Integer.parseInt(breakfastService.getText());
        int finalPrice = checkinDB.checkout(booking, checkoutDay, breakfast, wlan, bed);
        checkoutRoom.clear();
        datepicker.getEditor().clear();
        wlanService.setSelected(false);
        bedService.setSelected(false);
        breakfastService.setText(String.valueOf(0));
        // Show pop-up with final price for room
        Window owner = checkoutButton.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check-out");
        alert.setHeaderText(null);
        alert.setContentText("Amount to be paid: "+finalPrice);
        alert.initOwner(owner);
        alert.show();
        try {
            updateLists();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert();
        }
    }

    private void updateLists() throws SQLException {
        paymentlist.setItems(paymentDB.getAllPayments());
        availableRoomsList.setItems(checkinDB.getAvailableRooms());
        mainGuest.setItems(checkinDB.getAvailableGuests());
        paymentAvailable.setItems(checkinDB.getPaymentAvailable());
        checkedInList.setItems(checkinDB.getOccupiedRooms());
        checkoutList.setItems(checkinDB.getOccupiedRooms());
        additionalGuests.setItems(mainGuest.getItems());
    }

    private void showErrorAlert(){
        Window owner = checkoutButton.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred! Please reload the application");
        alert.initOwner(owner);
        alert.show();
    }

    public void closeConnection() throws SQLException {
        guestDB.closeConnection();
        paymentDB.closeConnection();
        checkinDB.closeConnection();
        roomDB.closeConnection();
    }
}
