/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import javax.ejb.Stateful;

/**
 *
 * @author yappeizhen
 */
@Stateful
public class InventorySessionBean implements InventorySessionBeanRemote, InventorySessionBeanLocal {
    private int numSeats;

}
