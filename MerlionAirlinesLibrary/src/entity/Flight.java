/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.TripType;
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
 * @author yappeizhen
 */
@Entity
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightID;

    @Column(unique = true, nullable = false)
    @NotNull
    private String flightNumber;

    @Column(nullable = false)
    @NotNull
    private boolean twoWay;

    @Column(nullable = false)
    @NotNull
    private boolean enabled; 

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;

    @OneToMany(mappedBy = "flight")
    private List<FlightSchedulePlan> flightSchedulePlans;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FlightRoute flightRoute;

    @OneToOne(fetch = FetchType.LAZY)
    Flight complementaryFlight;

    public Flight() {
        this.flightSchedulePlans = new ArrayList<FlightSchedulePlan>();
    }

    public Flight(String flightNumber) {
        this.flightSchedulePlans = new ArrayList<FlightSchedulePlan>();
        this.flightNumber = "ML" + flightNumber;
        this.twoWay = false;
        enabled = true;
    }

    public Flight getComplementaryFlight() {
        return complementaryFlight;
    }

    public void setComplementaryFlight(Flight complementaryFlight) {
        this.complementaryFlight = complementaryFlight;
    }

    public Long getFlightID() {
        return flightID;
    }

    public void setFlightID(Long flightID) {
        this.flightID = flightID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightID != null ? flightID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightID fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightID == null && other.flightID != null) || (this.flightID != null && !this.flightID.equals(other.flightID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + flightID + " ]";
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    public void setFlightRoute(FlightRoute flightRoute) {
        this.flightRoute = flightRoute;
    }

    public List<FlightSchedulePlan> getFlightSchedulePlans() {
        return flightSchedulePlans;
    }

    public void setFlightSchedulePlans(List<FlightSchedulePlan> flightSchedulePlans) {
        this.flightSchedulePlans = flightSchedulePlans;
    }

    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }
}
