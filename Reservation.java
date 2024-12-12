package com.example.flightreservation;

import java.util.List;

public class Reservation {
    private static int counter = 1;
    private int reservationNumber;
    private Flight flight;
    private List<Seat> seats;
    private List<Passenger> passengers;             // The selected flight

    // Constructor for creating a reservation
    public Reservation(Flight flight, List<Seat> seats, List<Passenger> passengers) {
        this.reservationNumber = counter++;
        this.flight = flight;
        this.passengers = passengers;
        this.seats = seats;
    }

    // Getters for the details
    public int getReservationNumber() {
        return reservationNumber;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Flight getFlight() {
        return flight;
    }

    // Optionally, you can implement toString() to easily print reservation details
    @Override
    public String toString() {
        return "Reservation Number: " + reservationNumber + "\n" +
                "Passengers: " + passengers + "\n" +
                "Seats: " + seats + "\n" +
                "Flight: " + flight;
    }
}

