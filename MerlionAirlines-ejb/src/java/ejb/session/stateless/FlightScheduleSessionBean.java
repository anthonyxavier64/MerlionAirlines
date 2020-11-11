/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import enumeration.CabinType;
import enumeration.FlightType;
import enumeration.TripType;
import exception.FlightSchedulesOverlapException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    @Override
    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule) {
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDateTime = ?1 AND fs.duration = ?2");
        query.setParameter(1, flightSchedule.getDepartureDateTime());
        query.setParameter(2, flightSchedule.getDuration());
        try {
            flightSchedule = (FlightSchedule) query.getSingleResult();
            return flightSchedule;
        } catch (NoResultException ex) {
            em.persist(flightSchedule);
            em.flush();
            return flightSchedule;
        }
    }
    
    public void addRecurringFlightSchedules(Long flightSchedulePlanId, LocalDateTime startDateTime, Duration duration, LocalDateTime endDateTime, Integer intervalByDays) {
        for (LocalDateTime date = startDateTime; date.isBefore(endDateTime); date = date.plusDays(intervalByDays)) {
            FlightSchedule newFlightSchedule = createNewFlightSchedule(new FlightSchedule(date, duration));
            try {
                addFlightScheduleToFlightSchedulePlan(flightSchedulePlanId, newFlightSchedule.getFlightScheduleID());
            } catch (FlightSchedulesOverlapException ex) {
                continue;
            }
        }
    }
    
    private void addFlightScheduleToFlightSchedulePlan(Long flightSchedulePlanId, Long flightScheduleId) throws FlightSchedulesOverlapException {
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
    
    public List<FlightSchedule> searchOneWayDirectFlightOnDay(TripType tripType, Airport departureAirport, Airport destinationAirport, LocalDate departureDate, int numPassengers, FlightType flightTypePreference, CabinType cabinTypePreference) {
        boolean twoWay = true;    
        if (tripType == TripType.ONEWAY) {
            twoWay = false;
        }
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.twoWay = ?1 AND fs.flightSchedulePlan.flight.flightRoute.origin = ?2 AND fs.flightSchedulePlan.flight.flightRoute.destination = ?3");
        List<FlightSchedule> filter1 = query.getResultList();
        List<FlightSchedule> result = new ArrayList<>();
        
        for (FlightSchedule fs : filter1) {
            if (departureDate.equals(fs.getDepartureDateTime().toLocalDate())) {
                int numAvailSeats = 0;
                for(SeatInventory seatInv : fs.getSeatInventories()) {
                }
            }
        }
        return null;
    }
}
