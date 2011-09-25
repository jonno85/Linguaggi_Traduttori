/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.ManagerException;
import it.polito.lt.skype.manager.Operation;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author jo
 */
public class Resolver {
	private VarManager manager=null;
        private VarManager tmp_manager=null;
        private Stack<String> stack = null;
        private List<String> exps = null;
        private Operation oper = null;
        
        
        public Resolver(VarManager vm, ArrayList<String> exps, String tmp_var){
		this.exps = exps;
		manager = vm;
                tmp_manager = new VarManager();
                tmp_manager.setTmpName(tmp_var);
                tmp_manager.setMainManager(vm);
                stack = new Stack();                
        }
	
        public void setVars(ArrayList<String> list_vars){
            exps = list_vars;
            Utility.mf(exps.toString());
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

	public myVar exec() throws ManagerException{
            myVar op1 = null;
            myVar op2 = null;
            myVar result = null;
            //Utility.mf(exps.toString());
            	for(String s : exps){
                    if(!isOperator(s))
                        stack.push(s);
                    else
                    {
                    	 
                    	
                        op1 = tmp_manager.extractVar(stack.pop());
                        //Utility.mf("RESOLVE: OP1: "+op1);
                        if(!isUnaryOperator(s))
                        {
                            //operazione con 2 operandi
                            
                            	
                                op2 = tmp_manager.extractVar(stack.pop());
                               // Utility.mf("RESOLVE: OP2: "+op2);
                                result = tmp_manager.makeOper(op1, op2, s);
                                tmp_manager.add_var(result);
                                stack.push(result.getName());
                            
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
                result= tmp_manager.extractVar(stack.pop());
                //Utility.mf("RESULT: "+result);
                return result;
	}
	

	
	public String toString(){
		return exps.toString();
	}

}
