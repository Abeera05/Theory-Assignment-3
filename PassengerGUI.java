package com.example.flightreservation;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class PassengerGUI extends Application {

    private int numberOfPassengers;
    private List<Passenger> passengers;
    private Flight selectedFlight;
    private GridPane grid;

    public PassengerGUI(Flight selectedFlight) {
        this.selectedFlight = selectedFlight;
        this.passengers = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        Label passengerCountLabel = new Label("Select number of passengers:");
        ComboBox<Integer> passengerComboBox = new ComboBox<>();

        passengerComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        passengerComboBox.setValue(1);

        grid.add(passengerCountLabel, 0, 0); //row and colum index
        grid.add(passengerComboBox, 1, 0);

        Button showFormButton = new Button("Show Form");
        showFormButton.setOnAction(e -> {
            numberOfPassengers = passengerComboBox.getValue();
            displayPassengerForm(primaryStage);
        });

        grid.add(showFormButton, 1, 1);

        Scene scene = new Scene(grid, 600, 500);
        primaryStage.setTitle("Passenger Details");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayPassengerForm(Stage primaryStage) {
        grid.getChildren().clear();
        List<TextField[]> fieldsList = new ArrayList<>(); //overall object

        for (int i = 0; i < numberOfPassengers; i++) {
            int colOffset = i * 2; //controls column position side by side display hojaye

            Label label = new Label("Details of person " + (i + 1));
            grid.add(label, colOffset, 0); //row 0

            TextField nameField = new TextField();
            TextField emailField = new TextField();
            TextField phoneField = new TextField();
            TextField passportField = new TextField();
            TextField nationalityField = new TextField();
            TextField addressField = new TextField();

            fieldsList.add(new TextField[]{nameField, emailField, phoneField, passportField, nationalityField, addressField});

            // Add labels and input fields to the grid
            grid.add(new Label("Name:"), colOffset, 1);
            grid.add(nameField, colOffset + 1, 1);
            grid.add(new Label("Email:"), colOffset, 2);
            grid.add(emailField, colOffset + 1, 2);
            grid.add(new Label("Phone:"), colOffset, 3);
            grid.add(phoneField, colOffset + 1, 3);
            grid.add(new Label("Passport Number:"), colOffset, 4);
            grid.add(passportField, colOffset + 1, 4);
            grid.add(new Label("Nationality:"), colOffset, 5);
            grid.add(nationalityField, colOffset + 1, 5);
            grid.add(new Label("Address:"), colOffset, 6);
            grid.add(addressField, colOffset + 1, 6);
        }

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {   //event handler
            if (areAllFieldsFilled(fieldsList)) {
                passengers.clear();
                for (TextField[] fields : fieldsList) {
                    Passenger passenger = new Passenger(
                            fields[0].getText(),
                            fields[1].getText(),
                            fields[2].getText(),
                            fields[3].getText(),
                            fields[4].getText(),
                            fields[5].getText()
                    );
                    passengers.add(passenger);
                }

                SeatMapGUI seatMapGUI = new SeatMapGUI(selectedFlight, passengers);
                primaryStage.close();
                seatMapGUI.start(new Stage());

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all fields before proceeding.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        grid.add(nextButton, 1, numberOfPassengers * 7 + 1); // Add the Next button below the form
    }

    private boolean areAllFieldsFilled(List<TextField[]> fieldsList) {
        for (TextField[] fields : fieldsList) {
            for (TextField field : fields) {
                if (field.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

}
