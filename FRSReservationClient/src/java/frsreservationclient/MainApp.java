/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import enumeration.EmployeeType;
import exception.EmployeeNotFoundException;
import exception.InvalidLoginCredentialException;
import java.util.Scanner;
import menus.AdministratorMenu;
import menus.FleetManagerMenu;
import menus.RoutePlannerMenu;
import menus.SalesManagerMenu;
import menus.ScheduleManagerMenu;
import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;

/**
 *
 * @author Antho
 */
public class MainApp {

    private Employee employee;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private CabinClassSessionBeanRemote cabinClassSessionBeanRemote;
    private Scanner sc = new Scanner(System.in);

    public MainApp() {
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
            CabinClassSessionBeanRemote cabinClassSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.aircraftTypeSessionBeanRemote = aircraftTypeSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.cabinClassSessionBeanRemote = cabinClassSessionBeanRemote;
    }

    public void run() {
        int response = 0;

        while (true) {
            System.out.println("*** Welcome to Flight Reservation System (v1.0) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                    System.out.println("Login successfully!\n");
                    if (employee.getEmployeeType() == EmployeeType.FLEET_MANAGER) {
                        FleetManagerMenu menu = new FleetManagerMenu(employee);
                        menu.run(aircraftTypeSessionBeanRemote, aircraftConfigurationSessionBeanRemote,
                                cabinClassSessionBeanRemote);
                    } else if (employee.getEmployeeType() == EmployeeType.ROUTE_PLANNER) {
                        RoutePlannerMenu menu = new RoutePlannerMenu(employee);
                        menu.run();
                    } else if (employee.getEmployeeType() == EmployeeType.SCHEDULE_MANAGER) {
                        ScheduleManagerMenu menu = new ScheduleManagerMenu(employee);
                        menu.run();
                    } else if (employee.getEmployeeType() == EmployeeType.SALES_MANAGER) {
                        SalesManagerMenu menu = new SalesManagerMenu(employee);
                        menu.run();
                    } else if (employee.getEmployeeType() == EmployeeType.ADMINISTRATOR) {
                        AdministratorMenu menu = new AdministratorMenu(employee);
                        menu.run();
                    }
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

    private void doLogin() throws InvalidLoginCredentialException {
        System.out.println("*** Flight Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        String userName = sc.next().trim();
        System.out.print("Enter password> ");
        String password = sc.next().trim();
        System.out.println();
        if (userName.length() > 0 && password.length() > 0) {
            try {
                employee = employeeSessionBeanRemote.login(userName, password);
            } catch (EmployeeNotFoundException ex) {
                throw new InvalidLoginCredentialException(ex.getMessage());
            }
        } else {
            throw new InvalidLoginCredentialException("Missing login credentials!");
        }
    }

}
