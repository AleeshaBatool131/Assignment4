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
    private String plotType; // Commercial or Residential
    private String location;
    private String status;// Available, Reserved, Sold
    private double pricePerMarla;
    private double totalPrice;
    private String plotCategory; // Corner, Park-facing

    public Plot(int plotId, String plotNumber, double length, double width, double totalArea, String location, String plotType, String plotCategory, double pricePerMarla, double totalPrice, String status) {
        this.plotId = plotId;
        this.plotNumber = plotNumber;
        this.length = length;
        this.width = width;
        this.totalArea = totalArea;
        this.location = location;
        this.plotType = plotType;
        this.plotCategory = plotCategory;
        this.pricePerMarla = pricePerMarla;
        this.totalPrice = totalPrice;
        this.status = status;
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

    public double getPricePerMarla() {
        return pricePerMarla;
    }

    public void setPricePerMarla(double pricePerMarla) {
        this.pricePerMarla = pricePerMarla;
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

    public String getPlotCategory() {
        return plotCategory;
    }

    public void setPlotCategory(String plotCategory) {
        this.plotCategory = plotCategory;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
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
                    plot.setPricePerMarla(this.pricePerMarla);
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
                        System.out.println("Price Per Unit: "+ plot.getPricePerMarla());
                        System.out.println("Total Price: "+plot.getTotalPrice());
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
        return pricePerMarla *totalArea;
    }
    @Override
    public String toString() {
        return String.format("Plot Number: %s\nLength: %f\nWidth: %f\nTotal Area: %f\nLocation: %s\nGPS Coordinates: %s\nPrice Per Unit: %f\nTotal Price: %f\nDevelopment Status: %s\nStatus: %s", plotNumber, length, width, totalArea, location, pricePerMarla, totalPrice, status);

    }
}
