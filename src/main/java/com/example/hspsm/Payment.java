package com.example.hspsm;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payment {
    public static String PaymentFileName= "Payments.ser";
    private static int paymentCount = 0;
    private int paymentId;
    private int plotId;
    private int buyerId;
    private LocalDate paymentDate;
    private double amountPaid;
    private String paymentMethod;// cash, bank transfer, card
    private double outstandingBalance;

    public Payment(int plotId, int buyerId,  double amountPaid, String paymentMethod) {
        this.paymentId=++paymentCount;
        this.plotId = plotId;
        this.buyerId = buyerId;
        this.paymentDate = LocalDate.now();
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.outstandingBalance = getOutstandingBalance();
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getPlotId() {
        return plotId;
    }

    public void setPlotId(int plotId) {
        this.plotId = plotId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public  void recordPayment(){
        List<Payment> payments = loadPayments();
        payments.add(this);
        savePayments(payments);

    }

    public List<Payment> trackPaymentHistory(int plotId){
        List<Payment> paymentList = new ArrayList<>();
        List<Payment> payments = loadPayments();
        for(Payment payment: payments){
            if(payment.getPlotId()==plotId){
                paymentList.add(payment);
            }
        }
        return paymentList;

    }

    public double getOutstandingBalance(int plotId){
        List<Payment> payments = loadPayments();
        double balance=0;
        for(Payment payment: payments){
            if(payment.getPlotId()==plotId){
                balance= payment.getOutstandingBalance();
            }
        }
        return balance;
    }
    public List<Payment> loadPayments(){
        List<Payment> payments = null;
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Payments.ser"))){
            payments=(List<Payment>) inputStream.readObject();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return payments;
    }

    public void savePayments(List<Payment> payments){
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Payments.ser"))){
            outputStream.writeObject(payments);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return String.format("Payment ID: %d\nBuyer ID: %d\nPlot ID: %d\nPaid Amount: %.2f\nOutstanding Balance: %.2f\nPayment Method: %s\nPayment Date: %s",paymentId,buyerId,plotId,amountPaid,outstandingBalance,paymentMethod,paymentDate);
    }
}
