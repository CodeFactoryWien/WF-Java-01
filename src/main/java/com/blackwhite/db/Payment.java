package com.blackwhite.db;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENT")
public class Payment {
    private int id;
    private int paymentMethod;
    private int paymentStatus;
    private int amount;
    private String systemId;
    private PaymentStatus payStatus;
    private PaymentMethod payMethod;

    @Id
    @Column(name="ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="PAYMENT_METHOD_ID")
    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Column(name="PAYMENT_STATUS_ID")
    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Column(name="AMOUNT")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Column(name="PAYMENT_SYSTEM_ID")
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public PaymentStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PaymentStatus payStatus) {
        this.payStatus = payStatus;
    }

    public PaymentMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PaymentMethod payMethod) {
        this.payMethod = payMethod;
    }

    @Override
    public String toString() {
        if(payStatus != null){
            return payStatus.getName() + ": " + systemId;
        }
        return systemId;
    }
}
