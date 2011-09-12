package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.myVar;

import java.nio.file.Path;
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
    private LinkedList<LinkedList<ICommand>> inside_command;
    
    
    public if_command(myVar condition)
    {
        this.condition = condition;
        this.close = false;
    }
    
    public boolean close_command(myVar step)
    {   
        if(!close)
        {
            this.close = true;
            return close;
        }
        return false;
    }
    
    public void exec_alter_flow_command(){
    
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
        Utility.mf("start: "+
                ((condition!=null)?condition.getStringValue():"null"));
    }

    @Override
    public boolean exec() {
        return false;
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
    
    
    
}
