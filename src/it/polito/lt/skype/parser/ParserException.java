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
	private String ce;
    
    public ParserException(ParserErrorType pe, String classname, String method, 
	    String message) 
    {
    	super(message);

	    this.pe        = pe;
	    this.setClassname(classname);
	    this.setMethod(method);
	    this.message   = message;
    }

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getCe() {
		return ce;
	}

	public void setCe(String ce) {
		this.ce = ce;
	}
    
}
