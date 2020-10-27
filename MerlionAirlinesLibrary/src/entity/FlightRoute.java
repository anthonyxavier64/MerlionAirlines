/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import exception.FlightRouteAddFlightException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author yappeizhen
 */
@Entity
public class FlightRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long flightRouteId;
    
    @Column(unique = true, nullable = false)
    private boolean enabled;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Airport origin;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Airport destination;
    
    @OneToMany(mappedBy = "flightRoute")
    @JoinColumn(nullable = false)
    private List<Flight> flights = new ArrayList<>();
    
    public FlightRoute(Airport origin, Airport destination) {
        this.enabled = true;
        this.origin = origin;
        this.destination = destination;
    }
    
    public FlightRoute() {
    }

    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public void setFlightRouteId(Long flightRouteId) {
        this.flightRouteId = flightRouteId;
    }
    
    public void addFlight(Flight flight) throws FlightRouteAddFlightException {
        if (flight != null && !this.getFlights().contains(flight)) {
            this.getFlights().add(flight);
        } else {
            throw new FlightRouteAddFlightException("Flight already added to Flight Route");
        }
        
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightRouteId != null ? flightRouteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightRouteId fields are not set
        if (!(object instanceof FlightRoute)) {
            return false;
        }
        FlightRoute other = (FlightRoute) object;
        if ((this.flightRouteId == null && other.flightRouteId != null) || (this.flightRouteId != null && !this.flightRouteId.equals(other.flightRouteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightRoute[ id=" + flightRouteId + " ]";
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
    
}
