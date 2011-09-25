package it.polito.lt.skype.manager;


public class ManagerException extends Exception {
	private static final long serialVersionUID = 1L;
        private ManagerErrorType ce; // a unique id
        private String classname; // the name of the class
        private ManagerErrorType cet=null; //type of exception
        private String method; // the name of the method
        private String message; // a detailed message 
        private ManagerException previous = null; // the exception which was caught
        private String separator = "\n"; // line separator
	  
	  public ManagerException(ManagerErrorType ce, String classname, String method, 
	    String message, ManagerException previous) {
		super(message);
		this.setCe(ce);
	    this.setClassname(classname);
	    this.setMethod(method);
	    this.message   = message;
	    this.previous  = previous;
	  }  
	    
//	  public String traceBack() {
//	    return traceBack("\n");
//	  }  

//	  public String traceBack(String sep) {
//	    this.separator = sep;
//	    int level = 0;
//	    ManagerException e = this;
//	    String text = line("Calling sequence (top to bottom)");
//	    while (e != null) {
//	      level++;
//	      text += line("--level " + level + "--------------------------------------");
//	      text += line("Class/Method: " + e.getClassname() + "/" + e.getMethod());
//	      text += line("Id          : " + e.getCe());
//	      text += line("Message     : " + e.message);
//	      e = e.previous;
//	    }  
//	    return text;
//	  }  

	  private String line(String s) {
	    return s + separator;
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

	public ManagerErrorType getCe() {
		return ce;
	}

	public void setCe(ManagerErrorType ce) {
		this.ce = ce;
	}  

}
