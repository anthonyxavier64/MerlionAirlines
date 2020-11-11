/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import exception.FlightScheduleAlreadyExistException;
import exception.FlightSchedulesOverlapException;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.ejb.Local;

/**
 *
 * @author yappeizhen
 */
@Local
public interface FlightScheduleSessionBeanLocal {

    public FlightSchedule createNewFlightSchedule(FlightSchedule flightSchedule);

    public void addRecurringFlightSchedules(Long flightSchedulePlanId, LocalDateTime startDateTime, Duration duration, LocalDateTime endDateTime, Integer intervalByDays);
    
}
