/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jo
 */
public class MKDCommand implements ICommand {

    private CommandParameter[] params = null;
    private int n_par = 0;
    private Path position = null;
    private Set<PosixFilePermission> paramPermissions =null;
    private List<Path> pathResult = null;
    private List<String> string_result = null;
    private PosixFileAttributes my_perm;

    
    public MKDCommand(String current){
        position = Paths.get(current);
        pathResult = new ArrayList<>();
        string_result = new ArrayList<>();
    }
    
    @Override
    public boolean exec() throws CommandException {
        Path dir = null;
        synchronized(this){
            n_par = params.length;
            while(n_par>0)
            {
                n_par--;
                try {
                    my_perm = Files.readAttributes(position,PosixFileAttributes.class);
                    FileAttribute<Set<PosixFilePermission>> attr = (FileAttribute<Set<PosixFilePermission>>) my_perm.permissions();
                    dir = Paths.get(params[n_par].getValue());
                    Files.createDirectories(dir, attr);
                    pathResult.add(dir);
                } catch (IOException ex) {
                    throw new CommandException(CommandErrorType.MAKE_DIR_ERROR,this.getClass().getName(),Thread.currentThread().getStackTrace()[2].getMethodName(), "RM recursive Exception: "+ex.getMessage(), null);
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
    public void setCommandParameter(CommandParameter[] cpl) {
       if (cpl.length>=1)
       {
            params = cpl;
       }
       else
           System.err.println("Numero parametri minimo richiesto: 1, ricevuti"+cpl.length);
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
        
    }

    @Override
    public List<Path> getCommandResult() {
        return Collections.synchronizedList(pathResult);
    }

    @Override
    public String getCommandStringResult() {
        return Collections.synchronizedList(pathResult).toString();
    }

    @Override
    public void usage() {
        Utility.mf("mkdir <path>");
    }
}