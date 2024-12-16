package com.example.hspsm;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Analytics {

    private int totalPlotsSold;
    private double totalRevenue;
    private int remainingPlots;
    private String popularPlotSize;
    private List<Buyer> frequentBuyers;

    public Analytics(int totalPlotsSold, double totalRevenue, int remainingPlots, String popularPlotSize, List<Buyer> frequentBuyers) {
        this.totalPlotsSold = totalPlotsSold;
        this.totalRevenue = totalRevenue;
        this.remainingPlots = remainingPlots;
        this.popularPlotSize = popularPlotSize;
        this.frequentBuyers = frequentBuyers;
    }

    public List<Buyer> getFrequentBuyers() {
        return frequentBuyers;
    }

    public void setFrequentBuyers(List<Buyer> frequentBuyers) {
        this.frequentBuyers = frequentBuyers;
    }

    public String getPopularPlotSize() {
        return popularPlotSize;
    }

    public void setPopularPlotSize(String popularPlotSize) {
        this.popularPlotSize = popularPlotSize;
    }

    public int getRemainingPlots() {
        return remainingPlots;
    }

    public void setRemainingPlots(int remainingPlots) {
        this.remainingPlots = remainingPlots;
    }

    public int getTotalPlotsSold() {
        return totalPlotsSold;
    }

    public void setTotalPlotsSold(int totalPlotsSold) {
        this.totalPlotsSold = totalPlotsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String generateSalesReport(){
        List<Payment> payments = loadPayments();
        double totalRevenue = 0;
        List<Integer> soldPlots = new ArrayList<>();
        for(Payment payment: payments){
            totalRevenue+= payment.getAmountPaid();
            if(!(soldPlots.contains(payment.getPlotId())))
                soldPlots.add(payment.getPlotId());
        }
        return String.format("Sales Report\nTotal Revenue: %f\nTotal Plots Sold: %d\n", totalRevenue,soldPlots.size());
    }
    public String analyzePlotStatistics(){
        List<Plot> plots = loadPlots();
        int totalPlots = plots.size();
        int soldPlots = 0;
        int availablePlots=0;
        List<Double> plotAreasSold = new ArrayList<>();
        for(Plot plot: plots){
            if("Sold".equals(plot.getStatus())){
                soldPlots++;
                plotAreasSold.add(plot.getTotalArea());
            } else if ("Available".equals(plot.getStatus())) {
                availablePlots++;
            }
        }
        double popularArea = 0;
        int maxCount =0;
        for(double area: plotAreasSold){
            int count = Collections.frequency(plotAreasSold, area);
            if(count> maxCount){
                maxCount = count;
                popularArea = area;
            }
        }
        return String.format("Plot Statistics:\nTotal Plots: %d\nSold Plots: %d\nAvailable Plots: %d\nPopular Plot Area: %.2f",
                totalPlots, soldPlots, availablePlots, popularArea);
    }
    public List<Buyer> getBuyerActivity(){
        List<Payment> payments = loadPayments();
        List<Buyer> buyers = loadBuyers();
        List<Buyer> frequentBuyers = new ArrayList<>();
        List<Integer> paymentCounts = new ArrayList<>();

        for(Buyer buyer: buyers){
            int count = 0;
            for(Payment payment: payments){
                if(payment.getBuyerId()==buyer.getBuyerId()){
                    count++;
                }
            }
            paymentCounts.add(count);
        }

        for(int i =0; i<paymentCounts.size(); i++){
            for(int j = i+1; j<paymentCounts.size(); j++){
                if(paymentCounts.get(j)>paymentCounts.get(i)){
                    int tempCount = paymentCounts.get(i);
                    paymentCounts.set(i, paymentCounts.get(j));
                    paymentCounts.set(j, tempCount);

                    Buyer tempBuyer = buyers.get(i);
                    buyers.set(i, buyers.get(j));
                    buyers.set(j, tempBuyer);
                }
            }
            frequentBuyers.addAll(buyers);
        }
        return frequentBuyers;
    }
    private List<Plot> loadPlots() {
        List<Plot> plots = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Plots.ser"))) {
            plots = (List<Plot>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Plots file not found. Starting with an empty list.");
            plots = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return plots;
    }
    public List<Payment> loadPayments(){
        List<Payment> payments = null;
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Payment.ser"))){
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
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Payment.ser"))){
            outputStream.writeObject(payments);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public List<Buyer> loadBuyers() {
        List<com.example.hspsm.Buyer> buyers = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Buyer.ser"))) {
            buyers = (List<com.example.hspsm.Buyer>) inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return buyers;
    }
}
