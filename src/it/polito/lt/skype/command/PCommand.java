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
	
        
        public PCommand(myVar mv, VarManager vm){
		this.mv = mv;
                myVm = vm;
	}
	
	public PCommand(){
	}
	
	@Override
	public boolean exec() throws CommandException {
		String vvalue="NULL";
		myVar tmp = null;
                
		if(mv!=null){
                     tmp = myVm.extractVar(mv.getName());
                    //Utility.mf(tmp.getStringValue());
                    Utility.mf(tmp.toString());
                }
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
