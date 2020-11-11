/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Employee;
import enumeration.EmployeeType;
import exception.CustomerAlreadyExistsException;
import exception.CustomerNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author yappeizhen
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    @Override
    public Customer login(String username, String password) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = ?1 AND c.password = ?2");
        query.setParameter(1, username);
        query.setParameter(2, password);
        Customer customer;
        try {
            customer = (Customer) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        return customer;
    }
    
    @Override
    public Long createNewCustomer(Customer newCust) throws CustomerAlreadyExistsException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = ?1").setParameter(1, newCust.getUsername());
        try {
            query.getSingleResult();
            throw new CustomerAlreadyExistsException("Username is taken!");
            
        } catch (NoResultException ex) {
            em.persist(newCust);
            em.flush();
            return newCust.getCustomerID();
        }
    }
}
