/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightSchedulePlan;
import exception.FlightSchedulesOverlapException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author yappeizhen
 */
@Local
public interface FlightSchedulePlanSessionBeanLocal {

    public List<FlightSchedulePlan> viewAllFlightSchedulePlans();

    public Long createNewFlightSchedulePlan(FlightSchedulePlan newFsp, Flight flight);

    public void addFlightScheduleToFlightSchedulePlan(Long flightSchedulePlanId, Long flightScheduleId) throws FlightSchedulesOverlapException;
    
    public FlightSchedulePlan retrieveFSPById(Long id);

    public int deleteFlightSchedulePlan(long fspId);

}
