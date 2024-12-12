package com.example.flightreservation;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class SeatMapGUI extends Application {
    private static final int ROWS = 18;
    private static final int COLS = 6;

    private int maxSeats;
    private final Seat[][] seats = new Seat[ROWS][COLS];
    private SeatSelectionHandler seatSelectionHandler = new SeatSelectionHandler();
    private Flight selectedFlight;
    private List<Passenger> passengers;

    private GridPane seatGrid;
    private Text selectionStatus; //jo seat select krenge
    private Text totalPrice;

    public SeatMapGUI(Flight selectedFlight, List<Passenger> passengers) {
        this.selectedFlight = selectedFlight;
        this.passengers = passengers;
        this.maxSeats = passengers.size();
    }

    @Override
    public void start(Stage primaryStage) {
        seatGrid = createSeatGrid();
        VBox bookingSection = createBookingSection(primaryStage);
        VBox seatInfoMap = seatMap();

        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(seatGrid, bookingSection, seatInfoMap);

        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setTitle("Seat Selection and Price Calculation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createSeatGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10); //10 pixel gap between columns
        grid.setVgap(10);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String seatID = (row + 1) + "" + (char) ('A' + col); //concatenation
                SeatClass seatClass;

                if (row < 3) {
                    seatClass = SeatClass.BUSINESS;
                } else if (row < 8) {
                    seatClass = SeatClass.PREMIUM_ECONOMY;
                } else {
                    seatClass = SeatClass.ECONOMY;
                }

                Seat seat = new Seat(seatID, seatClass);
                seats[row][col] = seat;

                Button seatButton = createSeatButton(seat);
                if (col == 2) {
                    GridPane.setMargin(seatButton, new Insets(0, 20, 0, 0));
                }
                grid.add(seatButton, col, row);
            }
        }

        return grid;
    }

    private Button createSeatButton(Seat seat) {
        Button seatButton = new Button(seat.getSeatID());
        updateSeatButtonStyle(seat, seatButton);

        seatButton.setOnAction(e -> {
            if (seat.isBooked()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This seat is already booked.", ButtonType.OK);
                alert.showAndWait();
            } else {
                if (!seat.isSelected() && seatSelectionHandler.getSelectedSeats().size() >= maxSeats) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "You can select only " + maxSeats + " seats.", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    seatSelectionHandler.seatSelection(seat);
                    updateSeatButtonStyle(seat, seatButton);
                    updatestatus();
                }
            }
        });

        return seatButton;
    }

    private VBox createBookingSection(Stage primaryStage) {
        VBox bookingSection = new VBox(20);
        bookingSection.setPadding(new Insets(20));

        selectionStatus = new Text("Select your seats");
        totalPrice = new Text("Total Price: $0.0");
        Button bookButton = new Button("Complete Reservation");
        bookButton.setOnAction(e -> {
            if (!seatSelectionHandler.getSelectedSeats().isEmpty()) {
                ReservationGUI reservationGUI = new ReservationGUI(selectedFlight, passengers, seatSelectionHandler.getSelectedSeats());
                Stage reservationStage = new Stage();
                reservationGUI.start(reservationStage);
                primaryStage.close();

                updateSeatButtons();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select at least one seat.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        bookingSection.getChildren().addAll(selectionStatus, totalPrice, bookButton);

        return bookingSection;
    }
    private void updatestatus() {
        StringBuilder status= new StringBuilder("Selected Seats: ");
        for (Seat selectedSeat : seatSelectionHandler.getSelectedSeats()) {
            status.append(selectedSeat.getSeatID()).append(" ");
        }
        selectionStatus.setText(status.toString().trim());
        totalPrice.setText("Total Price: $" + calculateTotalPrice());
    }


    private void updateSeatButtonStyle(Seat seat, Button seatButton) {
        if (seat.isBooked()) {
            seatButton.setStyle("-fx-background-color: gray; -fx-font-size: 14px;");
        } else if (seat.isSelected()) {
            seatButton.setStyle("-fx-background-color: pink; -fx-font-size: 14px;");
        } else {
            switch (seat.getSeatClass()) {
                case BUSINESS:
                    seatButton.setStyle("-fx-background-color: gold; -fx-font-size: 14px;");
                    break;
                case PREMIUM_ECONOMY:
                    seatButton.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px;");
                    break;
                case ECONOMY:
                    seatButton.setStyle("-fx-background-color: lightgreen; -fx-font-size: 14px;");
                    break;
            }
        }
    }

    private void updateSeatButtons() {
        seatGrid.getChildren().forEach(e -> {
            if (e instanceof Button seatButton) {
                String seatId = seatButton.getText();
                Seat seat = findSeatById(seatId);
                if (seat != null) {
                    updateSeatButtonStyle(seat, seatButton);
                }
            }
        });
    }

    private Seat findSeatById(String seatId) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (seats[row][col].getSeatID().equals(seatId)) {
                    return seats[row][col];
                }
            }
        }
        return null;
    }

    private double calculateTotalPrice() {
        double businessPrice = 1000.0;
        double premiumEconomyPrice = 500.0;
        double economyPrice = 350.0;
        double total = 0.0;

        for (Seat seat : seatSelectionHandler.getSelectedSeats()) {
            switch (seat.getSeatClass()) {
                case BUSINESS:
                    total += businessPrice;
                    break;
                case PREMIUM_ECONOMY:
                    total += premiumEconomyPrice;
                    break;
                case ECONOMY:
                    total += economyPrice;
                    break;
            }
        }

        return total;
    }

    private VBox seatMap() {
        VBox infoMap = new VBox(20);
        infoMap.setPadding(new Insets(20));

        Text infoTitle = new Text("Seat Class Information");
        infoMap.getChildren().add(infoTitle);

        infoMap.getChildren().add(createInfoBox("Business Class", "gold"));
        infoMap.getChildren().add(createInfoBox("Premium Economy Class", "lightblue"));
        infoMap.getChildren().add(createInfoBox("Economy Class", "lightgreen"));

        return infoMap;
    }

    private HBox createInfoBox(String label, String color) {
        HBox infoBox = new HBox(10);
        infoBox.setPadding(new Insets(5));

        Button colorBox = new Button();
        colorBox.setStyle("-fx-background-color:  " + color + "; -fx-min-width: 30px; -fx-min-height: 30px;");

        Text classLabel = new Text(label);
        infoBox.getChildren().addAll(colorBox, classLabel);

        return infoBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
