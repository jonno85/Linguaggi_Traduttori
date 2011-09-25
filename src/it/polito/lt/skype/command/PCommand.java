package it.polito.lt.skype.command;

import it.polito.lt.skype.manager.ManagerException;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import it.polito.lt.skype.parser.ParserException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PCommand implements ICommand {
	//private StringBuffer text = new StringBuffer();
	private int n_substr = 0;
	
	private String var_name=null;
    private VarManager manager = null;
    private ArrayList<String> exps=null;
	private VarManager tmp_manager;
	private Stack<String> stack;
	private myVar result = null;	
        
        public PCommand(ArrayList<String> token_list, VarManager vm){
		
        manager = vm;
        tmp_manager = new VarManager();
        tmp_manager.setTmpName("pr_tmp_");
        tmp_manager.setMainManager(vm);
        stack = new Stack<String>();      
        this.exps=token_list;
	}
	
	public PCommand(){
	}
	
	@Override
	public boolean exec() throws CommandException {
            
            myVar op1 = null;
            myVar op2 = null;
        
             for(String s : exps){
                 if(!isOperator(s))
                     stack.push(s);
                 else
                 {
                     try {
						op2 = tmp_manager.extractVar(stack.pop());
					} catch (ManagerException e) {
						throw new CommandException(CommandErrorType.ASSIG_ERROR, this.getClass().getName(),
	                            Thread.currentThread().getStackTrace()[2].getMethodName(),
	                            "PCommand FAIL: "+e.getMessage(), e);
					}
                     try {
						op1 = tmp_manager.extractVar(stack.pop());
					} catch (ManagerException e) {
						throw new CommandException(CommandErrorType.ASSIG_ERROR, this.getClass().getName(),
	                            Thread.currentThread().getStackTrace()[2].getMethodName(),
	                            "PCommand FAIL: "+e.getMessage(), e);
					}
                     //Utility.mf("print: "+op1+" "+op2); 
                     //operazione con 2 operandi

                             result = tmp_manager.makeOper(op1, op2, s);
                            // Utility.mf("print: "+op1+" "+op2);
                             //Utility.mf("print: "+result);
                             tmp_manager.add_var(result);
                             stack.push(result.getName());
                     }    
             }
             try {
				result= tmp_manager.extractVar(stack.pop());
			} catch (ManagerException e) {
				throw new CommandException(CommandErrorType.ASSIG_ERROR, this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[2].getMethodName(),
                        "PCommand FAIL: "+e.getMessage(), e);
			}
             Utility.mf("# "+result.getStringValue());
             
             return true;
		
		
	
	}

	public boolean isOperator(String op){
        switch(op){
                case "+":
                    return true;
        }
        return false;
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
		return result.getStringValue()+"\n";
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub

	}

}
