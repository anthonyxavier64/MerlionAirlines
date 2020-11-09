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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBean;

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createFlight(String flightNumber, Long flightRouteId, Long aircraftConfigurationId) throws FlightAlreadyExistException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", "ML" + flightNumber);
        try {
            query.getSingleResult();
        } catch (NoResultException ex) {
            FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);
            AircraftConfiguration aircraftConfiguration = em.find(AircraftConfiguration.class, aircraftConfigurationId);
            Flight flight = new Flight(flightNumber);
            flight.setFlightRoute(flightRoute);
            flight.setAircraftConfiguration(aircraftConfiguration);
            em.persist(flight);
            flightRoute.getFlights().add(flight);
            em.flush();
            return flight.getFlightID();
        }
        throw new FlightAlreadyExistException("Flight already exists!");
    }

    @Override
    public Long createComplementaryFlight(String flightNumber, long flightRouteId, long aircraftConfigurationId, long originalFlightId) {
        Flight originalFlight = em.find(Flight.class, originalFlightId);
        originalFlight.setTwoWay(true);
        Flight complementaryFlight;
        try {
            Long complementaryFlightId = createFlight(flightNumber, flightRouteId, aircraftConfigurationId);
            complementaryFlight = em.find(Flight.class, complementaryFlightId);
        } catch (FlightAlreadyExistException ex) {
            Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
            query.setParameter("flightNumber", "ML" + flightNumber);
            complementaryFlight = (Flight) query.getSingleResult();
        }
        originalFlight.setComplementaryFlight(complementaryFlight);
        complementaryFlight.setTwoWay(true);
        return complementaryFlight.getFlightID();
    }

    @Override
    public List<Flight> viewAllFlights() {
        Query query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC");
        List<Flight> flights = query.getResultList();
        List<Flight> flightsFiltered = new ArrayList<>();
        for (Flight f : flights) {
            if (f.getComplementaryFlight() != null && f.isTwoWay()) {
                flightsFiltered.add(f);
                flightsFiltered.add(f.getComplementaryFlight());
            } else if (f.getComplementaryFlight() == null && !f.isTwoWay()) {
                flightsFiltered.add(f);
            }
        }
        return flightsFiltered;
    }

    @Override
    public Flight viewFlightDetails(String flightNumber) throws FlightDoesNotExistException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", "ML" + flightNumber);
        try {
            Flight flight = (Flight) query.getSingleResult();
            flight.getAircraftConfiguration().getCabinClassConfigurations().size();
            flight.getFlightRoute();
            return flight;
        } catch (NoResultException ex) {
            throw new FlightDoesNotExistException("Flight number does not exists!");
        }
    }

}
