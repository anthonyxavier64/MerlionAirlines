/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import exception.FlightAlreadyExistException;
import exception.FlightDoesNotExistException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Antho
 */
@Local
public interface FlightSessionBeanLocal {

    Long createFlight(String flightNumber, Long flightRouteId, Long aircraftConfigurationId) throws FlightAlreadyExistException;

    List<Flight> viewAllFlights();

    Long createComplementaryFlight(String flightNumber, long flightRouteId, long aircraftConfigurationId, long originalFlightId);

    Flight viewFlightDetails(String flightNumber) throws FlightDoesNotExistException;

}
