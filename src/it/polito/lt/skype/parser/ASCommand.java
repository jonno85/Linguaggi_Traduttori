package it.polito.lt.skype.parser;

import java.nio.file.Path;
import java.util.List;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;

public class ASCommand implements ICommand {

	private String dst;
	private VarManager manager=null;
	private myVar nuova=null, src_var=null;
	
	public ASCommand(VarManager vm,myVar src){
		manager=vm;
		this.src_var=src;
	}
	
	
	@Override
	public boolean exec() throws CommandException {
		myVar dst_var=manager.extractVar(dst);
		nuova.setName(dst_var.getName());
		manager.assig(nuova);
		return true;
		//la set sulla variabile di destinazione di tutti i valori della variabile sorgente
		//va sempre fatta ad ogni exec, ma va anche lasciata sulla setcommandparameter?
		//rivedere con debug
	}
	
	public myVar getVar(){
		return nuova;
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
		
		dst=cpl[0].getValue();
		nuova = new myVar();
		//myVar src_var=manager.extractVar(src);
		nuova.set(src_var);
		nuova.setName(dst);

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
