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
import javax.validation.constraints.NotNull;

/**
 *
 * @author yappeizhen
 */
@Entity
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditCardID;

    @Column(nullable = false)
    @NotNull
    private String nameOnCreditCard;

    @Column(nullable = false)
    @NotNull
    private String creditCardNumber;

    @Column(nullable = false)
    @NotNull
    private String expiryDate;

    @Column(nullable = false)
    @NotNull
    private String cvv;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;

    public CreditCard() {
    }

    public CreditCard(String nameOnCreditCard, String creditCardNumber, String expiryDate, String cvv) {
        this.nameOnCreditCard = nameOnCreditCard;
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public Long getCreditCardID() {
        return creditCardID;
    }

    public void setCreditCardID(Long creditCardID) {
        this.creditCardID = creditCardID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creditCardID != null ? creditCardID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the creditCardID fields are not set
        if (!(object instanceof CreditCard)) {
            return false;
        }
        CreditCard other = (CreditCard) object;
        if ((this.creditCardID == null && other.creditCardID != null) || (this.creditCardID != null && !this.creditCardID.equals(other.creditCardID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CreditCard[ id=" + creditCardID + " ]";
    }

    public String getNameOnCreditCard() {
        return nameOnCreditCard;
    }

    public void setNameOnCreditCard(String nameOnCreditCard) {
        this.nameOnCreditCard = nameOnCreditCard;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
