/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.CabinType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Antho
 */
@Entity
public class Fare implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long fareID;
    
    @Column(unique = true, nullable = false)
    private CabinType cabinType;
    
    @Column(unique = true, nullable = false)
    private String fareBasisCode;
    
    @Column(nullable = false)
    private double fareAmount;

    public Fare() {
    }

    public Fare(String fareBasisCode, double fareAmount, String cabinClassName) {
        this.fareBasisCode = fareBasisCode;
        this.fareAmount = fareAmount;
    }

    public Long getFareID() {
        return fareID;
    }

    public void setFareID(Long fareID) {
        this.fareID = fareID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareID != null ? fareID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fareID fields are not set
        if (!(object instanceof Fare)) {
            return false;
        }
        Fare other = (Fare) object;
        if ((this.fareID == null && other.fareID != null) || (this.fareID != null && !this.fareID.equals(other.fareID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fare[ id=" + fareID + " ]";
    }

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(double fareAmount) {
        this.fareAmount = fareAmount;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public void setCabinClassType(CabinType cabinClassType) {
        this.cabinType = cabinClassType;
    }
}