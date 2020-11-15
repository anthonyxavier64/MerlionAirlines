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
import entity.Seat;
import entity.SeatInventory;
import enumeration.CabinType;
import enumeration.TripType;
import exception.FlightSchedulesOverlapException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule, long flightSchedulePlanId) throws FlightSchedulesOverlapException {
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, flightSchedulePlanId);
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.departureDateTime = ?1 AND fs.duration = ?2 AND fs.flightSchedulePlan = ?3");
        query.setParameter(1, flightSchedule.getDepartureDateTime());
        query.setParameter(2, flightSchedule.getDuration());
        query.setParameter(3, flightSchedule.getFlightSchedulePlan());
        try {
            flightSchedule = (FlightSchedule) query.getSingleResult();
            throw new FlightSchedulesOverlapException("Flight schedule already exists!");

        } catch (NoResultException ex) {
            flightSchedule.setFlightSchedulePlan(fsp);
            em.persist(flightSchedule);
            em.flush();
            return flightSchedule;
        }
    }

    @Override
    public void addRecurringFlightSchedules(Long flightSchedulePlanId, LocalDateTime startDateTime, Duration duration, LocalDateTime endDateTime, Integer intervalByDays) {
        for (LocalDateTime date = startDateTime; date.isBefore(endDateTime); date = date.plusDays(intervalByDays)) {
            try {
                FlightSchedule newFlightSchedule = createNewFlightSchedule(new FlightSchedule(date, duration), flightSchedulePlanId);
                addFlightScheduleToFlightSchedulePlan(flightSchedulePlanId, newFlightSchedule.getFlightScheduleID());
            } catch (FlightSchedulesOverlapException ex) {
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
                if (fs.getDepartureDateTime().equals(flightSchedule.getDepartureDateTime()) && fs.getDuration().equals(flightSchedule.getDuration())) {
                    throw new FlightSchedulesOverlapException("Flight schedules overlap with an existing flight schedule plan!");
                }
            }
        }

        fsp.getFlightSchedules().add(flightSchedule);
        em.flush();
    }

    @Override
    public List<FlightSchedule> getFlightSchedules(Airport departureAirport, Airport destinationAirport, java.time.LocalDate depatureDate, Integer numPassengers) {
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.flightRoute.origin = :departureAirport AND fs.flightSchedulePlan.flight.flightRoute.destination = :destinationAirport AND fs.departureDateTime = :departureDate");
        query.setParameter("departureAirport", departureAirport);
        query.setParameter("destinationAirport", destinationAirport);
        query.setParameter("depatureDate", depatureDate);
        List<FlightSchedule> flightSchedules = query.getResultList();
        List<FlightSchedule> flightSchedulesFiltered = new ArrayList<>();
        for (FlightSchedule f : flightSchedules) {
            int availableSeats = 0;
            List<SeatInventory> seatInventories = f.getSeatInventories();
            for (SeatInventory s : seatInventories) {
                availableSeats += s.getAvailableSeats();
                if (availableSeats >= numPassengers) {
                    flightSchedulesFiltered.add(f);
                    break;
                }
            }
        }
        return flightSchedulesFiltered;
    }

    @Override
    public List<FlightSchedule> getConnectingFlightSchedules(Airport departureAirport, Airport destinationAirport, java.time.LocalDate depatureDate, Integer numPassengers) {
        Query query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.flightRoute.origin = :departureAirport AND fs.flightSchedulePlan.flight.flightRoute.destination <> :destinationAirport AND fs.departureDateTime = :departureDate");
        query.setParameter("departureAirport", departureAirport);
        query.setParameter("destinationAirport", destinationAirport);
        query.setParameter("depatureDate", depatureDate);
        List<FlightSchedule> flightSchedulesFiltered = new ArrayList<>();
        List<FlightSchedule> flightSchedules = query.getResultList();
        for (FlightSchedule f : flightSchedules) {
            int availableSeats = 0;
            List<SeatInventory> seatInventories = f.getSeatInventories();
            for (SeatInventory s : seatInventories) {
                availableSeats += s.getAvailableSeats();
            }
            if (availableSeats < numPassengers) {
                flightSchedules.remove(f);
            }
        }
        for (FlightSchedule f : flightSchedules) {
            departureAirport = f.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination();
            query = em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.flightRoute.origin = :departureAirport AND fs.flightSchedulePlan.flight.flightRoute.destination = :destinationAirport");
            try {
                FlightSchedule cf = (FlightSchedule) query.getSingleResult();
                long hours = ChronoUnit.HOURS.between(f.getArrivalDateTime(), cf.getDepartureDateTime());
                List<SeatInventory> seatInventories = cf.getSeatInventories();
                int availableSeats = 0;
                for (SeatInventory s : seatInventories) {
                    availableSeats += s.getAvailableSeats();
                }
                if (hours <= 24 && hours > 0 && availableSeats >= numPassengers) {
                    flightSchedulesFiltered.add(f);
                    flightSchedulesFiltered.add(cf);
                }
            } catch (NoResultException ex) {
                continue;
            }
        }
        return flightSchedulesFiltered;
    }

    @Override
    public int deleteFlightSchedule(long fsId) {
        FlightSchedule flightSchedule = em.find(FlightSchedule.class, fsId);
        if (flightSchedule == null) {
            return 0;
        }
        List<SeatInventory> inventories = flightSchedule.getSeatInventories();
        // If there are existing bookings
        if (!flightSchedule.getFlightReservations().isEmpty()) {
            return 2;
        }
        // If there are no existing bookings
        long fspId = flightSchedule.getFlightSchedulePlan().getFlightSchedulePlanID();
        FlightSchedulePlan fsp = em.find(FlightSchedulePlan.class, fspId);
        fsp.getFlightSchedules().remove(flightSchedule);
        flightSchedule.setFlightSchedulePlan(null);

        // In inventory, need to delete cabin class config and seats
        for (SeatInventory inventory : inventories) {
            List<Seat> seats = inventory.getSeats();
            for (Seat s : seats) {
                s.setSeatInventory(null);
                Seat seat = em.find(Seat.class, s.getSeatID());
                em.remove(seat);
            }
            inventory.setCabinClassConfiguration(null);
            SeatInventory inv = em.find(SeatInventory.class, inventory.getSeatInventoryID());
            em.remove(inv);
        }
        em.remove(flightSchedule);
        return 1;
    }
}
