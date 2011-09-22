package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.CommandErrorType;
import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;

import java.nio.file.Path;
import java.util.ArrayList;
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
    private String index_name = null;
    private String start_name = null;
    private String end_name = null;
    private String step_name = null;
    private boolean close = false;
    private LinkedList<ICommand> inside_command = null;
    private LinkedList<ICommand> backup_command  = null;
    private Resolver ris = null;
    private VarManager manager = null;
    private ArrayList<String> token_list = null;
 

	private VarManager common_vm = null;
    
    
    public for_command(String index, String start, String end, VarManager vm/*, ArrayList token_list*/)
    { 
    	this.common_vm = vm;
    	this.index_name=index;
    	this.start_name=start;
    	this.end_name=end;
        this.close = false;
        inside_command = new LinkedList<ICommand>();
        //this.token_list= token_list;
        //this.manager = manager;
    }
    
    public boolean close_command(String step)
    {   
        if(!close)
        {
            this.step_name = step;
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
    	//fissiamo gli estremi
     	index = common_vm.extractVar(index_name);
    	index.setValue(common_vm.extractVar(start_name).getValue());
        start = common_vm.extractVar(start_name);
        end = common_vm.extractVar(end_name);
        step=common_vm.extractVar(step_name);
        if(common_vm.isPos(step_name)){
           //Utility.mf("for exec: "+index+" "+end+" "+step);
           //Utility.mf("FOR COMMAND LIST: "+inside_command.toString());
            while(((Boolean)common_vm.makeLogicOper(index, end, "!=").getValue()).booleanValue())
            {
            	index = common_vm.extractVar(index_name);
            	start = common_vm.extractVar(start_name);
                end = common_vm.extractVar(end_name);
                step=common_vm.extractVar(step_name);
                
                for(ICommand c : inside_command){
                    try {
                    	//Utility.mf("step: "+ common_vm.extractVar(index_name));
                        c.exec();
                    } catch (CommandException ex) {
                        throw new CommandException(CommandErrorType.STATEMENT_ERROR,this.getClass().getName(),
                                Thread.currentThread().getStackTrace()[2].getMethodName(),
                                "FOR recursive Exception: "+ex.getMessage(), null);
                    }
                }
                //aggiornamento indice
                try {
					index.setValue(common_vm.makeOper(index, step,"+").getValue());
					common_vm.assig(index);
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
