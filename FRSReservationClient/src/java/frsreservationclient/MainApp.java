/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import entity.Airport;
import entity.Customer;
import entity.Fare;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import enumeration.CabinType;
import enumeration.FlightType;
import enumeration.TripType;
import exception.AirportDoesNotExistException;
import exception.CustomerAlreadyExistsException;
import exception.CustomerAlreadyLoggedInException;
import exception.CustomerNotFoundException;
import exception.InvalidCabinClassException;
import exception.InvalidChoiceException;
import exception.InvalidLoginCredentialException;
import exception.InvalidTripTypeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author yappeizhen
 */
public class MainApp {

    private Scanner sc = new Scanner(System.in);
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private Customer customer = null;

    MainApp() {
    }

    MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote,
            FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
    }

    public void run() {
        int response = 0;

        while (true) {
            System.out.println("*** Welcome to Flight Reservation System (v1.0) ***\n");
            System.out.println("1: Register as customer");
            System.out.println("2: Login");
            System.out.println("3: Search flight");
            System.out.println("4: Exit\n");
            response = 0;

            OUTER:
            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                switch (response) {
                    case 1:
                        try {
                        registerNewCustomer();
                    } catch (CustomerAlreadyExistsException | CustomerAlreadyLoggedInException ex) {
                        System.out.println("Unable to register new customer: " + ex.getMessage());
                    }
                    break;

                    case 2:
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                    } catch (InvalidLoginCredentialException | CustomerAlreadyLoggedInException ex) {
                        System.out.println("Login unsuccessful! " + ex.getMessage());
                    }
                    break;

                    case 3:
                        searchFlight(airportSessionBeanRemote);
                    case 4:
                        break OUTER;

                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException, CustomerAlreadyLoggedInException {
        if (customer != null) {
            throw new CustomerAlreadyLoggedInException("You are already logged in");
        }

        System.out.println("*** Flight Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        String userName = sc.next().trim();
        System.out.print("Enter password> ");
        String password = sc.next().trim();
        System.out.println();
        if (userName.length() > 0 && password.length() > 0) {
            try {
                Customer customer = customerSessionBeanRemote.login(userName, password);
            } catch (CustomerNotFoundException ex) {
                throw new InvalidLoginCredentialException(ex.getMessage());
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }

    private void registerNewCustomer() throws CustomerAlreadyExistsException, CustomerAlreadyLoggedInException {
        if (customer != null) {
            throw new CustomerAlreadyLoggedInException("You are already logged in");
        }
        System.out.println("*** Register new customer account ***\n");
        System.out.print("Enter first name> ");
        String firstName = sc.nextLine().trim();
        System.out.print("Enter last name> ");
        String lastName = sc.nextLine().trim();
        System.out.print("Enter email> ");
        String email = sc.nextLine().trim();
        System.out.print("Enter mobile phone number> ");
        String mobile = sc.nextLine().trim();
        System.out.print("Enter address> ");
        String address = sc.nextLine().trim();
        System.out.print("Enter username> ");
        String userName = sc.nextLine().trim();
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();

        Customer newCustomer = new Customer(firstName, lastName, email, mobile, address, userName, password);
        customerSessionBeanRemote.createNewCustomer(newCustomer);
    }

    public void searchFlight(AirportSessionBeanRemote airportSessionBeanRemote) {

        System.out.println("*** Search flight ***\n");
        TripType tripType = processTripType();
        System.out.println();
        List<Airport> airports = airportSessionBeanRemote.viewAllAirports();
        Airport departureAirport = selectAirport(airports);
        Airport destinationAirport = selectAirport(airports);
        System.out.print("Enter departure date> ");
        LocalDate departureDate = readDate();
        LocalDate returnDate = null;
        if (tripType == tripType.ROUNDTRIP) {
            System.out.print("Enter return date> ");
            returnDate = readDate();
        }
        System.out.print("Enter number of passengers>");
        int numPassengers = sc.nextInt();
        System.out.print("Any preference for flight type? (Y/N)>");
        FlightType flightPreference = indicateFlightPreference();
        System.out.print("Any preference for cabin class? (Y/N)>");
        CabinType cabinTypePreference = indicateCabinClassPreference();
        List<FlightSchedule> flightSchedules = new ArrayList<>();
        FlightSchedule flightSchedule;
        int index = 0;
        if (flightPreference == FlightType.DIRECT) {
            System.out.println("*** Direct flights ***");
            index = displayDirectFlightSchedules(departureAirport, destinationAirport, departureDate,
                    numPassengers, flightPreference, flightSchedules, index);
            flightSchedule = flightSchedules.get(index);
        } else if (flightPreference == FlightType.CONNECTING) {
            System.out.println("*** Connecting flights ***");
            index = displayConnectFlightSchedules(departureAirport, destinationAirport, departureDate,
                    numPassengers, flightPreference, flightSchedules, index);
            flightSchedule = flightSchedules.get(index);
        }
    }

    private FlightType indicateFlightPreference() {
        char hasPreference = sc.nextLine().trim().charAt(0);

        while (hasPreference != 'Y' && hasPreference != 'N') {
            System.out.println("Invalid response! Enter Y/N");
            System.out.print("> ");
            hasPreference = sc.nextLine().trim().charAt(0);
        }

        if (hasPreference == 'Y') {
            int prefer = 0;
            System.out.println("Select preferred flight type: ");
            System.out.println("1: Direct flight");
            System.out.println("2: Connecting flight");
            while (prefer < 1 || prefer > 2) {
                System.out.print("> ");
                prefer = sc.nextInt();
                if (prefer == 1) {
                    return FlightType.DIRECT;
                } else if (prefer == 2) {
                    return FlightType.CONNECTING;

                } else {
                    System.out.println("Invalid option! Try again.");
                }
            }
        }
        return null;
    }

    private CabinType indicateCabinClassPreference() {
        char hasPreference = sc.nextLine().charAt(0);

        while (hasPreference != 'Y' && hasPreference != 'N') {
            System.out.println("Invalid response! Enter Y/N");
            System.out.print("> ");
            hasPreference = sc.nextLine().charAt(0);
        }

        if (hasPreference == 'Y') {
            int prefer = 0;
            System.out.println("Select preferred cabin class: ");
            System.out.println("1: First class");
            System.out.println("2: Business class");
            System.out.println("3: Premium economy class");
            System.out.println("4: Economy class");

            while (prefer < 1 || prefer > 4) {
                System.out.print(">");
                prefer = sc.nextInt();
                if (prefer == 1) {
                    return CabinType.FIRST_CLASS;
                } else if (prefer == 2) {
                    return CabinType.BUSINESS;
                } else if (prefer == 3) {
                    return CabinType.PREMIUM_ECONOMY;
                } else if (prefer == 4) {
                    return CabinType.ECONOMY;
                } else {
                    System.out.println("Invalid option! Try again.");
                }
            }
        }

        return null;
    }

    private TripType processTripType() {
        System.out.println("Enter trip type:");
        System.out.println("1: One-way");
        System.out.println("2: Round-trip");
        int response = 0;
        while (response < 1 || response > 2) {
            System.out.print(">");
            response = sc.nextInt();
            if (response == 1) {
                return TripType.ONEWAY;
            } else if (response == 2) {
                return TripType.ROUNDTRIP;
            } else {
                System.out.println("Invalid option!");
            }
        }
        return null;
    }

    /*
    private Airport processAirport(String airportName) throws AirportDoesNotExistException {
        Airport airport = airportSessionBeanRemote.retrieveAirportByAirportName(airportName);
        return airport;
    }
     */
    private LocalDate readDate() {
        System.out.print("Enter year>");
        int year = sc.nextInt();
        System.out.print("Enter month (as integer)>");
        int month = sc.nextInt();
        System.out.print("Enter day of month>");
        int day = sc.nextInt();

        LocalDate date = LocalDate.of(year, month, day);
        return date;
    }

    private Airport selectAirport(List<Airport> airports) {
        for (int i = 1; i <= airports.size(); i++) {
            System.out.println(i + ": " + airports.get(i - 1).getAirportName());
        }
        int answer = sc.nextInt() - 1;
        return airports.remove(answer);
    }

    private int displayDirectFlightSchedules(Airport departureAirport, Airport destinationAirport, LocalDate departureDate, Integer numPassengers, FlightType flightPreference,
            List<FlightSchedule> flightSchedules, int index) {

        List<FlightSchedule> flightSchedulesOnDate = flightScheduleSessionBeanRemote.getFlightSchedules(departureAirport,
                destinationAirport, departureDate, numPassengers);
        if (flightSchedulesOnDate.size() > 0) {
            System.out.println("*** Flight schedules departing on " + departureDate.toString() + " ***");
            index = addToFlightSchedules(flightSchedules, flightSchedulesOnDate, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesThreeDaysBefore = flightScheduleSessionBeanRemote
                .getFlightSchedules(departureAirport, destinationAirport, departureDate.minusDays(3), numPassengers);
        if (flightSchedulesThreeDaysBefore.size() > 0) {
            System.out.println("*** Flight schedules departing on " + departureDate.minusDays(3).toString() + " ***");
            index = addToFlightSchedules(flightSchedules, flightSchedulesThreeDaysBefore, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesTwoDaysBefore = flightScheduleSessionBeanRemote
                .getFlightSchedules(departureAirport, destinationAirport, departureDate.minusDays(2), numPassengers);
        if (flightSchedulesTwoDaysBefore.size() > 0) {
            System.out.println("*** Flight schedules departing on " + departureDate.minusDays(2).toString() + " ***");
            index = addToFlightSchedules(flightSchedules, flightSchedulesTwoDaysBefore, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesOneDayBefore = flightScheduleSessionBeanRemote
                .getFlightSchedules(departureAirport, destinationAirport, departureDate.minusDays(1), numPassengers);
        if (flightSchedulesOneDayBefore.size() > 0) {
            System.out.println("*** Flight schedules departing on " + departureDate.minusDays(1).toString() + " ***");
            index = addToFlightSchedules(flightSchedules, flightSchedulesOneDayBefore, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesOneDayAfter = flightScheduleSessionBeanRemote
                .getFlightSchedules(departureAirport, destinationAirport, departureDate.plusDays(1), numPassengers);
        if (flightSchedulesOneDayAfter.size() > 0) {
            System.out.println("*** Flight schedules departing on " + departureDate.plusDays(1).toString() + " ***");
            index = addToFlightSchedules(flightSchedules, flightSchedulesOneDayAfter, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesTwoDaysAfter = flightScheduleSessionBeanRemote
                .getFlightSchedules(departureAirport, destinationAirport, departureDate.plusDays(2), numPassengers);
        if (flightSchedulesTwoDaysAfter.size() > 0) {
            System.out.println("*** Flight schedules departing on " + departureDate.plusDays(2).toString() + " ***");
            index = addToFlightSchedules(flightSchedules, flightSchedulesTwoDaysAfter, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesThreeDaysAfter = flightScheduleSessionBeanRemote
                .getFlightSchedules(departureAirport, destinationAirport, departureDate.plusDays(3), numPassengers);
        if (flightSchedulesThreeDaysAfter.size() > 0) {
            System.out.println("*** Flight schedules departing on " + departureDate.plusDays(3).toString() + " ***");
            index = addToFlightSchedules(flightSchedules, flightSchedulesThreeDaysAfter, index, numPassengers);
        }

        /*
        if (flightPreference == FlightType.DIRECT) {
            System.out.print("Select flight schedule> ");
            int answer = sc.nextInt();
            return answer - 1;
        }
         */
        return index - 1;
    }

    private int addToFlightSchedules(List<FlightSchedule> flightSchedules,
            List<FlightSchedule> flightSchedulesToBeAdded, int index, int numPassengers) {
        for (FlightSchedule f : flightSchedulesToBeAdded) {
            System.out.println((index + 1) + ": depature time: " + f.getDepartureDateTime().getHour() + ":"
                    + f.getDepartureDateTime().getMinute() + "; arrival time: " + f.getArrivalDateTime().getHour()
                    + ":" + f.getArrivalDateTime().getMinute());
            List<SeatInventory> seatInventories = f.getSeatInventories();
            FlightSchedulePlan flightSchedulePlan = f.getFlightSchedulePlan();
            List<Fare> fares = flightSchedulePlan.getFares();
            for (SeatInventory s : seatInventories) {
                if (s.getAvailableSeats() > 0) {
                    System.out.println(s.getCabinClassConfiguration().getCabinType() + ": " + s.getAvailableSeats() + " seats available");
                    Fare fare = null;
                    for (Fare fr : fares) {
                        if (fr.getCabinType() == s.getCabinClassConfiguration().getCabinType()) {
                            fare = fr;
                            break;
                        }
                    }
                    System.out.println("Price per passenger: " + fare.getFareAmount() + "; " + "Total amount: " + (fare.getFareAmount() * numPassengers));
                }
            }
            flightSchedules.add(f);
            index++;
        }
        return index;
    }

    private int addToConnectingFlightSchedules(List<FlightSchedule> flightSchedules,
            List<FlightSchedule> connectingFlightSchedulesToBeAdded, int index, int numPassengers) {
        for (int i = 0; i < connectingFlightSchedulesToBeAdded.size(); i += 2) {
            FlightSchedule original = connectingFlightSchedulesToBeAdded.get(i);
            FlightSchedule connector = connectingFlightSchedulesToBeAdded.get(i + 1);
            System.out.println((index + 1) + ":");
            System.out.println("Starting flight route: " + original.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIATACode()
                    + "-" + original.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIATACode());
            System.out.println("Depature time: " + original.getDepartureDateTime().getHour() + ":" + original.getDepartureDateTime()
                    + "; arrival time: " + original.getArrivalDateTime().getHour() + ":" + original.getArrivalDateTime().getMinute());
            List<SeatInventory> seatInventories = original.getSeatInventories();
            FlightSchedulePlan flightSchedulePlan = original.getFlightSchedulePlan();
            List<Fare> fares = flightSchedulePlan.getFares();
            for (SeatInventory s : seatInventories) {
                if (s.getAvailableSeats() > 0) {
                    System.out.println(s.getCabinClassConfiguration().getCabinType() + ": " + s.getAvailableSeats() + " seats available");
                    Fare fare = null;
                    for (Fare fr : fares) {
                        if (fr.getCabinType() == s.getCabinClassConfiguration().getCabinType()) {
                            fare = fr;
                            break;
                        }
                    }
                    System.out.println("Price per passenger: " + fare.getFareAmount() + "; " + "Total amount: " + (fare.getFareAmount() * numPassengers));
                }
            }
            System.out.println("Connecting flight route: " + connector.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIATACode()
                    + "-" + connector.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIATACode());
            System.out.println("Depature time: " + connector.getDepartureDateTime().getHour() + ":" + original.getDepartureDateTime()
                    + "; arrival time: " + connector.getArrivalDateTime().getHour() + ":" + original.getArrivalDateTime().getMinute());
            seatInventories = connector.getSeatInventories();
            flightSchedulePlan = connector.getFlightSchedulePlan();
            fares = flightSchedulePlan.getFares();
            for (SeatInventory s : seatInventories) {
                if (s.getAvailableSeats() > 0) {
                    System.out.println(s.getCabinClassConfiguration().getCabinType() + ": " + s.getAvailableSeats() + " seats available");
                    Fare fare = null;
                    for (Fare fr : fares) {
                        if (fr.getCabinType() == s.getCabinClassConfiguration().getCabinType()) {
                            fare = fr;
                            break;
                        }
                    }
                    System.out.println("Price per passenger: " + fare.getFareAmount() + "; " + "Total amount: " + (fare.getFareAmount() * numPassengers));
                }
            }
            flightSchedules.add(original);
            flightSchedules.add(connector);
            index++;
        }
        return index;
    }

    private int displayConnectFlightSchedules(Airport departureAirport, Airport destinationAirport,
            LocalDate departureDate, int numPassengers, FlightType flightPreference,
            List<FlightSchedule> flightSchedules, int index) {

        List<FlightSchedule> flightSchedulesOnDate = flightScheduleSessionBeanRemote.getConnectingFlightSchedules(departureAirport,
                destinationAirport, departureDate, numPassengers);
        if (flightSchedulesOnDate.size() > 0) {
            System.out.println("*** Connecting flight schedules departing on " + departureDate.toString() + " ***");
            index = addToConnectingFlightSchedules(flightSchedules, flightSchedulesOnDate, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesThreeDaysBefore = flightScheduleSessionBeanRemote.getConnectingFlightSchedules(departureAirport,
                destinationAirport, departureDate.minusDays(3), numPassengers);
        if (flightSchedulesThreeDaysBefore.size() > 0) {
            System.out.println("*** Connecting flight schedules departing on " + departureDate.minusDays(3).toString() + " ***");
            index = addToConnectingFlightSchedules(flightSchedules, flightSchedulesThreeDaysBefore, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesTwoDaysBefore = flightScheduleSessionBeanRemote.getConnectingFlightSchedules(departureAirport,
                destinationAirport, departureDate.minusDays(2), numPassengers);
        if (flightSchedulesTwoDaysBefore.size() > 0) {
            System.out.println("*** Connecting flight schedules departing on " + departureDate.minusDays(2).toString() + " ***");
            index = addToConnectingFlightSchedules(flightSchedules, flightSchedulesTwoDaysBefore, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesOneDayBefore = flightScheduleSessionBeanRemote.getConnectingFlightSchedules(departureAirport,
                destinationAirport, departureDate.minusDays(1), numPassengers);
        if (flightSchedulesOneDayBefore.size() > 0) {
            System.out.println("*** Connecting flight schedules departing on " + departureDate.minusDays(1).toString() + " ***");
            index = addToConnectingFlightSchedules(flightSchedules, flightSchedulesOneDayBefore, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesOneDayAfter = flightScheduleSessionBeanRemote.getConnectingFlightSchedules(departureAirport,
                destinationAirport, departureDate.plusDays(1), numPassengers);
        if (flightSchedulesOneDayAfter.size() > 0) {
            System.out.println("*** Connecting flight schedules departing on " + departureDate.plusDays(1).toString() + " ***");
            index = addToConnectingFlightSchedules(flightSchedules, flightSchedulesOneDayAfter, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesTwoDaysAfter = flightScheduleSessionBeanRemote.getConnectingFlightSchedules(departureAirport,
                destinationAirport, departureDate.plusDays(2), numPassengers);
        if (flightSchedulesTwoDaysAfter.size() > 0) {
            System.out.println("*** Connecting flight schedules departing on " + departureDate.plusDays(2).toString() + " ***");
            index = addToConnectingFlightSchedules(flightSchedules, flightSchedulesTwoDaysAfter, index, numPassengers);
        }

        List<FlightSchedule> flightSchedulesThreeDaysAfter = flightScheduleSessionBeanRemote.getConnectingFlightSchedules(departureAirport,
                destinationAirport, departureDate.plusDays(3), numPassengers);
        if (flightSchedulesThreeDaysAfter.size() > 0) {
            System.out.println("*** Connecting flight schedules departing on " + departureDate.plusDays(3).toString() + " ***");
            index = addToConnectingFlightSchedules(flightSchedules, flightSchedulesTwoDaysAfter, index, numPassengers);
        }

        return index - 1;
    }

}
