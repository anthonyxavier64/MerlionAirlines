/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import entity.Fare;
import entity.FlightSchedulePlan;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author yappeizhen
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    @Override
    public Fare retrieveFareByFareBasisCode(String fareBasisCode) {
        Query query = em.createQuery("SELECT f FROM Fare f WHERE f.fareBasisCode = ?1").setParameter(1, fareBasisCode);
        try {
            Fare fare = (Fare) query.getSingleResult();
            return fare;
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public Long addFareToFlightSchedulePlan(Long fspId, Long cabinClassConfigId, Fare newFare) {
        FlightSchedulePlan flightSchedulePlan = em.find(FlightSchedulePlan.class, fspId);
        CabinClassConfiguration ccc = em.find(CabinClassConfiguration.class, cabinClassConfigId);
        newFare.setFlightSchedulePlan(flightSchedulePlan);
        newFare.setCabinClassConfiguration(ccc);
        em.persist(newFare);
        em.flush();
        ccc.getFares().add(newFare);
        flightSchedulePlan.getFares().add(newFare);
        return newFare.getFareID();
    }
    
    @Override
    public long changeFare(long fspId, long oldFareId, Fare newFare) {
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, fspId);
        Fare oldFare = em.find(Fare.class, oldFareId);
        CabinClassConfiguration ccc = em.find(CabinClassConfiguration.class, oldFare.getCabinClassConfiguration().getCabinClassID());
        ccc.getFares().remove(oldFare);
        fsp.getFares().remove(oldFare);
        newFare.setCabinClassConfiguration(ccc);
        newFare.setFlightSchedulePlan(fsp);
        em.remove(oldFare);
        em.persist(newFare);
        em.flush();
        ccc.getFares().add(newFare);
        fsp.getFares().add(newFare);
        return newFare.getFareID();
    }
        
}
