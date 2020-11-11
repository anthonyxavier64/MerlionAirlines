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
public class InvalidChoiceException extends Exception {

    /**
     * Creates a new instance of <code>InvalidChoiceException</code> without
     * detail message.
     */
    public InvalidChoiceException() {
    }

    /**
     * Constructs an instance of <code>InvalidChoiceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidChoiceException(String msg) {
        super(msg);
    }
}
