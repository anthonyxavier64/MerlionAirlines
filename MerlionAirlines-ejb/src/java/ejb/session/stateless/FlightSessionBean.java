/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import entity.Flight;
import entity.FlightRoute;
import enumeration.TripType;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import exception.FlightAlreadyExistException;
import exception.FlightDoesNotExistException;
import java.util.ArrayList;
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
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

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
    public Flight retrieveFlightByFlightNumber(String flightNumber) throws FlightDoesNotExistException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :flightNumber");
        query.setParameter("flightNumber", "ML" + flightNumber);
        try {
            Flight flight = (Flight) query.getSingleResult();
            if (!flight.isEnabled()) {
                throw new FlightDoesNotExistException("Flight has been disabled!");
            }
            List<FlightSchedulePlan> flightSchedulePlans = flight.getFlightSchedulePlans();
            for (FlightSchedulePlan f : flightSchedulePlans) {
                List<FlightSchedule> flightSchedules = f.getFlightSchedules();
                for (FlightSchedule fs : flightSchedules) {
                    List<SeatInventory> seatInventries = fs.getSeatInventories();
                    for (SeatInventory sit : seatInventries) {
                        sit.getSeats().size();
                    }
                }
            }
            return flight;
        } catch (NoResultException ex) {
            throw new FlightDoesNotExistException("Flight number does not exists!");
        }
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
        complementaryFlight.setComplementaryFlight(originalFlight);
        complementaryFlight.setTwoWay(true);
        return complementaryFlight.getFlightID();
    }

    @Override
    public List<Flight> viewAllFlights() {
        Query query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC");
        List<Flight> flights = query.getResultList();
        List<Flight> flightsFiltered = new ArrayList<>();
        for (Flight f : flights) {
            if (!f.isEnabled()) {
                continue;
            }
            
            f.getFlightRoute().getOrigin();
            f.getFlightRoute().getDestination();
            f.getAircraftConfiguration().getCabinClassConfigurations().size();
            f.getAircraftConfiguration().getAircraftType();
            f.getFlightRoute().getComplementaryFlightRoute();
            f.getComplementaryFlight();
            f.getFlightSchedulePlans().size();
            for(CabinClassConfiguration ccc : f.getAircraftConfiguration().getCabinClassConfigurations()) {
                ccc.getSeatNumbers();
            }
            
            if (f.getComplementaryFlight() != null && !flightsFiltered.contains(f.getComplementaryFlight())) {
                flightsFiltered.add(f);
                flightsFiltered.add(f.getComplementaryFlight());
            } else if (f.getComplementaryFlight() == null) {
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
            if (!flight.isEnabled()) {
                throw new FlightDoesNotExistException("Flight has been disabled1");
            }
            flight.getAircraftConfiguration().getCabinClassConfigurations().size();
            flight.getFlightRoute();
            return flight;
        } catch (NoResultException ex) {
            throw new FlightDoesNotExistException("Flight number does not exists!");
        }
    }

    @Override
    public Flight updateFlightNumber(String flightNumber, String newFlightNumber) throws FlightDoesNotExistException {
        Flight flight = retrieveFlightByFlightNumber(flightNumber);
        flight.setFlightNumber("ML" + newFlightNumber);
        em.persist(flight);
        em.flush();
        return flight;
    }

    @Override
    public Flight updateFlightRoute(String flightNumber, FlightRoute flightRoute) throws FlightDoesNotExistException {
        Flight flight = retrieveFlightByFlightNumber(flightNumber);
        flight.setFlightRoute(flightRoute);
        em.persist(flight);
        em.flush();
        return flight;
    }

    @Override
    public Flight updateAircraftConfiguration(String flightNumber, AircraftConfiguration newAircraftConfig) throws FlightDoesNotExistException {
        Flight flight = retrieveFlightByFlightNumber(flightNumber);
        flight.setAircraftConfiguration(newAircraftConfig);
        em.persist(flight);
        em.flush();
        return flight;
    }

    @Override
    public int removeFlight(String flightNumber) {
        boolean hasBeenBooked = false;
        try {
            Flight flight = retrieveFlightByFlightNumber(flightNumber);

            List<FlightSchedulePlan> fsps = flight.getFlightSchedulePlans();
            for (FlightSchedulePlan fsp : fsps) {
                for (FlightSchedule fs : fsp.getFlightSchedules()) {
                    if (fs.getFlightReservations().size() != 0) {
                        hasBeenBooked = true;
                        break;
                    }
                    if (hasBeenBooked) {
                        break;
                    }
                }
            }
            
            if (!hasBeenBooked) {
                // Diassociate flight from flight route
                FlightRoute flightRoute = flight.getFlightRoute();
                flightRoute.getFlights().remove(flight);

                if (flight.getComplementaryFlight() != null) {
                    Flight complementaryFlight = flight.getComplementaryFlight();
                    complementaryFlight.setComplementaryFlight(null);
                    complementaryFlight.setTwoWay(false);
                }

                em.remove(flight);
                em.flush();
                return 2;

            } else {
                flight.setEnabled(false);
                Flight complementaryFlight = flight.getComplementaryFlight();
                complementaryFlight.setComplementaryFlight(null);
                complementaryFlight.setTwoWay(false);
                return 1;
            }

        } catch (FlightDoesNotExistException ex) {
            return 0;
        }
    }
}
