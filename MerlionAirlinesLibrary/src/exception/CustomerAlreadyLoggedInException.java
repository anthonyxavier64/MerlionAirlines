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
public class CustomerAlreadyLoggedInException extends Exception {

    /**
     * Creates a new instance of <code>CustomerAlreadyLoggedInException</code>
     * without detail message.
     */
    public CustomerAlreadyLoggedInException() {
    }

    /**
     * Constructs an instance of <code>CustomerAlreadyLoggedInException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerAlreadyLoggedInException(String msg) {
        super(msg);
    }
}
