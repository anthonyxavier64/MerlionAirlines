/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.CabinType;
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
import javax.persistence.OneToOne;

/**
 *
 * @author Antho
 */
@Entity
public class CabinClassConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long cabinClassID;
    
    @Column(nullable = false)
    private int numAisles;
    
    @Column(nullable = false)
    private int numRows;
    
    @Column(nullable = false)
    private int numSeatsAbreast;
    
    @Column(nullable = false)
    private String configPerColumn;
    
    @Column(nullable = false)
    private List<CabinType> cabinClasses = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;
    
    @OneToOne
    @JoinColumn(nullable = false)
    private Fare fare;
    
    public CabinClassConfiguration() {
    }

    public CabinClassConfiguration(int numAisles, int numRows, int numSeatsAbreast, int[] configArray, CabinType cabinType) {
        this.numAisles = numAisles;
        this.numRows = numRows;
        this.numSeatsAbreast = numSeatsAbreast;
        this.configPerColumn = "" + configArray[0];
        for (int i = 1; i < configArray.length; i++) { 
            this.configPerColumn += "-" + configArray[i];
        }
    }
    
    public List<CabinType> getCabinClasses() {
        return cabinClasses;
    }

    public void setCabinClasses(List<CabinType> cabinClasses) {
        this.cabinClasses = cabinClasses;
    }

    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }

    public Long getCabinClassID() {
        return cabinClassID;
    }

    public void setCabinClassID(Long cabinClassID) {
        this.cabinClassID = cabinClassID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassID != null ? cabinClassID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassID fields are not set
        if (!(object instanceof CabinClassConfiguration)) {
            return false;
        }
        CabinClassConfiguration other = (CabinClassConfiguration) object;
        if ((this.cabinClassID == null && other.cabinClassID != null) || (this.cabinClassID != null && !this.cabinClassID.equals(other.cabinClassID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClass[ id=" + cabinClassID + " ]";
    }

    public int getNumAisles() {
        return numAisles;
    }

    public void setNumAisles(int numAisles) {
        this.numAisles = numAisles;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumSeatsAbreast() {
        return numSeatsAbreast;
    }

    public void setNumSeatsAbreast(int numSeatsAbreast) {
        this.numSeatsAbreast = numSeatsAbreast;
    }

    public String getConfigPerColumn() {
        return configPerColumn;
    }

    public void setConfigPerColumn(String configPerColumn) {
        this.configPerColumn = configPerColumn;
    }
    }