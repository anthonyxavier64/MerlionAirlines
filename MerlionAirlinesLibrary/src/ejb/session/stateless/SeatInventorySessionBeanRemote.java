/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Seat;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Antho
 */
@Remote
public interface SeatInventorySessionBeanRemote {

    List<Seat> viewReservedSeats(SeatInventory seatInventory);
    
}
