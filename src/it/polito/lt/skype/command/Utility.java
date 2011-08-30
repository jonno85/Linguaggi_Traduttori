/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.command;

import it.polito.lt.skype.parser.ParserException;
import java.io.IOException;

/**
 *
 * @author jo
 */
public class Utility {
	
    public static boolean debug=true;
    
    public static void mf(String s)
    {
        if(debug)
        	System.out.println(s);
    }
    
    public static void mf(UnsupportedOperationException uoe)
    {
        System.out.println("Warning: "+uoe.getMessage());
    }
    public static void mf(IOException ioe)
    {
        System.out.println("Warning: "+ioe.getMessage());
    }
    public static void mf(CommandException ce)
    {
        System.out.println("Error: "+ce.getMessage()+"\nClass: "+ce.getClass()+"\nMethod: "+ce.getStackTrace()[0].getMethodName());
    }
    public static void mf(ParserException ce)
    {
        System.out.println("Error: "+ce.getMessage()+"\nClass: "+ce.getClass()+"\nMethod: "+ce.getStackTrace()[0].getMethodName());
    }
    
}
