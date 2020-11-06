/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.CabinClassSessionBeanRemote;

/**
 *
 * @author yappeizhen
 */
public class Main {

    @EJB
    private static CabinClassSessionBeanRemote cabinClassSessionBeanRemote;
    @EJB
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    @EJB
    private static AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, aircraftTypeSessionBeanRemote, aircraftConfigurationSessionBeanRemote,
                cabinClassSessionBeanRemote);
        mainApp.run();
    }

}
