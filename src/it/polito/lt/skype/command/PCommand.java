package it.polito.lt.skype.command;

import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.parser.ParserException;

import java.nio.file.Path;
import java.util.List;

public class PCommand implements ICommand {
	private StringBuffer text = new StringBuffer();
	private int n_substr = 0;
	private VarManager common_vm = null;

	public PCommand(VarManager vm){
		this.common_vm = vm;
	}
	
	public PCommand(){
	}
	
	@Override
	public boolean exec() throws CommandException {
		Utility.mf(text.toString());
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
		n_substr = cpl.length;
		int i = 0;
		while(i < n_substr){
			if(cpl[i].getValue().equalsIgnoreCase("#"))
				i++;
			if(cpl[i].getValue().contains("$"))
				text.append(common_vm.extractVar(cpl[i++].getValue()).getStringValue());
			else
				text.append(cpl[i++].getValue());
		}

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
		return text.toString();
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub

	}

}
