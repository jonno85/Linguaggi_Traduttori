package it.polito.lt.skype.parser;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.VarManager;

public class CScript implements ICommand {
	private LinkedList<ICommand> list=null;
        private VarManager myVm = null;
	private VarManager bak_myVm = null;
	private String stringResult="";
	
	public CScript(LinkedList<ICommand> l, VarManager bak_vm)
	{
		list=l;
                myVm = new VarManager();
                bak_myVm = bak_vm;
	}
        
        public VarManager getScriptVM(){
            return myVm;
        }
        
        public void setBakVm(VarManager bak_vm){
            bak_myVm = bak_vm;
        }
        
        public VarManager getBakVm(){
            return bak_myVm;
        }
        
        
        
	
	@Override
	public boolean exec() throws CommandException {
		Utility.mf("lista exec: "+list.toString());
		for(ICommand c: list)
		{
			Utility.mf("elemento: "+c.toString());
			c.exec();
			stringResult=stringResult+""+c.getCommandStringResult();
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return stringResult;
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub

	}

}
