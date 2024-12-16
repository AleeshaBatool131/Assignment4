package com.example.hspsm;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.example.hspsm.Payment.PaymentFileName;
import static com.example.hspsm.Plot.PlotFileName;

public class Admin extends User{
    private String adminId;
    private static int adminCount = 1;

    public Admin(String email, String phoneNumber) {
        super("Admin", "admin", "Admin", email, phoneNumber);
        this.adminId = String.format("admin%04d",adminCount++);
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public static void addPlot(Plot plot){
        List<Plot> plots = loadPlots();
        plots.add(plot);
        savePlots(plots);
        }

    public static void removePlot(int plotId){
        List<Plot> plots = loadPlots();
        plots.removeIf(plot -> plot.getPlotId()==plotId);
        savePlots(plots);
        System.out.println("Plot removed successfully");
    }

    public static String generateReports(){
        List<Plot> plots = loadPlots();
        List<Payment> payments = loadPayments();

        int totalPlots = plots.size();
        int soldPlots = 0;
        int availablePlots = 0;
        double totalRevenue = 0;
        double popularArea = 0;
        int maxCount = 0;
        List<Double> soldPlotAreas = new ArrayList<>();

        for (Plot plot : plots) {
            if ("Sold".equalsIgnoreCase(plot.getStatus())) {
                soldPlots++;
                soldPlotAreas.add(plot.getTotalArea());
            } else if ("Available".equalsIgnoreCase(plot.getStatus())) {
                availablePlots++;
            }
        }

        for (Payment payment : payments) {
            totalRevenue += payment.getAmountPaid();
        }

        for (double area : soldPlotAreas) {
            int count = Collections.frequency(soldPlotAreas, area);
            if (count > maxCount) {
                maxCount = count;
                popularArea = area;
            }
        }

        return String.format(
                "--- Report ---\n" +
                        "Total Plots: %d\n" +
                        "Sold Plots: %d\n" +
                        "Available Plots: %d\n" +
                        "Popular Plot Area: %.2f sq. meters\n" +
                        "Total Revenue: $%.2f\n",
                totalPlots, soldPlots, availablePlots, popularArea, totalRevenue
        );
    }
    public static String analyzePlotStatistics() {
        List<Plot> plots = loadPlots();
        int totalPlots = plots.size();
        int soldPlots = 0;
        int availablePlots = 0;
        List<Double> plotAreasSold = new ArrayList<>();
        for (Plot plot : plots) {
            if ("Sold".equals(plot.getStatus())) {
                soldPlots++;
                plotAreasSold.add(plot.getTotalArea());
            } else if ("Available".equals(plot.getStatus())) {
                availablePlots++;
            }
        }
        double popularArea = 0;
        int maxCount = 0;
        for (double area : plotAreasSold) {
            int count = Collections.frequency(plotAreasSold, area);
            if (count > maxCount) {
                maxCount = count;
                popularArea = area;
            }
        }
        return String.format("Plot Statistics:\nTotal Plots: %d\nSold Plots: %d\nAvailable Plots: %d\nPopular Plot Area: %.2f",
                totalPlots, soldPlots, availablePlots, popularArea);
    }
    public static void updatePlotDetails(Plot plot){
        plot.updatePlotDetails();
    }

//    public static void manageUsers(){
//        Scanner scanner = new Scanner(System.in);
//        List<User> users = loadUsers();
//
//        while (true) {
//            System.out.println("\n--- User Management Menu ---");
//            System.out.println("1. Add New User");
//            System.out.println("2. Delete User");
//            System.out.println("3. Update User");
//            System.out.println("4. View All Users");
//            System.out.println("5. Search User by ID");
//            System.out.println("6. Exit");
//            System.out.print("Enter your choice: ");
//            int choice = scanner.nextInt();
//
//            switch (choice) {
//                case 1 -> addUser(users, scanner);
//                case 2 -> deleteUser(users, scanner);
//                case 3 -> updateUser(users, scanner);
//                case 4 -> viewAllUsers(users);
//                case 5 -> searchUser(users, scanner);
//                case 6 -> {
//                    saveUsers(users);
//                    System.out.println("Exiting User Management.");
//                    return;
//                }
//                default -> System.out.println("Invalid choice! Please try again.");
//            }
//        }
//    }
//
//    private static void addUser(List<User> users, Scanner scanner) {
//        scanner.nextLine(); // Consume newline
//        System.out.print("Enter Username: ");
//        String username = scanner.nextLine();
//        System.out.print("Enter Password: ");
//        String password = scanner.nextLine();
//        System.out.print("Enter Role (Admin/Buyer): ");
//        String role = scanner.nextLine();
//        System.out.print("Enter Email: ");
//        String email = scanner.nextLine();
//        System.out.print("Enter Phone Number: ");
//        String phoneNumber = scanner.nextLine();
//
//        users.add(new User(username, password, role, email, phoneNumber));
//        System.out.println("User added successfully!");
//    }
//
//    private static void deleteUser(List<User> users, Scanner scanner) {
//        System.out.print("Enter User ID to delete: ");
//        String userId = scanner.nextLine();
//
//        boolean found = users.removeIf(user -> user.getUserId().equals(userId));
//        if (found) {
//            System.out.println("User deleted successfully!");
//        } else {
//            System.out.println("User not found!");
//        }
//    }
//
//    private static void updateUser(List<User> users, Scanner scanner) {
//        System.out.print("Enter User ID to update: ");
//        String userId = scanner.nextLine();
//        scanner.nextLine(); // Consume newline
//
//        for (User user : users) {
//            if (user.getUserId().equals(userId)) {
//                System.out.print("Enter new Email (leave blank to keep unchanged): ");
//                String email = scanner.nextLine();
//                if (!email.isBlank()) user.setEmail(email);
//
//                System.out.print("Enter new Phone Number (leave blank to keep unchanged): ");
//                String phoneNumber = scanner.nextLine();
//                if (!phoneNumber.isBlank()) user.setPhoneNumber(phoneNumber);
//
//                System.out.print("Enter new Role (leave blank to keep unchanged): ");
//                String role = scanner.nextLine();
//                if (!role.isBlank()) user.setRole(role);
//
//                System.out.println("User updated successfully!");
//                return;
//            }
//        }
//        System.out.println("User not found!");
//    }
//
//    private static void viewAllUsers(List<User> users) {
//        if (users.isEmpty()) {
//            System.out.println("No users to display.");
//            return;
//        }
//        System.out.println("\n--- All Users ---");
//        for (User user : users) {
//            System.out.println("ID: " + user.getUserId() +
//                    "\nUsername: " + user.getUsername() +
//                    "\nRole: " + user.getRole() +
//                    "\nEmail: " + user.getEmail() +
//                    "\nPhone: " + user.getPhoneNumber() +
//                    "\nRegistered On: " + user.getRegistrationDate());
//        }
//    }
//
//    private static void searchUser(List<User> users, Scanner scanner) {
//        System.out.print("Enter User ID to search: ");
//        String userId = scanner.nextLine();
//
//        for (User user : users) {
//            if (user.getUserId().equals(userId)) {
//                System.out.println("\nUser Details:");
//                System.out.println("ID: " + user.getUserId());
//                System.out.println("Username: " + user.getUsername());
//                System.out.println("Role: " + user.getRole());
//                System.out.println("Email: " + user.getEmail());
//                System.out.println("Phone: " + user.getPhoneNumber());
//                System.out.println("Registered On: " + user.getRegistrationDate());
//                return;
//            }
//        }
//        System.out.println("User not found!");
//    }

//    public static List<User> loadUsers() {
//        List<User> users = null;
//        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(UserFileName))) {
//            users = (List<User>) inputStream.readObject();
//        } catch (FileNotFoundException e) {
//            System.out.println("Users file not found. Starting with an empty list.");
//            users = new ArrayList<>();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return users;
//    }
//
//    public static void saveUsers(List<User> users) {
//        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(UserFileName))) {
//            outputStream.writeObject(users);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static List<Payment> loadPayments(){
        List<Payment> payments = null;
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PaymentFileName))){
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
    public static List<Plot> loadPlots() {
        List<Plot> plots = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PlotFileName))) {
            plots = (List<Plot>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty list.");
            plots = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return plots;
    }

    public static void savePlots(List<Plot> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PlotFileName))) {
            outputStream.writeObject(plots);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

