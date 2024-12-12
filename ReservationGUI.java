package com.example.flightreservation;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ReservationGUI extends Application {
    private Flight flight;
    private List<Passenger> passengers;
    private List<Seat> selectedSeats;

    public ReservationGUI(Flight flight, List<Passenger> passengers, List<Seat> selectedSeats) {
        this.flight = flight;
        this.passengers = passengers;
        this.selectedSeats = selectedSeats;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        String flightDetails = "Flight ID: " + flight.getFlightId() +
                "\nSource: " + flight.getSource() +
                "\nDestination: " + flight.getDestination() +
                "\nDeparture: " + flight.getDepartureTime() +
                "\nArrival: " + flight.getArrivalTime();

        layout.getChildren().add(new javafx.scene.text.Text("Flight Details:\n" + flightDetails));

        StringBuilder passengerDetails = new StringBuilder("Passengers:\n");
        for (Passenger passenger : passengers) {
            passengerDetails.append(passenger.getFirstName()).append("\n");
        }
        layout.getChildren().add(new javafx.scene.text.Text(passengerDetails.toString()));

        StringBuilder seatDetails = new StringBuilder("Selected Seats:\n");
        for (Seat seat : selectedSeats) {
            seatDetails.append(seat.getSeatID()).append(" - ").append(seat.getSeatClass()).append("\n");
        }
        layout.getChildren().add(new javafx.scene.text.Text(seatDetails.toString()));

        Button completeReservationButton = new Button("Complete Reservation");
        completeReservationButton.setOnAction(e -> makeReservation());

        layout.getChildren().add(completeReservationButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setTitle("Reservation Details");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void makeReservation() {
        try {
            ReservationManager reservationManager = new ReservationManager();
            reservationManager.makeReservation(flight, selectedSeats, passengers);
            showConfirmationAlert();
        } catch (IllegalArgumentException e) {
            showErrorMessage("Failed to make reservation: " + e.getMessage());
        }
    }

    private void showConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Success");
        alert.setHeaderText(null);
        alert.setContentText("Reservation has been made successfully.");
        alert.showAndWait();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
