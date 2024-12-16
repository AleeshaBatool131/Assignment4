package com.example.hspsm;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

import java.io.*;

import static com.example.hspsm.Admin.analyzePlotStatistics;
import static com.example.hspsm.Admin.generateReports;

public class HelloApplication extends Application{
    public static int userCount = 1;
    @Override
    public void start(Stage stage) throws IOException {
        welcomeScreen(stage);
        stage.show();
    }

    public  void welcomeScreen(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(20));
        vBox.setBackground(background());

        Text welcome = new Text("Welcome to Housing Society Plot Management System");
        welcome.setTextAlignment(TextAlignment.CENTER);
        welcome.setFill(Color.BLACK);
        welcome.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-font-family: Times New Roman;");

        Label loadingLabel = new Label("Loading....");
        loadingLabel.setTextFill(Color.BLACK);
        loadingLabel.setFont(new Font("Times New Roman", 36));

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(800);
        progressBar.setStyle("-fx-background-color: transparent; -fx-accent: blue;");

        vBox.getChildren().addAll(welcome, loadingLabel, progressBar);

        Task<Void> loadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for(int i=0;i<=100;i++){
                    Thread.sleep(10);
                    updateProgress(i, 100);
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(loadingTask.progressProperty());
        loadingTask.setOnSucceeded(e-> loginScreen(stage));

        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Welcome Screen");

        new Thread(loadingTask).start();
    }
    public void loginScreen(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        vBox.setBackground(background());
        Label loginLabel = new Label ("Login");
        loginLabel.setFont(new Font("Times New Roman", 60));
        loginLabel.setTextFill(Color.BLACK);
        loginLabel.setStyle("-fx-font-weight: bold;");
        loginLabel.setAlignment(Pos.CENTER);
        loginLabel.setPadding(new Insets(10, 20, 50, 150));

        Label signupLabel = new Label("New Buyer? Register Here");
        signupLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-family: Arial; -fx-font-weight: bold;");
        signupLabel.setOnMouseClicked(e->registerUser(stage));
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        usernameLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-family: Arial; -fx-font-weight: bold;");
        passwordLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-family: Arial; -fx-font-weight: bold;");


        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        usernameField.setPrefWidth(500);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        passwordField.setPrefWidth(500);

        usernameField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                passwordField.requestFocus();
        });

        Button login = new Button("Login");
        Button admin = new Button("Login as Admin");

        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 12px 30px; -fx-font-size: 16px; -fx-font-family: Arial; -fx-border-radius: 5px;";

        login.setStyle(buttonStyle);
        admin.setStyle(buttonStyle);
        signupLabel.setOnMouseEntered(e->{
            signupLabel.setStyle("-fx-text-fill: blue; -fx-font-size: 18px; -fx-font-family: Arial; -fx-font-weight: bold;");
        });
        signupLabel.setOnMouseExited(e->signupLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-family: Arial; -fx-font-weight: bold;"));
        login.setOnMouseEntered(e -> login.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 12px 30px; -fx-font-size: 16px; -fx-font-family: Arial; -fx-border-radius: 5px;"));
        login.setOnMouseExited(e -> login.setStyle(buttonStyle));

        admin.setOnMouseEntered(e -> admin.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 12px 30px; -fx-font-size: 16px; -fx-font-family: Arial; -fx-border-radius: 5px;"));
        admin.setOnMouseExited(e -> admin.setStyle(buttonStyle));

        Text invalidMessage = new Text();
        invalidMessage.setTextAlignment(TextAlignment.CENTER);
        invalidMessage.setFill(Color.RED);
        invalidMessage.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        List<User> users = loadUsers();
        login.setOnAction(e -> {
            boolean isValidUser = users.stream().anyMatch(user ->
                    user.getUsername().equals(usernameField.getText()) &&
                            user.getPassword().equals(passwordField.getText()));

            if (isValidUser) {
                buyerDashboard(stage);
            } else {
                invalidMessage.setText("Invalid Username or Password");
            }
        });

        admin.setOnAction(e -> adminLoginScreen(stage));

        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(30);
        inputGrid.add(loginLabel, 0, 0);
        inputGrid.add(usernameLabel, 0, 1);
        inputGrid.add(usernameField, 0, 2);
        inputGrid.add(passwordLabel, 0, 3);
        inputGrid.add(passwordField, 0, 4);


        vBox.getChildren().addAll(inputGrid, invalidMessage, login, admin, signupLabel);

        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }
    public  void adminDashboardScene(Stage stage) {

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setPadding(new Insets(20));
        vBox.setBackground(background());

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setFont(new Font("Times New Roman", 60));
        titleLabel.setPadding(new Insets(20, 30,30, 30));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

        GridPane functionsGridPane = new GridPane();

        Button manageUsersButton = new Button();
        Button managePlotsButton = new Button();
        Button managePaymentsButton = new Button();
        Button generateReportsButton = new Button();

        VBox userBox = new VBox();
        VBox plotsBox = new VBox();
        VBox reportBox = new VBox();
        VBox paymentBox = new VBox();

        userBox.setSpacing(10);
        userBox.setAlignment(Pos.CENTER);
        Image userLogo = new Image(this.getClass().getResource("/usersLogo.png").toExternalForm());
        ImageView userImage = new ImageView(userLogo);
        Label userText = new Label("Manage Users");
        userText.setFont(new Font("Times New Roman", 18));
        userBox.getChildren().addAll(userImage, userText);
        manageUsersButton.setPrefSize(500, 300);
        manageUsersButton.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        manageUsersButton.setGraphic(userBox);

        plotsBox.setSpacing(10);
        plotsBox.setAlignment(Pos.CENTER);
        Image plotLogo = new Image(this.getClass().getResource("/plotsLogo.png").toExternalForm());
        ImageView plotImage = new ImageView(plotLogo);
        Label plotText = new Label("Manage Plots");
        plotText.setFont(new Font("Times New Roman", 18));
        plotsBox.getChildren().addAll(plotImage, plotText);
        managePlotsButton.setPrefSize(500, 300);
        managePlotsButton.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        managePlotsButton.setGraphic(plotsBox);

        paymentBox.setSpacing(10);
        paymentBox.setAlignment(Pos.CENTER);
        Image paymentLogo = new Image(this.getClass().getResource("/paymentsLogo.png").toExternalForm());
        ImageView paymentImage = new ImageView(paymentLogo);
        Label paymentText = new Label("Manage Payments");
        paymentText.setFont(new Font("Times New Roman", 18));
        paymentBox.getChildren().addAll(paymentImage, paymentText);
        managePaymentsButton.setPrefSize(500, 300);
        managePaymentsButton.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        managePaymentsButton.setGraphic(paymentBox);

        reportBox.setSpacing(10);
        reportBox.setAlignment(Pos.CENTER);
        Image reportLogo = new Image(this.getClass().getResource("/reportLogo.png").toExternalForm());
        ImageView reportImage = new ImageView(reportLogo);
        Label reportText = new Label("Generate Reports");
        reportText.setFont(new Font("Times New Roman", 18));
        reportBox.getChildren().addAll(reportImage, reportText);
        generateReportsButton.setPrefSize(500, 300);
        generateReportsButton.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        generateReportsButton.setGraphic(reportBox);

        functionsGridPane.setHgap(20);
        functionsGridPane.setVgap(20);
        functionsGridPane.setAlignment(Pos.CENTER);

        functionsGridPane.add(manageUsersButton, 0,0);
        functionsGridPane.add(managePlotsButton,1,0);
        functionsGridPane.add(managePaymentsButton, 0, 1);
        functionsGridPane.add(generateReportsButton,1, 1);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle( "-fx-background-color: red ;-fx-text-fill: white;-fx-font-size: 14; -fx-padding: 10 20; -fx-background-radius: 5;");
        logoutButton.setPrefWidth(300);

        vBox.getChildren().addAll(
                titleLabel,
                functionsGridPane,
                logoutButton
        );


        manageUsersButton.setOnAction(e -> UserManagementScene(stage));
        managePlotsButton.setOnAction(e -> managePlotsScene(stage));
        managePaymentsButton.setOnAction(e -> managePaymentsScene(stage));
        generateReportsButton.setOnAction(e -> generateReportScene(stage));

        logoutButton.setOnAction(e -> loginScreen(stage));


        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
    }


    public void UserManagementScene(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label titleLabel = new Label("User Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vBox.getChildren().add(titleLabel);


        TableView<User> userTable = new TableView<>();
        userTable.setItems(loadUsers());

        TableColumn<User, String> idColumn = new TableColumn<>("User ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        userTable.getColumns().addAll(idColumn, usernameColumn, roleColumn, emailColumn, phoneColumn);


        Button addButton = new Button("Add User");
        Button editButton = new Button("Edit User");
        Button deleteButton = new Button("Delete User");


        addButton.setOnAction(e -> {
            Stage addStage = new Stage();
            VBox addVBox = new VBox(10);
            addVBox.setAlignment(Pos.CENTER);

            TextField usernameField = new TextField();
            usernameField.setPromptText("Username");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");

            TextField roleField = new TextField();
            roleField.setPromptText("Role (Admin/Buyer)");

            TextField emailField = new TextField();
            emailField.setPromptText("Email");

            TextField phoneField = new TextField();
            phoneField.setPromptText("Phone Number");

            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
                ObservableList<User> users = userTable.getItems();
                users.add(new User(usernameField.getText(), passwordField.getText(), roleField.getText(), emailField.getText(), phoneField.getText()));
                saveUsers(users);
                addStage.close();
            });

            addVBox.getChildren().addAll(usernameField, passwordField, roleField, emailField, phoneField, saveButton);
            addStage.setScene(new Scene(addVBox, 1300, 800));
            addStage.setTitle("Add User");
            addStage.show();
        });


        editButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                Stage editStage = new Stage();
                VBox editVBox = new VBox(10);
                editVBox.setAlignment(Pos.CENTER);

                TextField emailField = new TextField(selectedUser.getEmail());
                emailField.setPromptText("Email");

                TextField phoneField = new TextField(selectedUser.getPhoneNumber());
                phoneField.setPromptText("Phone Number");

                TextField roleField = new TextField(selectedUser.getRole());
                roleField.setPromptText("Role (Admin/Buyer)");

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(event -> {
                    selectedUser.setEmail(emailField.getText());
                    selectedUser.setPhoneNumber(phoneField.getText());
                    selectedUser.setRole(roleField.getText());
                    userTable.refresh();
                    saveUsers(userTable.getItems());
                    editStage.close();
                });

                editVBox.getChildren().addAll(emailField, phoneField, roleField, saveButton);
                editStage.setScene(new Scene(editVBox, 1300, 800));
                editStage.setTitle("Edit User");
                editStage.show();
            }
        });


        deleteButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                userTable.getItems().remove(selectedUser);
                saveUsers(userTable.getItems());
            }
        });


        Button backButton = new Button("Back");
        backButton.setOnAction(e -> adminDashboardScene(stage));


        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(userTable, buttonBox, backButton);

        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("User Management");
    }

    public void managePlotsScene(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        Label titleLabel = new Label("Plot Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vBox.getChildren().add(titleLabel);


        TableView<Plot> plotTable = new TableView<>();
        plotTable.setItems(loadPlots());

        TableColumn<Plot, Integer> idColumn = new TableColumn<>("Plot ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Plot, String> numberColumn = new TableColumn<>("Plot Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("plotNumber"));

        TableColumn<Plot, Double> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<Plot, Double> widthColumn = new TableColumn<>("Width");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Plot, Double> areaColumn = new TableColumn<>("Total Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("totalArea"));

        TableColumn<Plot, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Plot, String> gpsColumn = new TableColumn<>("GPS Coordinates");
        gpsColumn.setCellValueFactory(new PropertyValueFactory<>("gpsCoordinates"));

        TableColumn<Plot, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Plot, Double> priceUnitColumn = new TableColumn<>("Price Per Unit");
        priceUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));

        TableColumn<Plot, Double> priceColumn = new TableColumn<>("Total Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Plot, String> developmentColumn = new TableColumn<>("Development Status");
        developmentColumn.setCellValueFactory(new PropertyValueFactory<>("developmentStatus"));

        plotTable.getColumns().addAll(idColumn, numberColumn, lengthColumn, widthColumn, areaColumn, locationColumn, gpsColumn, statusColumn, priceUnitColumn, priceColumn, developmentColumn);


        Button addButton = new Button("Add Plot");
        Button editButton = new Button("Edit Plot");
        Button deleteButton = new Button("Delete Plot");


        addButton.setOnAction(e -> {
            Stage addStage = new Stage();
            VBox addVBox = new VBox(10);
            addVBox.setAlignment(Pos.CENTER);

            TextField numberField = new TextField();
            numberField.setPromptText("Plot Number");

            TextField lengthField = new TextField();
            lengthField.setPromptText("Length");

            TextField widthField = new TextField();
            widthField.setPromptText("Width");

            TextField locationField = new TextField();
            locationField.setPromptText("Location");

            TextField gpsField = new TextField();
            gpsField.setPromptText("GPS Coordinates");

            TextField statusField = new TextField();
            statusField.setPromptText("Status");

            TextField priceUnitField = new TextField();
            priceUnitField.setPromptText("Price Per Unit");

            TextField developmentField = new TextField();
            developmentField.setPromptText("Development Status");

            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
                try {
                    int newId = plotTable.getItems().size() + 1;
                    double length = Double.parseDouble(lengthField.getText());
                    double width = Double.parseDouble(widthField.getText());
                    double pricePerUnit = Double.parseDouble(priceUnitField.getText());

                    Plot newPlot = new Plot(newId, numberField.getText(), length, width, locationField.getText(), gpsField.getText(), statusField.getText(), pricePerUnit, developmentField.getText());
                    ObservableList<Plot> plots = plotTable.getItems();
                    plots.add(newPlot);
                    savePlots(plots);
                    addStage.close();
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input values.");
                }
            });

            addVBox.getChildren().addAll(numberField, lengthField, widthField, locationField, gpsField, statusField, priceUnitField, developmentField, saveButton);
            addStage.setScene(new Scene(addVBox, 1300, 800));
            addStage.setTitle("Add Plot");
            addStage.show();
        });


        editButton.setOnAction(e -> {
            Plot selectedPlot = plotTable.getSelectionModel().getSelectedItem();
            if (selectedPlot != null) {
                Stage editStage = new Stage();
                VBox editVBox = new VBox(10);
                editVBox.setAlignment(Pos.CENTER);

                TextField numberField = new TextField(selectedPlot.getPlotNumber());
                numberField.setPromptText("Plot Number");

                TextField lengthField = new TextField(String.valueOf(selectedPlot.getLength()));
                lengthField.setPromptText("Length");

                TextField widthField = new TextField(String.valueOf(selectedPlot.getWidth()));
                widthField.setPromptText("Width");

                TextField locationField = new TextField(selectedPlot.getLocation());
                locationField.setPromptText("Location");

                TextField gpsField = new TextField(selectedPlot.getGpsCoordinates());
                gpsField.setPromptText("GPS Coordinates");

                TextField statusField = new TextField(selectedPlot.getStatus());
                statusField.setPromptText("Status");

                TextField priceUnitField = new TextField(String.valueOf(selectedPlot.getPricePerUnit()));
                priceUnitField.setPromptText("Price Per Unit");

                TextField developmentField = new TextField(selectedPlot.getDevelopmentStatus());
                developmentField.setPromptText("Development Status");

                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(event -> {
                    try {
                        selectedPlot.setPlotNumber(numberField.getText());
                        selectedPlot.setLength(Double.parseDouble(lengthField.getText()));
                        selectedPlot.setWidth(Double.parseDouble(widthField.getText()));
                        selectedPlot.setLocation(locationField.getText());
                        selectedPlot.setGpsCoordinates(gpsField.getText());
                        selectedPlot.setStatus(statusField.getText());
                        selectedPlot.setPricePerUnit(Double.parseDouble(priceUnitField.getText()));
                        selectedPlot.setTotalArea(selectedPlot.getLength() * selectedPlot.getWidth());
                        selectedPlot.setTotalPrice(selectedPlot.getTotalArea() * selectedPlot.getPricePerUnit());
                        selectedPlot.setDevelopmentStatus(developmentField.getText());

                        plotTable.refresh();
                        savePlots(plotTable.getItems());
                        editStage.close();
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid input values.");
                    }
                });

                editVBox.getChildren().addAll(numberField, lengthField, widthField, locationField, gpsField, statusField, priceUnitField, developmentField, saveButton);
                editStage.setScene(new Scene(editVBox, 1300, 800));
                editStage.setTitle("Edit Plot");
                editStage.show();
            }
        });


        deleteButton.setOnAction(e -> {
            Plot selectedPlot = plotTable.getSelectionModel().getSelectedItem();
            if (selectedPlot != null) {
                plotTable.getItems().remove(selectedPlot);
                savePlots(plotTable.getItems());
            }
        });


        Button backButton = new Button("Back");
        backButton.setOnAction(e -> adminDashboardScene(stage));


        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(plotTable, buttonBox, backButton);

        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Plot Management");
    }

    public void managePaymentsScene(Stage stage){
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> adminDashboardScene(stage));

        ListView<Payment> paymentListView = new ListView<>();
        paymentListView.getItems().setAll(loadPayments());

        Button addPaymentButton = new Button("Add Payment");
        addPaymentButton.setOnAction(e -> {

            System.out.println("Adding a new payment...");
        });


        Button removePaymentButton = new Button("Remove Payment");
        removePaymentButton.setOnAction(e -> {
            Payment selectedPayment = paymentListView.getSelectionModel().getSelectedItem();
            if (selectedPayment != null) {
                ObservableList<Payment> payments = loadPayments();
                payments.remove(selectedPayment);
                savePayments(payments);
                paymentListView.getItems().setAll(loadPayments());
                System.out.println("Payment removed: " + selectedPayment);
            } else {
                System.out.println("Please select a payment to remove.");
            }
        });


        Button updatePaymentButton = new Button("Update Payment");
        updatePaymentButton.setOnAction(e -> {
            Payment selectedPayment = paymentListView.getSelectionModel().getSelectedItem();
            if (selectedPayment != null) {

                System.out.println("Updating payment: " + selectedPayment);
            } else {
                System.out.println("Please select a payment to update.");
            }
        });


        layout.getChildren().addAll(paymentListView, addPaymentButton, removePaymentButton, updatePaymentButton,backButton);


        Scene scene = new Scene(layout, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Manage Payments");
        stage.show();
    }
    public static void generateReportScene(Stage stage){
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));


        String report = generateReports();
        String plotStatistics = analyzePlotStatistics();


        final TextArea reportTextArea = new TextArea(report + "\n\n" + plotStatistics);
        reportTextArea.setEditable(false);
        reportTextArea.setWrapText(true);


        Button printButton = new Button("Print Report");
        reportTextArea.setEditable(false);


        reportTextArea.setText(report);

        printButton.setOnAction(e -> {

            String contentToPrint = reportTextArea.getText();
            if (!contentToPrint.isEmpty()) {
                print(contentToPrint);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No report to print!");
                alert.show();
            }
        });


        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            stage.close();
        });


        layout.getChildren().addAll(reportTextArea, printButton, closeButton);


        Scene scene = new Scene(layout, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Generate Report");
        stage.show();
    }
    private static void print(String content) {

        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if (printerJob == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No printers found. Please check your printer setup.");
            alert.show();
            return;
        }


        Text printableContent = new Text(content);
        printableContent.setWrappingWidth(500);


        boolean proceed = printerJob.showPrintDialog(null);

        if (proceed) {

            boolean success = printerJob.printPage(printableContent);

            if (success) {
                printerJob.endJob();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Printing complete.");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to print.");
                alert.show();
            }
        } else {
            // User cancelled the print dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Printing cancelled.");
            alert.show();
        }
    }
    public void buyerDashboard(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setPadding(new Insets(20));
        vBox.setBackground(background());

        Label titleLabel = new Label("Buyer Dashboard");
        titleLabel.setFont(new Font("Times New Roman", 55));
        titleLabel.setPadding(new Insets(20, 30,30, 30));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

        GridPane functionsGridPane = new GridPane();
        functionsGridPane.setVgap(15);
        functionsGridPane.setHgap(15);
        functionsGridPane.setAlignment(Pos.CENTER);

        VBox viewPlotsBox = new VBox();
        VBox requestPlotBox = new VBox();
        VBox ownershipDetailsBox = new VBox();
        VBox paymentBox = new VBox();
        VBox updateBox = new VBox();
        VBox mapBox = new VBox();

        // Create buttons
        Button viewPlots = new Button();
        Button requestPlot = new Button();
        Button ownershipDetails = new Button();
        Button trackPaymentStatus = new Button();
        Button updatePreference = new Button();
        Button viewMap = new Button(); // New Map button

        viewPlotsBox.setSpacing(10);
        viewPlotsBox.setAlignment(Pos.CENTER);
        Image viewPlotsLogo = new Image(this.getClass().getResource("/plotsLogo.png").toExternalForm());
        ImageView viewPlotsImage = new ImageView(viewPlotsLogo);
        Label userText = new Label("View Available Plots");
        userText.setFont(new Font("Times New Roman", 18));
        viewPlotsBox.getChildren().addAll(viewPlotsImage, userText);
        viewPlots.setPrefSize(500, 300);
        viewPlots.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        viewPlots.setGraphic(viewPlotsBox);

        requestPlotBox.setSpacing(10);
        requestPlotBox.setAlignment(Pos.CENTER);
        Image requestPlotLogo = new Image(this.getClass().getResource("/requestplotLogo.png").toExternalForm());
        ImageView requestPlotImage = new ImageView(requestPlotLogo);
        Label requestPlotText = new Label("Request Plot");
        requestPlotText.setFont(new Font("Times New Roman", 18));
        requestPlotBox.getChildren().addAll(requestPlotImage, requestPlotText);
        requestPlot.setPrefSize(500, 300);
        requestPlot.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        requestPlot.setGraphic(requestPlotBox);

        ownershipDetailsBox.setSpacing(10);
        ownershipDetailsBox.setAlignment(Pos.CENTER);
        Image ownershipDetailsLogo = new Image(this.getClass().getResource("/ownershipDetailLogo.png").toExternalForm());
        ImageView ownershipDetailsImage = new ImageView(ownershipDetailsLogo);
        Label ownershipDetailsText = new Label("Ownership Details");
        ownershipDetailsText.setFont(new Font("Times New Roman", 18));
        ownershipDetailsBox.getChildren().addAll(ownershipDetailsImage, ownershipDetailsText);
        ownershipDetails.setPrefSize(500, 300);
        ownershipDetails.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        ownershipDetails.setGraphic(ownershipDetailsBox);

        paymentBox.setSpacing(10);
        paymentBox.setAlignment(Pos.CENTER);
        Image paymentLogo = new Image(this.getClass().getResource("/paymentsLogo.png").toExternalForm());
        ImageView paymentImage = new ImageView(paymentLogo);
        Label paymentText = new Label("Track Payment Status");
        paymentText.setFont(new Font("Times New Roman", 18));
        paymentBox.getChildren().addAll(paymentImage, paymentText);
        trackPaymentStatus.setPrefSize(500, 300);
        trackPaymentStatus.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        trackPaymentStatus.setGraphic(paymentBox);

        updateBox.setSpacing(10);
        updateBox.setAlignment(Pos.CENTER);
        Image updateLogo = new Image(this.getClass().getResource("/updateLogo.png").toExternalForm());
        ImageView updateImage = new ImageView(updateLogo);
        Label updateText = new Label("Update Preferences");
        updateText.setFont(new Font("Times New Roman", 18));
        updateBox.getChildren().addAll(updateImage, updateText);
        updatePreference.setPrefSize(500, 300);
        updatePreference.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        updatePreference.setGraphic(updateBox);

        mapBox.setSpacing(10);
        mapBox.setAlignment(Pos.CENTER);
        Image mapLogo = new Image(this.getClass().getResource("/viewMapLogo.png").toExternalForm());
        ImageView mapImage = new ImageView(mapLogo);
        Label mapText = new Label("View Map");
        mapText.setFont(new Font("Times New Roman", 18));
        mapBox.getChildren().addAll(mapImage, mapText);
        viewMap.setPrefSize(500, 300);
        viewMap.setStyle("-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 2;");
        viewMap.setGraphic(mapBox);

        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 14;");
        logout.setPrefSize(150, 80);
//         Set button actions
        viewPlots.setOnAction(e -> viewPlots(stage));
        requestPlot.setOnAction(e -> requestPlot(stage));
        ownershipDetails.setOnAction(e -> ownershipDetails(stage));
        trackPaymentStatus.setOnAction(e -> trackPaymentStatus(stage));
        updatePreference.setOnAction(e -> updatePreference(stage));

        viewMap.setOnAction(e -> {
            stage.setScene(ViewMap.getMainScene(stage));
        });

        logout.setOnAction(e -> {
            loginScreen(stage);
        });

        functionsGridPane.add(viewPlots, 0,0);
        functionsGridPane.add(requestPlot,0,1);
        functionsGridPane.add(ownershipDetails,0,2);
        functionsGridPane.add(trackPaymentStatus,1,0);
        functionsGridPane.add(updatePreference,1,1);
        functionsGridPane.add(viewMap, 1, 2);
        // Add buttons to VBox
        vBox.getChildren().addAll(titleLabel,
                functionsGridPane,
                logout
        );

        // Create scene and set on stage
        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Buyer Dashboard");
    }

    public void adminLoginScreen(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        vBox.setBackground(background());
        Label loginLabel = new Label ("Login");
        loginLabel.setFont(new Font("Times New Roman", 60));
        loginLabel.setTextFill(Color.BLACK);
        loginLabel.setStyle("-fx-font-weight: bold;");
        loginLabel.setAlignment(Pos.CENTER);
        loginLabel.setPadding(new Insets(10, 20, 50, 150));

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        usernameLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-family: Arial; -fx-font-weight: bold;");
        passwordLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-family: Arial; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter admin username");
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        usernameField.setPrefWidth(500);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter admin password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        passwordField.setPrefWidth(500);

        usernameField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                passwordField.requestFocus();
        });

        Button login = new Button("Login");

        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 12px 30px; -fx-font-size: 16px; -fx-font-family: Arial; -fx-border-radius: 5px;";

        login.setStyle(buttonStyle);
        login.setOnMouseEntered(e -> login.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 12px 30px; -fx-font-size: 16px; -fx-font-family: Arial; -fx-border-radius: 5px;"));
        login.setOnMouseExited(e -> login.setStyle(buttonStyle));

        Text invalidMessage = new Text();
        invalidMessage.setTextAlignment(TextAlignment.CENTER);
        invalidMessage.setFill(Color.RED);
        invalidMessage.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        login.setOnAction(e -> {
            boolean isAdmin = "Admin".equals(usernameField.getText()) && "admin".equals(passwordField.getText());
            if (isAdmin) {
                adminDashboardScene(stage);
            } else {
                invalidMessage.setText("Invalid Username or Password");
            }
        });

        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(30);
        inputGrid.add(loginLabel, 0, 0);
        inputGrid.add(usernameLabel, 0, 1);
        inputGrid.add(usernameField, 0, 2);
        inputGrid.add(passwordLabel, 0, 3);
        inputGrid.add(passwordField, 0, 4);


        vBox.getChildren().addAll(inputGrid, invalidMessage, login);

        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Admin Login");
    }
    public void registerUser(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.setPadding(new Insets(10));
        vBox.setBackground(background());

        Label signupLabel = new Label("Sign Up");
        signupLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 60));
        signupLabel.setTextFill(Color.BLACK);
        signupLabel.setAlignment(Pos.CENTER);
        signupLabel.setPadding(new Insets(10, 20, 50, 30));

        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(15);
        inputGrid.setPadding(new Insets(20, 30, 20, 20));
        Label username = new Label("Username:");
        Label password = new Label("Password:");
        Label email = new Label("Email:");
        Label phoneNumber = new Label("Phone Number:");
        Label preferredLocation = new Label("Preferred Location:");
        Label preferredSize = new Label("Preferred Size:");
        Label budget = new Label("Budget:");

        username.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");
        password.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");
        email.setStyle("-fx-text-fill:black; -fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");
        phoneNumber.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");
        preferredLocation.setStyle("-fx-text-fill:black; -fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");
        preferredSize.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");
        budget.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        usernameField.setPrefWidth(800);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        passwordField.setPrefWidth(800);
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        emailField.setPrefWidth(800);
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Enter your phone number");
        phoneNumberField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        phoneNumberField.setPrefWidth(800);
        TextField preferredLocationField = new TextField();
        preferredLocationField.setPromptText("Enter your Preferred Plot Location");
        preferredLocationField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        preferredLocationField.setPrefWidth(800);
        TextField preferredSizeField = new TextField();
        preferredSizeField.setPromptText("Enter your Preferred Plot Size");
        preferredSizeField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        preferredSizeField.setPrefWidth(800);
        TextField budgetField = new TextField();
        budgetField.setPromptText("Enter your Budget");
        budgetField.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-background-color: #fff; -fx-border-color: black; -fx-border-radius: 5px;");
        budgetField.setPrefWidth(800);

        Button register = new Button("Register");
        register.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: black; -fx-padding: 10 20; -fx-font-size: 16px; -fx-font-family: Arial;");

        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: black; -fx-padding: 10 20; -fx-font-size: 16px; -fx-font-family: Arial;");

        HBox buttonBox = new HBox(cancel, register);

        inputGrid.add(username, 0, 0);
        inputGrid.add(usernameField, 1,0);
        inputGrid.add(password, 0, 1);
        inputGrid.add(passwordField,1,1);
        inputGrid.add(email,0, 2);
        inputGrid.add(emailField, 1, 2);
        inputGrid.add(phoneNumber,0,3);
        inputGrid.add(phoneNumberField,1,3);
        inputGrid.add(preferredLocation,0, 4);
        inputGrid.add(preferredLocationField,1, 4);
        inputGrid.add(preferredSize,0,5);
        inputGrid.add(preferredSizeField,1,5);
        inputGrid.add(budget,0,6);
        inputGrid.add(budgetField,1,6);

        usernameField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                passwordField.requestFocus();
        });
        passwordField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                emailField.requestFocus();
        });
        emailField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                phoneNumberField.requestFocus();
        });
        phoneNumberField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                preferredLocationField.requestFocus();
        });
        usernameField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                passwordField.requestFocus();
        });
        preferredLocationField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                preferredSizeField.requestFocus();
        });
        preferredSizeField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER"))
                budgetField.requestFocus();
        });

        buttonBox.setSpacing(15);
        buttonBox.setAlignment(Pos.CENTER);
        register.setOnAction(e -> {
            List<User> users = loadUsers();
            List<Buyer> buyers = loadBuyers();
            Buyer buyer = new Buyer(usernameField.getText(), passwordField.getText(), "Buyer", emailField.getText(), phoneNumberField.getText(), preferredLocationField.getText(), Double.parseDouble(preferredSizeField.getText()), Double.parseDouble(budgetField.getText()));
            buyers.add(buyer);
            users.add(buyer);
            saveBuyers(FXCollections.observableArrayList(buyers));
            saveUsers(FXCollections.observableArrayList(users));
            buyerDashboard(stage);
        });

        cancel.setOnAction(e->{
            loginScreen(stage);
        });

        vBox.getChildren().addAll(signupLabel,inputGrid, buttonBox);

        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Register User");
    }

    private void viewPlots(Stage stage){
        VBox vBox = new VBox();
        Button exit = new Button("Exit");
        TableView plotTable = new TableView<>();
        ObservableList<Plot> plots = loadPlots();

        // Define columns
        TableColumn<Plot, Integer> plotIdColumn = new TableColumn<>("Plot ID");
        plotIdColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Plot, String> plotNumberColumn = new TableColumn<>("Plot Number");
        plotNumberColumn.setCellValueFactory(new PropertyValueFactory<>("plotNumber"));

        TableColumn<Plot, Double> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<Plot, Double> widthColumn = new TableColumn<>("Width");
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

        TableColumn<Plot, Double> totalAreaColumn = new TableColumn<>("Total Area");
        totalAreaColumn.setCellValueFactory(new PropertyValueFactory<>("totalArea"));

        TableColumn<Plot, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Plot, String> gpsCoordinatesColumn = new TableColumn<>("GPS Coordinates");
        gpsCoordinatesColumn.setCellValueFactory(new PropertyValueFactory<>("gpsCoordinates"));

        TableColumn<Plot, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Plot, Double> pricePerUnitColumn = new TableColumn<>("Price per Unit");
        pricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerUnit"));

        TableColumn<Plot, Double> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Plot, String> developmentStatusColumn = new TableColumn<>("Development Status");
        developmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("developmentStatus"));

        // Add columns to TableView
        plotTable.getColumns().addAll(
                plotIdColumn, plotNumberColumn, lengthColumn, widthColumn, totalAreaColumn,
                locationColumn, gpsCoordinatesColumn, statusColumn, pricePerUnitColumn,
                totalPriceColumn, developmentStatusColumn
        );

        // Add sample data
        ObservableList<Plot> plotData = FXCollections.observableArrayList(
                new Plot(1, "P001", 50.0, 30.0, "Sector A", "27.2046,77.4977", "Available", 2000.0,  "Developed"),
                new Plot(2, "P002", 60.0, 40.0, "Sector B", "28.7041,77.1025", "Available", 2200.0,  "Under Development")
        );
        for(Plot plot: plotData){
            if(!(plot.getStatus().equals("Available")))
                plotData.remove(plot);
        }
        plotTable.setItems(plotData);
        exit.setOnAction(e->{
            buyerDashboard(stage);
        });
        vBox.getChildren().addAll(plotTable,exit);
        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Login Screen");
    }
    public void requestPlot(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Request a Plot");
        TextField plotIdField = new TextField();
        plotIdField.setPromptText("Enter Plot ID");
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> {
            // Placeholder for request plot logic
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Plot request submitted!");
            alert.showAndWait();
            ObservableList<Plot> plots = loadPlots();
            for (Plot plot : plots) {
                if (plot.getPlotId()==Integer.parseInt(plotIdField.getText())) {
                    plot.setStatus("Reserved");
                    break;
                }
            }
            savePlots(plots);
        });

        backButton.setOnAction(e -> {
            buyerDashboard(stage);
        });

        vBox.getChildren().addAll(label, plotIdField, submitButton, backButton);
        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Request Plot");
    }

    public void ownershipDetails(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Ownership Details");
        TableView<Document> tableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Document, Integer> documentIdColumn = new TableColumn<>("Document ID");
        documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("documentId"));

        TableColumn<Document, Integer> buyerIdColumn = new TableColumn<>("Buyer ID");
        buyerIdColumn.setCellValueFactory(new PropertyValueFactory<>("buyerId"));

        TableColumn<Document, Integer> plotIdColumn = new TableColumn<>("Plot ID");
        plotIdColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Document, String> documentTypeColumn = new TableColumn<>("Document Type");
        documentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("documentType"));

        TableColumn<Document, LocalDate> uploadDateColumn = new TableColumn<>("Upload Date");
        uploadDateColumn.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));

        // Add columns to the TableView
        tableView.getColumns().addAll(documentIdColumn, buyerIdColumn, plotIdColumn, documentTypeColumn, uploadDateColumn);

        // Load ownership documents and set them in the TableView
        List<Document> documents = loadDocuments();
        ObservableList<Document> ownershipDocs = FXCollections.observableArrayList();

        for (Document doc : documents) {
            if ("Ownership".equalsIgnoreCase(doc.getDocumentType())) {
                ownershipDocs.add(doc);
            }
        }

        tableView.setItems(ownershipDocs);

        // Back button to return to buyerDashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> buyerDashboard(stage));

        vBox.getChildren().addAll(label, tableView, backButton);
        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Ownership Details");
    }

    public void trackPaymentStatus(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Track Payment Status");

        // Input fields for plot ID (to track payments)
        TextField plotIdField = new TextField();
        plotIdField.setPromptText("Enter Plot ID");

        Button searchButton = new Button("Search Payments");

        // TableView for displaying payment history
        TableView<Payment> tableView = new TableView<>();

        // Define columns for the TableView
        TableColumn<Payment, Integer> paymentIdColumn = new TableColumn<>("Payment ID");
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));

        TableColumn<Payment, Integer> buyerIdColumn = new TableColumn<>("Buyer ID");
        buyerIdColumn.setCellValueFactory(new PropertyValueFactory<>("buyerId"));

        TableColumn<Payment, Integer> plotIdColumn = new TableColumn<>("Plot ID");
        plotIdColumn.setCellValueFactory(new PropertyValueFactory<>("plotId"));

        TableColumn<Payment, Double> amountPaidColumn = new TableColumn<>("Amount Paid");
        amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));

        TableColumn<Payment, Double> outstandingBalanceColumn = new TableColumn<>("Outstanding Balance");
        outstandingBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("outstandingBalance"));

        TableColumn<Payment, String> paymentMethodColumn = new TableColumn<>("Payment Method");
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Payment, LocalDate> paymentDateColumn = new TableColumn<>("Payment Date");
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        // Add columns to the TableView
        tableView.getColumns().addAll(paymentIdColumn, buyerIdColumn, plotIdColumn, amountPaidColumn, outstandingBalanceColumn, paymentMethodColumn, paymentDateColumn);

        // Action for search button
        searchButton.setOnAction(e -> {
            String plotIdText = plotIdField.getText();
            if (plotIdText.isEmpty()) {
                showAlert("Error", "Please enter a Plot ID.");
                return;
            }

            try {
                int plotId = Integer.parseInt(plotIdText);

                // Fetch payment history for the entered plot ID
                List<Payment> paymentList = new ArrayList<>();
                List<Payment> payments = loadPayments();
                for(Payment payment: payments){
                    if(payment.getPlotId()==plotId){
                        paymentList.add(payment);
                    }
                }

                if (paymentList.isEmpty()) {
                    showAlert("No Payments Found", "No payment records found for the given Plot ID.");
                } else {
                    ObservableList<Payment> paymentData = FXCollections.observableArrayList(paymentList);
                    tableView.setItems(paymentData);
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid Plot ID.");
            }
        });

        // Back button to return to buyerDashboard
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> buyerDashboard(stage));

        vBox.getChildren().addAll(label, plotIdField, searchButton, tableView, backButton);
        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Track Payment Status");
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updatePreference(Stage stage) {
        VBox vBox = new VBox();
        Label label = new Label("Update Preferences");

        // Fields to input preferred location, size, and budget
        TextField preferredLocationField = new TextField();
        preferredLocationField.setPromptText("Enter preferred location");

        TextField preferredSizeField = new TextField();
        preferredSizeField.setPromptText("Enter preferred size (in square meters)");

        TextField budgetField = new TextField();
        budgetField.setPromptText("Enter budget");

        Button updateButton = new Button("Update");
        Button backButton = new Button("Back");

        updateButton.setOnAction(e -> {
            // Retrieve the input values
            String preferredLocation = preferredLocationField.getText();
            String preferredSizeText = preferredSizeField.getText();
            String budgetText = budgetField.getText();

            // Validate the inputs
            if (preferredLocation.isEmpty() || preferredSizeText.isEmpty() || budgetText.isEmpty()) {
                showAlert("Error", "Please fill all the fields.");
                return;
            }

            double preferredSize = 0;
            double budget = 0;

            try {
                preferredSize = Double.parseDouble(preferredSizeText);
                budget = Double.parseDouble(budgetText);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid numbers for size and budget.");
                return;
            }

            // Placeholder: Logic for updating preferences (e.g., storing them in a database)
            // For now, just show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Preferences updated successfully!");
            alert.showAndWait();

            // After updating, go back to the buyer dashboard
            buyerDashboard(stage);
        });

        backButton.setOnAction(e -> {
            buyerDashboard(stage);
        });

        vBox.getChildren().addAll(label, preferredLocationField, preferredSizeField, budgetField, updateButton, backButton);
        Scene scene = new Scene(vBox, 1300, 800);
        stage.setScene(scene);
        stage.setTitle("Update Preferences");
    }
    // Load Users as ObservableList
    public static ObservableList<User> loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Users.ser"))) {
            List<User> userList = (List<User>) inputStream.readObject();
            users.addAll(userList); // Add all items to ObservableList
            userCount = users.size() + 1; // Assuming `userCount` is declared elsewhere.
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Save Users
    public static void saveUsers(ObservableList<User> users) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Users.ser"))) {
            outputStream.writeObject(new ArrayList<>(users)); // Convert ObservableList to ArrayList
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Buyers as ObservableList
    public static ObservableList<Buyer> loadBuyers() {
        ObservableList<Buyer> buyers = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Buyers.ser"))) {
            List<Buyer> buyerList = (List<Buyer>) inputStream.readObject();
            buyers.addAll(buyerList);
        } catch (FileNotFoundException e) {
            System.out.println("Buyers file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return buyers;
    }

    // Save Buyers
    public static void saveBuyers(ObservableList<Buyer> buyers) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Buyers.ser"))) {
            outputStream.writeObject(new ArrayList<>(buyers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Plots as ObservableList
    public static ObservableList<Plot> loadPlots() {
        ObservableList<Plot> plots = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Plots.ser"))) {
            List<Plot> plotList = (List<Plot>) inputStream.readObject();
            plots.addAll(plotList);
        } catch (FileNotFoundException e) {
            System.out.println("Plots file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return plots;
    }

    // Save Plots
    public static void savePlots(ObservableList<Plot> plots) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Plots.ser"))) {
            outputStream.writeObject(new ArrayList<>(plots));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Payments as ObservableList
    public static ObservableList<Payment> loadPayments() {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Payments.ser"))) {
            List<Payment> paymentList = (List<Payment>) inputStream.readObject();
            payments.addAll(paymentList);
        } catch (FileNotFoundException e) {
            System.out.println("Payments file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Save Payments
    public static void savePayments(ObservableList<Payment> payments) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Payments.ser"))) {
            outputStream.writeObject(new ArrayList<>(payments));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load Documents as ObservableList
    public static ObservableList<Document> loadDocuments() {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Documents.ser"))) {
            List<Document> documentList = (List<Document>) inputStream.readObject();
            documents.addAll(documentList);
        } catch (FileNotFoundException e) {
            System.out.println("Documents file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return documents;
    }

    // Save Documents
    public static void saveDocuments(ObservableList<Document> documents) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Documents.ser"))) {
            outputStream.writeObject(new ArrayList<>(documents));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Background background(){
        Image image = new Image(this.getClass().getResource("/LandscapeBackground.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(100, 100, true, true, true, false));
        return new Background(backgroundImage);
    }

}