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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class CabinClassConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassID;

    @Column(nullable = false)
    @NotNull
    private Integer numAisles;

    @Column(nullable = false)
    @NotNull
    private Integer numRows;

    @Column(nullable = false)
    @NotNull
    private Integer numSeatsAbreast;

    @Column(nullable = false)
    @NotNull
    private String configPerColumn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private CabinType cabinType;

    @Column(nullable = false)
    @NotNull
    List<String> seatNumbers = new ArrayList<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private AircraftConfiguration aircraftConfiguration;

    @OneToMany(mappedBy = "cabinClassConfiguration")
    private List<Fare> fares = new ArrayList<>();

    public CabinClassConfiguration() {
    }

    public CabinClassConfiguration(int numAisles, int numRows, int numSeatsAbreast,
            String configPerColumn, CabinType cabinType, int startRowNum) {
        this.numAisles = numAisles;
        this.numRows = numRows;
        this.numSeatsAbreast = numSeatsAbreast;
        this.configPerColumn = configPerColumn;
        /*
        this.configPerColumn = "" + configArray[0];
        for (int i = 1; i < configArray.length; i++) {
            this.configPerColumn += "-" + configArray[i];
        }
         */
        this.cabinType = cabinType;
        String[] seatLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        for (int i = startRowNum; i <= numRows + startRowNum - 1; i++) {
            for (int r = 0; r < numSeatsAbreast; r++) {
                String seatNumber = startRowNum + seatLetters[r];
                seatNumbers.add(seatNumber);
            }
        }
    }

    public CabinType getCabinType() {
        return cabinType;
    }

    public void setCabinType(CabinType cabinType) {
        this.cabinType = cabinType;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
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
