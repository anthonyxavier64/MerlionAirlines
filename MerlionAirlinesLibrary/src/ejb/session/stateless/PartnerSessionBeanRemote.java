/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Remote;

/**
 *
 * @author Antho
 */
@Remote
public interface PartnerSessionBeanRemote {

    Long createPartner(String name, String username, String password);
    
}
