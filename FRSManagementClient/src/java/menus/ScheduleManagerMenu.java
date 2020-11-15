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
import entity.Fare;
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
import exception.FlightSchedulesOverlapException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import ejb.session.stateless.FareSessionBeanRemote;
import entity.Airport;
import enumeration.FSPType;

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
            FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote) {
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
            System.out.println("7: View all flight schedule plans");
            System.out.println("8: View flight schedule plan details");
            System.out.println("9: Update flight schedule plan");
            System.out.println("10: Delete flight schedule plan");
            System.out.println("11: Logout\n");
            response = 0;

            while (response < 1 || response > 11) {
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
                    viewFlightDetails(flightSessionBeanRemote);

                } else if (response == 4) {
                    try {
                        updateFlight(flightSessionBeanRemote, aircraftConfigurationSessionBeanRemote, flightRouteSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error updating flight: " + ex.getMessage());
                    }
                } else if (response == 5) {
                    try {
                        deleteFlight(flightSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error deleting flight: " + ex.getMessage());
                    }

                } else if (response == 6) {
                    try {
                        createFlightSchedulePlan(flightSchedulePlanSessionBeanRemote, flightSessionBeanRemote, flightScheduleSessionBeanRemote, fareSessionBeanRemote);
                    } catch (FlightDoesNotExistException ex) {
                        System.out.println("Error creating flight schedule plan: " + ex.getMessage());
                    }
                } else if (response == 7) {
                    viewAllFlightSchedulePlans(flightSchedulePlanSessionBeanRemote);

                } else if (response == 8) {
                    System.out.println("*** View flight schedule plan details***\n");
                    viewFlightSchedulePlanDetails(flightSchedulePlanSessionBeanRemote);

                } else if (response == 9) {
                    System.out.println("*** Update flight schedule plan ***\n");
                    updateFlightSchedulePlan(flightSchedulePlanSessionBeanRemote, flightScheduleSessionBeanRemote, fareSessionBeanRemote);

                } else if (response == 10) {
                    System.out.println("*** Update flight schedule plan ***\n");
                    deleteFlightSchedulePlan(flightSchedulePlanSessionBeanRemote);
                    
                } else if (response == 11) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 11) {
                break;
            }
        }
    }

    private void deleteFlightSchedulePlan(FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        int selection = 0;
        System.out.println("Select flight schedule plan to delete:");
        List<FlightSchedulePlan> fsps = flightSchedulePlanSessionBeanRemote.viewAllFlightSchedulePlans();
        for (int i = 0; i < fsps.size(); i++) {
            FlightSchedulePlan fsp = fsps.get(i);
            if (fsp.isEnabled()) {
                System.out.println((i + 1) + ": " + "Flight number -> " + fsp.getFlight().getFlightNumber()
                        + " Type -> " + fsp.getFlightSchedulePlanType().name());
            }
        }
        System.out.print("\n> ");
        selection = sc.nextInt();
        if (selection < 1 && selection > fsps.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        long fspId = fsps.get(selection - 1).getFlightSchedulePlanID();
        
        int removed = flightSchedulePlanSessionBeanRemote.deleteFlightSchedulePlan(fspId);
        switch (removed) {
            case 0:
                System.out.println("No such flight exists!");
                break;
            case 1:
                System.out.println("Flight has been deleted successfully!\n");
                break;
            case 2:
                System.out.println("Flight has been disabled successfully!\n");
                break;
            default:
                break;
        }
    }
    
    
    private void updateFlightSchedulePlan(FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote) {
        long fspId = viewFlightSchedulePlanDetails(flightSchedulePlanSessionBeanRemote);

        if (fspId == -1) {
            // Due to invalid selection when viewing details
            return;
        }

        FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.retrieveFSPById(fspId);

        int selection = 0;
        System.out.println("Update details of flight schedule plan " + fspId + ":");
        System.out.println("1: Add flight schedule");
        System.out.println("2: Delete flight schedule");
        System.out.println("3: Change fare");
        System.out.println("4: Finish\n");

        int response = 0;

        OUTER:
        while (response < 1 || response > 4) {
            System.out.print("> ");
            response = sc.nextInt();
            if (response == 1) {
                addFlightScheduleToFSP(fspId, flightSchedulePlanSessionBeanRemote, flightScheduleSessionBeanRemote);
            } else if (response == 2) {
                deleteFlightSchedule(fspId, flightSchedulePlanSessionBeanRemote, flightScheduleSessionBeanRemote);
            } else if (response == 3) {
                changeFare(fspId, fareSessionBeanRemote, flightSchedulePlanSessionBeanRemote);
            } else if (response == 4) {
                break;

            } else {
                System.out.println("Invalid option, please try again!\n");
                break;
            }
        }
    }

    private void changeFare(long fspId, FareSessionBeanRemote fareSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.retrieveFSPById(fspId);
        System.out.println("*** View Fares ***\n");
        List<Fare> fares = fsp.getFares();
        for (int i = 0; i < fares.size(); i++) {
            Fare fare = fares.get(i);
            System.out.println((i + 1) + ": " + fare.toString());
        }
        System.out.println();
        System.out.print("Select fare to change> ");
        int selection = sc.nextInt();
        if (selection < 1 || selection > fares.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        Fare oldFare = fares.get(selection - 1);
        CabinClassConfiguration cb = oldFare.getCabinClassConfiguration();

        System.out.print("Enter new fare basis code for " + cb.getCabinType().name() + " class > ");
        String fareBasisCode = sc.next();
        System.out.print("Enter new fare amount for " + cb.getCabinType().name() + " class > ");
        Double fareAmount = sc.nextDouble();
        Fare newFare = new Fare(cb.getCabinType(), fareBasisCode, fareAmount);
        fareSessionBeanRemote.changeFare(fspId, oldFare.getFareID(), newFare);
    }

    private void deleteFlightSchedule(long fspId, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote) {
        System.out.println("*** Delete flight schedule ***\n");
        int selection = 0;
        System.out.println("Select flight schedule:");
        FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.retrieveFSPById(fspId);

        List<FlightSchedule> flightSchedules = fsp.getFlightSchedules();
        for (int i = 0; i < flightSchedules.size(); i++) {
            FlightSchedule fs = flightSchedules.get(i);
            System.out.println((i + 1) + ": " + "Flight number -> " + fsp.getFlight().getFlightNumber()
                    + " Type -> " + fsp.getFlightSchedulePlanType().name());
        }
        System.out.print("\n> ");
        selection = sc.nextInt();
        if (selection < 1 && selection > flightSchedules.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        long fsId = flightSchedules.get(selection - 1).getFlightScheduleID();

        int removed = flightScheduleSessionBeanRemote.deleteFlightSchedule(fsId);
        switch (removed) {
            case 0:
                System.out.println("No such flight exists!");
                break;
            case 1:
                System.out.println("Flight schedule has been removed successfully!\n");
                break;
            case 2:
                System.out.println("Flight schedule cannot be removed!\n");
                break;
            default:
                break;
        }
    }

    // For preexisting FSP
    private void addFlightScheduleToFSP(long fspId, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote) {
        System.out.println("*** Create new flight schedule(s) ***");
        System.out.println("Enter flight schedule type to be created\n(single / multiple / recurrent weekly / recurrent by days)>");
        System.out.println("1: Single");
        System.out.println("2: Multiple");
        System.out.println("3: Recurrent every nth day");
        System.out.println("4: Recurrent weekly");

        int ans = sc.nextInt();

        Duration duration;
        LocalDateTime departureDateTime;

        if (ans == 1) {
            System.out.println("Enter flight departure date and time:");
            departureDateTime = readDate();
            System.out.println("Enter flight duration:");
            duration = readDuration();
            try {
                FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.createNewFlightSchedule(new FlightSchedule(departureDateTime, duration), fspId);
                flightSchedulePlanSessionBeanRemote.addFlightScheduleToFlightSchedulePlan(fspId, flightSchedule.getFlightScheduleID());
                System.out.println("Single flight schedule sucessfully created!");
            } catch (FlightSchedulesOverlapException ex) {
                System.out.println("Unable to create flight schedule plan " + ex.getMessage());
            }

        } else if (ans == 2) {
            char addMore = 'Y';
            while (addMore == 'Y') {
                System.out.println("Enter flight departure date and time:");
                departureDateTime = readDate();
                System.out.println("Enter flight duration:");
                duration = readDuration();
                try {
                    FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.createNewFlightSchedule(new FlightSchedule(departureDateTime, duration), fspId);
                    flightSchedulePlanSessionBeanRemote.addFlightScheduleToFlightSchedulePlan(fspId, flightSchedule.getFlightScheduleID());
                } catch (FlightSchedulesOverlapException ex) {
                    System.out.println("Unable to create flight schedule plan " + ex.getMessage());
                    continue;
                }

                System.out.println("Flight schedule successfully added!\n");
                System.out.print("Add another flight schedule? (Y/N) >");
                addMore = sc.nextLine().charAt(0);
            }
        } else if (ans == 3) {
            System.out.println("Enter number of days between recurrence>");
            Integer n = sc.nextInt();
            departureDateTime = readDate();
            duration = readDuration();
            LocalDateTime endDateTime;
            System.out.println("Enter end date and time:");
            endDateTime = readDate();

            flightScheduleSessionBeanRemote.addRecurringFlightSchedules(fspId, departureDateTime, duration, endDateTime, n);
            System.out.println("Recurring flight schedule successfully created!\n");

        } else if (ans == 4) {
            int n = 7;
            System.out.println("Enter flight departure date and time:");
            departureDateTime = readDate();
            System.out.println("Enter flight duration:");
            duration = readDuration();
            System.out.println("Enter end date and time:");
            LocalDateTime endDateTime = readDate();

            flightScheduleSessionBeanRemote.addRecurringFlightSchedules(fspId, departureDateTime, duration, endDateTime, n);
            System.out.println("Recurring weekly flight schedule successfully created!\n");

        } else {
            System.out.println("Invalid flight schedule type!");
        }
    }

    private long viewFlightSchedulePlanDetails(FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        int selection = 0;
        System.out.println("Select flight schedule plan:");
        List<FlightSchedulePlan> fsps = flightSchedulePlanSessionBeanRemote.viewAllFlightSchedulePlans();
        for (int i = 0; i < fsps.size(); i++) {
            FlightSchedulePlan fsp = fsps.get(i);
            if (fsp.isEnabled()) {
                System.out.println((i + 1) + ": " + "Flight number -> " + fsp.getFlight().getFlightNumber()
                        + " Type -> " + fsp.getFlightSchedulePlanType().name());
            }
        }
        System.out.print("\n> ");
        selection = sc.nextInt();
        if (selection < 1 && selection > fsps.size()) {
            System.out.println("Invalid selection!");
            return -1;
        }
        long fspId = fsps.get(selection - 1).getFlightSchedulePlanID();
        FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.retrieveFSPById(fspId);
        Airport origin = fsp.getFlight().getFlightRoute().getOrigin();
        Airport destination = fsp.getFlight().getFlightRoute().getDestination();

        System.out.println("** Flight origin airport **");
        System.out.println(origin.toString());
        System.out.println("** Flight destination airport **");
        System.out.println(destination.toString());
        System.out.println();

        System.out.println("** Flight schedules **");
        List<FlightSchedule> flightSchedules = fsp.getFlightSchedules();
        for (int i = 0; i < flightSchedules.size(); i++) {
            FlightSchedule fs = flightSchedules.get(i);
            System.out.println((i + 1) + ": ");
            System.out.println(fs.toString());
        }
        System.out.println();

        System.out.println("** Fares **");
        List<Fare> fares = fsp.getFares();
        for (int i = 0; i < fares.size(); i++) {
            Fare f = fares.get(i);
            System.out.println((i + 1) + ": ");
            System.out.println(f.toString());
        }
        System.out.println();
        return fspId;
    }

    private void viewAllFlightSchedulePlans(FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        System.out.println("*** View all flight schedule plans ***\n");
        List<FlightSchedulePlan> fsps = flightSchedulePlanSessionBeanRemote.viewAllFlightSchedulePlans();
        for (int i = 0; i < fsps.size(); i++) {
            FlightSchedulePlan fsp = fsps.get(i);
            if (fsp.isEnabled()) {
                System.out.println((i + 1) + ": " + "Flight number -> " + fsp.getFlight().getFlightNumber()
                        + " Type -> " + fsp.getFlightSchedulePlanType().name());
            }
        }
        System.out.println();
    }

    private void createFlightSchedulePlan(FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FareSessionBeanRemote fareSessionBeanRemote)
            throws FlightDoesNotExistException {

        System.out.println("*** Create flight schedule plan ***\n");
        sc.nextLine();
        System.out.print("Enter flight number> ");
        String flightNumber = sc.nextLine();

        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);
        if (!flight.isEnabled()) {
            System.out.println("Flight has been disabled!");
            return;
        }
        System.out.println("*** Create flight schedule(s) ***");
        System.out.println("Enter flight schedule type to be created\n(single / multiple / recurrent weekly / recurrent by days)>");
        System.out.println("1: Single");
        System.out.println("2: Multiple");
        System.out.println("3: Recurrent every nth day");
        System.out.println("4: Recurrent weekly");

        int ans = sc.nextInt();

        Duration duration;
        LocalDateTime departureDateTime;
        Long fspId = null;

        if (ans == 1) {
            System.out.println("Enter flight departure date and time:");
            departureDateTime = readDate();
            System.out.println("Enter flight duration:");
            duration = readDuration();
            try {
                fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(FSPType.SINGLE), flight);
                FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.createNewFlightSchedule(new FlightSchedule(departureDateTime, duration), fspId);
                flightSchedulePlanSessionBeanRemote.addFlightScheduleToFlightSchedulePlan(fspId, flightSchedule.getFlightScheduleID());
                System.out.println("Single flight schedule sucessfully created!");
            } catch (FlightSchedulesOverlapException ex) {
                System.out.println("Unable to create flight schedule plan " + ex.getMessage());
            }

        } else if (ans == 2) {
            char response = 'Y';
            while (response == 'Y') {
                System.out.println("Enter flight departure date and time:");
                departureDateTime = readDate();
                System.out.println("Enter flight duration:");
                duration = readDuration();
                try {
                    fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(FSPType.MULTIPLE), flight);
                    FlightSchedule flightSchedule = flightScheduleSessionBeanRemote.createNewFlightSchedule(new FlightSchedule(departureDateTime, duration), fspId);
                    flightSchedulePlanSessionBeanRemote.addFlightScheduleToFlightSchedulePlan(fspId, flightSchedule.getFlightScheduleID());
                } catch (FlightSchedulesOverlapException ex) {
                    System.out.println("Unable to create flight schedule plan " + ex.getMessage());
                    continue;
                }

                System.out.println("Flight schedule successfully added!\n");
                System.out.print("Add another flight schedule? (Y/N) >");
                response = sc.nextLine().charAt(0);
            }
        } else if (ans == 3) {
            System.out.println("Enter number of days between recurrence>");
            Integer n = sc.nextInt();
            departureDateTime = readDate();
            duration = readDuration();
            LocalDateTime endDateTime;
            System.out.println("Enter end date and time:");
            endDateTime = readDate();

            fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(FSPType.RECURRENT_NDAY), flight);
            flightScheduleSessionBeanRemote.addRecurringFlightSchedules(fspId, departureDateTime, duration, endDateTime, n);
            System.out.println("Recurring flight schedule successfully created!\n");

        } else if (ans == 4) {
            int n = 7;
            System.out.println("Enter flight departure date and time:");
            departureDateTime = readDate();
            System.out.println("Enter flight duration:");
            duration = readDuration();
            System.out.println("Enter end date and time:");
            LocalDateTime endDateTime = readDate();

            fspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(FSPType.RECURRENT_WEEKLY), flight);
            flightScheduleSessionBeanRemote.addRecurringFlightSchedules(fspId, departureDateTime, duration, endDateTime, n);
            System.out.println("Recurring weekly flight schedule successfully created!\n");

        } else {
            System.out.println("Invalid flight schedule type!");
            return;
        }

        enterFare(fspId, fareSessionBeanRemote, flightSchedulePlanSessionBeanRemote);
        FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.retrieveFSPById(fspId);

        createComplementaryFlightSchedulePlan(fsp, flightSchedulePlanSessionBeanRemote, flightSessionBeanRemote, flightScheduleSessionBeanRemote, fareSessionBeanRemote);

    }

    private void enterFare(Long fspId, FareSessionBeanRemote fareSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote) {
        FlightSchedulePlan fsp = flightSchedulePlanSessionBeanRemote.retrieveFSPById(fspId);
        Flight flight = fsp.getFlight();
        AircraftConfiguration aircraftConfig = flight.getAircraftConfiguration();
        List<CabinClassConfiguration> cabinClasses = aircraftConfig.getCabinClassConfigurations();
        for (CabinClassConfiguration cb : cabinClasses) {
            System.out.print("Enter fare basis code for " + cb.getCabinType().name() + " class > ");
            String fareBasisCode = sc.next();
            System.out.print("Enter fare amount for " + cb.getCabinType().name() + " class > ");
            Double fareAmount = sc.nextDouble();
            Fare fare = new Fare(cb.getCabinType(), fareBasisCode, fareAmount);
            fareSessionBeanRemote.addFareToFlightSchedulePlan(fspId, cb.getCabinClassID(), fare);
        }
    }

    private void createComplementaryFlightSchedulePlan(FlightSchedulePlan fsp, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote,
            FlightSessionBeanRemote flightSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote,
            FareSessionBeanRemote fareSessionBeanRemote) {

        fsp = flightSchedulePlanSessionBeanRemote.retrieveFSPById(fsp.getFlightSchedulePlanID());
        if (fsp.getFlight().getComplementaryFlight() != null) {
            System.out.println("Selected flight schedule plan has a complementary return flight!");
            sc.nextLine();
            System.out.print("Create complementary return flight schedule plan? Y/N> ");
            String ans = sc.nextLine().toLowerCase();
            if (ans.equals("n")) {
                return;
            }
            if (ans.equals("y")) {
                System.out.println("Enter layover duration:");
                Duration layover = readDuration();
                Long complFspId = flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(new FlightSchedulePlan(), fsp.getFlight().getComplementaryFlight());

                for (FlightSchedule fs : fsp.getFlightSchedules()) {
                    try {
                        FlightSchedule newFlightSchedule = flightScheduleSessionBeanRemote.createNewFlightSchedule(new FlightSchedule(fs.getArrivalDateTime().plus(layover), fs.getDuration()), complFspId);
                        flightSchedulePlanSessionBeanRemote.addFlightScheduleToFlightSchedulePlan(complFspId, newFlightSchedule.getFlightScheduleID());

                    } catch (FlightSchedulesOverlapException ex) {
                    }
                }
                enterFare(complFspId, fareSessionBeanRemote, flightSchedulePlanSessionBeanRemote);
            }
        }
    }

    private void deleteFlight(FlightSessionBeanRemote flightSessionBeanRemote) throws FlightDoesNotExistException {
        System.out.println("*** Delete flight ***\n");
        System.out.print("Enter flight number> ");
        sc.nextLine();
        String flightNumber = sc.nextLine();

        Flight flight = flightSessionBeanRemote.retrieveFlightByFlightNumber(flightNumber);

        // A flight can only be deleted if not used (i.e. not included in a flight schedule plan with a flight schedule that has been booked)
        int removed = flightSessionBeanRemote.removeFlight(flightNumber);

        switch (removed) {
            case 0:
                System.out.println("No such flight exists!");
                break;
            case 1:
                System.out.println("Flight has been disabled successfully!\n");
                break;
            case 2:
                System.out.println("Flight has been deleted successfully!\n");
                break;
            default:
                break;
        }
    }

    private void updateFlight(FlightSessionBeanRemote flightSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote) throws FlightDoesNotExistException {
        System.out.println("*** Update flight ***\n");
        System.out.print("Enter flight number> ");
        sc.nextLine();
        String flightNumber = sc.nextLine().trim();

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
            if (response == 1) {
                String newFlightNumber = updateFlightNumber(flightSessionBeanRemote, flightNumber);
                flightNumber = newFlightNumber;

            } else if (response == 2) {
                updateAircraftConfiguration(flightSessionBeanRemote, aircraftConfigurationSessionBeanRemote, flightNumber);

            } else if (response == 3) {
                updateFlightRoute(flightSessionBeanRemote, flightRouteSessionBeanRemote, flightNumber);

            } else if (response == 4) {
                return;

            } else {
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
        System.out.println("Enter new flight number>");
        sc.nextLine();
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
            Flight flight = flights.get(i);
            if (flight.isEnabled()) {
                System.out.println((i + 1) + ": " + flight.getFlightNumber());
            }
        }
    }

    private void viewFlightDetails(FlightSessionBeanRemote flightSessionBeanRemote) {
        System.out.println("*** View flight details ***\n");

        List<Flight> flights = flightSessionBeanRemote.viewAllFlights();
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            if (flight.isEnabled()) {
                System.out.println((i + 1) + ": " + flight.getFlightNumber());
            }
        }
        System.out.println();
        System.out.print("Select flight to view> ");
        int selection = sc.nextInt();
        if (selection < 1 || selection > flights.size()) {
            System.out.println("Invalid selection!");
            return;
        }
        Flight flight = flights.get(selection - 1);
        System.out.println("Flight origin airport: " + flight.getFlightRoute().getOrigin().getIATACode());
        System.out.println("Flight destiantion airport: " + flight.getFlightRoute().getDestination().getIATACode());
        System.out.println("** Available cabin classes **");
        List<CabinClassConfiguration> cabinClasses = flight.getAircraftConfiguration().getCabinClassConfigurations();
        for (int i = 0; i < cabinClasses.size(); i++) {
            CabinClassConfiguration cabinClass = cabinClasses.get(i);
            System.out.println((i + 1) + ": " + cabinClass.getCabinType() + "; Available seats: " + cabinClass.getSeatNumbers().size());
        }
        System.out.println();
    }

    private LocalDateTime readDate() {
        LocalDateTime dateTime;
        System.out.print("Enter year> ");
        int year = sc.nextInt();
        System.out.print("Enter month (as integer)> ");
        int month = sc.nextInt();
        System.out.print("Enter day of month> ");
        int day = sc.nextInt();
        System.out.print("Enter hour> ");
        int hour = sc.nextInt();
        System.out.print("Enter minute> ");
        int min = sc.nextInt();
        System.out.println();

        LocalDateTime date = LocalDateTime.of(year, month, day, hour, min);
        return date;
    }

    private Duration readDuration() {
        Duration duration = Duration.ZERO;
        System.out.print("Enter duration hours> ");
        int hour = sc.nextInt();
        duration.plusHours(hour);

        System.out.print("Enter duration minutes> ");
        int min = sc.nextInt();
        duration.plusMinutes(min);
        System.out.println();
        return duration;
    }
}
