/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Antho
 */
@Entity
public class FlightReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightReservationID;

    @Column(nullable = false)
    @NotNull
    BigDecimal totalAmount;

    private String partnerName;

    @OneToMany(mappedBy = "flightReservation")
    private List<Seat> seats;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FlightSchedule flightSchedule;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer; // Person who made the booking   

    public FlightReservation() {
    }

    public FlightReservation(BigDecimal totalAmount, String partnerName) {
        this.totalAmount = totalAmount;
        this.partnerName = partnerName;
    }

    public Long getFlightReservationID() {
        return flightReservationID;
    }

    public void setFlightReservationID(Long flightReservationID) {
        this.flightReservationID = flightReservationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightReservationID != null ? flightReservationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightReservationID fields are not set
        if (!(object instanceof FlightReservation)) {
            return false;
        }
        FlightReservation other = (FlightReservation) object;
        if ((this.flightReservationID == null && other.flightReservationID != null) || (this.flightReservationID != null && !this.flightReservationID.equals(other.flightReservationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightReservation[ id=" + flightReservationID + " ]";
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

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

}
