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
public class InvalidTripTypeException extends Exception {

    /**
     * Creates a new instance of <code>InvalidTripTypeException</code> without
     * detail message.
     */
    public InvalidTripTypeException() {
    }

    /**
     * Constructs an instance of <code>InvalidTripTypeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidTripTypeException(String msg) {
        super(msg);
    }
}
