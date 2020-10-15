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
public class SeatInventoryAddSeatException extends Exception {

    /**
     * Creates a new instance of <code>SeatInventoryAddSeatException</code>
     * without detail message.
     */
    public SeatInventoryAddSeatException() {
    }

    /**
     * Constructs an instance of <code>SeatInventoryAddSeatException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public SeatInventoryAddSeatException(String msg) {
        super(msg);
    }
}
