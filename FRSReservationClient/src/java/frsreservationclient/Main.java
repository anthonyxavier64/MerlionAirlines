/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frsreservationclient;

import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FareSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.PassengerSessionBeanRemote;
import ejb.session.stateless.SeatSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author yappeizhen
 */
public class Main {

    @EJB
    private static FareSessionBeanRemote fareSessionBean;
    @EJB
    private static PassengerSessionBeanRemote passengerSessionBean;
    @EJB
    private static SeatSessionBeanRemote seatSessionBean;
    @EJB
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBean;
    @EJB
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    @EJB
    private static AirportSessionBeanRemote airportSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(customerSessionBeanRemote, airportSessionBeanRemote, flightScheduleSessionBeanRemote, flightSchedulePlanSessionBean, passengerSessionBean, seatSessionBean, fareSessionBean);
        mainApp.run();
    }

}
