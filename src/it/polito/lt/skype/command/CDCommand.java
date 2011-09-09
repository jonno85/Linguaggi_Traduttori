package it.polito.lt.skype.command;

import it.polito.lt.skype.parser.ParserException;

import java.io.File;
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
    
    
    private Path current = null;
 
    private Path target = null;
    
    private List<Path> result = null;
    private String string_result = null;

    
	 public CDCommand(String current)
	    {
	            result = new ArrayList<>();       
	           /* position_src = Paths.get(".");
	            pattern_src = "";
	            target = Paths.get(".");
	            */
	            this.current=Paths.get(current);
	            
	    }
	
	@Override
	public boolean exec() throws CommandException {
		target=current.resolve(target);
		if(Files.exists(target)){
			return true;
		}
		else
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
		if(cpl[2]!=null)
			target= Paths.get(cpl[2].getValue()).normalize();
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
		return target.toString();
	}

	@Override
	public void usage() {
		// TODO Auto-generated method stub
		
	}

}
