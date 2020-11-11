/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author yappeizhen
 */
public class FlightSchedulesOverlapException extends Exception {

    /**
     * Creates a new instance of <code>FlightSchedulesOverlapException</code>
     * without detail message.
     */
    public FlightSchedulesOverlapException() {
    }

    /**
     * Constructs an instance of <code>FlightSchedulesOverlapException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightSchedulesOverlapException(String msg) {
        super(msg);
    }
}
