package com.example.hspsm;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Notification implements Serializable {
    private int notificationId;
    private int userId;
    private String message;
    private LocalDate notificationDate;
    private String status;// read, unread

    public Notification(int notificationId, int userId, String message, String status) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.notificationDate = LocalDate.now();
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDate notificationDate) {
        this.notificationDate = notificationDate;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static void sendNotification(int userId, String message){
        List<Notification> notifications = loadNotifications();
        int newNotificationId = notifications.isEmpty() ? 1 : notifications.get(notifications.size() - 1).getNotificationId() + 1;

        Notification newNotification = new Notification(newNotificationId, userId, message, "unread");
        notifications.add(newNotification);
        saveNotifications(notifications);

        System.out.println("Notification sent successfully to User ID: " + userId);
    }

    public static List<Notification> getNotifications(int userId){
        List<Notification> notifications = loadNotifications();
        List<Notification> userNotifications = new ArrayList<>();

        for (Notification notification : notifications) {
            if (notification.getUserId() == userId) {
                userNotifications.add(notification);
            }
        }

        return userNotifications;
    }

    public static void markAsRead(int notificationId){
        List<Notification> notifications = loadNotifications();
        boolean found = false;

        for (Notification notification : notifications) {
            if (notification.getNotificationId() == notificationId) {
                notification.setStatus("read");
                found = true;
                break;
            }
        }

        if (found) {
            saveNotifications(notifications);
            System.out.println("Notification ID " + notificationId + " marked as read.");
        } else {
            System.out.println("Notification ID " + notificationId + " not found.");
        }
    }
    private static List<Notification> loadNotifications() {
        List<Notification> notifications = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Notifications.ser"))) {
            notifications = (List<Notification>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Notifications file not found. Starting with an empty list.");
            notifications = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    // Helper method to save notifications to the file
    private static void saveNotifications(List<Notification> notifications) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Notifications.ser"))) {
            outputStream.writeObject(notifications);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
