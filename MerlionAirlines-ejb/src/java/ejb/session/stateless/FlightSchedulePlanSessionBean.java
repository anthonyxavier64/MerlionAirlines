/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import entity.Fare;
import entity.Seat;
import entity.SeatInventory;
import exception.FlightSchedulesOverlapException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Comparator;
import java.util.Collections;

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
        Query query = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp ORDER BY fsp.flight.flightNumber ASC");
        List<FlightSchedulePlan> flightSchedulePlans = query.getResultList();
        Comparator<FlightSchedulePlan> comparer = (fsp1, fsp2) -> {
            int flightNo1 = Integer.valueOf(fsp1.getFlight().getFlightNumber().substring(2));
            int flightNo2 = Integer.valueOf(fsp2.getFlight().getFlightNumber().substring(2));
            if (flightNo1 == flightNo2) {
                return fsp2.getFlightSchedules().get(0).getDepartureDateTime().compareTo(fsp1.getFlightSchedules().get(0).getDepartureDateTime());
            }
            return flightNo2 - flightNo1;
        };
        Collections.sort(flightSchedulePlans, comparer);
        
        List<FlightSchedulePlan> results = new ArrayList<>(flightSchedulePlans);
        for (FlightSchedulePlan f : flightSchedulePlans) {
            f.getFlight().getAircraftConfiguration();
            f.getComplementaryFlightSchedulePlan();
            f.getFares().size();
            f.getFlightSchedules().size();
            f.getFlight().getFlightRoute();
            
            if (!f.isEnabled()) {
                continue;
            }
            if (f.getComplementaryFlightSchedulePlan() != null && !results.contains(f.getComplementaryFlightSchedulePlan())) {
                results.add(f);
                results.add(f.getComplementaryFlightSchedulePlan());
                
            } else if (f.getComplementaryFlightSchedulePlan() == null) {
                results.add(f);
            }
        }        
        return flightSchedulePlans;
    }
    
    
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
    public Long createNewFlightSchedulePlan(FlightSchedulePlan newFsp, Flight flight) {
        flight = em.find(Flight.class, flight.getFlightID());
        newFsp.setFlight(flight);
        em.persist(newFsp);
        em.flush();
        flight.getFlightSchedulePlans().add(newFsp);
        return newFsp.getFlightSchedulePlanID();
    }
    
      
    @Override
    public FlightSchedulePlan retrieveFSPById(Long id) {
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, id);
        fsp.getComplementaryFlightSchedulePlan();
        fsp.getFares().size();
        Flight flight = fsp.getFlight();
        fsp.getFlightSchedules().size();
        
        AircraftConfiguration aircraftConfig = flight.getAircraftConfiguration();
        aircraftConfig.getCabinClassConfigurations().size();

        flight.getComplementaryFlight();
        
        flight.getFlightRoute().getOrigin();
        flight.getFlightRoute().getDestination();

        return fsp;
    }

    @Override
    public void addFlightScheduleToFlightSchedulePlan(Long flightSchedulePlanId, Long flightScheduleId) throws FlightSchedulesOverlapException {
        // Need to ensure that flight schedule is not already added 
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, flightSchedulePlanId);
        FlightSchedule flightSchedule = em.find(FlightSchedule.class, flightScheduleId);
        
        // Need to ensure that flight schedule is not already added
        List<FlightSchedulePlan> existingPlans = fsp.getFlight().getFlightSchedulePlans();
        for (FlightSchedulePlan plan : existingPlans) {
            if (!plan.isEnabled()) {
                continue;
            }
            for (FlightSchedule fs : plan.getFlightSchedules()) {
                if (fs.getDepartureDateTime().equals(flightSchedule.getDepartureDateTime()) && fs.getDuration().equals(flightSchedule.getDuration()))
                    throw new FlightSchedulesOverlapException("Flight schedules overlap with an existing flight schedule plan!");
            }
        }
        
        fsp.getFlightSchedules().add(flightSchedule);
        em.flush();
    } 
    
    @Override
    public int deleteFlightSchedulePlan(long fspId) {
        int status = 0; //0 for not found, 1 for deleted, 2 for disabled
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, fspId);
        if (fsp == null) {
            return 0;
        }
        // Check whether it has already been booked
        boolean canDelete = true;
        for (FlightSchedule fs : fsp.getFlightSchedules()) {
            if (fs.getFlightReservations().size() != 0) {
                canDelete = false;
                break;
            }
        }
        if (!canDelete) {
            fsp.setEnabled(false);
            fsp.getComplementaryFlightSchedulePlan().setComplementaryFlightSchedulePlan(null);
            fsp.getComplementaryFlightSchedulePlan();
            return 2;
        }
        
        List<Fare> fares = fsp.getFares();
        for (Fare f : fares) {
            f.getCabinClassConfiguration().getFares().remove(f);
            f.setFlightSchedulePlan(null);
            f = em.find(Fare.class, f.getFareID());
            em.remove(f);
        }
        fsp.getFares().clear();
        
        Flight flight = fsp.getFlight();
        flight.getFlightSchedulePlans().remove(fsp);
        
        List<FlightSchedule> fss = fsp.getFlightSchedules();
        
        for (FlightSchedule fs : fss) {
            List<SeatInventory> invs = fs.getSeatInventories();
            
            for (SeatInventory inv : invs) {
                inv.setCabinClassConfiguration(null);            
                List<Seat> seats = inv.getSeats();
                for (Seat seat : seats) {
                    seat.setSeatInventory(null);
                    seat = em.find(Seat.class, seat.getSeatID());
                    em.remove(seat);
                }
                inv.setFlightSchedule(null);
                inv.getSeats().clear();
                inv = em.find(SeatInventory.class, inv.getSeatInventoryID());
                em.remove(inv);
            }
            fs.getSeatInventories().clear();
            fs.setFlightSchedulePlan(null);
            em.find(FlightSchedule.class, fs.getFlightScheduleID());
            em.remove(fs);
        }
        fsp.setComplementaryFlightSchedulePlan(null);
        em.remove(fsp);
        return 1;
    }
}