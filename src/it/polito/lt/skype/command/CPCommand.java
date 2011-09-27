/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polito.lt.skype.command;

import it.polito.lt.skype.manager.ManagerException;
import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import it.polito.lt.skype.parser.ParserErrorType;
import it.polito.lt.skype.parser.ParserException;

import it.polito.lt.skype.parser.Resolver;
import java.nio.file.attribute.FileTime;
import java.nio.file.FileSystemLoopException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import static java.nio.file.StandardCopyOption.*;

/**
 *
 * @author jo
 */
public class CPCommand implements ICommand{
    
    private Path currentPath=null;
    private CommandParameter[] params = null;
    private int tot_elem = 0;
    
    private CommandEnv env;
    private Path paramPath_src;
    private Path position_src = null;
    private String pattern_src = null;
    private Path target = null;
    
    private List<Path> result = null;
    private String string_result = null;
    private Boolean dir = null;
    private boolean isFile_src;
    private boolean isRegFolder_src;
    private Resolver ris = null;
    private VarManager manager = null;
    private ArrayList<ArrayList<String>> token_list = null;

    /*
         * vettore Params: 
         *          0 = file|directory|tutto src
         *          1 = file|directory|tutto dst
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
    
    public CPCommand(CommandEnv currentEnv)
    {
            env = currentEnv;
            result = new ArrayList<>();       
            target=env.getCurrentPath();
            position_src = currentPath = env.getCurrentPath();
            pattern_src = "";
            token_list = new ArrayList<ArrayList<String>>();
    }
    
    public void setAdditionalParameters(VarManager manager, ArrayList<String> token_list){
        this.manager = manager;
        Utility.mf("primo polish " + token_list.toString());
        this.token_list.add(token_list);
    }
    
    public static void copyFile(Path source, Path target)throws IOException, UnsupportedOperationException{
        //CopyOption[] options = new CopyOption[] {COPY_ATTRIBUTES};
        Utility.mf("COMANDO CP: source "+source.toString()+" target "+target.toString());
        //target = Paths.get(target.toString()+"/"+source.getFileName().toString());
  
        //if(Files.notExists(target))
        //{
        Files.copy(source, target, COPY_ATTRIBUTES,  REPLACE_EXISTING);
         /*   try{
                Files.copy(source, target, COPY_ATTRIBUTES,  REPLACE_EXISTING);
            }catch(IOException | UnsupportedOperationException ioe){
                
            	System.err.format("Impossibile copiare: %s %s%n", source,ioe);
            }*/
        //}
    }
    
    @Override
    public boolean exec() throws CommandException {
        
        DirectoryStream<Path> stream = null;
        BasicFileAttributes b_attr = null;
        
        //dobbiamo estrarre il parametro dal vettore $RESULT
        Utility.mf("EXEC");
        if(manager!=null){
            Utility.mf("dentro manager manager "+manager.toString());
            Utility.mf("token list prima di exec "+token_list.toString());
            myVar p1 = null;
            myVar p2 = null;
            if(params[2].getParamType().equals(ParamType.COMPOSITO)){
                Utility.mf("token list el 0 "+token_list.get(0));
                ris = new Resolver(manager, token_list.get(0), "result_");
                try {
                    p1 = ris.exec();
                    params[2] = new CommandParameter(ParamType.NULL, p1.getStringValue(), null);
                    Utility.mf("params_2: "+params[2].getValue());
                    paramPath_src = Paths.get(params[2].getValue()).normalize();
                    paramPath_src= currentPath.resolve(paramPath_src);
                } catch (ManagerException ex) {
                    ex.printStackTrace();
                }
            }
            
            
            Utility.mf("inizio gestione secondo parametro");
            
            if(params[3].getParamType().equals(ParamType.COMPOSITO)){
                Utility.mf("token list el 1 "+token_list.get(1));
                ris = new Resolver(manager, token_list.get(1), "result_");
                try {
                    params[3] = new CommandParameter(ParamType.NULL, ris.exec().getStringValue(), null);
                    Utility.mf("params_3: "+params[3].getValue());
                    target = Paths.get(params[3].getValue()).normalize();
                } catch (ManagerException ex) {
                    ex.printStackTrace();
                }
            }else{
                target = Paths.get(params[3].getValue()).normalize();
            }
              //target = currentPath.resolve(target); 
         }
        paramPath_src = Paths.get(params[2].getValue()).normalize();
            paramPath_src= currentPath.resolve(paramPath_src);
            pattern_src = paramPath_src.getFileName().toString();
            position_src = paramPath_src.getParent();
        Utility.mf("params[2] "+params[2].getValue());
        Utility.mf("params[3] "+params[3].getValue());
        Utility.mf("paramspath_src "+paramPath_src);
        Utility.mf("target "+target);
/*
        target= currentPath.resolve(target);
        currentPath=env.getCurrentPath();
        target = Paths.get(params[3].getValue()).normalize();
        

        paramPath_src = Paths.get(params[2].getValue()).normalize();
        paramPath_src= currentPath.resolve(paramPath_src);
        pattern_src = paramPath_src.getFileName().toString();
        position_src = paramPath_src.getParent();
        */
        Utility.mf("CP: da "+paramPath_src.toString()+" a "+target.toString());
        
        isRegFolder_src = false;
        isFile_src = false;
        BasicFileAttributes attr = null;
        
        FileEngine fe = new FileEngine();
        try {
            stream = fe.getStreamFromString(paramPath_src.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
                         
        for (Path file: stream) {
            PosixFileAttributes p_attr;
            try {
                
                boolean isDir = Files.isDirectory(target);
                Path dest = (isDir) ? target.resolve(file.getFileName()) : target;
                
                if(Files.isDirectory(file))
                {
                    EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                    TreeCopier tc = new TreeCopier(file, dest, result);
                    Files.walkFileTree(file, opts, Integer.MAX_VALUE, tc);
                    tot_elem += tc.get_n_dir()+tc.get_n_file();
                }
                else
                {
                    Utility.mf("target file singolo: "+target.toString());
                    copyFile(file, dest);
                    result.add(file);
                    tot_elem++;
                }
            } catch (IOException | UnsupportedOperationException ex) {
                //File permission problems are caught here.
                
                CommandException ce = new CommandException(CommandErrorType.COPY_ERROR, this.getClass().getName(), 
                		Thread.currentThread().getStackTrace()[2].getMethodName(), 
                		ex.getMessage());
                Utility.mf(ce);
              throw ce;
            }
        }
        return true;
    }

    @Override
    public void setCommandParameter(CommandParameter[] cpl) {
        params = cpl;
        if(params.length!=7)
            Utility.mf(new ParserException(ParserErrorType.INVALID_NUMBER_PARAMETER, this.getClass().getName(),
                 Thread.currentThread().getStackTrace()[2].getMethodName(), "CP Parameter Exception"));
        
         Utility.mf("CP SETCOMMANDPARAMETER:");
              for(int i=0; i<7;i++)
              	if(cpl[i]!=null)
              		Utility.mf("param "+i+" = "+cpl[i].getValue());
              	else 
              		Utility.mf("nulllll");
              
    	 
         
      } 

    

    @Override
    public List<Path> getCommandResult() {
        return result;
    }

    @Override
    public String getCommandStringResult() {
        string_result = result.toString();
        string_result += "\nElementi copiati: "+tot_elem;
        return string_result+"\n";
    }

    @Override
    public void usage() {
        System.err.println("cp <source> <destination>");
        System.exit(-1);
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        target = Paths.get(params[3].getValue()).normalize();   
        for (Path file: stream) {
            PosixFileAttributes p_attr;
            try {
                
                boolean isDir = Files.isDirectory(target);
                Path dest = (isDir) ? target.resolve(file.getFileName()) : target;
                
                if(Files.isDirectory(file))
                {
                    EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                    TreeCopier tc = new TreeCopier(file, dest, result);
                    Files.walkFileTree(file, opts, Integer.MAX_VALUE, tc);
                    tot_elem += tc.get_n_dir()+tc.get_n_file();
                }
                else
                {
                    Utility.mf("target file singolo: "+target.toString());
                    copyFile(file, dest);
                    result.add(file);
                    tot_elem++;
                }
            } catch (IOException ex) {
                //File permission problems are caught here.
                System.err.println(ex);
            }
        }
        return true;
    }
    
    public static class TreeCopier implements FileVisitor<Path>
    {
        private final Path source;
        private final Path target;
        private PosixFileAttributes my_perm;
        private PosixFileAttributes target_perm;
        private int num_file = 0;
        private int num_dir = 0;
        private List<Path> internal_result = null;

        public TreeCopier(Path source, Path target, List<Path>result){
            this.source = source;
            this.target = target;
            internal_result = result;
            try {
                my_perm = Files.readAttributes(source, PosixFileAttributes.class);
                target_perm = Files.readAttributes(target, PosixFileAttributes.class);
                if(!my_perm.owner().equals(target_perm.owner()))
                {
                    System.err.println("Il proprietario della cartella di destinazione non risulta uguale:\ncopia interrotta");
                    System.exit(-1);
                }
            } catch (IOException ex) {
                System.err.println(ex.getCause());
            }
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            
            //CopyOption[] options = new CopyOption[] { COPY_ATTRIBUTES };
            
            Path dest = target.resolve(source.relativize(dir));
            
            try{
                Files.copy(dir, dest,REPLACE_EXISTING,COPY_ATTRIBUTES);//,ATOMIC_MOVE);
                internal_result.add(dir);
                num_dir++;
            }catch(FileAlreadyExistsException x){
                
            }catch(IOException ioe){
                System.err.format("Impossibile creare: %s: %s%n",dest,ioe);
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            System.err.format("%s dentro il FileVisit%n", file);
            
            copyFile(file, target.resolve(source.relativize(file)));
            internal_result.add(file);
            num_file++;
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            System.err.format("%s dentro il visitFileFailed%n", file);
            System.err.format("%s: impossibile leggere il file%n", file);
            if (exc instanceof FileSystemLoopException) {
                System.err.println("ciclo rilevato: " + file);
            } else {
                System.err.format("Impossibile copiare: %s: %s%n", file, exc);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            System.err.format("%s dentro il postDirectory visit%n", dir);
            if(exc == null)
            {
                Path new_dir = target.resolve(source.relativize(dir));
                try{
                    FileTime time = Files.getLastModifiedTime(dir);
                    Files.setLastModifiedTime(new_dir, time);
                }catch(IOException ioe){
                    System.err.format("Impossibile copiare tutti gli attributi: %s: %s%n",new_dir,ioe);
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
