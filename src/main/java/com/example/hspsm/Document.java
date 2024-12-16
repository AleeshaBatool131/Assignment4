package com.example.hspsm;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable {
    public static String DocumentFileName = "Documents.ser";
    private int documentId;
    private int plotId;
    private int buyerId;
    private String documentType;// ownership, sales agreement
    private LocalDate uploadDate;

    public Document(int documentId, int plotId, int buyerId, String documentType, LocalDate uploadDate) {
        this.documentId = documentId;
        this.plotId = plotId;
        this.buyerId = buyerId;
        this.documentType = documentType;
        this.uploadDate = uploadDate;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getPlotId() {
        return plotId;
    }

    public void setPlotId(int plotId) {
        this.plotId = plotId;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void uploadDocument(){
        List<Document> documents =loadDocuments();
        documents.add(this);
        saveDocuments(documents);
    }

    public String viewDocument(int documentId) {
        List<Document> documents = loadDocuments();
        for (Document document : documents) {
            if (document.getDocumentId() == documentId) {
                return document.toString();
            }
        }
        return "Document Not Found";
    }
    public void deleteDocument(int documentId){
        List<Document> documents = loadDocuments();
        for (Document document : documents) {
            if (document.getDocumentId() == documentId) {
                documents.remove(document);
                break;
            }
        }
        saveDocuments(documents);
    }


    @Override
    public String toString() {
        return "Document Details"+
                "\nDocument Id: " + documentId +
                "\nBuyer Id: " + buyerId +
                "\nPlot Id:" + plotId +
                "\nDocument Type='" + documentType +
                "\nUpload Date=" + uploadDate +
                '}';
    }

   public List<Document> loadDocuments() {
        List<Document> documents = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Documents.ser"))) {
            documents = (List<Document>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Document file not found. Starting with an empty list.");
            documents = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return documents;
    }

    public void saveDocuments(List<Document> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Documents.ser"))) {
            outputStream.writeObject(plots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
