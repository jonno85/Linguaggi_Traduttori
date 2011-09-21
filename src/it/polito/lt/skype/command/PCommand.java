package it.polito.lt.skype.command;

import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import it.polito.lt.skype.parser.ParserException;

import java.nio.file.Path;
import java.util.List;

public class PCommand implements ICommand {
	//private StringBuffer text = new StringBuffer();
	private int n_substr = 0;
	private myVar mv = null;
	private String var_name=null;
        private VarManager myVm = null;
	
        
        public PCommand(String name, VarManager vm){
		var_name = name;
                myVm = vm;
	}
	
	public PCommand(){
	}
	
	@Override
	public boolean exec() throws CommandException {
            //stampiamo solo il valore di una variabile
            if(myVm.extractVar(var_name)!=null)
                Utility.mf(myVm.extractVar(var_name).getStringValue());
            else
                /*var_name include una stringa concatenata di sotto strighe che 
                non è riconducibile ad una variabile singola */
                Utility.mf(var_name);
            
            /*
                String vvalue="NULL";
            myVar tmp = null;
            if(mv!=null){
                 tmp = myVm.extractVar(mv.getName());
                //Utility.mf(tmp.getStringValue());
                //Utility.mf(tmp.toString());
            }*/
            return true;
	}

	@Override
	public boolean exec_from_prev_result(List<Path> stream)
			throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCommandParameter(CommandParameter[] cpl)
			throws ParserException {
		/*n_substr = cpl.length;
		int i = 0;
		while(i < n_substr){
			if(cpl[i].getValue().equalsIgnoreCase("#"))
				i++;
			if(cpl[i].getValue().contains("$"))
				text.append(common_vm.extractVar(cpl[i++].getValue()).getStringValue());
			else
				text.append(cpl[i++].getValue());
		}*/
		//var_name=cpl[0].getValue();

	}

	@Override
	public void setCommandParameter(CommandParameter[][] cpl)
			throws ParserException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Path> getCommandResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommandStringResult() {
		return null;
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub

	}

}
