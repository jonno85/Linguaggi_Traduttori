package it.polito.lt.skype.command;

import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Path;
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
}
