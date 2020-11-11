/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import exception.FlightAlreadyExistException;
import exception.FlightDoesNotExistException;
import exception.FlightScheduleAlreadyExistException;
import exception.FlightSchedulesOverlapException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
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
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote,
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        int response = 0;

        while (true) {
            System.out.println("*** Flight Reservation System (v1.0) ***\n");
            System.out.println("You are logged in as " + employee.getName() + " with "
                    + employee.getEmployeeType()
                    + " rights\n");
            System.out.println("1: Create flight");
            System.out.println("2: View all flights");
            System.out.println("3: View flight details");
            System.out.println("4: Update flight");
            System.out.println("5: Delete flight");
            System.out.println("6: Create flight schedule plan");
            System.out.println("7: Logout\n");
            response = 0;

            OUTER:
            while (response < 1 || response > 7) {
                System.out.print("> ");
                response = sc.nextInt();
                switch (response) {
                    case 1:
                        try {
                        createFlight(flightSessionBeanRemote, flightRouteSessionBeanRemote,
                                aircraftConfigurationSessionBeanRemote);
                    } catch (FlightAlreadyExistException ex) {
                        System.out.println("Error creating flight: " + ex.getMessage());
                    }
                    break;
                    case 2:
                        viewAllFlights(flightSessionBeanRemote);
                        break;
                    case 3:
                        try {
                        viewFlightDetails(flightSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error viewing flight details: " + ex.getMessage());
                    }
                    break;

                    case 4:
                        try {
                        updateFlight(flightSessionBeanRemote, aircraftConfigurationSessionBeanRemote, flightRouteSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error updating flight: " + ex.getMessage());
                    }
                    break;

                    case 5: 
                        try {
                        deleteFlight(flightSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error deleting flight: " + ex.getMessage());
                    }
                    break;

                    case 6:
                        try {
                        createFlightSchedulePlan(flightSchedulePlanSessionBeanRemote, flightSessionBeanRemote, flightScheduleSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error creating flight schedule plan: " + ex.getMessage());
                    }

                    case 7:
                        break OUTER;

                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    private void createFlightSchedulePlan(FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote,
            FlightSessionBeanRemote flightSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote)
            throws FlightDoesNotExistException {

        System.out.println("*** Create flight schedule plan ***\n");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine();

        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);

        System.out.print("Enter flight schedule type to be created\n(single / multiple / recurrent weekly / recurrent by days)>");
        String ans = sc.nextLine().trim().toLowerCase();

        Duration duration;
        LocalDateTime departureDateTime;

        switch (ans) {
            case "single":
                departureDateTime = readDate();
                duration = readDuration();
                FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.createNewFlightSchedule(new FlightSchedule(departureDateTime, duration));
                try {
                    Long fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(), flight);
                    flightSchedulePlanSessionBeanRemote.addFlightScheduleToFlightSchedulePlan(fspId, flightSchedule.getFlightScheduleID());
                } catch (FlightSchedulesOverlapException ex) {
                    System.out.println("Unable to create flight schedule plan " + ex.getMessage());
                }
                break;

            case "multiple":
                char response = 'Y';
                while (response == 'Y') {
                    departureDateTime = readDate();
                    duration = readDuration();
                    flightSchedule = flightScheduleSessionBeanRemote.createNewFlightSchedule(new FlightSchedule(departureDateTime, duration));
                    try {
                        Long fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(), flight);
                        flightSchedulePlanSessionBeanRemote.addFlightScheduleToFlightSchedulePlan(fspId, flightSchedule.getFlightScheduleID());
                    } catch (FlightSchedulesOverlapException ex) {
                        System.out.println("Unable to create flight schedule plan " + ex.getMessage());
                        continue;
                    }

                    System.out.println("Flight schedule successfully added!");
                    System.out.print("Add another flight schedule? (Y/N) >");
                    response = sc.nextLine().charAt(0);
                }
                break;

            case "recurrent by days":
                System.out.println("Enter number of days between recurrence>");
                Integer n = sc.nextInt();
                departureDateTime = readDate();
                duration = readDuration();
                LocalDateTime endDateTime;
                while (true) {
                    System.out.println("Enter end date and time (yyyy-MM-dd HH:mm)");
                    String dateTimeString = sc.nextLine();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    try {
                        endDateTime = LocalDateTime.parse(dateTimeString, format);
                        break;

                    } catch (DateTimeParseException ex) {
                        System.out.println("Incorrect date format! Try again with the format (yyyy-MM-dd HH:mm)");
                    }
                }

                try {
                    Long fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(), flight);
                    flightScheduleSessionBeanRemote.addRecurringFlightSchedules(fspId, departureDateTime, duration, endDateTime, n);
                    System.out.println("Recurring flight schedule successfully created!");

                } catch (FlightSchedulesOverlapException ex) {
                    System.out.println("Unable to create flight schedule plan " + ex.getMessage());
                }
                break;

            case "recurrent weekly":
                n = 7;
                departureDateTime = readDate();
                duration = readDuration();
                while (true) {
                    System.out.println("Enter end date and time (yyyy-MM-dd HH:mm)");
                    String dateTimeString = sc.nextLine();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    try {
                        endDateTime = LocalDateTime.parse(dateTimeString, format);
                        break;

                    } catch (DateTimeParseException ex) {
                        System.out.println("Incorrect date format! Try again with the format (yyyy-MM-dd HH:mm)");
                    }
                }

                try {
                    Long fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(), flight);
                    flightScheduleSessionBeanRemote.addRecurringFlightSchedules(fspId, departureDateTime, duration, endDateTime, n);
                    System.out.println("Recurring weekly flight schedule successfully created!");

                } catch (FlightSchedulesOverlapException ex) {
                    System.out.println("Unable to create flight schedule plan " + ex.getMessage());
                }
                break;

            default:
                System.out.println("Invalid flight schedule type!");
                break;
        }
        
    }

    private void createComplementaryFlightSchedulePlan(Flight flight, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote,
            FlightSessionBeanRemote flightSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote) {
        
        if (selectedFlightRoute.getComplementaryFlightRoute() != null) {
            System.out.println("Selected flight route has a complementary flight route!");
            sc.nextLine();
            System.out.print("Create complementary return flight? Y/N> ");
            String ans = sc.nextLine().toLowerCase();
            if (ans.equals("y")) {
                System.out.print("Enter flight number> ");
                String complementaryFlightNumber = sc.nextLine();
                FlightRoute complementaryFlightRoute = selectedFlightRoute.getComplementaryFlightRoute();
                Long complementartyFlightId = flightSessionBeanRemote.createComplementaryFlight(complementaryFlightNumber, complementaryFlightRoute.getFlightRouteId(),
                        flight.getAircraftConfiguration().getAircraftConfigurationID(), flight.getFlightID());
  
        
    }
    
    private void deleteFlight(FlightSessionBeanRemote flightSessionBeanRemote) throws FlightDoesNotExistException {
        System.out.println("*** Delete flight ***\n");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine();

        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);

        // A flight can only be deleted if not used (i.e. not included in a flight schedule plan)
        int removed = flightSessionBeanRemote.removeFlight(flightNumber);

        switch (removed) {
            case 0:
                System.out.println("No such flight exists!");
                break;
            case 1:
                System.out.println("Flight has been disabled successfully!");
                break;
            case 2:
                System.out.println("Flight has been deleted successfully!");
                break;
            default:
                break;
        }
    }

    private void updateFlight(FlightSessionBeanRemote flightSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote) throws FlightDoesNotExistException {
        System.out.println("*** Update flight ***\n");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine();

        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);

        System.out.println();

        System.out.println("Update details of flight " + flightNumber + ":");
        System.out.println("1: Update flight number");
        System.out.println("2: Update aircraft configuration");
        System.out.println("3: Update flight route");
        System.out.println("4: Finish\n");

        int response = 0;

        OUTER:
        while (response < 1 || response > 4) {
            System.out.print("> ");
            response = sc.nextInt();
            switch (response) {
                case 1:
                    String newFlightNumber = updateFlightNumber(flightSessionBeanRemote, flightNumber);
                    flightNumber = newFlightNumber;

                    break;

                case 2:
                    updateAircraftConfiguration(flightSessionBeanRemote, aircraftConfigurationSessionBeanRemote, flightNumber);

                    break;

                case 3:
                    updateFlightRoute(flightSessionBeanRemote, flightRouteSessionBeanRemote, flightNumber);

                case 4:
                    break OUTER;

                default:
                    System.out.println("Invalid option, please try again!\n");
                    break;
            }
        }
    }

    private void updateAircraftConfiguration(FlightSessionBeanRemote flightSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, String flightNumber) throws FlightDoesNotExistException {
        System.out.println("*** Select an aircraft configuration ***");
        List<AircraftConfiguration> aircraftConfigurations = aircraftConfigurationSessionBeanRemote.viewAllAircraftConfigurations();
        for (int i = 0; i < aircraftConfigurations.size(); i++) {
            AircraftConfiguration aircraftConfiguration = aircraftConfigurations.get(i);
            System.out.println((i + 1) + ": " + aircraftConfiguration.getName());
        }
        System.out.print("> ");
        int answer = sc.nextInt();
        AircraftConfiguration selectedAircraftConfiguration = aircraftConfigurations.get(answer - 1);

        flightSessionBeanRemote.updateAircraftConfiguration(flightNumber, selectedAircraftConfiguration);
    }

    private String updateFlightNumber(FlightSessionBeanRemote flightSessionBeanRemote, String flightNumber) throws FlightDoesNotExistException {
        System.out.println("*** Update flight number ***\n");
        System.out.println("Enter new flight number");
        String newFlightNumber = sc.nextLine().trim();

        flightSessionBeanRemote.updateFlightNumber(flightNumber, newFlightNumber);
        return newFlightNumber;

    }

    private void updateFlightRoute(FlightSessionBeanRemote flightSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, String flightNumber) throws FlightDoesNotExistException {

        System.out.println("*** Select a flight route ***");
        List<FlightRoute> flightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        for (int i = 0; i < flightRoutes.size(); i++) {
            FlightRoute flightRoute = flightRoutes.get(i);
            System.out.println((i + 1) + ": " + flightRoute.getOrigin().getIATACode() + "-" + flightRoute.getDestination().getIATACode());
        }
        System.out.print("> ");
        int answer = sc.nextInt();
        FlightRoute selectedFlightRoute = flightRoutes.get(answer - 1);

        Flight flight = flightSessionBeanRemote.updateFlightRoute(flightNumber, selectedFlightRoute);

        String result = "";
        if (selectedFlightRoute.getComplementaryFlightRoute() != null) {
            System.out.println("Selected flight route has a complementary flight route!");
            sc.nextLine();
            System.out.print("Create complementary return flight? Y/N> ");
            String ans = sc.nextLine().toLowerCase();
            if (ans.equals("y")) {
                System.out.print("Enter flight number> ");
                String complementaryFlightNumber = sc.nextLine();
                FlightRoute complementaryFlightRoute = selectedFlightRoute.getComplementaryFlightRoute();
                Long complementartyFlightId = flightSessionBeanRemote.createComplementaryFlight(complementaryFlightNumber, complementaryFlightRoute.getFlightRouteId(),
                        flight.getAircraftConfiguration().getAircraftConfigurationID(), flight.getFlightID());
                result += "Complementary flight number ML" + complementaryFlightNumber + " with ID " + complementartyFlightId + " successfully created!\n";
            }
        }
        System.out.print(result);

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

    private LocalDateTime readDate() {
        LocalDateTime departureDateTime;
        while (true) {
            System.out.println("Enter departure date and time (dd/MM/yyyy HH:mm)");
            String dateTimeString = sc.nextLine();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            try {
                departureDateTime = LocalDateTime.parse(dateTimeString, format);
                return departureDateTime;

            } catch (DateTimeParseException ex) {
                System.out.println("Incorrect date format! Try again with the format (dd/MM/yyyy HH:mm)");
            }
        }
    }

    private Duration readDuration() {
        System.out.println("Enter Duration (hours:minutes");
        String s = sc.nextLine().trim();
        String[] values = s.split(":");
        // get the hours, minutes and seconds value and add it to the duration
        Duration duration = Duration.ofHours(Long.parseLong(values[0]));
        duration.plusMinutes(Long.parseLong(values[1]));
        return duration;
    }
}
