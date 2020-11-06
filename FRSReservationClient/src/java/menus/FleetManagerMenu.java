/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menus;

import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import entity.AircraftType;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;
import entity.AircraftConfiguration;
import enumeration.CabinType;
import exception.MaximumCapacityExceeded;
import java.util.ArrayList;

/**
 *
 * @author Antho
 */
public class FleetManagerMenu {

    private Employee employee;
    private Scanner sc = new Scanner(System.in);

    public FleetManagerMenu() {
    }

    public FleetManagerMenu(Employee employee) {
        this.employee = employee;
    }

    public void run(AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote,
            AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote,
            CabinClassSessionBeanRemote cabinClassSessionBeanRemote) {
        int response = 0;

        while (true) {
            System.out.println("*** Flight Reservation System (v1.0) ***\n");
            System.out.println("You are logged in as " + employee.getName() + " with "
                    + employee.getEmployeeType()
                    + " rights\n");
            System.out.println("1: Create new aircraft configuration");
            System.out.println("2: View all aircraft configurations");
            System.out.println("3: View aircraft configuration details");
            //System.out.println("4: Issue replacement ATM card");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    List<AircraftType> aircraftTypes = aircraftTypeSessionBeanRemote.getAllAircraftTypes();
                    AircraftType aircraftType = selectAircraftType(aircraftTypes);
                    int maxCapacity = aircraftType.getMaxPassengers();
                    System.out.println("Aircraft type selected: " + aircraftType.getAircraftTypeName());
                    sc.nextLine();
                    System.out.print("Enter name of aircraft configuration> ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter number of cabin classes> ");
                    Integer numCabinClasses = sc.nextInt();
                    Long aircraftConfigurationId = aircraftConfigurationSessionBeanRemote.createAircraftConfiguration(aircraftType, name, numCabinClasses);
                    System.out.println("New aircraft configuration with ID " + aircraftConfigurationId + " created succesfully!");
                    System.out.println("Create cabin classes");
                    try {
                        createCabinClasses(numCabinClasses, aircraftConfigurationId, cabinClassSessionBeanRemote, maxCapacity);
                    } catch (MaximumCapacityExceeded ex) {
                        System.out.println("Error creating cabin classes: " + ex.getMessage());
                    }
                } else if (response == 2) {
                    List<AircraftConfiguration> aircraftConfigurations = aircraftConfigurationSessionBeanRemote.viewAllAircraftConfigurations();
                    for (int i = 0; i < aircraftConfigurations.size(); i++) {
                        System.out.println((i + 1) + ": Aircraft type: " + aircraftConfigurations.get(i).getAircraftType().getAircraftTypeName()
                                + "; Configuration name: " + aircraftConfigurations.get(i).getName());
                    }
                } else if (response == 3) {
                    sc.nextLine();
                    System.out.print("Enter aircraft configuration name> ");
                    String name = sc.nextLine();
                    AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanRemote.viewAircraftConfigurationDetails(name);
                    System.out.println("Aircraft configuration ID: " + aircraftConfiguration.getAircraftConfigurationID()
                            + "; Aircraft configuration name: " + aircraftConfiguration.getName() + "; Number of cabin classes: "
                            + aircraftConfiguration.getNumCabinClasses() + "; Aircraft type: "
                            + aircraftConfiguration.getAircraftType().getAircraftTypeName());
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

    private AircraftType selectAircraftType(List<AircraftType> aircraftTypes) {
        System.out.println("*** Select an aircraft type ***");
        for (int i = 0; i < aircraftTypes.size(); i++) {
            System.out.println((i + 1) + ": " + aircraftTypes.get(i).getAircraftTypeName());
        }
        System.out.print("> ");
        int response = sc.nextInt();
        return aircraftTypes.get(response - 1);
    }

    private void createCabinClasses(Integer numCabinClasses, Long aircraftConfigurationId,
            CabinClassSessionBeanRemote cabinClassSessionBeanRemote, int maxCapacity) throws MaximumCapacityExceeded {
        List<CabinType> cabinTypes = new ArrayList<>();
        cabinTypes.add(CabinType.FIRST_CLASS);
        cabinTypes.add(CabinType.BUSINESS);
        cabinTypes.add(CabinType.PREMIUM_ECONOMY);
        cabinTypes.add(CabinType.ECONOMY);
        int startRowNum = 1;
        int numberOfSeats = 0;
        for (int i = 0; i < numCabinClasses; i++) {
            CabinType cabinType = selectCabinType(cabinTypes);
            System.out.print("Enter number of aisles> ");
            int numAisles = sc.nextInt();
            System.out.print("Enter number of rows> ");
            int numRows = sc.nextInt();
            System.out.print("Enter number of seats abreast> ");
            int numSeatsAbreast = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter seating configuration per column> ");
            String seatConfiguration = sc.nextLine().trim();
            numberOfSeats += numRows * numSeatsAbreast;
            if (numberOfSeats > maxCapacity) {
                throw new MaximumCapacityExceeded("The maximum capacity for the aircraft type is exceeded!");
            }
            Long cabinClassConfigurationID = cabinClassSessionBeanRemote
                    .createCabinClass(numAisles, numRows, numSeatsAbreast,
                            seatConfiguration, cabinType, aircraftConfigurationId, startRowNum);
            System.out.println("Cabin class with ID " + cabinClassConfigurationID + " successfully created!");
            startRowNum += numRows;
        }
    }

    private CabinType selectCabinType(List<CabinType> cabinTypes) {
        System.out.println("*** Select cabin type ***");
        for (int i = 0; i < cabinTypes.size(); i++) {
            System.out.println((i + 1) + ": " + cabinTypes.get(i));
        }
        System.out.print("> ");
        int response = sc.nextInt();
        return cabinTypes.remove(response - 1);
    }

}
