package com.example.flightreservation;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;



public class FlightManager {
    private List<Flight> flights;

    public FlightManager() {
        flights = new ArrayList<>();
    }

    public void loadFlightsFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] parts = line.split(",");
                    Flight flight = new Flight();
                    flight.setFlightId(parts[0]);
                    flight.setSource(parts[1]);
                    flight.setDestination(parts[2]);

                    flight.setDepartureTime(LocalDate.parse(parts[3], dateFormatter));
                    flight.setArrivalTime(LocalDate.parse(parts[4], dateFormatter));

                    flight.setTotalSeats(Integer.parseInt(parts[5]));
                    flight.setAvailableSeats(Integer.parseInt(parts[6]));
                    flight.setPrice(Double.parseDouble(parts[7]));
                    flight.setStatus(parts[8]);

                    flights.add(flight);
                    System.out.println("Loaded flight: " + flight.getFlightId());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }
    }


    public List<Flight> searchFlights(String source, String destination, LocalDate departureDate, LocalDate arrivalDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String formattedDepartureDate = departureDate.format(formatter);
        String formattedArrivalDate = arrivalDate.format(formatter);

        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            String flightDepartureDate = flight.getDepartureTime().format(formatter);
            String flightArrivalDate = flight.getArrivalTime().format(formatter);

            if (flight.getSource().equalsIgnoreCase(source)
                    && flight.getDestination().equalsIgnoreCase(destination)
                    && flightDepartureDate.equals(formattedDepartureDate)
                    && flightArrivalDate.equals(formattedArrivalDate)) {
                result.add(flight);
            }
        }

        return result;
    }


}
