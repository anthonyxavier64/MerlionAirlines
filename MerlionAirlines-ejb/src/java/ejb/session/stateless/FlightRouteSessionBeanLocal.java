/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import exception.FlightRouteAlreadyExistException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Antho
 */
@Local
public interface FlightRouteSessionBeanLocal {

    Long createFlightRoute(Airport origin, Airport destination) throws FlightRouteAlreadyExistException;

    Long createComplementaryFlightRoute(Airport origin, Airport destination, Long originalFlightRouteId);

    List<FlightRoute> viewAllFlightRoutes();

    void deleteFlightRoute(Long flightRouteId);

}
