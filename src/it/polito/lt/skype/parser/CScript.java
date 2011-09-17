package it.polito.lt.skype.parser;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;

public class CScript implements ICommand {
	private LinkedList<ICommand> list=null;
	
	
	public CScript(LinkedList<ICommand> l)
	{
		list=l;
	}
	
	@Override
	public boolean exec() throws CommandException {
		for(ICommand c: list)
		{
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
		return null;
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub

	}

}
