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

/**
 *
 * @author yappeizhen
 */
@Entity
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerID;
    
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String passportNumber;
    
    public Passenger() {
    }

    public Passenger(String firstName, String lastName, String passportNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Long getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(Long passengerID) {
        this.passengerID = passengerID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passengerID != null ? passengerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the passengerID fields are not set
        if (!(object instanceof Passenger)) {
            return false;
        }
        Passenger other = (Passenger) object;
        if ((this.passengerID == null && other.passengerID != null) || (this.passengerID != null && !this.passengerID.equals(other.passengerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Passenger[ id=" + passengerID + " ]";
    }
    
}
