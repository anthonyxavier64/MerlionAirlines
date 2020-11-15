/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.CabinType;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Antho
 */
@Entity
public class Fare implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fareID;

    @Column(nullable = false)
    @NotNull
    private CabinType cabinType;

    @Column(unique = true, nullable = false)
    @NotNull
    private String fareBasisCode;

    @Column(nullable = false)
    @NotNull
    private Double fareAmount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FlightSchedulePlan flightSchedulePlan;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CabinClassConfiguration cabinClassConfiguration;

    public Fare() {
    }

    public Fare(CabinType cabinType, String fareBasisCode, Double fareAmount) {
        this.cabinType = cabinType;
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
    public String toString() {
        return "Fare basis code -> " + fareBasisCode + "; Cabin type -> " + cabinType.name() + "; Fare amount -> " + fareAmount;
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

    public String getFareBasisCode() {
        return fareBasisCode;
    }

    public void setFareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
    }

    public Double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(Double fareAmount) {
        this.fareAmount = fareAmount;
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public void setCabinClassType(CabinType cabinClassType) {
        this.cabinType = cabinClassType;
    }

    public CabinClassConfiguration getCabinClassConfiguration() {
        return cabinClassConfiguration;
    }

    public void setCabinClassConfiguration(CabinClassConfiguration cabinClassConfiguration) {
        this.cabinClassConfiguration = cabinClassConfiguration;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }
}
