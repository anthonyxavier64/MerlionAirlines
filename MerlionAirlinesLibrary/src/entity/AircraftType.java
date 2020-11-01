/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author yappeizhen
 */
@Entity
public class AircraftType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long aircraftTypeID;
    
    @Column(unique = true, nullable = false)
    private String aircraftTypeNameeee;
    
    @Column(nullable = false)
    private int maxPassengers;
    
    @OneToMany(mappedBy = "aircraftType")
    private List<AircraftConfiguration> aircraftConfigurations = new ArrayList<AircraftConfiguration>(); 

    public AircraftType() {
    }

    public AircraftType(String aircraftTypeName, int maxPassengers) {
        this.aircraftTypeName = aircraftTypeName;
        this.maxPassengers = maxPassengers;
    }
    
    public Long getAircraftTypeID() {
        return aircraftTypeID;
    }

    public void setAircraftTypeID(Long aircraftTypeID) {
        this.aircraftTypeID = aircraftTypeID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftTypeID != null ? aircraftTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftTypeID fields are not set
        if (!(object instanceof AircraftType)) {
            return false;
        }
        AircraftType other = (AircraftType) object;
        if ((this.aircraftTypeID == null && other.aircraftTypeID != null) || (this.aircraftTypeID != null && !this.aircraftTypeID.equals(other.aircraftTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftType[ id=" + aircraftTypeID + " ]";
    }

    public String getAircraftTypeName() {
        return aircraftTypeName;
    }

    public void setAircraftTypeName(String aircraftTypeName) {
        this.aircraftTypeName = aircraftTypeName;
    }

    public long getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public List<AircraftConfiguration> getAircraftConfigurations() {
        return aircraftConfigurations;
    }

    public void setAircraftConfigurations(List<AircraftConfiguration> aircraftConfigurations) {
        this.aircraftConfigurations = aircraftConfigurations;
    }
    
    
}
