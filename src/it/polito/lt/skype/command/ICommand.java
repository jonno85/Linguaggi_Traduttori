package it.polito.lt.skype.command;

import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;



public interface ICommand {
    
        public int debug = 1;

	public boolean exec() throws CommandException;
        
        public boolean exec_from_prev_result(List<Path> stream) throws CommandException;
	
	public void setCommandParameter(CommandParameter[] cpl) throws ParserException;
        
        public void setCommandParameter(CommandParameter[][] cpl) throws ParserException;
	
	public List<Path> getCommandResult();
        
        public String getCommandStringResult();
	
	public void usage();
        
        public void setAdditionalParameters(VarManager manager, ArrayList<String> token_list);
        
        public void setCommandParameterAt(int index, CommandParameter cp);
        
        public CommandParameter getCommandParameterAt(int index);
}
