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
    private ArrayList<PaymentMethod> methods = new ArrayList<>();
    private ArrayList<PaymentStatus> statuse = new ArrayList<>();

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
    }

    public ObservableList<Payment> getAllPayments() throws SQLException {
        String sql = "SELECT * FROM payment;";
        ResultSet data = statement.executeQuery(sql);
        ArrayList<Payment> payments = new ArrayList<>();

        while (data.next()) {
            Payment payment = new Payment();
            payment.setId(data.getInt("id"));
            payment.setPaymentMethod(data.getInt("payment_method_id"));
            payment.setPaymentStatus(data.getInt("payment_status_id"));
            payment.setSystemId(data.getString("payment_system_id"));
            payment.setAmount(data.getInt("amount"));
            for (PaymentMethod paymentM: methods) {
                if(paymentM.getId()==payment.getPaymentMethod()) {
                    payment.setPayMethod(paymentM);
                }
            }
            for (PaymentStatus paymentS: statuse) {
                if(paymentS.getId()==payment.getPaymentStatus()) {
                    payment.setPayStatus(paymentS);
                }
            }
            payments.add(payment);
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

    public Payment addPayment (int method, int status, int amount, String systemId) {
        try {
            String sql3="INSERT INTO payment (payment_method_id, payment_status_id, amount, payment_system_id) VALUES (?,?,?,?);";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, method);
            pstm.setInt(2, status);
            pstm.setInt(3, amount);
            pstm.setString(4, systemId);
            pstm.executeUpdate();
            ResultSet result = pstm.getGeneratedKeys();
            if(result.next()){
                Payment payment = new Payment();
                payment.setId(result.getInt(1));
                payment.setPaymentMethod(method);
                payment.setPaymentStatus(status);
                payment.setAmount(amount);
                payment.setSystemId(systemId);
                for (PaymentMethod paymentM: methods) {
                    if(paymentM.getId()==payment.getPaymentMethod()) {
                        payment.setPayMethod(paymentM);
                    }
                }
                for (PaymentStatus paymentS: statuse) {
                    if(paymentS.getId()==payment.getPaymentStatus()) {
                        payment.setPayStatus(paymentS);
                    }
                }
                return payment;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deletePayment (Payment payment) {
        try {
                if (payment.getPaymentStatus()==1){
                    String sql4 = "DELETE FROM payment WHERE id=?;";
                    PreparedStatement pstm = db.getConnection().prepareStatement(sql4);
                    pstm.setInt(1, payment.getId());
                    pstm.executeUpdate();
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean updatePayment (int methodid, int statusid, int amount, String systemid) {
        try {
            String sql5="UPDATE payment SET payment_method_id=?, payment_status_id=?, amount=? WHERE payment_system_id=?;";
            PreparedStatement pstm = db.getConnection().prepareStatement(sql5);
            pstm.setInt(1, methodid);
            pstm.setInt(2, statusid);
            pstm.setInt(3, amount);
            pstm.setString(4, systemid);
            pstm.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
