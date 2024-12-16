package com.example.hspsm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Map implements Serializable {

    private int mapId;
    private String societyLayout;
    private List<Plot> plotMarkers;

    public Map(int mapId, String societyLayout, List<Plot> plotMarkers) {
        this.mapId = mapId;
        this.societyLayout = societyLayout;
        this.plotMarkers = plotMarkers;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public List<Plot> getPlotMarkers() {
        return plotMarkers;
    }

    public void setPlotMarkers(List<Plot> plotMarkers) {
        this.plotMarkers = plotMarkers;
    }

    public String getSocietyLayout() {
        return societyLayout;
    }

    public void setSocietyLayout(String societyLayout) {
        this.societyLayout = societyLayout;
    }

    public static Map loadMap(){
        Map map = null;
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Maps.ser"))){
            map = (Map) inputStream.readObject();
        }
        catch (FileNotFoundException e){
            System.out.println("Map File not Found, Creating a new Map");
            map = new Map(1, "Default Layout", new ArrayList<>());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return map;
    }

    public static void markPlot(int plotId, String status){
        Map map = loadMap();
        if(map ==null){
            System.out.println("Map not loaded.");
            return;
        }
        boolean found = false;
        for(Plot plot: map.getPlotMarkers()){
            if(plot.getPlotId()==plotId){
                plot.setStatus(status);
                found = true;
                break;
            }
        }
        if(found){
            saveMap(map);
            System.out.println("Plot ID " + plotId + " marked as " + status + ".");
        } else {
            System.out.println("Plot ID " + plotId + " not found.");
        }
    }

    public static Plot getPlotDetailsFromMap(String location){
        Map map = loadMap();
        if (map == null) {
            System.out.println("Map not loaded.");
            return null;
        }

        for (Plot plot : map.getPlotMarkers()) {
            if (plot.getLocation().equalsIgnoreCase(location)) {
                return plot;
            }
        }

        System.out.println("No plot found at location: " + location);
        return null;
    }
    private static void saveMap(Map map) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Map.ser"))) {
            outputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
