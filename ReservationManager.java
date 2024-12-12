package com.example.flightreservation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ReservationManager {
    private List<Reservation> reservations;

    public ReservationManager() {
        this.reservations = new ArrayList<>();
    }

    public void makeReservation(Flight flight, List<Seat> seats, List<Passenger> passengers) {

        Reservation reservation = new Reservation(flight, seats, passengers);
        reservations.add(reservation);
        System.out.println("Reservation successful");
        reservationdetails(flight, seats, passengers);
    }

    public void reservationdetails(Flight flight, List<Seat> selectedSeats, List<Passenger> passengers) {

        StringBuilder details = new StringBuilder();

        details.append("Flight ID: ").append(flight.getFlightId()).append("\n")
                .append("Source: ").append(flight.getSource()).append("\n")
                .append("Destination: ").append(flight.getDestination()).append("\n")
                .append("Departure: ").append(flight.getDepartureTime()).append("\n")
                .append("Arrival: ").append(flight.getArrivalTime()).append("\n\n");

        details.append("Passengers:\n");
        for (Passenger passenger : passengers) {
            details.append(passenger.getFirstName()).append("\n");
        }

        details.append("\nSelected Seats:\n");
        for (Seat seat : selectedSeats) {
            details.append("Seat ID: ").append(seat.getSeatID()).append(" - ")
                    .append(seat.getSeatClass()).append("\n");
        }
        details.append("\n--- End of Reservation ---\n\n");
        writeToFile(details.toString());
    }

    private void writeToFile(String details) {
        File file = new File("AllReservations.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(details);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }



    public List<Reservation> getReservations() {
        return reservations;
    }
}
