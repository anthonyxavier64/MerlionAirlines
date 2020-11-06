/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import enumeration.EmployeeType;
import exception.EmployeeNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Antho
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createEmployee(String name, String username, String password, EmployeeType userRole) {
        Employee newEmployee = new Employee(name, username, password, userRole);
        em.persist(newEmployee);
        em.flush();
        return newEmployee.getEmployeeID();
    }

    @Override
    public Employee login(String username, String password) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username AND e.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        Employee employee;
        try {
            employee = (Employee) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new EmployeeNotFoundException("Employee not found!");
        }
        return employee;
    }

}
