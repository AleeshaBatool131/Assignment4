package com.example.hspsm;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private int reservationId;
    private int plotId;
    private int buyerId;
    private LocalDate reservationDate;
    private String status; // reserved , cancelled

    public Reservation(int reservationId, int plotId, int buyerId, LocalDate reservationDate, String status) {
        this.reservationId = reservationId;
        this.plotId = plotId;
        this.buyerId = buyerId;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getPlotId() {
        return plotId;
    }

    public void setPlotId(int plotId) {
        this.plotId = plotId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean reservePlot(int plotId){
        boolean reserved =false;
        List<Plot> plots = loadPlots();
        for(Plot plot: plots){
            if(plot.getPlotId()== plotId){
                plot.setStatus("Reserved");
                reserved = true;
                savePlots(plots);
                break;
            }
        }
        return reserved;
    }

    public void cancelReservation(){
        List<Plot> plots = loadPlots();
        for(Plot plot: plots){
            if(plot.getPlotId()== this.plotId){
                plot.setStatus("Available");
                savePlots(plots);
                break;
            }
        }
    }

    public void  getReservationDetails(){
        List<Plot> plots = loadPlots();
        for(Plot plot: plots){
            if(plot.getStatus().equals("Reserved"))
                System.out.println(plot);
        }

    }
    private List<Plot> loadPlots() {
        List<Plot> plots = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Plots.ser"))) {
            plots = (List<Plot>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty list.");
            plots = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return plots;
    }

    private void savePlots(List<Plot> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Plots.ser"))) {
            outputStream.writeObject(plots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
