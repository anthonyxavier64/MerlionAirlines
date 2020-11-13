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
import entity.Employee;
import enumeration.EmployeeType;
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
@Startup
public class DataInitialisationSessionBean {

    @EJB
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
        if (em.find(Employee.class, 1l) == null) {
            //employeeSessionBean.createEmployee("Administrator", "administrator", "password", EmployeeType.ADMINISTRATOR);
            employeeSessionBean.createEmployee("Fleet Manager", "fleetmanager", "password", EmployeeType.FLEET_MANAGER);
            employeeSessionBean.createEmployee("Route Planner", "routeplanner", "password", EmployeeType.ROUTE_PLANNER);
            employeeSessionBean.createEmployee("Schedule Manager", "schedulemanager", "password", EmployeeType.SCHEDULE_MANAGER);
            employeeSessionBean.createEmployee("Sales Manager", "salesmanager", "password", EmployeeType.SALES_MANAGER);
            partnerSessionBean.createPartner("Holiday.com", "holidaydotcom", "password");
            airportSessionBean.createAirport("Changi", "SIN", "Singapore", "Singapore", "Singapore", 8);
            airportSessionBean.createAirport("Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China", 8);
            airportSessionBean.createAirport("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C", 8);
            airportSessionBean.createAirport("Narita", "NRT", "Narita", "Chiba", "Japan", 9);
            airportSessionBean.createAirport("Sydney", "SYD", "Sydney", "New South Wales", "Australia", 9);
            aircraftTypeSessionBean.createAircraftType("Boeing 737", 200);
            aircraftTypeSessionBean.createAircraftType("Boeing 747", 400);
        }
    }

}
