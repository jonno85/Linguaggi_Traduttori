/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.parser;

import it.polito.lt.skype.manager.myVar;
import java.util.LinkedList;

/**
 *
 * @author jo
 */
public interface IFlowCommandControl { 
    
    /**
     * Ogni comando dovrà avere una lista di comandi interni da eseguire
     * private LinkedList inside_command;
     */
    
    public boolean exec();
    
    public void set_list_command(LinkedList inside_command);
    
    /*
     * comando per chiudere lo statement del controllo di flusso e 
     * impostare per i comandi che lo prevedono un valore finale o incremento
     */
    public boolean close_command(myVar step);
    
    public boolean isClose();
    
    public void print_parameters();
}