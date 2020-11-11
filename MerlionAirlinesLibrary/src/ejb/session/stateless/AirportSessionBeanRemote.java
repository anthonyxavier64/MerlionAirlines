/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import exception.AirportDoesNotExistException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Antho
 */
@Remote
public interface AirportSessionBeanRemote {

    Long createAirport(String airportName, String airportCode, String country, String city, String state);

    List<Airport> viewAllAirports();

    public Airport retrieveAirportByAirportName(String name) throws AirportDoesNotExistException;
}
