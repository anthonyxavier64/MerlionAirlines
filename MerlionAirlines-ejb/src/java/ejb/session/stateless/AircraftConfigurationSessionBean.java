/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.AircraftConfiguration;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Antho
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {
    
    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createAircraftConfiguration(AircraftType aircraftType, String name, Integer numCabinClasses) {
        AircraftType currentAircraftType = em.find(AircraftType.class, aircraftType.getAircraftTypeID());
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration(name, numCabinClasses);
        em.persist(aircraftConfiguration);
        currentAircraftType.getAircraftConfigurations().add(aircraftConfiguration);
        aircraftConfiguration.setAircraftType(currentAircraftType);
        em.flush();
        return aircraftConfiguration.getAircraftConfigurationID();
    }
    
}
