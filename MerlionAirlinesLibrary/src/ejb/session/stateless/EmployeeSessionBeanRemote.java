/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import enumeration.EmployeeType;
import exception.EmployeeNotFoundException;
import javax.ejb.Remote;

/**
 *
 * @author Antho
 */
@Remote
public interface EmployeeSessionBeanRemote {

    Long createEmployee(String name, String username, String password, EmployeeType userRole);

    Employee login(String username, String password) throws EmployeeNotFoundException;

}
