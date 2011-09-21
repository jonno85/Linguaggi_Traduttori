package it.polito.lt.skype.parser;


import java.nio.file.Path;
import java.util.List;

import it.polito.lt.skype.command.*;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.manager.Operation;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import java.util.ArrayList;
import java.util.Stack;

public class ASCommand implements ICommand {

	private String dst, var;
	private VarManager manager=null;
        private VarManager tmp_manager=null;
	private myVar nuova=null, src_var=null;
        private Stack<String> stack = null;
        private List<String> exps = null;
        private boolean declaration = false;
        private Operation oper = null;
        
	/*
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
	}*/
        
        public ASCommand(VarManager vm, String var, ArrayList<String> exps){
		this.exps = exps;
		this.var = var;
		manager = vm;
                tmp_manager = new VarManager();
                tmp_manager.setTmpName("as_tmp_");
                stack = new Stack();                
	}
	
        public void setVars(ArrayList<String> list_vars){
            exps = list_vars;
            Utility.mf(exps.toString());
        }
        
        public void setDeclaration(boolean value){
            declaration = value;
        }
        
        public boolean  getDeclaration(){
            return declaration;
        }
        
        public boolean isUnaryOperator(String op){
            switch(op){
                    case "--":
                        return true;
                    case "!!":
                        return true;
            }
            return false;
        }
        
        public boolean isOperator(String op){
            switch(op){
                    case "+":
                        return true;
                    case "-":
                        return true;
                    case "*":
                        return true;
                    case "/":
                        return true;
                    case "<":
                        return true;
                    case "<=":
                        return true;
                    case ">":
                        return true;
                    case ">=":
                        return true;
                    case "=":
                        return true;
                    case "==":
                        return true;
                    case "!=":
                        return true;
                    case "&":
                        return true;
                    case "|":
                        return true;
                    case "!":
                        return true;
                    case "--":
                        return true;
                    case "!!":
                        return true;
            }
            return false;
        }
	
        public myVar extractVar(String name){
            if(manager.extractVar(name)!=null)
                return manager.extractVar(name);
            if(tmp_manager.extractVar(name)!=null)
                return tmp_manager.extractVar(name);
            return null;
        }
        
	@Override
	public boolean exec() throws CommandException {
            myVar op1 = null;
            myVar op2 = null;
            myVar result = null;
                for(String s : exps){
                    if(!isOperator(s))
                        stack.push(s);
                    else
                    {
                        op1 = extractVar(stack.pop());
                        if(!isUnaryOperator(s))
                        {
                            //operazione con 2 operandi
                            try {
                                op2 = extractVar(stack.pop());
                                result = tmp_manager.makeOper(op1, op2, s);
                                tmp_manager.add_var(result);
                                stack.push(result.getName());
                            } catch (ParserException ex) {
                                ex.printStackTrace();
                            }
                        }
                        else
                        { 
                            //operazione con 1 operando
                            result = tmp_manager.makeSOper(op1, s);
                            tmp_manager.add_var(result);
                            stack.push(result.getName());
                        }
                    }
                }
                Utility.mf("nome "+result.getName()+" valore finale: "+result.getStringValue());
                
                if(declaration)
                    manager.add_var(result);
                else
                    manager.assig(result);
                
                return true;
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

