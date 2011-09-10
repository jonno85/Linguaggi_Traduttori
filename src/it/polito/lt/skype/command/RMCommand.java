/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.command;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author jo
 */
public class RMCommand implements ICommand{
    
    private CommandParameter[] params = null;
    private int tot_elem = 0;
    private String current=null;
    private Path paramPath;
    private Path position = null;
    private boolean isRegFolder;
    private boolean isFile;
    private List<Path> result = null;
    private String string_result = null;
    private Boolean dir = null;
    private String pattern = null;

    /*
         * vettore Params: 
         *          0 = file|directory|tutto
         */
    
    /*
     * vettore Params: 
     *          0 = asc|desc
     *          1 = file|directory|tutto
     *          2 = path src | rm target | ls target
     *      	3 = path dst cp | mv
     *      	4 =	data ls
     *          5 = permessi ls
     *          6 = dimensione
     */
        
        
    public RMCommand(String current)
    {
            result = new ArrayList<>();       
            this.current =current;
            position = Paths.get(current);
            pattern = "";
    }
    
    public static void removeFile(Path target){
        Utility.mf("target"+target.toString());
        try{
            Files.delete(target);
        }catch(IOException ioe){
            Utility.mf(ioe);
        }

    }
    
    @Override
    public boolean exec() throws CommandException {
        
        DirectoryStream<Path> stream = null;
        BasicFileAttributes b_attr = null;
        
        pattern = paramPath.getFileName().toString();
        position = paramPath.getParent();
        Utility.mf("PARAMPATH: "+paramPath.toString());
        Utility.mf("PATTERN: "+pattern.toString());
        Utility.mf("POSITION: "+position.toString());
       
        boolean finalDeleteDir=false;
        
        isRegFolder = false;
        isFile = false;
        
        finalDeleteDir=Files.isDirectory(paramPath); //se il parametro è una directory secca la cancello alla fine
        	
        
        FileEngine fe = new FileEngine();
        try {
            stream = fe.getStreamFromString(paramPath.toString());
        } catch (IOException ex) {
        	Utility.mf(ex);
        	return false;
        }
        
        
  
       
        for (Path file: stream) {
            
        	Utility.mf("stream LISTA: "+file.toString());
        	//Path listPosition =null;
        	//listPosition=file.getParent();
        	PosixFileAttributes attr;
            try {
                attr = Files.readAttributes(file, PosixFileAttributes.class);
                if(attr.isDirectory())
                    throw new DirectoryNotEmptyException(file.toString());
                Utility.mf("ELIMINAZIONE FILE DALLA LISTA STREAM "+file.toString());
                result.add(file);
                removeFile(file);
                tot_elem++;
                
            } catch (NoSuchFileException x) {
                Utility.mf(x);
            } catch (DirectoryNotEmptyException x) {
                Utility.mf(x);
                try {
                    Treefinder tf = new Treefinder(file,result,position);
                    Files.walkFileTree(file, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,tf);                    
                    tot_elem += tf.get_matches_dir()+tf.get_matches_file();
                }catch(IOException ex){
                    Utility.mf(ex);
                }
            } catch (IOException ex) {
                //File permission problems are caught here.
                throw new CommandException(CommandErrorType.REMOVE_ERROR,this.getClass().getName(),Thread.currentThread().getStackTrace()[2].getMethodName(), "RM recursive Exception: "+ex.getMessage(), null);
            }
        }
        
        if(finalDeleteDir)
        {
        	   result.add(paramPath);
               removeFile(paramPath);
               tot_elem++;
        }
        
        return true;
    }

    @Override
    public void setCommandParameter(CommandParameter[] cpl) {
       if (cpl.length==7)
       {
            params = cpl;
            //percorso
            paramPath = (Paths.get(params[2].getValue()).normalize());
            paramPath= Paths.get(current).resolve(paramPath);
            //root
            if(paramPath.getParent()==null)
            {
            	position=Paths.get("/");
            	paramPath=Paths.get("/*");
            }
       }
       else
           System.err.println("Numero parametri incorretto: "+cpl.length);
    } 

    @Override
    public List<Path> getCommandResult() {
        return result;
    }

    @Override
    public String getCommandStringResult() {
        string_result = result.toString();
        string_result += "\nElementi eliminati: "+tot_elem;
        return string_result;
    }

