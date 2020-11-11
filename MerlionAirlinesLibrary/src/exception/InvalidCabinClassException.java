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
public class InvalidCabinClassException extends Exception {

    /**
     * Creates a new instance of <code>InvalidCabinClassException</code> without
     * detail message.
     */
    public InvalidCabinClassException() {
    }

    /**
     * Constructs an instance of <code>InvalidCabinClassException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidCabinClassException(String msg) {
        super(msg);
    }
}
