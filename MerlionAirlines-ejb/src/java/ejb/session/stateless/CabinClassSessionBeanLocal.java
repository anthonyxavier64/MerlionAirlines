/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CabinClassConfiguration;
import enumeration.CabinType;
import javax.ejb.Local;

/**
 *
 * @author Antho
 */
@Local
public interface CabinClassSessionBeanLocal {

    Long createCabinClass(int numAisles, int numRows, int numSeatsAbreast, String seatConfiguration, CabinType cabinType, Long aircraftConfigurationId, Integer startRowNum);

}
