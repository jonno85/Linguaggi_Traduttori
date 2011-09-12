package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.myVar;

import java.util.LinkedList;

/**
 *
 * @author jo
 */
public class for_command implements IFlowCommandControl{
    private myVar name = null;
    private myVar start = null;
    private myVar end = null;
    private myVar step = null;
    private boolean close = false;
    private LinkedList inside_command;
    private LinkedList PROVA;
    
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
        Utility.mf("start: "+
                ((start!=null)?start.getStringValue():"null")+
                "\nend: "+
                ((end!=null)?end.getStringValue():"null")+
                "\nstep: "+
                ((step!=null)?step.getStringValue():"null"));
    }

    @Override
    public boolean exec() {
        return false;
    }
    
    
    
}
