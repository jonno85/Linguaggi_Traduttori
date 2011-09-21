package it.polito.lt.skype.manager;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Path;
import java.util.List;


public class myVar implements ICommand{
private String name;
private int type;
private Object value;
private Operation pre_op = null;
private VarManager myVm = null;
public static final int _notInit = 0;
public static final int _int = 1;
public static final int _float = 2;
public static final int _string = 3;
public static final int _data = 4;
public static final int _bool = 5;


	public myVar(){
        }

        public myVar(String name, int type, Object value){
		this.name = name;
		this.type = type;
		this.value = value;
        }
        
        /**
         * si suppone che sia intero
         * @param name
         * @param value 
         */
        public myVar(String name, Object value){
		this.name = name;
		this.type = 1;
		this.value = value;
        }
        

	public myVar(int type, Object value){
		
		this.type = type;
		this.value = value;
	}

	public myVar(String name){
		this.name = name;
		this.type = _notInit;
		this.value = null;
	}
        
	public void setName(String name){	
		this.name = name;
	}
        
	public String toString(){
		String pre_op_s="#";
                if(pre_op!=null)
                    pre_op_s= "--{"+pre_op.toString()+"}";
                return name+"("+value+")."+type+pre_op_s;	
	}
        
	public String getName(){
		return name;	
	}
        
	public Integer getType(){
		return type;	
	}
        
	public void setType(int type){
		this.type=type;
	}
        
        public void setValue(Object value){
            this.value = value;
        }
    
        
    
        public void set(myVar src )
        {
            this.setName(src.getName());
            this.setType(src.getType());
            this.setValue(src.getValue());
        }
        
	public String getStringValue(){
		String valore = null;
		if(this.type==0)
			valore = new String("notInit");
		else
			valore = ""+value;		
		return valore;
	}
        
	public Object getValue(){
		return this.value;
	}
        
    @Override
    public boolean exec() throws CommandException {
        
        return true;
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

