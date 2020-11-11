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
public class AirportDoesNotExistException extends Exception {

    /**
     * Creates a new instance of <code>AirportDoesNotExistException</code>
     * without detail message.
     */
    public AirportDoesNotExistException() {
    }

    /**
     * Constructs an instance of <code>AirportDoesNotExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AirportDoesNotExistException(String msg) {
        super(msg);
    }
}
