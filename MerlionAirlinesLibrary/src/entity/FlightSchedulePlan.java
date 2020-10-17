/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Antho
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanID;
    private String flightNumber;
    @ManyToOne
    private Flight flight;
    @OneToMany(mappedBy = "flightSchedule")
    private List<FlightSchedule>  flightSchedules = new ArrayList<FlightSchedule>();
    @OneToOne(mappedBy = "complementaryFlightSchedulePlan")
    private FlightSchedulePlan flighSchedulePlan;
    @OneToOne
    private FlightSchedulePlan complementaryFlightSchedulePlan;

    public FlightSchedulePlan() {
    }

    public FlightSchedulePlan(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public Long getFlightSchedulePlanID() {
        return flightSchedulePlanID;
    }

    public void setFlightSchedulePlanID(Long flightSchedulePlanID) {
        this.flightSchedulePlanID = flightSchedulePlanID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanID != null ? flightSchedulePlanID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanID fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightSchedulePlanID == null && other.flightSchedulePlanID != null) || (this.flightSchedulePlanID != null && !this.flightSchedulePlanID.equals(other.flightSchedulePlanID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + flightSchedulePlanID + " ]";
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public List<FlightSchedule> getFlightSchedules() {
        return flightSchedules;
    }

    public void setFlightSchedules(List<FlightSchedule> flightSchedules) {
        this.flightSchedules = flightSchedules;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public FlightSchedulePlan getFlighSchedulePlan() {
        return flighSchedulePlan;
    }

    public void setFlighSchedulePlan(FlightSchedulePlan flighSchedulePlan) {
        this.flighSchedulePlan = flighSchedulePlan;
    }

    public FlightSchedulePlan getComplementaryFlightSchedulePlan() {
        return complementaryFlightSchedulePlan;
    }

    public void setComplementaryFlightSchedulePlan(FlightSchedulePlan complementaryFlightSchedulePlan) {
        this.complementaryFlightSchedulePlan = complementaryFlightSchedulePlan;
    }
}
