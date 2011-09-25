/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.command;

import it.polito.lt.skype.parser.ParserErrorType;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.attribute.FileTime;
import java.nio.file.FileSystemLoopException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import static java.nio.file.StandardCopyOption.*;

/**
 *
 * @author jo
 */
public class MVCommand implements ICommand{
    
	private CommandEnv env;
    private Path currentPath=null;
    private CommandParameter[] params = null;
    private int tot_elem = 0;
    
    private Path paramPath_src;
    private Path position_src = null;
    private String pattern_src = null;
    private Path target = null;
    
    private List<Path> result = null;
    private String string_result = null;
    private Boolean dir = null;
    private boolean isFile_src;
    private boolean isRegFolder_src;

    /*
         * vettore Params: 
         *          0 = file|directory|tutto src
         *          1 = file|directory|tutto dst
         */
   
    /*
         * vettore Params:                                          used:
         *          0 = asc|desc
         *          1 = file|directory|tutto                       
         *          2 = path src | rm target | ls target            *
         *          3 = path dst cp | mv                            *
         *          4 =	data ls
         *          5 = permessi ls
         *          6 = dimensione
         */
        
    public MVCommand(CommandEnv currentEnv)
    {
            env=currentEnv;
    		result = new ArrayList<>();       
            position_src = currentPath = env.getCurrentPath();
            pattern_src = "";
    }
    
    public static void moveFile(Path source, Path target){
        Utility.mf("target"+target.toString());
        Utility.mf("source"+source.toString());
        //if(Files.notExists(target))
        //{

            try{
                Files.move(source, target, REPLACE_EXISTING, ATOMIC_MOVE);
            }catch(  UnsupportedOperationException uoe){
                Utility.mf(uoe);
            }catch(IOException ioe){
                Utility.mf(ioe);
            }
        //}
    }
    
    @Override
    public boolean exec() throws CommandException {
        
        DirectoryStream<Path> stream = null;
        BasicFileAttributes b_attr = null;
       /* if(params[3]!=null)
            target = Paths.get(params[3].getValue()).normalize();
        paramPath_src = Paths.get(params[2].getValue()).normalize();
        pattern_src = paramPath_src.getFileName().toString();
        position_src = paramPath_src.getParent();*/
        currentPath=env.getCurrentPath();
        if(params[3]!=null)
            target = Paths.get(params[3].getValue()).normalize();
        target= currentPath.resolve(target);
        
        paramPath_src = Paths.get(params[2].getValue()).normalize();
        paramPath_src= currentPath.resolve(paramPath_src);
        pattern_src = paramPath_src.getFileName().toString();
        position_src = paramPath_src.getParent();
        
        isRegFolder_src = false;
        isFile_src = false;
        BasicFileAttributes attr = null;
        
        FileEngine fe = new FileEngine();
        synchronized(this){
            try {
                stream = fe.getStreamFromString(paramPath_src.toString());
            } catch (IOException ex) {
                Utility.mf(ex);
            }  

            for (Path file: stream) {
                PosixFileAttributes p_attr;
                try {

                    boolean isDir = Files.isDirectory(target);
                    Path dest = (isDir) ? target.resolve(file.getFileName()) : target;

                    if(Files.isDirectory(file))
                    {
                        EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                        TreeMover tc = new TreeMover(file, dest, result);
                        Files.walkFileTree(file, opts, Integer.MAX_VALUE, tc);
                        tot_elem += tc.get_n_dir()+tc.get_n_file();
                    }
                    else
                    {
                        Utility.mf("target file singolo: "+target.toString());
                        moveFile(file, dest);
                        result.add(file);
                        tot_elem++;
                    }
                } catch (IOException ex) {
                    //File permission problems are caught here.
                    Utility.mf(ex);
                }
            }
        }
        return true;
    }

