package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.myVar;

import java.util.LinkedList;

/**
 *
 * @author jo
 */
public class if_command implements IFlowCommandControl{
    private myVar name = null;
    private boolean close = false;
    private myVar condition = null;
    private LinkedList<LinkedList<>> inside_command;
    
    
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
    
    
    
}