    @Override
    public void usage() {
        System.err.println("rm <path>");
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
        
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        for (Path file: stream) {
            PosixFileAttributes attr;
            try {
                attr = Files.readAttributes(file, PosixFileAttributes.class);
                if(attr.isDirectory())
                    throw new DirectoryNotEmptyException(file.toString());
                Utility.mf("ELIMINAZIONE FILE DALLA LISTA STREAM "+file.toString());
                removeFile(file);
                tot_elem++;
            } catch (NoSuchFileException x) {
                System.err.format("%s: no such file or directory%n", file);
            } catch (DirectoryNotEmptyException x) {
                Utility.mf(x);
                try {
                    Treefinder tf = new Treefinder(file,result,position);
                    Files.walkFileTree(file, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,tf);                    
                    tot_elem += tf.get_matches_dir()+tf.get_matches_file();
                } catch (IOException ex) {
                    Utility.mf(ex);
                }
            } catch (IOException ex) {
                //File permission problems are caught here.
                throw new CommandException(CommandErrorType.REMOVE_ERROR,this.getClass().getName(),Thread.currentThread().getStackTrace()[2].getMethodName(), "RM recursive Exception: "+ex.getMessage(), null);
            }
        }
        return true;
    }
    
    public static class Treefinder implements FileVisitor<Path>
    {
        private PosixFileAttributes my_perm;
        private PosixFileAttributes folder_attr;
        private PosixFileAttributes file_attr;
        private int numMatches_file = 0;
        private int numMatches_dir = 0;
        private List<Path> internal_result = null;
        private Path root=null;

        public Treefinder(Path file, BasicFileAttributes readAttributes){
            //root=file;
        	try {
                my_perm = Files.readAttributes(file, PosixFileAttributes.class);
            } catch (IOException ex) {
                System.err.println(ex.getCause());
            }
        }
        
        public Treefinder(Path file, List<Path> result, Path superRoot){
        	//root=superRoot;
            try {
                my_perm = Files.readAttributes(file, PosixFileAttributes.class);
                internal_result = result;
            } catch (IOException ex) {
                Utility.mf(ex);
            }
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){
            try{
                folder_attr = Files.readAttributes(dir,PosixFileAttributes.class);
                //Utility.mf("directory "+dir.toString()+" attrb "+folder_attr.owner()+"->folder\n"+"directory "+dir.getParent().toString()+" attrb "+my_perm.owner()+"->parent folder");
            
                /**
                 * Verifico che il propietario del percorso dal quale provengo sia lo stesso della cartella
                 * destinazione altrimenti salto il sottoalbero
                 */
                if(my_perm.owner().equals(folder_attr.owner()))
                {
                    internal_result.add(dir);
                    return FileVisitResult.CONTINUE;
                }
                else
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }catch(IOException ioe){
                Utility.mf(ioe);
            }
            return FileVisitResult.SKIP_SUBTREE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
            System.err.format("%s dentro il FileVisit%n", file);
            
            try{
                file_attr = Files.readAttributes(file, PosixFileAttributes.class);
                Utility.mf("file in esame "+file.toString());
                if (my_perm.owner().equals(file_attr.owner()))
                {
                    Utility.mf("ELIMINAZIONE FILE DURANTE NAVIGAZIONE"+file.toString());
                    removeFile(file);
                    numMatches_file++;
                    internal_result.add(file);
                }
            } catch (NoSuchFileException x) {
                Utility.mf(x);
            }catch (IOException ex) {
                //File permission problems are caught here.
                Utility.mf(ex);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc){
            System.err.format("%s dentro il visitFileFailed%n", file);
            System.err.format("%s: impossibile leggere il file%n", file);
            if (exc instanceof FileSystemLoopException) {
                System.err.println("ciclo rilevato: " + file);
            } else {
                System.err.format("Impossibile eliminare: %s: %s%n", file, exc);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
           System.err.format("%s dentro il postDirectory visit%n", dir);
          // if(dir.compareTo(root)!=0)
           {
        	Utility.mf("ELIMINAZIONE DIR"+dir.toString());
            removeFile(dir);
            numMatches_dir++;
           }
           return FileVisitResult.CONTINUE;
        }   
        
        public int get_matches_file(){
            return numMatches_file;
        }
        
        public int get_matches_dir(){
            return numMatches_dir;
        }
        
    }
}