    @Override
    public void setCommandParameter(CommandParameter[] cpl) {
       Utility.mf("dentro setcommandparameter" );
       if (cpl.length==7||params[2]!=null)
       {
            params = cpl;
            if(params[3]!=null)
                target = Paths.get(params[3].getValue()).normalize();
            target= currentPath.resolve(target);
            
            paramPath_src = Paths.get(params[2].getValue()).normalize();
            paramPath_src= currentPath.resolve(paramPath_src);
            pattern_src = paramPath_src.getFileName().toString();
            position_src = paramPath_src.getParent();
   
    } 


       else
           Utility.mf(new ParserException(ParserErrorType.INVALID_NUMBER_PARAMETER, this.getClass().getName(),
                   Thread.currentThread().getStackTrace()[2].getMethodName(), "MV Parameter Exception"));
    } 

    @Override
    public List<Path> getCommandResult() {
        return Collections.synchronizedList(result);
    }

    @Override
    public String getCommandStringResult() {
        string_result = Collections.synchronizedList(result).toString();
        string_result += "\nElementi spostati: "+tot_elem;
        return string_result+"\n";
    }

    @Override
    public void usage() {
        Utility.mf("cp <source> <destination>");
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
        Utility.mf("dentro setcommandparameter matrice" );
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        target = Paths.get(params[3].getValue()).normalize();                          
        for (Path file: stream) {
            synchronized(file){
                PosixFileAttributes p_attr;
                try {

                    boolean isDir = Files.isDirectory(target);
                    Path dest = (isDir) ? target.resolve(file.getFileName()) : target;

                    if(Files.isDirectory(file))
                    {
                        EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                        TreeMover tc = new TreeMover(file, dest, result);
                        Files.walkFileTree(file, opts, Integer.MAX_VALUE, tc);
                        tot_elem += tc.get_n_dir()+tc.get_n_file();
                    }
                    else
                    {
                        Utility.mf("target file singolo: "+target.toString());
                        moveFile(file, dest);
                        result.add(file);
                        tot_elem++;
                    }
                } catch (IOException ex) {
                    throw new CommandException(CommandErrorType.MOVE_ERROR,this.getClass().getName(),Thread.currentThread().getStackTrace()[2].getMethodName(), "MV recursive Exception: "+ex.getMessage(), null);
                }
            }
        }
        return true;
    }
    
    public static class TreeMover implements FileVisitor<Path>
    {
        private final Path source;
        private final Path target;
        private PosixFileAttributes my_perm;
        private PosixFileAttributes target_perm;
        private int num_file = 0;
        private int num_dir = 0;
        private List<Path> internal_result = null;

        public TreeMover(Path source, Path target, List<Path>result){
            this.source = source;
            this.target = target;
            internal_result = result;
            try {
                my_perm = Files.readAttributes(source, PosixFileAttributes.class);
                target_perm = Files.readAttributes(target, PosixFileAttributes.class);
                if(!my_perm.owner().equals(target_perm.owner()))
                {
                    throw new IOException("Permission Denied");
                }
            } catch (IOException ex) {
                Utility.mf(ex);
            }
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){

            Path dest = target.resolve(source.relativize(dir));
            
            try{
                Files.move(dir, dest, REPLACE_EXISTING, ATOMIC_MOVE);
                internal_result.add(dir);
                num_dir++;
            //}catch(FileAlreadyExistsException x){  
            }catch(IOException ioe){
                Utility.mf(ioe);
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {            
            moveFile(file, target.resolve(source.relativize(file)));
            internal_result.add(file);
            num_file++;
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.format("%s dentro il visitFileFailed%n", file);
            System.err.format("%s: impossibile leggere il file%n", file);
            if (exc instanceof FileSystemLoopException) {
                System.err.println("ciclo rilevato: " + file);
            } else {
                Utility.mf(exc);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc){
            if(exc == null)
            {
                Path new_dir = target.resolve(source.relativize(dir));
                try{
                    FileTime time = Files.getLastModifiedTime(dir);
                    Files.setLastModifiedTime(new_dir, time);
                }catch(IOException ioe){
                    Utility.mf(ioe);
                }
            }
            return FileVisitResult.CONTINUE;
        }

        public int get_n_file(){
            return num_file;
        }
        
        public int get_n_dir(){
            return num_dir;
        }
        
    }
}
