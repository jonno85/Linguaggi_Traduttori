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
        
        
    public RMCommand(Path current)
    {
            result = new ArrayList<>();       
            position = current;
            pattern = "";
    }
    
    public static void removeFile(Path target){
        Utility.mf("target"+target.toString());

        try{
            Files.delete(target);
            Utility.mf("ciao");
        }catch(IOException ioe){
            System.err.format("Impossibile eliminare: %s %s%n", target,ioe);
        }

    }
    
    @Override
    public boolean exec() throws CommandException {
        
        DirectoryStream<Path> stream = null;
        BasicFileAttributes b_attr = null;
        
        paramPath = ((Path)Paths.get(params[0].getValue()).normalize());
        pattern = paramPath.getFileName().toString();
        position = paramPath.getParent();
        isRegFolder = false;
        isFile = false;
        
        FileEngine fe = new FileEngine();
        try {
            stream = fe.getStreamFromParameter(params[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
                    
        for (Path file: stream) {
            PosixFileAttributes attr;
            try {
                attr = Files.readAttributes(file, PosixFileAttributes.class);
                if(attr.isDirectory())
                    throw new DirectoryNotEmptyException(file.toString());
                removeFile(file);
                tot_elem++;
            } catch (NoSuchFileException x) {
                System.err.format("%s: no such file or directory%n", file);
            } catch (DirectoryNotEmptyException x) {
                System.err.format("%s not empty%n", file);
                try {
                    System.err.format("%s dentro il try%n", file);
                    Treefinder tf = new Treefinder(file,result);
                    Files.walkFileTree(file.getParent(), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,tf);                    
                    tot_elem += tf.get_matches_dir()+tf.get_matches_file();
                } catch (IOException ex) {
                    System.err.println(ex.getCause());
                }
            } catch (IOException ex) {
                //File permission problems are caught here.
                System.err.println(ex);
            }
        }
        return true;
    }

    @Override
    public void setCommandParameter(CommandParameter[] cpl) {
       if (cpl.length==1)
       {
            params = cpl;
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
        System.exit(-1);
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
                removeFile(file);
                tot_elem++;
            } catch (NoSuchFileException x) {
                System.err.format("%s: no such file or directory%n", file);
            } catch (DirectoryNotEmptyException x) {
                System.err.format("%s not empty%n", file);
                try {
                    System.err.format("%s dentro il try%n", file);
                    Treefinder tf = new Treefinder(file,result);
                    Files.walkFileTree(file.getParent(), EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,tf);                    
                    tot_elem += tf.get_matches_dir()+tf.get_matches_file();
                } catch (IOException ex) {
                    System.err.println(ex.getCause());
                }
            } catch (IOException ex) {
                //File permission problems are caught here.
                System.err.println(ex);
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

        public Treefinder(Path file, BasicFileAttributes readAttributes){
            try {
                my_perm = Files.readAttributes(file, PosixFileAttributes.class);
            } catch (IOException ex) {
                System.err.println(ex.getCause());
            }
        }
        
        public Treefinder(Path file, List<Path> result){
            try {
                my_perm = Files.readAttributes(file, PosixFileAttributes.class);
                internal_result = result;
            } catch (IOException ex) {
                System.err.println(ex.getCause());
            }
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            
            folder_attr = Files.readAttributes(dir,PosixFileAttributes.class);
            
            Utility.mf("directory "+dir.toString()+" attrb "+folder_attr.owner()+"->folder\n"+"directory "+dir.getParent().toString()+" attrb "+my_perm.owner()+"->parent folder");
            
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
             
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            System.err.format("%s dentro il FileVisit%n", file);
            
            try{
                file_attr = Files.readAttributes(file, PosixFileAttributes.class);
                Utility.mf("file in esame "+file.toString());
                if (my_perm.owner().equals(file_attr.owner()))
                {
                    Utility.mf("stessi attributi "+file.toString());
                    removeFile(file);
                    numMatches_file++;
                    internal_result.add(file);
                }
            } catch (NoSuchFileException x) {
                System.err.format("%s: no such file or directory%n", file);
            } /*catch (DirectoryNotEmptyException x) {
                Treefinder tf_depth = new Treefinder(file, internal_result);
                Files.walkFileTree(file, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, tf_depth);
                numMatches_dir += tf_depth.get_matches_dir();
                numMatches_file += tf_depth.get_matches_file();
                System.err.format("%s new WalkFileTree per la cartella ", file);
                System.err.format("%s not empty%n", file);
            } */catch (IOException ex) {
                //File permission problems are caught here.
                System.err.println(ex);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
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
            removeFile(dir);
            numMatches_dir++;
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
