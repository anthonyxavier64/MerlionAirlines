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
public class NoFlightScheduleFoundException extends Exception {

    /**
     * Creates a new instance of <code>NoFlightScheduleFoundException</code>
     * without detail message.
     */
    public NoFlightScheduleFoundException() {
    }

    /**
     * Constructs an instance of <code>NoFlightScheduleFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoFlightScheduleFoundException(String msg) {
        super(msg);
    }
}
