/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftType;
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
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createAircraftType(String aircraftTypeName, int maxPassengers) {
        AircraftType newAircraftType = new AircraftType(aircraftTypeName, maxPassengers);
        em.persist(newAircraftType);
        em.flush();
        return newAircraftType.getAircraftTypeID();
    }

    @Override
    public List<AircraftType> getAllAircraftTypes() {
        Query query = em.createQuery("SELECT a FROM AircraftType a");
        List<AircraftType> aircraftTypes = query.getResultList();
        return aircraftTypes;
    }

}
