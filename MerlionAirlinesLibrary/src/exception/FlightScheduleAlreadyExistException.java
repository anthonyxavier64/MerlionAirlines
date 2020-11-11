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
public class FlightScheduleAlreadyExistException extends Exception {

    /**
     * Creates a new instance of
     * <code>FlightScheduleAlreadyExistException</code> without detail message.
     */
    public FlightScheduleAlreadyExistException() {
    }

    /**
     * Constructs an instance of
     * <code>FlightScheduleAlreadyExistException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FlightScheduleAlreadyExistException(String msg) {
        super(msg);
    }
}
