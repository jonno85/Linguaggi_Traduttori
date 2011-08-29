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
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jo
 */
public class MKDCommand implements ICommand {

    private Path currentPath = null;

    private CommandParameter[] params = null;
    private FileEngine eng = new FileEngine();
    private int n_par = 0;
    private boolean goAsc;
    private boolean includeFolders;
    private Path paramPath;
    private Path position = null;
    private String pattern = null;
    private Set<PosixFilePermission> paramPermissions =null;
    private FileTime paramLastModTime =null;
    private boolean isRegFolder;
    private boolean isFile;
    private List<Path> pathResult = null;
    private List<String> string_result = null;

    
    public MKDCommand(Path current){
        position = current;
        pathResult = new ArrayList<>();
        string_result = new ArrayList<>();
    }
    
    @Override
    public boolean exec() throws CommandException {
        Path dir = null;
        int n_par = params.length;
        while(n_par>0)
        {
            n_par--;
            Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
            FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
            try {
                dir = Paths.get(params[n_par].getValue());
                Files.createDirectories(dir, attr);
                pathResult.add(dir);
            } catch (IOException ex) {
                ex.printStackTrace();
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
        return pathResult;
    }

    @Override
    public String getCommandStringResult() {
        return pathResult.toString();
    }

    @Override
    public void usage() {
        System.err.println("mkdir <path>");
        System.exit(-1);
    }
    
}
