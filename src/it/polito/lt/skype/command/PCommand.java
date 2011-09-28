package it.polito.lt.skype.command;

import it.polito.lt.skype.manager.ManagerException;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import it.polito.lt.skype.parser.ParserException;
import it.polito.lt.skype.parser.Resolver;

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
	private Resolver res = null;
        
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
	public void setCommandParameterAt(int index, CommandParameter cp) {

	}

	@Override
	public CommandParameter getCommandParameterAt(int index) {

		return null;
	}
	
	@Override
	public boolean exec() throws CommandException {
		result = null;
		
        res = new Resolver(manager, exps, "print_tmp");
        try {
			result = res.exec();
		} catch (ManagerException e) {
			throw new CommandException(CommandErrorType.PRINT_ERROR, this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[2].getMethodName(),
                    "PCommand FAIL: "+e.getMessage(), e);
		}
		
		

             Utility.mf("# "+result.getStringValue());
             
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
		return result.getStringValue()+"\n";
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
