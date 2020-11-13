/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftTypeSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Airport;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Antho
 */
@Singleton
@LocalBean
//@Startup
public class DataInitSessionBean {

    private PartnerSessionBeanLocal partnerSessionBean;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBean;
    @EJB
    private AirportSessionBeanLocal airportSessionBean;
    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBean;

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        if (em.find(Airport.class, 2l) == null) {
            airportSessionBean.createAirport("Malaysia Airport", "MSA", "Malaysia", "Malaysia", "Malaysia", 8);
            airportSessionBean.createAirport("Japan Airport", "JPA", "Japan", "Japan", "Japan", 6);
            airportSessionBean.createAirport("China Airport", "CHI", "China", "China", "China", 5);
            airportSessionBean.createAirport("Bangkok Airport", "BKK", "Bangkok", "Bangkok", "Bangkok", -7);
            airportSessionBean.createAirport("Italy Airport", "ITA", "Italy", "Italy", "Italy", -6);
            airportSessionBean.createAirport("Indonesia Airport", "IDO", "Indonesia", "Indonesia", "Indonesia", 3);
            airportSessionBean.createAirport("Brunei Airport", "BRU", "Brunei", "Brunei", "Brunei", 2);
            aircraftTypeSessionBean.createAircraftType("Boeing 748", 400);
            aircraftTypeSessionBean.createAircraftType("Boeing 749", 300);
            aircraftTypeSessionBean.createAircraftType("Boeing 750", 200);
            aircraftTypeSessionBean.createAircraftType("Boeing 751", 100);
        }
    }

}
