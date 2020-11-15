/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Fare;
import javax.ejb.Local;

/**
 *
 * @author yappeizhen
 */
@Local
public interface FareSessionBeanLocal {
    Fare retrieveFareByFareBasisCode(String fareBasisCode);
    public Long addFareToFlightSchedulePlan(Long fspId, Long cabinClassConfigId, Fare newFare);

    public long changeFare(long fspId, long oldFareId, Fare newFare);
}
