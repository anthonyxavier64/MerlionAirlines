/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Antho
 */
@Entity
public class FlightSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightScheduleID;

    @JoinColumn(nullable = false)
    @NotNull
    private LocalDateTime departureDateTime;

    @NotNull
    private Duration duration; // converted to minutes but in the UI will ask for hours and minutes

    /*@Temporal(javax.persistence.TemporalType.DATE)
    @Column(unique = true, nullable = false)
    @NotNull
    private LocalDateTime endDate;*/
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;

    @OneToMany(mappedBy = "flightSchedule")
    private List<FlightReservation> flightReservations = new ArrayList<FlightReservation>();

    @OneToMany(mappedBy = "flightSchedule")
    private List<SeatInventory> seatInventories;

    public FlightSchedule() {
    }

    // incomplete
    public FlightSchedule(LocalDateTime departureDateTime, Duration duration) {
        this.departureDateTime = departureDateTime;
        this.duration = duration;
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
        return "Departure date and time (local time) -> " + getDepartureDateTime() + "; Arrival date and time (local time) ->  " 
                    + getArrivalDateTime() + "; Duration -> " 
                    + getDuration();
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime dateTime) {
        this.departureDateTime = dateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getArrivalDateTime() {
        return departureDateTime.plus(duration).minusHours(flightSchedulePlan.getFlight().getFlightRoute().getTimeZoneDifference());
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }

    public List<SeatInventory> getSeatInventories() {
        return seatInventories;
    }

    public void setSeatInventories(List<SeatInventory> seatInventories) {
        this.seatInventories = seatInventories;
    }
}
