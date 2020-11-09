/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import entity.Flight;
import entity.FlightRoute;
import exception.FlightAlreadyExistException;
import exception.FlightDoesNotExistException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antho
 */
public class ScheduleManagerMenu {

    private Employee employee;
    private Scanner sc = new Scanner(System.in);

    public ScheduleManagerMenu() {
    }

    public ScheduleManagerMenu(Employee employee) {
        this.employee = employee;
    }

    public void run(FlightSessionBeanRemote flightSessionBeanRemote,
            FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote) {
        int response = 0;

        while (true) {
            System.out.println("*** Flight Reservation System (v1.0) ***\n");
            System.out.println("You are logged in as " + employee.getName() + " with "
                    + employee.getEmployeeType()
                    + " rights\n");
            System.out.println("1: Create flight");
            System.out.println("2: View all flights");
            System.out.println("3: View flight details");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        createFlight(flightSessionBeanRemote, flightRouteSessionBeanRemote,
                                aircraftConfigurationSessionBeanRemote);
                    } catch (FlightAlreadyExistException ex) {
                        System.out.println("Error creating flight: " + ex.getMessage());
                    }
                } else if (response == 2) {
                    viewAllFlights(flightSessionBeanRemote);
                } else if (response == 3) {
                    try {
                        viewFlightDetails(flightSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error viewing flight details: " + ex.getMessage());
                    }
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void createFlight(FlightSessionBeanRemote flightSessionBeanRemote,
            FlightRouteSessionBeanRemote flightRouteSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote) throws FlightAlreadyExistException {
        String result = "";
        System.out.println("*** Create flight ***\n");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine();
        System.out.println();
        System.out.println("*** Select a aircraft configuration ***");
        List<AircraftConfiguration> aircraftConfigurations = aircraftConfigurationSessionBeanRemote.viewAllAircraftConfigurations();
        for (int i = 0; i < aircraftConfigurations.size(); i++) {
            AircraftConfiguration aircraftConfiguration = aircraftConfigurations.get(i);
            System.out.println((i + 1) + ": " + aircraftConfiguration.getName());
        }
        System.out.print("> ");
        int answer = sc.nextInt();
        AircraftConfiguration selectedAircraftConfiguration = aircraftConfigurations.get(answer - 1);
        System.out.println("*** Select a flight route ***");
        List<FlightRoute> flightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        for (int i = 0; i < flightRoutes.size(); i++) {
            FlightRoute flightRoute = flightRoutes.get(i);
            System.out.println((i + 1) + ": " + flightRoute.getOrigin().getIATACode() + "-" + flightRoute.getDestination().getIATACode());
        }
        System.out.print("> ");
        answer = sc.nextInt();
        FlightRoute selectedFlightRoute = flightRoutes.get(answer - 1);
        Long flightId;
        try {
            flightId = flightSessionBeanRemote.createFlight(flightNumber, selectedFlightRoute.getFlightRouteId(),
                    selectedAircraftConfiguration.getAircraftConfigurationID());
        } catch (FlightAlreadyExistException ex) {
            throw ex;
        }
        result += "Flight number ML" + flightNumber + " with ID " + flightId + " successfully created!\n";
        if (selectedFlightRoute.getComplementaryFlightRoute() != null) {
            System.out.println("Selected flight route has a complementary flight route!");
            sc.nextLine();
            System.out.print("Create complementary return flight? Y/N> ");
            String ans = sc.nextLine().toLowerCase();
            if (ans.equals("y")) {
                System.out.print("Enter flight number> ");
                flightNumber = sc.nextLine();
                FlightRoute complementaryFlightRoute = selectedFlightRoute.getComplementaryFlightRoute();
                Long complementartyFlightId = flightSessionBeanRemote.createComplementaryFlight(flightNumber, complementaryFlightRoute.getFlightRouteId(),
                        selectedAircraftConfiguration.getAircraftConfigurationID(), flightId);
                result += "Complementary flight number ML" + flightNumber + " with ID " + complementartyFlightId + " successfully created!\n";
            }
        }
        System.out.print(result);
    }

    private void viewAllFlights(FlightSessionBeanRemote flightSessionBeanRemote) {
        System.out.println("*** View all flights ***\n");
        sc.nextLine();
        List<Flight> flights = flightSessionBeanRemote.viewAllFlights();
        for (int i = 0; i < flights.size(); i++) {
            System.out.println((i + 1) + ": " + flights.get(i).getFlightNumber());
        }
    }

    private void viewFlightDetails(FlightSessionBeanRemote flightSessionBeanRemote) throws FlightDoesNotExistException {
        System.out.println("*** View flight details ***\n");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine();
        try {
            Flight flight = flightSessionBeanRemote.viewFlightDetails(flightNumber);
            System.out.println("Flight origin airport: " + flight.getFlightRoute().getOrigin().getIATACode());
            System.out.println("Flight destiantion airport: " + flight.getFlightRoute().getDestination().getIATACode());
            System.out.println("** Available cabin classes **");
            List<CabinClassConfiguration> cabinClasses = flight.getAircraftConfiguration().getCabinClassConfigurations();
            for (int i = 0; i < cabinClasses.size(); i++) {
                CabinClassConfiguration cabinClass = cabinClasses.get(i);
                System.out.println((i + 1) + ": " + cabinClass.getCabinType() + "; Available seats: " + cabinClass.getSeatNumbers().size());
            }
            System.out.println();
        } catch (FlightDoesNotExistException ex) {
            throw ex;
        }
    }

}
