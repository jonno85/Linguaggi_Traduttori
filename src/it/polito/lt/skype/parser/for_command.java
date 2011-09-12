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
public class for_command implements ICommand, IFlowCommandControl{
    private myVar name = null;
    private myVar start = null;
    private myVar end = null;
    private myVar step = null;
    private boolean close = false;
    private LinkedList<ICommand> inside_command;
    
    
    public for_command(myVar name, myVar start, myVar end)
    {
        this.name = name;
        this.start = start;
        this.end = end;
        this.close = false;
        /*
        IFlowCommandControl fc = null;
        
        PROVA = new LinkedList();
        
        //PROVA.add(new for_command(new myVar(), new myVar(), new myVar()));
        //Utility.mf("inserito primo elemento");
        //PROVA.toString();
        fc = ((IFlowCommandControl) PROVA.listIterator().next());
        */
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
        Utility.mf("print PARAMETERS");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
