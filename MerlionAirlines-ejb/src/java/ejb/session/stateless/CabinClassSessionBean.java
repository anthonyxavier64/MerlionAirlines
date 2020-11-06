/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClassConfiguration;
import enumeration.CabinType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Antho
 */
@Stateless
public class CabinClassSessionBean implements CabinClassSessionBeanRemote, CabinClassSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createCabinClass(int numAisles, int numRows, int numSeatsAbreast, String seatConfiguration, CabinType cabinType, Long aircraftConfigurationId, Integer startRowNum) {
        AircraftConfiguration aircraftConfiguration = em.find(AircraftConfiguration.class, aircraftConfigurationId);
        if (aircraftConfiguration == null) {
            return -1l;
        }
        CabinClassConfiguration cabinClassConfiguration = new CabinClassConfiguration(numAisles, numRows, numSeatsAbreast, seatConfiguration, cabinType, startRowNum);
        cabinClassConfiguration.setAircraftConfiguration(aircraftConfiguration);
        em.persist(cabinClassConfiguration);
        aircraftConfiguration.getCabinClassConfigurations().add(cabinClassConfiguration);
        em.flush();
        return cabinClassConfiguration.getCabinClassID();
    }

}
