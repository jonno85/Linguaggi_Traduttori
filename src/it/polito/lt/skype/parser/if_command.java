package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.CommandErrorType;
import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.ManagerException;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author jo
 */
public class if_command implements ICommand, IFlowCommandControl{
    private myVar name = null;
    private boolean close = false;
    private myVar condition = null;
    private LinkedList<ICommand> inside_command = null;
    private LinkedList<ICommand> inside_command_else = null;
    private LinkedList<ICommand> backup_command  = null;
    private Resolver ris = null;
    private VarManager manager = null;
    private ArrayList<String> token_list = null;
    
    
    public if_command(VarManager manager, ArrayList token_list)
    {
        //this.condition = condition;
        //Utility.mf(this.condition.getStringValue());
        this.close = false;
        inside_command = new LinkedList<ICommand>();
        inside_command_else = new LinkedList<ICommand>();
        this.token_list= token_list;
        this.manager = manager;
    }
    
    public boolean close_command(String step)
    {   
        return false;
    }
    

    @Override
    public void set_list_command(LinkedList inside_command) {
        this.inside_command = inside_command;
    }
    
    public void set_list_command_else(LinkedList inside_command_else) {
        this.inside_command_else = inside_command_else;
    }

    @Override
    public boolean isClose() {
        return close;
    }

    @Override
    public void print_parameters() {
//        Utility.mf("start: "+ ((condition!=null)?condition.toString():"null"));
//        Utility.mf("LISTA IF vuota?: "+((inside_command!=null)?"no":"si"));
//        Utility.mf("BLOCCO COMANDI IF: "+ inside_command.toString());
//        Utility.mf("LISTA IF ELSE vuota?: "+((inside_command_else!=null)?"no":"si"));
//        Utility.mf("BLOCCO COMANDI ELSE: "+ inside_command_else.toString());
//        Iterator it = inside_command.iterator();
//        while (it.hasNext()) {
//            ICommand c = (ICommand) it.next();
//            c.toString();
//            
//        }
    }

    // IF EXEC
    @Override
    public boolean exec() throws CommandException {
        ris = new Resolver(manager, token_list, "if_tmp_");
        try {
			condition = ris.exec();
		} catch (ManagerException e) {
			throw new CommandException(CommandErrorType.STATEMENT_ERROR,this.getClass().getName(),
                    Thread.currentThread().getStackTrace()[2].getMethodName(),
                    "IFCommand FAIL: " +e.getMessage(),e);
		}
        //Utility.mf("CONDIZIONE IF: "+condition);
        if(((Boolean)condition.getValue()).booleanValue()){
            for(ICommand c : inside_command){
                try {
                    //Utility.mf("Command if exec() iterator: "+c.toString());
                    c.exec();
                } catch (CommandException ex) {
                    throw new CommandException(CommandErrorType.STATEMENT_ERROR,this.getClass().getName(),
                            Thread.currentThread().getStackTrace()[2].getMethodName(),
                            "IF recursive Exception: "+ex.getMessage());
                }
            }
        }
        else{
            for(ICommand c : inside_command_else){
                try {
                    //Utility.mf("Command if exec() iterator: "+c.toString());
                    c.exec();
                } catch (CommandException ex) {
                    throw new CommandException(CommandErrorType.STATEMENT_ERROR,this.getClass().getName(),
                            Thread.currentThread().getStackTrace()[2].getMethodName(),
                            "IF recursive Exception: "+ex.getMessage());
                }
            }
        }
  
    	return true;
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommandParameter(CommandParameter[] cpl) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Path> getCommandResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCommandStringResult() {
        return "";
    }

    @Override
    public void usage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean close_command() {
        close = true;
        return true;
    }

    @Override
    public void setBackupCommand(LinkedList<ICommand> bak) {
        backup_command = bak;
    }

    @Override
    public LinkedList<ICommand> getBackupCommand() {
        ////Utility.mf("BACKUP COMMAND: "+backup_command.toString());
        //Utility.mf("INSIDE COMMAND: "+inside_command.toString());
        return backup_command;
    }
    
    public LinkedList<ICommand> getInsideCommand() {
        return inside_command;
    }
    
    public LinkedList<ICommand> getElseCommand() {
        return inside_command_else;
    }
    
}
