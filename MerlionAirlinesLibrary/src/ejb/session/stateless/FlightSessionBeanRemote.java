/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import exception.FlightAlreadyExistException;
import exception.FlightDoesNotExistException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Antho
 */
@Remote
public interface FlightSessionBeanRemote {

    Long createFlight(String flightNumber, Long flightRouteId, Long aircraftConfigurationId) throws FlightAlreadyExistException;

    List<Flight> viewAllFlights();

    Long createComplementaryFlight(String flightNumber, long flightRouteId, long aircraftConfigurationId, long originalFlightId);

    Flight viewFlightDetails(String flightNumber) throws FlightDoesNotExistException;
    
    public Flight updateFlightNumber(String flightNumber, String newFlightNumber) throws FlightDoesNotExistException;

    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightDoesNotExistException;

    public Flight updateAircraftConfiguration(String flightNumber, AircraftConfiguration newAircraftConfig) throws FlightDoesNotExistException;

    public Flight updateFlightRoute(String flightNumber, FlightRoute flightRoute) throws FlightDoesNotExistException;
    
    public int removeFlight(String flightNumber);
}
