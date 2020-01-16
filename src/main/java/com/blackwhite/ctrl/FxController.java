package com.blackwhite.ctrl;

import com.blackwhite.db.Room;
import com.blackwhite.db.RoomDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FxController implements Initializable {
    @FXML
    private TextField sizeContent;
    @FXML
    private TextField roomNumberContent;
    @FXML
    private ListView<Room> roomList;
    @FXML
    private WebView webview;
    @FXML
    private Insets x4;
    @FXML
    private Insets x1;
    @FXML
    private Insets x5;
    private RoomDAO roomDB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            roomDB = new RoomDAO();
            roomList.setItems(roomDB.getAllRooms());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        roomList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            roomNumberContent.setText(Integer.toString(newValue.getRoomNumber()));
            sizeContent.setText(Integer.toString(newValue.getSize()));
//            try {
//                roomList.setItems(roomDB.getTeacherClasses(newValue));
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        });
        WebEngine webEngine = webview.getEngine();
        webEngine.loadContent("<iframe width=\"350\" height=\"240\" src=\"https://www.youtube.com/embed/XyNlqQId-nk\" " +
                "frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\"></iframe>");
    }

    public void closeConnection() throws SQLException {
        roomDB.closeConnection();
    }
}
