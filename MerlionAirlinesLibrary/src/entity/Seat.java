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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author yappeizhen
 */
@Entity
public class Seat implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long seatID;
   
    @Column(nullable = false)
    private String rowNumber;
    
    @Column(nullable = false)
    private String columnNumber;
    
    @Column(nullable = false)
    private String seatNumber;
    
    @Column(nullable = false)
    private boolean available;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private SeatInventory seatInventory;
    
    public Seat() {
    }

    public Seat(String rowNumber, String columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.seatNumber = rowNumber + columnNumber;
        this.available = true;
    }

    public SeatInventory getSeatInventory() {
        return seatInventory;
    }

    public void setSeatInventory(SeatInventory seatInventory) {
        this.seatInventory = seatInventory;
    }
    
    public Long getSeatID() {
        return seatID;
    }

    public void setSeatID(Long seatID) {
        this.seatID = seatID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seatID != null ? seatID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the seatID fields are not set
        if (!(object instanceof Seat)) {
            return false;
        }
        Seat other = (Seat) object;
        if ((this.seatID == null && other.seatID != null) || (this.seatID != null && !this.seatID.equals(other.seatID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Seat[ id=" + seatID + " ]";
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(String columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
}
