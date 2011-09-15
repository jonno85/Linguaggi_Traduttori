package it.polito.lt.skype.manager;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Path;
import java.util.List;


public class Operation implements ICommand{
	private myVar result;
        private myVar op1 = null;
        private myVar op2 = null;
        private String sign = null;
        
        
        public void setArgument(myVar a, myVar b, String segno)
        {
            op1 = a;
            op2 = b;
            sign = segno;
        }
        
	public Operation(){
		result = null;
		}
	public myVar Ioper(myVar a, myVar b, String segno){
		result = null;
                setArgument(a, b, segno);
		//Integer op1 = (Integer)(a.getValue());
		//Integer op2 = (Integer)(b.getValue());
		System.out.println("valori "+op1+" "+op2);
		int type = a.getType(); 
		if(segno.compareTo("+")==0){
			System.out.println("operazione somma");
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    + ((Integer)op2.getValue()).intValue()));
		}
		if(segno.compareTo("-")==0) 
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    - ((Integer)op2.getValue()).intValue()));
		if(segno.compareTo("*")==0)
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    * ((Integer)op2.getValue()).intValue()));
		if(segno.compareTo("/")==0)
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    / ((Integer)op2.getValue()).intValue()));
		return result;

	}
	public myVar Foper(myVar a, myVar b, String segno){
		result = null;
                setArgument(a, b, segno);
		Float op1 = (Float)(a.getValue());
		Float op2 = (Float)(b.getValue());
		int type = a.getType();
		if(segno.compareTo("+")==0)
			result = new myVar("",type,new Float(op1.floatValue()+op2.floatValue()));
		if(segno.compareTo("-")==0) 
			result = new myVar("",type,new Float(op1.floatValue()-op2.floatValue()));
		if(segno.compareTo("*")==0)
			result = new myVar("",type,new Float(op1.floatValue()*op2.floatValue()));
		if(segno.compareTo("/")==0)
			result = new myVar("",type,new Float(op1.floatValue()/op2.floatValue()));
		return result;
	}
	public myVar Soper(myVar a, myVar b, String segno){
		result = null;
		int type = a.getType();
		if(segno.compareTo("+")==0) //rimosso costruttore String - check!
			result = new myVar("",type,(String)(a.getValue()+(String)(b.getValue())));	
		return result;
	}
	public myVar Boper(myVar a, myVar b, String segno){
		result = null;
		int type = a.getType();
		if(segno.compareTo("&")==0 | segno.compareTo("&&")==0) //rimosso costruttore boolean - check!
			result = new myVar("",type, ((Boolean)(a.getValue())&((Boolean)(b.getValue()))));
		if(segno.compareTo("|")==0 || segno.compareTo("||")==0) 
			result = new myVar("",type, ((Boolean)(a.getValue())|((Boolean)(b.getValue()))));
		return result;	
	}
	public myVar BNoper(myVar a, String segno){
		int type = a.getType();
		if(segno.compareTo("!")==0) 
			result = new myVar("",type, (!((Boolean)(a.getValue()))));
		return result;	
	}
	public myVar Neg(myVar a){
		result = a;//null;
		int type = a.getType();
		if(type==1)
			result = new myVar("",type,new Integer(-(Integer)a.getValue()));
		if(type==2)
			result = new myVar("",type,new Float(-(Float)a.getValue()));
		return result;
	}

    @Override
    public boolean exec() throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommandParameter(CommandParameter[] cpl) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Path> getCommandResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCommandStringResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void usage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
}
