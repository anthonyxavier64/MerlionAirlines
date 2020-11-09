/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import entity.Airport;
import entity.Employee;
import entity.FlightRoute;
import exception.FlightRouteAlreadyExistException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Antho
 */
public class RoutePlannerMenu {

    private Employee employee;
    private Scanner sc = new Scanner(System.in);

    public RoutePlannerMenu() {
    }

    public RoutePlannerMenu(Employee employee) {
        this.employee = employee;
    }

    public void run(AirportSessionBeanRemote airportSessionBeanRemote,
            FlightRouteSessionBeanRemote flightRouteSessionBeanRemote) {
        int response = 0;

        while (true) {
            System.out.println("*** Flight Reservation System (v1.0) ***\n");
            System.out.println("You are logged in as " + employee.getName() + " with "
                    + employee.getEmployeeType()
                    + " rights\n");
            System.out.println("1: Create flight route");
            System.out.println("2: View all flight routes");
            System.out.println("3: Delete flight route");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    sc.nextLine();
                    List<Airport> airports = airportSessionBeanRemote.viewAllAirports();
                    Airport origin = selectOriginAirport(airports);
                    airports.remove(origin);
                    Airport destination = selectDestinationAirport(airports);
                    try {
                        Long flightRouteId = flightRouteSessionBeanRemote.createFlightRoute(origin, destination);
                        System.out.println("Flight route with ID " + flightRouteId + " successfully created!");
                        System.out.println("Create a complementary flight, Y/N>");
                        sc.nextLine();
                        String answer = sc.nextLine().toLowerCase();
                        if (answer.equals("y")) {
                            Long complementaryFlightId = flightRouteSessionBeanRemote.createComplementaryFlightRoute(destination, origin, flightRouteId);
                            System.out.println("Complementary flight route with ID " + complementaryFlightId + " successfully created!");
                        }
                    } catch (FlightRouteAlreadyExistException ex) {
                        System.out.println("Error creating flight route: " + ex.getMessage());
                    }
                } else if (response == 2) {
                    viewAllFlightRoutes(flightRouteSessionBeanRemote);
                } else if (response == 3) {
                    deleteFlightRoute(flightRouteSessionBeanRemote);
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

    private Airport selectOriginAirport(List<Airport> airports) {
        System.out.println("*** Select Origin Airport ***");
        for (int i = 0; i < airports.size(); i++) {
            System.out.println((i + 1) + ": " + airports.get(i).getAirportName());
        }
        System.out.print("> ");
        int response = sc.nextInt();
        return airports.get(response - 1);
    }

    private Airport selectDestinationAirport(List<Airport> airports) {
        System.out.println("*** Select Destination Airport ***");
        for (int i = 0; i < airports.size(); i++) {
            System.out.println((i + 1) + ": " + airports.get(i).getAirportName());
        }
        System.out.print("> ");
        int response = sc.nextInt();
        return airports.get(response - 1);
    }

    private List<FlightRoute> viewAllFlightRoutes(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote) {
        List<FlightRoute> flightRoutes = flightRouteSessionBeanRemote.viewAllFlightRoutes();
        for (int i = 0; i < flightRoutes.size(); i++) {
            FlightRoute flightRoute = flightRoutes.get(i);
            System.out.println((i + 1) + ": " + flightRoute.getOrigin().getIATACode() + "-" + flightRoute.getDestination().getIATACode());
        }
        return flightRoutes;
    }

    private void deleteFlightRoute(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote) {
        List<FlightRoute> flightRoutes = viewAllFlightRoutes(flightRouteSessionBeanRemote);
        System.out.print("> ");
        int answer = sc.nextInt() - 1;
        FlightRoute flightRoute = flightRoutes.get(answer);
        sc.nextLine();
        String ans;
        Long flightRouteId = flightRoute.getFlightRouteId();

        System.out.print("Confirm deletion? Y/N> ");
        ans = sc.nextLine().toLowerCase();
        if (ans.equals("y")) {
            flightRouteSessionBeanRemote.deleteFlightRoute(flightRouteId);
        }
    }

}
