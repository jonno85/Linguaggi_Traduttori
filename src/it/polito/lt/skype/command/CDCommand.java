package it.polito.lt.skype.command;

import it.polito.lt.skype.generated.parser.parser;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CDCommand implements ICommand {

	/*
     * vettore Params: 
     *          0 = asc|desc
     *          1 = file|directory|tutto
     *          2 = path src | rm target | ls target
     *      	3 = path dst cp | mv
     *      	4 =	data ls
     *          5 = permessi ls
     *          6 = dimensione
     */
	
	
	private CommandParameter[] params = null;
    private int tot_elem = 0;
    
    private parser p=null; 
    private CommandEnv currentEnv = null;
    
    private Path current = null;
 
    private Path target = null;
    
    private List<Path> result = null;
    private String string_result = null;
	private boolean goBack = false;

    
	 public CDCommand(CommandEnv currentEnv, parser p)
	    {
	            result = new ArrayList<>();       
	           /* position_src = Paths.get(".");
	            pattern_src = "";
	            target = Paths.get(".");
	            */
	            this.currentEnv=currentEnv;
	            this.current=currentEnv.getCurrentPath();
	            this.p=p;
	    }
	
		@Override
		public void setCommandParameterAt(int index, CommandParameter cp) {
			if(index<=params.length)
				params[index]=cp;
		}

		@Override
		public CommandParameter getCommandParameterAt(int index) {
			if(index<=params.length)
				return params[index];
			return null;
		} 
	 
	@Override
	public boolean exec() throws CommandException {
		current=currentEnv.getCurrentPath();
		if(goBack)
				currentEnv.setCurrentPath(currentEnv.getCurrentPath().getParent());
		else{
			target=current.resolve(target);
			Utility.mf("CD: "+target.toString());
			if(Files.exists(target)){
				currentEnv.setCurrentPath(target);
			}
			else
				return false;
		}
		currentEnv.appOutputString(">"+target.toString()+"\n");
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
		params=cpl;
		Utility.mf("CD SETCOMMANDPARAMETER: "+params.toString());
		if(cpl[2]!=null){
			if(cpl[2].getValue().equals("<<"))
				goBack  = true;
			target= Paths.get(cpl[2].getValue()).normalize();
		}
		else
			target=current;
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
		return ">"+target.toString()+"\n";
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void setAdditionalParameters(VarManager manager, ArrayList<String> token_list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public ArrayList<String> getTokenListAt(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTokenListAt(int index, ArrayList<String> list) {
		
	}

}
