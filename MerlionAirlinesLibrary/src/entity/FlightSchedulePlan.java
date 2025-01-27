/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.FSPType;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;

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

    private Boolean enabled = true;
    
    private FSPType flightSchedulePlanType;

    
    public FSPType getFlightSchedulePlanType() {
        return flightSchedulePlanType;
    }

    public void setFlightSchedulePlanType(FSPType flightSchedulePlanType) {
        this.flightSchedulePlanType = flightSchedulePlanType;
    }

    public FlightSchedulePlan(FSPType flightSchedulePlanType) {
        this.flightSchedulePlanType = flightSchedulePlanType;
    }
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Flight flight;

    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<FlightSchedule> flightSchedules = new ArrayList<FlightSchedule>();

    @OneToMany(mappedBy = "flightSchedulePlan")
    private List<Fare> fares = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private FlightSchedulePlan complementaryFlightSchedulePlan;
   
    public FlightSchedulePlan() {
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

    public FlightSchedulePlan getComplementaryFlightSchedulePlan() {
        return complementaryFlightSchedulePlan;
    }

    public void setComplementaryFlightSchedulePlan(FlightSchedulePlan complementaryFlightSchedulePlan) {
        this.complementaryFlightSchedulePlan = complementaryFlightSchedulePlan;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}
