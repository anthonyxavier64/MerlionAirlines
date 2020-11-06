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
public class MaximumCapacityExceeded extends Exception {

    /**
     * Creates a new instance of <code>MaximumCapacityExceeded</code> without
     * detail message.
     */
    public MaximumCapacityExceeded() {
    }

    /**
     * Constructs an instance of <code>MaximumCapacityExceeded</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MaximumCapacityExceeded(String msg) {
        super(msg);
    }
}
