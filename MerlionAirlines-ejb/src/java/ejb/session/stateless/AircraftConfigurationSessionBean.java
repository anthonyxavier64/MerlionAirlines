/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    @Override
    public List<AircraftConfiguration> viewAllAircraftConfigurations() {
        Query query = em.createQuery("SELECT a FROM AircraftConfiguration a ORDER BY a.aircraftType ASC, a.name ASC");
        List<AircraftConfiguration> aircraftConfigurations = query.getResultList();
        for (int i = 0; i < aircraftConfigurations.size(); i++) {
            AircraftType aircraftType = aircraftConfigurations.get(i).getAircraftType();
        }
        return aircraftConfigurations;
    }

    @Override
    public AircraftConfiguration viewAircraftConfigurationDetails(String name) {
        Query query = em.createQuery("SELECT a FROM AircraftConfiguration a WHERE a.name = :name");
        query.setParameter("name", name);
        AircraftConfiguration aircraftConfiguration = (AircraftConfiguration) query.getSingleResult();
        aircraftConfiguration.getAircraftType();
        for (int i = 0; i < aircraftConfiguration.getCabinClassConfigurations().size(); i++) {
            aircraftConfiguration.getCabinClassConfigurations().get(i);
        }
        return aircraftConfiguration;
    }

}
