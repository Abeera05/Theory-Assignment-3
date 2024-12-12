package com.example.flightreservation;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightDetailsGUI extends Application {
    private static FlightManager flightManager;
    private Flight selectedFlight;
    private ObservableList<Flight> searchResults;

    public FlightDetailsGUI() {
        searchResults = FXCollections.observableArrayList();
    }

    public static void setFlightManager(FlightManager manager) {
        flightManager = manager;
    }

    @Override
    public void start(Stage primaryStage) {
        Label sourceLabel = new Label("Source:");
        TextField sourceField = new TextField();

        Label destinationLabel = new Label("Destination:");
        TextField destinationField = new TextField();

        Label depdateLabel = new Label("Select Departure Date:");
        DatePicker depDatePicker = new DatePicker();

        Label arrdateLabel = new Label("Select Arrival Date:");
        DatePicker arrDatePicker = new DatePicker();

        Button searchButton = new Button("Search Flights");
        TableView<Flight> flightTable = createFlightTable();

        Button nextButton = new Button("Next");
        nextButton.setDisable(true); // Initially disable the Next button

        searchButton.setOnAction(e -> {                                 //e actionevent object
            String source = sourceField.getText();
            String destination = destinationField.getText();
            LocalDate departureDate = depDatePicker.getValue();
            LocalDate arrivalDate = arrDatePicker.getValue();

            System.out.println("Searching for flights ");

            if (source.isBlank() || destination.isBlank() || departureDate == null || arrivalDate == null) {
                showErrorMessage("Please fill all fields.");
            } else {
                List<Flight> results = flightManager.searchFlights(source, destination, departureDate, arrivalDate);
                if (results.isEmpty()) {
                    showErrorMessage("No flights found for the given criteria.");
                } else {
                    searchResults.setAll(results);
                    nextButton.setDisable(false); // Enable Next button when flights are found
                }
            }
        });

        nextButton.setOnAction(e -> {
            if (selectedFlight != null) {
                PassengerGUI passengerGUI = new PassengerGUI(selectedFlight);
                passengerGUI.start(new Stage());
                primaryStage.close();
            } else {
                showErrorMessage("Please select a flight.");
            }
        });


        VBox layout = new VBox(10,
                sourceLabel, sourceField,
                destinationLabel, destinationField,
                depdateLabel, depDatePicker,
                arrdateLabel, arrDatePicker,
                searchButton, flightTable, nextButton);

        Scene scene = new Scene(layout, 800, 600); //window k pixel
        primaryStage.setTitle("Flight Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private TableView<Flight> createFlightTable() {
        TableView<Flight> table = new TableView<>();
        table.setItems(searchResults);

        TableColumn<Flight, String> flightIdColumn = new TableColumn<>("Flight ID"); //gets string data from  flight objects...row: flight
        flightIdColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getFlightId())); //

        TableColumn<Flight, String> sourceColumn = new TableColumn<>("Source");
        sourceColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getSource()));

        TableColumn<Flight, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDestination())); //row k andr flight ka object

        TableColumn<Flight, String> departureColumn = new TableColumn<>("Departure");
        departureColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDepartureTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        TableColumn<Flight, String> arrivalColumn = new TableColumn<>("Arrival");
        arrivalColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getArrivalTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        table.getColumns().addAll(flightIdColumn, sourceColumn, destinationColumn, departureColumn, arrivalColumn);

        table.setRowFactory(e -> { //cutomise row behavior
            TableRow<Flight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {  // event listener: Double-click row to select flight
                    selectedFlight = row.getItem();
                    System.out.println("Selected flight: " + selectedFlight.getFlightId());
                }
            });
            return row;
        });

        return table;
    }


    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
    }
    public static void main(String[] args) {
        FlightManager manager = new FlightManager();
        manager.loadFlightsFromFile("flight.txt");
        setFlightManager(manager);
        launch(args);
    }
}

