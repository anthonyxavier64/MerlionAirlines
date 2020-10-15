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
public class FlightRouteAddFlightException extends Exception {

    /**
     * Creates a new instance of <code>FlightRouteAddFlightException</code>
     * without detail message.
     */
    public FlightRouteAddFlightException() {
    }

    /**
     * Constructs an instance of <code>FlightRouteAddFlightException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightRouteAddFlightException(String msg) {
        super(msg);
    }
}
