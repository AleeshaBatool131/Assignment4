package com.example.hspsm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.hspsm.Document.DocumentFileName;
import static com.example.hspsm.Payment.PaymentFileName;
import static com.example.hspsm.Plot.PlotFileName;

public class Buyer extends User implements Serializable {
    public static String BuyerFileName= "Buyers.ser";
    private static int buyerIdCounter = 0;
    private int buyerId;
    private String preferredLocation;
    private double preferredSize;
    private double budget;

    public Buyer(String username, String password, String role, String email, String phoneNumber, String preferredLocation, double preferredSize, double budget) {
        super(username, password, role, email, phoneNumber);
        this.buyerId = ++buyerIdCounter;
        this.preferredLocation = preferredLocation;
        this.preferredSize = preferredSize;
        this.budget = budget;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public double getPreferredSize() {
        return preferredSize;
    }

    public void setPreferredSize(double preferredSize) {
        this.preferredSize = preferredSize;
    }

    public List<Plot> viewAvailablePlots() {
        List<Plot> plots = loadPlots();
        List<Plot> availablePlots = new ArrayList<>();
        for (Plot plot : plots) {
            if (plot.getStatus().equals("Available"))
                availablePlots.add(plot);
        }
        return availablePlots;
    }

    public void requestPlot(int plotId) {
        List<Plot> plots = loadPlots();
        for (Plot plot : plots) {
            if (plot.getPlotId() == plotId) {
                plot.setStatus("Reserved");
                break;
            }
        }
        savePlots(plots);

    }

    public List<String> trackPaymentStatus() {
        List<Payment> payments = loadPayments();
        List<String> buyerPayment = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getBuyerId() == buyerId) {
                buyerPayment.add(payment.toString());
            }
        }
        return buyerPayment;
    }

    public List<String> getOwnershipDetails() {
        List<String> ownershipDetails = new ArrayList<>();
        List<Document> documents = loadDocuments();
        for (Document document : documents) {
            if (document.getBuyerId() == this.buyerId)
                ownershipDetails.add(document.toString());
        }
        return ownershipDetails;
    }

    private List<Plot> loadPlots() {
        List<Plot> plots = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PlotFileName))) {
            plots = (List<Plot>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Plots file not found. Starting with an empty list.");
            plots = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return plots;
    }

    private void savePlots(List<Plot> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PlotFileName))) {
            outputStream.writeObject(plots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Document> loadDocuments() {
        List<Document> documents = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DocumentFileName))) {
            documents = (List<Document>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Document file not found. Starting with an empty list.");
            documents = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public List<Payment> loadPayments() {
        List<Payment> payments = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PaymentFileName))) {
            payments = (List<Payment>) inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public List<Buyer> loadBuyers() {
        List<Buyer> buyers = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(BuyerFileName))) {
            buyers = (List<Buyer>) inputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return buyers;
    }

    public void saveBuyers(List<Buyer> buyers) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(BuyerFileName))) {
            outputStream.writeObject(buyers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
