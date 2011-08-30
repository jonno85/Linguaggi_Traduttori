/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.parser;

/**
 *
 * @author jo
 */
public class ParserException extends Exception{
    private static final long serialVersionUID = 1L;
    private ParserErrorType pe; // a unique id
    private String classname; // the name of the class
    private ParserErrorType cet=null; //type of exception
    private String method; // the name of the method
    private String message; // a detailed message 
    private String separator = "\n"; // line separator
    
    public ParserException(ParserErrorType pe, String classname, String method, 
	    String message) {
	    this.pe        = pe;
	    this.classname = classname;
	    this.method    = method;
	    this.message   = message;
    }
    
}
