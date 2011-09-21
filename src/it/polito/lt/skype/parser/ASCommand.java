package it.polito.lt.skype.parser;


import java.nio.file.Path;
import java.util.List;

import it.polito.lt.skype.command.*;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import java.util.ArrayList;
import java.util.Stack;

public class ASCommand implements ICommand {

	private String dst, var;
	private VarManager manager=null;
	private myVar nuova=null, src_var=null;
        private Stack stack = null;
        private List exps = null;
        
	
	public ASCommand(VarManager vm,String var, String dst){
		this.dst=dst;
		this.var = var;
		manager=vm;
		//this.src_var=var;
		nuova = new myVar();
		myVar src_var=manager.extractVar(var);
		nuova.set(src_var);
		nuova.setName(dst);
                stack = new Stack();
                this.exps = new ArrayList();
	}
        
        public ASCommand(VarManager vm, String var, ArrayList<String> exps){
		this.exps = exps;
		this.var = var;
		manager=vm;
                stack = new Stack();
	}
	
        public void setVars(ArrayList<String> list_vars){
            exps = list_vars;
            Utility.mf(exps.toString());
        }
	
	@Override
	public boolean exec() throws CommandException {
		myVar dst_var=manager.extractVar(dst);
		nuova=manager.extractVar(var);
		nuova.setName(dst);
		nuova.exec();
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

