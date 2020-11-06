/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Antho
 */
@Local
public interface AircraftTypeSessionBeanLocal {

    Long createAircraftType(String aircraftTypeName, int maxPassengers);

    List<AircraftType> getAllAircraftTypes();

}
