/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.clcalculator.exception;

/**
 *
 * @author Viswanathan Manickam
 */
public class CLException extends Exception {

    public final static String PARANTHESIS_ERROR = "Missing paranthesis. Please check your input string. Press 'h' for help";
    public final static String SYNTAX_ERROR = "The input command syntax is not valid. Please verify. Press 'h' for help";
    public final static String COMMAND_ERROR = "The input command is not valid. Please verify. Press 'h' for help";
    public final static String DIVIDE_ZERO_ERROR = "Divide by zero error. Please check the input";

    public CLException(String string) {
        super(string);
    }

}
