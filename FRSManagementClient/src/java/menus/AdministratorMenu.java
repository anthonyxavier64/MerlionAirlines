/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import ejb.session.stateless.AirportSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;

/**
 *
 * @author Antho
 */
public class AdministratorMenu {

    private Employee employee;
    private Scanner sc = new Scanner(System.in);

    public AdministratorMenu() {
    }

    public AdministratorMenu(Employee employee) {
        this.employee = employee;
    }

    public void run(AirportSessionBeanRemote airportSessionBeanRemote) {
        int response = 0;

        while (true) {
            System.out.println("*** Flight Reservation System (v1.0) ***\n");
            System.out.println("You are logged in as " + employee.getName() + " with "
                    + employee.getEmployeeType()
                    + " rights\n");
            System.out.println("1: Create new airport");
            //System.out.println("2: View all aircraft configurations");
            //System.out.println("3: View aircraft configuration details");
            //System.out.println("4: Create flight route");
            System.out.println("2: Logout\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    System.out.println("*** Create Airport Record ***\n");
                    sc.nextLine();
                    System.out.print("Enter airport name> ");
                    String airportName = sc.nextLine();
                    System.out.print("Enter airport IATA code> ");
                    String airportCode = sc.nextLine();
                    System.out.print("Enter airport country> ");
                    String country = sc.nextLine();
                    System.out.print("Enter airport city> ");
                    String city = sc.nextLine();
                    System.out.print("Enter airport state> ");
                    String state = sc.nextLine();
                    Long airportId = airportSessionBeanRemote.createAirport(airportName, airportCode, country, city, state);
                    System.out.println("Airport with ID " + airportId + " successfully created!");
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

}
