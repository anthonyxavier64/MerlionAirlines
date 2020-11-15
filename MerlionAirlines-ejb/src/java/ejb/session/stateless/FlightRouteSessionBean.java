/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import exception.FlightRouteAlreadyExistException;
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
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createFlightRoute(Airport origin, Airport destination) throws FlightRouteAlreadyExistException {
        Query query = em.createQuery("SELECT f FROM FlightRoute f WHERE f.origin = :origin AND f.destination = :destination");
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        try {
            query.getSingleResult();
        } catch (NoResultException ex) {
            FlightRoute flightRoute = new FlightRoute(origin, destination);
            em.persist(flightRoute);
            em.flush();
            return flightRoute.getFlightRouteId();
        }
        throw new FlightRouteAlreadyExistException("Flight route already exists!");
    }

    @Override
    public Long createComplementaryFlightRoute(Airport origin, Airport destination, Long originalFlightRouteId) {
        FlightRoute originalFlightRoute = em.find(FlightRoute.class, originalFlightRouteId); // exception here if original flight route does not exist
        originalFlightRoute.setTwoWay(true);
        FlightRoute complementaryFlightRoute;
        try {
            Long complementaryFlightRouteId = createFlightRoute(origin, destination);
            complementaryFlightRoute = em.find(FlightRoute.class, complementaryFlightRouteId);
        } catch (FlightRouteAlreadyExistException ex) {
            Query query = em.createQuery("SELECT f FROM FlightRoute f WHERE f.origin = :origin AND f.destination = :destination");
            query.setParameter("origin", origin);
            query.setParameter("destination", destination);
            complementaryFlightRoute = (FlightRoute) query.getSingleResult();
        }
        originalFlightRoute.setComplementaryFlightRoute(complementaryFlightRoute);
        complementaryFlightRoute.setTwoWay(true);
        complementaryFlightRoute.setComplementaryFlightRoute(originalFlightRoute);
        return complementaryFlightRoute.getFlightRouteId();
    }

    @Override
    public List<FlightRoute> viewAllFlightRoutes() {
        Query query = em.createQuery("SELECT f FROM FlightRoute f ORDER BY f.origin.IATACode ASC");
        List<FlightRoute> flightRoutes = query.getResultList();
        List<FlightRoute> flightRoutesFiltered = new ArrayList<>();
        for (FlightRoute f : flightRoutes) {
            if (!f.isEnabled()) {
                continue;
            }
            if (f.getComplementaryFlightRoute() != null && !flightRoutesFiltered.contains(f.getComplementaryFlightRoute())) {
                flightRoutesFiltered.add(f);
                flightRoutesFiltered.add(f.getComplementaryFlightRoute());

            } else if (f.getComplementaryFlightRoute() == null) {
                flightRoutesFiltered.add(f);
            }
        }
        return flightRoutesFiltered;
    }

    @Override
    public void deleteFlightRoute(Long flightRouteId) {
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);

        // Flight route can only be removed if not in used (i.e. not added to a flight)
        if (!flightRoute.getFlights().isEmpty()) {
            if (flightRoute.isTwoWay()) {
                FlightRoute complfr = em.find(FlightRoute.class, flightRoute.getComplementaryFlightRoute().getFlightRouteId());
                flightRoute.setTwoWay(false);
                complfr.setTwoWay(false);
                complfr.setComplementaryFlightRoute(null);
                flightRoute.setComplementaryFlightRoute(null);
            }
            flightRoute.setEnabled(false);
            return;
        }

        if (flightRoute.isTwoWay()) {
            FlightRoute complfr = em.find(FlightRoute.class, flightRoute.getComplementaryFlightRoute().getFlightRouteId());
            flightRoute.setTwoWay(false);
            complfr.setTwoWay(false);
            complfr.setComplementaryFlightRoute(null);
            flightRoute.setComplementaryFlightRoute(null);
        }
        
        em.remove(flightRoute);
    }

}
