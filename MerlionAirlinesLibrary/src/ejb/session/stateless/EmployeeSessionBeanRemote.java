/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import enumeration.EmployeeType;
import javax.ejb.Remote;

/**
 *
 * @author Antho
 */
@Remote
public interface EmployeeSessionBeanRemote {

    Long createEmployee(String name, String username, String password, EmployeeType userRole);
    
}
