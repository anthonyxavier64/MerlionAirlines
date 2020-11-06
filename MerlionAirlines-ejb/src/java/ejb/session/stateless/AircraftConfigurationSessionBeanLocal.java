/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.AircraftType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Antho
 */
@Local
public interface AircraftConfigurationSessionBeanLocal {

    Long createAircraftConfiguration(AircraftType aircraftType, String name, Integer numCabinClasses);

    List<AircraftConfiguration> viewAllAircraftConfigurations();

    AircraftConfiguration viewAircraftConfigurationDetails(String name);
    
}
