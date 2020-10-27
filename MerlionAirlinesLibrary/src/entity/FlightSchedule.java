/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Antho
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long flightScheduleID;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(unique = true, nullable = false)
    private Date departureDate;
    
    @Column(unique = true, nullable = false)
    private int departureTime; // in 24hrs format
    
    @Column(unique = true, nullable = false)
    private int duration; // converted to minutes but in the UI will ask for hours and minutes
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(unique = true, nullable = false)
    private Date arrivalDate;
    
    @Column(unique = true, nullable = false)
    private int arrivalTime;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(unique = true, nullable = false)
    private Date endDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;
    
    @OneToMany(mappedBy = "flightSchedule")
    private List<FlightReservation> flightReservations = new ArrayList<FlightReservation>();
    
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private SeatInventory seatInventory;
    
    public FlightSchedule() {
    }

    // incomplete
    public FlightSchedule(Date departureDate, int departureTime, int duration) {
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.duration = duration;
    }

    // incomplete
    // for recurrent schedule
    public FlightSchedule(Date departureDate, int departureTime, Date endDate) {
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.endDate = endDate;
    }

    public Long getFlightScheduleID() {
        return flightScheduleID;
    }

    public void setFlightScheduleID(Long flightScheduleID) {
        this.flightScheduleID = flightScheduleID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightScheduleID != null ? flightScheduleID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightScheduleID fields are not set
        if (!(object instanceof FlightSchedule)) {
            return false;
        }
        FlightSchedule other = (FlightSchedule) object;
        if ((this.flightScheduleID == null && other.flightScheduleID != null) || (this.flightScheduleID != null && !this.flightScheduleID.equals(other.flightScheduleID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedule[ id=" + flightScheduleID + " ]";
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }
}
