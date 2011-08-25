package it.polito.lt.skype.command;


import java.nio.file.Path;
import java.util.List;



public interface ICommand {
    
        public int debug = 1;

	public boolean exec() throws CommandException;
	
	public void setCommandParameter(CommandParameter[] cpl);
        
        public void setCommandParameter(CommandParameter[][] cpl);
	
	public List<Path> getCommandResult();
        
        public String getCommandStringResult();
	
	public void usage();
}
