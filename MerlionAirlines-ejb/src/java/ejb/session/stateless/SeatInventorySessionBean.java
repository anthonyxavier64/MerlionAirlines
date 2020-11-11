/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Seat;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Antho
 */
@Stateless
public class SeatInventorySessionBean implements SeatInventorySessionBeanRemote, SeatInventorySessionBeanLocal {

    @PersistenceContext(unitName = "MerlionAirlines-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Seat> viewReservedSeats(SeatInventory seatInventory) {
        List<Seat> seats = seatInventory.getSeats();
        Query query = em.createQuery("SELECT s FROM Seat s, IN (seats) st WHERE s.available = FALSE ORDER BY s.seatNumber ASC");
        return query.getResultList();
    }

}
