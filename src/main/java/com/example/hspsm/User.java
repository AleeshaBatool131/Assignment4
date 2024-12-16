package com.example.hspsm;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User implements Serializable {
    public static String UserFileName = "Users.ser";
    public static int userCount = 1;
    private String userId;
    private String username;
    private String password;
    private String role;//(admin, buyer, sales, visitor)
    private String email;
    private String phoneNumber;
    private LocalDate registrationDate;

    public User(String username, String password, String role, String email, String phoneNumber) {
        this.userId= String.format("%05d",userCount++);
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = LocalDate.now();
    }
    public User(){

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void registerUser(){
        List<User> users = loadUsers();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Role (Admin/Buyer): ");
        String role = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
        for(User user: users) {
            if (user.getUsername().equals(this.username)) {
                System.out.println("Username already exists. Registration failed.");
                return;
            }
        }
        User user = new User(username,password,role,email,phoneNumber);
        users.add(user);
        saveUsers(users);
    }

    public boolean loginUser(){
        boolean found = false;
        List<User> users = loadUsers();
        for(User user: users){
            if(username.equals(user.username)&& password.equals(user.password)){
               found = true;
               break;
            }
        }
        return found;
    }

    public void updateProfile(){
        List<User> users = loadUsers();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Role (Admin/Buyer): ");
        String role = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();
            for(User user: users){
                if(user.getUserId().equals(this.userId)){
                    user.setEmail(email);
                    user.setPhoneNumber(phoneNumber);
                    user.setPassword(password);
                    user.setUsername(username);
                    user.setRole(role);
                    break;
                }
            }
            saveUsers(users);
    }

    public void viewProfile(){
        List<User> users = loadUsers();
        boolean found = false;
            for(User user: users){
                if(user.getUserId().equals(this.userId)){
                    System.out.println("User Profile");
                    System.out.println("UserName: "+user.getUsername());
                    System.out.println("Email: "+user.getEmail());
                    System.out.println("Phone Number: "+user.getPhoneNumber());
                    System.out.println("Role: "+user.getRole());
                    System.out.println("Registration Date: "+user.getRegistrationDate());
                    found = true;
                    break;
                }
            }
             if(!found)
                System.out.println("User not Found");
    }
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(UserFileName))) {
            users = (ArrayList<User>) inputStream.readObject();
            userCount=users.size()+1;
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty list.");
            users = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            users = new ArrayList<>();
        }
        return users;
    }

    public void saveUsers(List<User> users) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(UserFileName))) {
            outputStream.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
