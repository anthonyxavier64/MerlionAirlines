/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import entity.Employee;
import java.util.Scanner;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.SeatInventorySessionBeanRemote;
import entity.CabinClassConfiguration;
import entity.Flight;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Seat;
import entity.SeatInventory;
import exception.FlightDoesNotExistException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antho
 */
public class SalesManagerMenu {

    private Employee employee;
    private Scanner sc = new Scanner(System.in);

    public SalesManagerMenu() {
    }

    public SalesManagerMenu(Employee employee) {
        this.employee = employee;
    }

    public void run(FlightSessionBeanRemote flightSessionBeanRemote,
            SeatInventorySessionBeanRemote seatInventorySessionBeanRemote) {
        int response = 0;

        while (true) {
            System.out.println("*** Flight Reservation System (v1.0) ***\n");
            System.out.println("You are logged in as " + employee.getName() + " with "
                    + employee.getEmployeeType()
                    + " rights\n");
            System.out.println("1: View seats inventory");
            System.out.println("2: View flight reservations");
            System.out.println("3: Logout");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        viewSeatsInventory(flightSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error viewing seats inventory: " + ex.getMessage());
                    }
                } else if (response == 2) {
                    try {
                        viewFlightReservation(flightSessionBeanRemote, seatInventorySessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error viewing flight reservations: " + ex.getMessage());
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void viewSeatsInventory(FlightSessionBeanRemote flightSessionBeanRemote) throws FlightDoesNotExistException {
        System.out.println("*** View seats inventory ***");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine().trim();
        Flight flight;
        try {
            flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        } catch (FlightDoesNotExistException ex) {
            throw ex;
        }
        List<FlightSchedulePlan> flightSchedulePlans = flight.getFlightSchedulePlans();
        FlightSchedule flightSchedule = selectFlightSchedule(flightSchedulePlans);
        List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
        int totalAvailSeats = 0;
        int totalReservedSeats = 0;
        int totalBalanceSeats = 0;
        for (SeatInventory s : seatInventories) {
            CabinClassConfiguration cabinClassConfiguration = s.getCabinClassConfiguration();
            System.out.println("[" + cabinClassConfiguration.getCabinType() + "]");
            System.out.println("Available seats: " + s.getAvailableSeats());
            totalAvailSeats += s.getAvailableSeats();
            System.out.println("Reserved seats: " + s.getReservedSeats());
            totalReservedSeats += s.getReservedSeats();
            System.out.println("Balance seats: " + s.getBalanceSeats());
            totalBalanceSeats += s.getBalanceSeats();
            System.out.println();
        }
        System.out.println("Total available seats: " + totalAvailSeats + "; Total reserved seats: " + totalReservedSeats + "; Total balanced seats: "
                + totalBalanceSeats);
    }

    private void viewFlightReservation(FlightSessionBeanRemote flightSessionBeanRemote,
            SeatInventorySessionBeanRemote seatInventorySessionBeanRemote) throws FlightDoesNotExistException {
        System.out.println("*** View seats inventory ***");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine().trim();
        Flight flight;
        try {
            flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        } catch (FlightDoesNotExistException ex) {
            throw ex;
        }
        List<FlightSchedulePlan> flightSchedulePlans = flight.getFlightSchedulePlans();
        FlightSchedule flightSchedule = selectFlightSchedule(flightSchedulePlans);
        List<SeatInventory> seatInventories = flightSchedule.getSeatInventories();
        System.out.println("*** Reserved Seats ***");
        for (SeatInventory s : seatInventories) {
            viewReservedSeats(s, seatInventorySessionBeanRemote);
        }
    }

    private FlightSchedule selectFlightSchedule(List<FlightSchedulePlan> flightSchedulePlans) {
        System.out.println("*** Select flight schedule ***");
        sc.nextLine();
        List<FlightSchedule> flightSchedules = new ArrayList<>();
        for (FlightSchedulePlan f : flightSchedulePlans) {
            for (FlightSchedule fs : f.getFlightSchedules()) {
                flightSchedules.add(fs);
            }
        }
        for (int i = 1; i <= flightSchedules.size(); i++) {
            System.out.println(i + ": " + flightSchedules.get(i).getDepartureDateTime());
        }
        System.out.print("> ");
        int answer = sc.nextInt() - 1;
        return flightSchedules.get(answer);
    }

    private void viewReservedSeats(SeatInventory seatInventory, SeatInventorySessionBeanRemote seatInventorySessionBeanRemote) {
        CabinClassConfiguration cabinClassConfiguration = seatInventory.getCabinClassConfiguration();
        System.out.println("[" + cabinClassConfiguration.getCabinType() + "]");
        List<Seat> reservedSeats = seatInventorySessionBeanRemote.viewReservedSeats(seatInventory);
        for (Seat s : reservedSeats) {
            String result = "";
            result += "Seat Number: " + s.getSeatNumber() + "; ";
            result += "Passenger name: " + s.getPassenger().getFirstName() + " " + s.getPassenger().getLastName() + "; ";
            result += "Fare basis code: " + s.getFareBasisCode();
            System.out.println(result);
        }
        System.out.println();
    }
}
