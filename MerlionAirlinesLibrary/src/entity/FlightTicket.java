/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Antho
 */
@Entity
public class FlightTicket implements Serializable {
    // NEEDS TO BE MAPPED TO A SEAT
    // Incomplete
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightTicketID;
    @OneToOne
    private FlightSchedule flightSchedule;
    @ManyToOne
    private Customer customer;

    public FlightTicket() {
    }

    public Long getFlightTicketID() {
        return flightTicketID;
    }

    public void setFlightTicketID(Long flightTicketID) {
        this.flightTicketID = flightTicketID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightTicketID != null ? flightTicketID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightTicketID fields are not set
        if (!(object instanceof FlightTicket)) {
            return false;
        }
        FlightTicket other = (FlightTicket) object;
        if ((this.flightTicketID == null && other.flightTicketID != null) || (this.flightTicketID != null && !this.flightTicketID.equals(other.flightTicketID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightTicket[ id=" + flightTicketID + " ]";
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
