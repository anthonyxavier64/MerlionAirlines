/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Antho
 */
public class FlightDoesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightDoesNotExist</code> without detail
     * message.
     */
    public FlightDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>FlightDoesNotExist</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightDoesNotExistException(String msg) {
        super(msg);
    }
}
