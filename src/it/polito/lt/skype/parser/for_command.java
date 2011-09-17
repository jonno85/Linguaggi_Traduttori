package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.CommandErrorType;
import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author jo
 */
public class for_command implements ICommand, IFlowCommandControl{
    private myVar index = null;
    private myVar start = null;
    private myVar end = null;
    private myVar step = null;
    private boolean close = false;
    private LinkedList<ICommand> inside_command = null;
    private LinkedList<ICommand> backup_command  = null;
 

	private VarManager common_vm = null;
    
    
    public for_command(myVar index, myVar start, myVar end, VarManager vm)
    {
        this.index = index;
        this.start = start;
        this.end = end;
        this.common_vm = vm;
        this.close = false;
        inside_command = new LinkedList<ICommand>();
    }
    
    public boolean close_command(myVar step)
    {   
        if(!close)
        {
            this.step = step;
            this.close = true;
            return close;
        }
        return false;
    }


    @Override
    public void set_list_command(LinkedList inside_command) {
        this.inside_command = inside_command;
        
    }

    @Override
    public boolean isClose() {
        return close;
    }

    @Override
    public void print_parameters() {
        /*Utility.mf("print PARAMETERS");
        Utility.mf("start: "+
                ((start!=null)?start.getStringValue():"null")+
                "\nend: "+
                ((end!=null)?end.getStringValue():"null")+
                "\nstep: "+
                ((step!=null)?step.getStringValue():"null"));
        Iterator it = inside_command.iterator();
        while (it.hasNext()) {
            ICommand c = (ICommand) it.next();
            c.toString();
            
        }*/
    }

    @Override
    public boolean exec() throws CommandException {
        //indice di partenza
        if(common_vm.isPos(step)){
            index = common_vm.extractVar(start.getName());
            while(((Boolean)common_vm.makeLogicOper(index, end, "!=").getValue()).booleanValue())
            {
                for(ICommand c : inside_command){
                    try {
                        c.exec();
                    } catch (CommandException ex) {
                        throw new CommandException(CommandErrorType.STATEMENT_ERROR,this.getClass().getName(),
                                Thread.currentThread().getStackTrace()[2].getMethodName(),
                                "FOR recursive Exception: "+ex.getMessage(), null);
                    }
                }
                //aggiornamento indice
                index = common_vm.makeOper(index, step,"+");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void usage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean close_command() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBackupCommand(LinkedList<ICommand> bak) {
        backup_command = bak;
    }

    @Override
    public LinkedList<ICommand> getBackupCommand() {
        return backup_command;
    }
    public LinkedList<ICommand> getInsideCommand() {
 		return inside_command;
 	}

 	public void setInsideCommand(LinkedList<ICommand> inside_command) {
 		this.inside_command = inside_command;
 	}

 	
 	
    
    
}
