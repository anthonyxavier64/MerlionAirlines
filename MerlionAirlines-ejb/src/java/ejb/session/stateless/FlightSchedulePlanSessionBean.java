/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import exception.FlightSchedulesOverlapException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    @Override
    public List<FlightSchedulePlan> viewAllFlightSchedulePlans() {
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp");
        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();
        return flightSchedulePlans;
    }
    
    // Check whether the flight schedules of fsp1 and fsp2 overlap
    /*private boolean checkOverlap(FlightSchedulePlan fsp1, FlightSchedulePlan fsp2) {
        Query query = em.createQuery("SELECT fs1 FROM FlightSchedule fs1, IN fs1.flightSchedulePlan,"
                + "FlightSchedule fs2 IN fs2.flightSchedulePlan JOIN fs1 JOIN fs2"
                + "WHERE fs2.departureDateTime = fs1.departureDateTime "
                + "AND fs2.duration = fs1.duration "
                + "AND fs1.flightSchedulePlan = ?1 "
                + "AND fs2.flightSchedulePlan = ?2")                
                .setParameter(1, fsp1)
                .setParameter(2, fsp2);
        
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }*/
    
    private boolean checkOverlap(FlightSchedulePlan fsp1, FlightSchedulePlan fsp2) {
        for (FlightSchedule fs1 : fsp1.getFlightSchedules()) {
            for (FlightSchedule fs2 : fsp2.getFlightSchedules()) {
                if (fs1.getDepartureDateTime().equals(fs2.getDepartureDateTime()) && fs1.getDuration().equals(fs2.getDuration())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    // Ensure that the flight's flight schedule plans do not have overlapping flight schedules
    public Long createNewFlightSchedulePlan(FlightSchedulePlan newFsp, Flight flight) throws FlightSchedulesOverlapException {
        
        for (FlightSchedulePlan fsp:flight.getFlightSchedulePlans()) {
            if (checkOverlap(newFsp, fsp)) {
                throw new FlightSchedulesOverlapException("Flight schedules overlap with an existing flight schedule plan!");
            }
        }
        
        newFsp.setFlight(flight);
        em.persist(newFsp);
        em.flush();
        flight.getFlightSchedulePlans().add(newFsp);
        return newFsp.getFlightSchedulePlanID();
    }
    

    @Override
    public void addFlightScheduleToFlightSchedulePlan(Long flightSchedulePlanId, Long flightScheduleId) throws FlightSchedulesOverlapException {
        // Need to ensure that flight schedule is not already added 
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, flightSchedulePlanId);
        FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightScheduleId);
        
        // Need to ensure that flight schedule is not already added
        List<FlightSchedulePlan> existingPlans = fsp.getFlight().getFlightSchedulePlans();
        for (FlightSchedulePlan plan : existingPlans) {
            for (FlightSchedule fs : plan.getFlightSchedules()) {
                if (fs.getDepartureDateTime().equals(flightSchedule.getDepartureDateTime()) && fs.getDuration().equals(flightSchedule.getDuration()))
                    throw new FlightSchedulesOverlapException("Flight schedules overlap with an existing flight schedule plan!");
            }
        }
        
        fsp.getFlightSchedules().add(flightSchedule);
        em.flush();
    } 
}