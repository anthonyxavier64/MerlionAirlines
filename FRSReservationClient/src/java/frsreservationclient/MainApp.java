/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Airport;
import entity.Customer;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author yappeizhen
 */
public class MainApp {

    Scanner sc = new Scanner(System.in);
    AirportSessionBeanRemote airportSessionBeanRemote;
    CustomerSessionBeanRemote customerSessionBeanRemote;
    Customer customer = null;

    MainApp() {
    }

    MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;

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
                        try {
                        searchFlight();
                    } catch (InvalidCabinClassException | AirportDoesNotExistException | InvalidTripTypeException ex) {
                        System.out.println("Search flight unsuccessful! " + ex.getMessage());
                    }

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

    public void searchFlight() throws AirportDoesNotExistException, InvalidTripTypeException, InvalidCabinClassException {

        System.out.println("*** Search flight ***\n");
        TripType tripType = processTripType();
        System.out.println();
        System.out.print("Enter departure airport name>");
        String depAirportName = sc.nextLine().trim();
        Airport departureAirport = processAirport(depAirportName);
        System.out.print("Enter destinaton airport name>");
        String destAirportName = sc.nextLine().trim();
        Airport destinationAirport = processAirport(destAirportName);
        System.out.print("Enter departure date>");
        LocalDate date = readDate();
        LocalDateTime departureDate = date.atStartOfDay();
        LocalDateTime returnDate = null;
        if (tripType == tripType.ROUNDTRIP) {
            System.out.print("Enter return date>");
            LocalDate returnDay = readDate();
            returnDate = returnDay.atStartOfDay();
        }
        System.out.print("Enter number of passengers>");
        Integer numPassengers = sc.nextInt();
        System.out.print("Any preference for flight type? (Y/N)>");
        FlightType flightPreference = indicateFlightPreference();
        System.out.print("Any preference for cabin class? (Y/N)>");
        CabinType cabinTypePreference = indicateCabinClassPreference();
        
        if (flightPreference != FlightType.CONNECTING) {
            flightSessionBeanRemote.searchDirectFlightOnDay(tripType, departureAirport, destinationAirport, departureDate, returnDate, numPassengers, cabinTypePreference, cabinTypePreference);
            
        }
    }

    private FlightType indicateFlightPreference() throws InvalidChoiceException {
        char hasPreference = sc.nextLine().charAt(0);

        while (hasPreference != 'Y' && hasPreference != 'N') {
            System.out.println("Invalid response! Enter Y/N");
            System.out.print("> ");
            hasPreference = sc.nextLine().charAt(0);
        }

        if (hasPreference == 'Y') {
            int prefer = 0;
            System.out.println("Select preferred cabin class: ");
            System.out.println("1: Direct flight");
            System.out.println("2: Connecting flight");
            System.out.print(">");
            prefer = sc.nextInt();
            if (prefer == 1) {
                return FlightType.DIRECT;
            }
            if (prefer == 2) {
                return FlightType.CONNECTING;

            } else {
                throw new InvalidChoiceException("No such route type!");
            }
        }
        return null;
    }

    private CabinType indicateCabinClassPreference() throws InvalidCabinClassException {
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
            System.out.print(">");
            prefer = sc.nextInt();
            if (prefer == 1) {
                return CabinType.FIRST_CLASS;
            }
            if (prefer == 2) {
                return CabinType.BUSINESS;
            }
            if (prefer == 3) {
                return CabinType.PREMIUM_ECONOMY;
            }
            if (prefer == 4) {
                return CabinType.ECONOMY;
            } else {
                throw new InvalidCabinClassException("No such cabin class!");
            }
        }
        return null;
    }

    private TripType processTripType() throws InvalidTripTypeException {
        System.out.println("Enter trip type:");
        System.out.println("1: One-way");
        System.out.println("2: Round-trip");
        System.out.print(">");
        int response = sc.nextInt();
        if (response == 1) {
            return TripType.ONEWAY;
        }
        if (response == 2) {
            return TripType.ROUNDTRIP;
        }
        throw new InvalidTripTypeException("No such trip type!");
    }

    private Airport processAirport(String airportName) throws AirportDoesNotExistException {
        Airport airport = airportSessionBeanRemote.retrieveAirportByAirportName(airportName);
        return airport;
    }

    private LocalDate readDate() {
        LocalDateTime departureDateTime;
        while (true) {
            String dateString = sc.nextLine();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            try {
                LocalDate departureDate = LocalDate.parse(dateString, format);
                return departureDate;
            } catch (DateTimeParseException ex) {
                System.out.println("Incorrect date format! Try again with the format (dd/MM/yyyy HH:mm)");
                System.out.print(">");
            }
        }
    }
}
