package it.polito.lt.skype.parser;

import it.polito.lt.skype.manager.VarManager;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;

public class DecCommand implements ICommand {

	private ArrayList<ASCommand> decList =null;
	
	public DecCommand(ArrayList<ASCommand> decList) {
		this.decList = decList;
	}
	
	public ArrayList<ASCommand> getDecList() {
		return decList;
	}

	public void setDecList(ArrayList<ASCommand> decList) {
		this.decList = decList;
	}

	@Override
	public boolean exec() throws CommandException {
		Utility.mf("lista dichiarazioni"+decList.toString());
		for(ASCommand c: decList){
			//Utility.mf("elemento di decList: "+c.toString());
			c.exec();
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
		return "";
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub

	}

    @Override
    public void setAdditionalParameters(VarManager manager, ArrayList<String> token_list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
