/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author yappeizhen
 */
@Entity
public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportID;
    
    @Column(unique = true, nullable = false)
    @NotNull
    private String airportName;
    
    @Column(unique = true, nullable = false)
    @NotNull
    private String IATACode;
    
    @Column(nullable = false)
    @NotNull
    private String country;
    
    @Column(nullable = false)
    @NotNull
    private String city;
    
    @Column(nullable = false)
    @NotNull
    private String state;

    public Airport() {
    }

    public Airport(String airportName, String airportCode, String country, String city, String state) {
        this.airportName = airportName;
        this.IATACode = airportCode;
        this.country = country;
        this.city = city;
        this.state = state;
    }

    public Long getAirportID() {
        return airportID;
    }

    public void setAirportID(Long airportID) {
        this.airportID = airportID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (airportID != null ? airportID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the airportID fields are not set
        if (!(object instanceof Airport)) {
            return false;
        }
        Airport other = (Airport) object;
        if ((this.airportID == null && other.airportID != null) || (this.airportID != null && !this.airportID.equals(other.airportID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Airport[ id=" + airportID + " ]";
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getIATACode() {
        return IATACode;
    }

    public void setIATACode(String IATACode) {
        this.IATACode = IATACode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
