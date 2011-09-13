package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
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
public class if_command implements ICommand, IFlowCommandControl{
    private myVar name = null;
    private boolean close = false;
    private myVar condition = null;
    private LinkedList<LinkedList<ICommand>> inside_command;
    private LinkedList<LinkedList<ICommand>> inside_command_else;
    private TreeSet inside_tree_command;
    
    
    public if_command(myVar condition)
    {
        this.condition = condition;
        Utility.mf(this.condition.getStringValue());
        this.close = false;
    }
    
    public boolean close_command(myVar step)
    {   
        return false;
    }
    
    public void exec_alter_flow_command(){
    
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
        Utility.mf("start: "+
                ((condition!=null)?condition.getStringValue():"null"));
        
        Iterator it = inside_command.iterator();
        while (it.hasNext()) {
            ICommand c = (ICommand) it.next();
            c.toString();
            
        }
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

    @Override
    public boolean close_command() {
        close = true;
        return true;
    }
    
}
