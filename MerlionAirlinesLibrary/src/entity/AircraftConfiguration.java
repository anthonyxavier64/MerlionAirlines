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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Antho
 */
@Entity
public class AircraftConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long aircraftConfigurationID;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(nullable = false)
    private int numCabinClasses;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AircraftType aircraftType;
    
    @OneToMany(mappedBy = "aircraftConfiguration")
    @JoinColumn(nullable = false)
    private List<CabinClassConfiguration> cabinClassConfigurations = new ArrayList<CabinClassConfiguration>();

    public AircraftConfiguration() {
    }

    public AircraftConfiguration(String name, int numCabinClasses, AircraftType aircraftType) {
        this.name = name;
        this.numCabinClasses = numCabinClasses;
        this.aircraftType = aircraftType;
    }

    public Long getAircraftConfigurationID() {
        return aircraftConfigurationID;
    }

    public void setAircraftConfigurationID(Long aircraftConfigurationID) {
        this.aircraftConfigurationID = aircraftConfigurationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigurationID != null ? aircraftConfigurationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftConfigurationID fields are not set
        if (!(object instanceof AircraftConfiguration)) {
            return false;
        }
        AircraftConfiguration other = (AircraftConfiguration) object;
        if ((this.aircraftConfigurationID == null && other.aircraftConfigurationID != null) || (this.aircraftConfigurationID != null && !this.aircraftConfigurationID.equals(other.aircraftConfigurationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfiguration[ id=" + aircraftConfigurationID + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumCabinClasses() {
        return numCabinClasses;
    }

    public void setNumCabinClasses(int numCabinClasses) {
        this.numCabinClasses = numCabinClasses;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public List<CabinClassConfiguration> getCabinClassConfigurations() {
        return cabinClassConfigurations;
    }

    public void setCabinClassConfigurations(List<CabinClassConfiguration> cabinClassConfigurations) {
        this.cabinClassConfigurations = cabinClassConfigurations;
    }
}
