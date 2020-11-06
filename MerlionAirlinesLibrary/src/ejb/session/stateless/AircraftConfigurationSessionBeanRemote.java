/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Remote;

/**
 *
 * @author Antho
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {

    Long createAircraftConfiguration(AircraftType aircraftType, String name, Integer numCabinClasses);
    
}
