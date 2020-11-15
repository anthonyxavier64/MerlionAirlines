/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import exception.FlightScheduleAlreadyExistException;
import exception.FlightSchedulesOverlapException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author yappeizhen
 */
@Local
public interface FlightScheduleSessionBeanLocal {

    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule, long flightSchedulePlanId) throws FlightSchedulesOverlapException;

    public void addRecurringFlightSchedules(Long flightSchedulePlanId, LocalDateTime startDateTime, Duration duration, LocalDateTime endDateTime, Integer intervalByDays);

    List<FlightSchedule> getFlightSchedules(Airport departureAirport, Airport destinationAirport, java.time.LocalDate depatureDate, Integer numPassengers);

    List<FlightSchedule> getConnectingFlightSchedules(Airport departureAirport, Airport destinationAirport, java.time.LocalDate depatureDate, Integer numPassengers);

    public int deleteFlightSchedule(long fsId);
    
}
