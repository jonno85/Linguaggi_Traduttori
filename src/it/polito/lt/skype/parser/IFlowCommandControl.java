/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.parser;

import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.manager.myVar;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 *
 * @author jo
 */
public interface IFlowCommandControl extends ICommand{ 
    
    /**
     * Ogni comando dovrà avere una lista di comandi interni da eseguire
     * private LinkedList inside_command;
     */
    
    public void set_list_command(LinkedList inside_command);
    
    /*
     * comando per chiudere lo statement del controllo di flusso e 
     * impostare per i comandi che lo prevedono un valore finale o incremento
     */
    public boolean close_command(myVar step);
    
    public boolean close_command();
    
    public boolean isClose();
    
    public void print_parameters();
    
    public void setBackupCommand(LinkedList<ICommand> bak);
    
    public LinkedList<ICommand> getBackupCommand();
}
