/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import enumeration.EmployeeType;
import exception.CustomerAlreadyExistsException;
import exception.CustomerNotFoundException;
import javax.ejb.Local;

/**
 *
 * @author yappeizhen
 */
@Local
public interface CustomerSessionBeanLocal {

    public Customer login(String username, String password) throws CustomerNotFoundException;

    public Long createNewCustomer(Customer newCust) throws CustomerAlreadyExistsException;

}
