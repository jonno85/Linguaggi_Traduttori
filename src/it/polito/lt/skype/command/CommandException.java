package it.polito.lt.skype.command;

import java.io.IOException;

import it.polito.lt.skype.manager.ManagerException;
import it.polito.lt.skype.parser.ParserException;


public class CommandException extends Exception {
	private static final long serialVersionUID = 1L;
        private CommandErrorType ce; // a unique id
        private String classname; // the name of the class
        private CommandErrorType cet=null; //type of exception
        private String method; // the name of the method
        private String message; // a detailed message 
        private Exception previous = null; // the exception which was caught

        private String separator = "\n"; // line separator
	  
	  public CommandException(CommandErrorType ce, String classname, String method, 		  
		String message, Exception previous) {
	    super(message);
		this.ce        = ce;
	    this.classname = classname;
	    this.method    = method;
	    this.message   = message;
	    this.previous  = previous;
	  }  
	
	  
	


	public Exception getPrevious() {
		return previous;
	}





	public CommandException(CommandErrorType ce, String classname, String method, 
			    String message) {
			    this.ce        = ce;
			    this.classname = classname;
			    this.method    = method;
			    this.message   = message;
			    
			  }  
	    
//	  public String traceBack() {
//	    return traceBack("\n");
//	  }  
//
//	  public String traceBack(String sep) {
//	    this.separator = sep;
//	    int level = 0;
//	    Exception e = this;
//	    
//
//	    String text = line("Calling sequence (top to bottom)");
//	    while (e != null) {
//	      level++;
//	      text += line("--level " + level + "--------------------------------------");
//	      text += line("Class/Method: " + e.getClass() );
//	      text += line("Message     : " + e.getMessage()+": "+e.getLocalizedMessage());
//	      e = this.previous;
//	    }  
//	    
//	 
//	    
//	    
//	    return text;
//	  }  
//
//	  private String line(String s) {
//	    return s + separator;
//	  }  

}
