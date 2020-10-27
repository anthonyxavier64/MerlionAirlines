/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import exception.SeatInventoryAddSeatException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author yappeizhen
 */
@Entity
public class SeatInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long seatInventoryID;
    
    @OneToMany(mappedBy = "seatInventory")
    @JoinColumn(nullable = false)
    private List<Seat> seats = new ArrayList<>();
    
    @OneToOne(mappedBy = "seatInventory")
    @JoinColumn(nullable = false)
    private FlightSchedule flightSchedule;
    
    @OneToOne
    @JoinColumn(nullable = false)
    private CabinClassConfiguration cabinClassConfiguration;

    public SeatInventory() {
    }
    
    public Long getSeatInventoryID() {
        return seatInventoryID;
    }

    public void setSeatInventoryID(Long seatInventoryID) {
        this.seatInventoryID = seatInventoryID;
    }
    
    public void addSeat(Seat seat) throws SeatInventoryAddSeatException {
        if (seat != null && !this.getSeats().contains(seat)) {
            this.getSeats().add(seat);
        } else {
            throw new SeatInventoryAddSeatException("Seat already added to Seat Inventory");
        }
        
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seatInventoryID != null ? seatInventoryID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the seatInventoryID fields are not set
        if (!(object instanceof SeatInventory)) {
            return false;
        }
        SeatInventory other = (SeatInventory) object;
        if ((this.seatInventoryID == null && other.seatInventoryID != null) || (this.seatInventoryID != null && !this.seatInventoryID.equals(other.seatInventoryID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SeatInventory[ id=" + seatInventoryID + " ]";
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public CabinClassConfiguration getCabinClassConfiguration() {
        return cabinClassConfiguration;
    }

    public void setCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration) {
        this.cabinClassConfiguration = cabinClassConfiguration;
    }   
}
