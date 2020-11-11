/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import exception.AirportDoesNotExistException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Antho
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanRemote, AirportSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createAirport(String airportName, String airportCode, String country, String city, String state) {
        Airport newAirport = new Airport(airportName, airportCode, country, city, state);
        em.persist(newAirport);
        em.flush();
        return newAirport.getAirportID();
    }

    @Override
    public List<Airport> viewAllAirports() {
        Query query = em.createQuery("SELECT a FROM Airport a");
        List<Airport> airports = query.getResultList();
        return airports;
    }
    
    public Airport retrieveAirportByAirportName(String name) throws AirportDoesNotExistException {
        Query query = em.createQuery("SELECT a FROM Airport a WHERE a.airportName = ?1");
        query.setParameter(1, name);

        try {
            Airport airport = (Airport) query.getSingleResult();
            return airport;
        } catch (NoResultException ex) {
            throw new AirportDoesNotExistException("Airport name does not exists!");
        }
    }
}
