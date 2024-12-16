package com.example.hspsm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Plot implements Serializable{
    public static String PlotFileName= "Plots.ser";
    private int plotId;
    private String plotNumber;
    private double length;
    private double width;
    private double totalArea;
    private String location;
    private String gpsCoordinates;
    private String status;// Available, Reserved, Sold
    private double pricePerUnit;
    private double totalPrice;
    private String developmentStatus; //Developed, Undeveloped

    public Plot(int plotId, String plotNumber, double length, double width, String location, String gpsCoordinates, String status, double pricePerUnit, String developmentStatus) {
        this.plotId = plotId;
        this.plotNumber = plotNumber;
        this.length = length;
        this.width = width;
        this.totalArea = calculateArea();
        this.location = location;
        this.gpsCoordinates = gpsCoordinates;
        this.status = status;
        this.pricePerUnit = pricePerUnit;
        this.totalPrice = calculateTotalPrice();
        this.developmentStatus = developmentStatus;
    }

    public String getDevelopmentStatus() {
        return developmentStatus;
    }

    public void setDevelopmentStatus(String developmentStatus) {
        this.developmentStatus = developmentStatus;
    }

    public String getGpsCoordinates() {
        return gpsCoordinates;
    }

    public void setGpsCoordinates(String gpsCoordinates) {
        this.gpsCoordinates = gpsCoordinates;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPlotId() {
        return plotId;
    }

    public void setPlotId(int plotId) {
        this.plotId = plotId;
    }

    public String getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        this.plotNumber = plotNumber;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(double totalArea) {
        this.totalArea = totalArea;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double calculateArea(){
        return length*width;

    }

    public void updatePlotDetails(){
        List<Plot> plots = loadPlots();
        if(plots!=null){
            for(Plot plot: plots){
                if(plot.getPlotId()==this.plotId){
                    plot.setPlotNumber(this.plotNumber);
                    plot.setLength(this.length);
                    plot.setWidth(this.width);
                    plot.setPricePerUnit(this.pricePerUnit);
                    plot.setDevelopmentStatus(this.developmentStatus);
                    plot.setGpsCoordinates(this.gpsCoordinates);
                    break;
                }
            }
            savePlots(plots);
        }
    }

    public void changeStatus(String newStatus) {
        List<Plot> plots = loadPlots();
        if (plots != null) {
            for (Plot plot : plots) {
                if (plot.getPlotId() == this.plotId) {
                    plot.setStatus(newStatus);
                    break;
                }
            }
            savePlots(plots);
        }
    }

    public void getPlotDetails() {
            List<Plot> plots = loadPlots();

        if (plots != null) {
                for (Plot plot : plots) {
                    if (plot.getPlotId() == this.plotId) {
                        System.out.println("Plot Details");
                        System.out.println("Plot Number: "+plot.getPlotNumber());
                        System.out.println("Length: "+plot.getLength());
                        System.out.println("Width: "+plot.getWidth());
                        System.out.println("Total Area: "+plot.getTotalArea());
                        System.out.println("Location: "+plot.getLocation());
                        System.out.println("GPS Coordinates: "+plot.getGpsCoordinates());
                        System.out.println("Price Per Unit: "+ plot.getPricePerUnit());
                        System.out.println("Total Price: "+plot.getTotalPrice());
                        System.out.println("Development Status: "+plot.getDevelopmentStatus());
                        System.out.println("Status: "+plot.getStatus());
                        return;
                    }
                    else
                        System.out.println("Plot not Found");
                }
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

    public  double calculateTotalPrice(){
        return pricePerUnit*totalArea;
    }
    @Override
    public String toString() {
        return String.format("Plot Number: %s\nLength: %f\nWidth: %f\nTotal Area: %f\nLocation: %s\nGPS Coordinates: %s\nPrice Per Unit: %f\nTotal Price: %f\nDevelopment Status: %s\nStatus: %s", plotNumber, length, width, totalArea, location, gpsCoordinates, pricePerUnit, totalPrice, developmentStatus, status);

    }
}
