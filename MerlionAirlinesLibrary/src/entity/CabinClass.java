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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Antho
 */
@Entity
public class CabinClass implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassID;
    private int numAisles;
    private int numRows;
    private int numSeatsAbreast;
    private String configPerColumn;
    private CabinType cabinType;
    @OneToMany(mappedBy = "cabinClass")
    private List<Fare> fares = new ArrayList<Fare>();

    public CabinClass() {
    }

    public CabinClass(int numAisles, int numRows, int numSeatsAbreast, int[] configArray, CabinType cabinType) {
        this.numAisles = numAisles;
        this.numRows = numRows;
        this.numSeatsAbreast = numSeatsAbreast;
        this.configPerColumn = "" + configArray[0];
        for (int i = 1; i < configArray.length; i++) { 
            this.configPerColumn += "-" + configArray[i];
        }
        this.cabinType = cabinType;
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
        if (!(object instanceof CabinClass)) {
            return false;
        }
        CabinClass other = (CabinClass) object;
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

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public void setCabinType(CabinType cabinType) {
        this.cabinType = cabinType;
    }
}
