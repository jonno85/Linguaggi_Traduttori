package it.polito.lt.skype.command;

import it.polito.lt.skype.manager.VarManager;
import it.polito.lt.skype.manager.myVar;
import it.polito.lt.skype.parser.ParserErrorType;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.List;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;

public class FINDCommand implements ICommand {
	
	private CommandParameter[][] params = new CommandParameter[6][];
        private FileEngine eng = null;
        
        int n_file = 1;
        int n_dir=1;
        private boolean doRecursive = false;
        private CommandEnv env;
        private String position = null;
        private String pattern = null;
        private List<Path> pathList= null;
        private List<Path> pathResult = null;
        private List<Path> pathResult_rec = null;
        private ArrayList<String> string_result = null;
        private VarManager manager = null;
      
        
	        /*
	         * vettore Params: 
	         *          0 = [] file | reg exp
	         *          1 = [] directories
	         *          2 = [] permessi
	         *          3 = [] data
	         *          4 = [] dimensione
	         *          5 = [1] esegui = comando
	         *          6 = [] parametri (esegui)  	->	0 = asc|desc
*          									1 = file|directory|tutto
*          									2 = path src | rm target | ls target
*      										3 = path dst cp | mv
*      										4 =	data ls
*          									5 = permessi ls
*          									6 = dimensione ls
	        */
        
        
        public FINDCommand(CommandEnv env, VarManager manager)
        {
                this.env=env;
        	pathResult = new ArrayList<>();          
                pathList = new ArrayList<>();
                string_result = new ArrayList<>();
                string_result.add(0,"Name\t\t\tPermissions\tSize\tLast Modified\n");
                position = env.getCurrentPathString();
                pattern = "*";
                eng = new FileEngine();
                this.manager = manager;
        }
        
        public FINDCommand(CommandEnv env)
        {
                this.env=env;
        	pathResult = new ArrayList<>();          
                pathList = new ArrayList<>();
                string_result = new ArrayList<>();
                string_result.add(0,"Name\t\t\tPermissions\tSize\tLast Modified\n");
                position = env.getCurrentPathString();
                pattern = "*";
                eng = new FileEngine();
        }
        
        
        /*
         *      params
         *          5 = comando
         *          6 = []parametri
         */
        
        public void recursive_cmd() throws CommandException{
        	int i=1;
        	for(Path curr_path : pathResult){
        		ICommand rec_cmd=FileEngine.iCommandFromString(params[5][0].getValue(),env);
        		Utility.mf("--------------------"+params[5][0].getValue());
        		CommandParameter[] dyn_param = new CommandParameter[7];
        		dyn_param=params[6].clone();
        		dyn_param[2]=new CommandParameter(ParamType.PATH, curr_path.toString(), null) ;
        		//dyn_param[3]=params[6][3];
        		try {
					rec_cmd.setCommandParameter(dyn_param);
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		rec_cmd.exec();
        		//controllare la stampa
        		string_result.set(i,string_result.get(i)+" EXEC-> "+rec_cmd.getCommandStringResult());
        	}
        
                
        }
        
	@Override
	public boolean exec() throws CommandException {
            try{
            	//il problema è il vett di pattern e path deve essere sempre pieno e mai con posizioni a null

                /*
                 *usando il walk tree occorre prima vedere se le directory passate sono contenute luna nell'altra ed eventualmente scartarle
                 *poi per le directory diverse lanciare la ricerca con filtro
                 *poi chiedere a jonni come lavorano i comandi per vedere se bisogna agire prima sulla cartella o prima sul file
                 */
               int acontb=0;
               String a=null;
               String b=null;
               for(int i =0; i<n_dir;i++){
                       acontb=0;
                       for(int j=0;j<n_dir;j++){ 
                                a = Paths.get(params[1][i].getValue()).normalize().toString()+"/";
                                b = Paths.get(params[1][j].getValue()).normalize().toString()+"/";
                               if(a.contains(b)) 
                                       acontb++;
                       }
                       if(acontb==1){//se a contiene b, più di una volta a non va aggiunta
                               pathList.add(env.getCurrentPath().resolve(Paths.get(params[1][i].getValue())));
                               Utility.mf("subfolder: checked: "+a);
                       }
                       else
                               Utility.mf("subfolder: deleted: "+a);
               }

               for(Path p : pathList)//per ogni path unico
               {
                       for(int i=0; i<n_file;i++)//per ogni pattern
                       {
                               Utility.mf("Ricerca di: "+params[0][i].getValue()+" in: "+p);
                               Finder finder = new Finder(params[0][i].getValue());
                               Files.walkFileTree(p, finder);//avvio la ricerca filtrante
                               finder.done();
                       }
               }

            } 
            catch (IOException | DirectoryIteratorException ex) {
                //IOException can never be thrown by the iteration.
                //In this snippet, it can only be thrown by newDirectoryStream.
                throw new CommandException(CommandErrorType.FIND_ERROR,this.getClass().getName(),
                		Thread.currentThread().getStackTrace()[2].getMethodName(), 
                		"FIND recursive Exception: "+ex.getMessage());
            }
            //sorting risultati
            Collections.sort(string_result);
            
            //riempimento vettore $RESULT[i]
            int index = 0;
            for(Path s : pathResult){
                manager.add_var(new myVar("result_"+index++,myVar._string,s));
            }
            Utility.mf("manager aggiunto lista var: ");
            manager.getListVar();
            if(doRecursive)
               	recursive_cmd();
            
            
            return true;
            
	}
	
        public int getLenght(){
            return string_result.size();
        }
	
	private void filterAddResult(Path path) throws IOException
	{
		PosixFileAttributes pathAttributes = Files.readAttributes(path, PosixFileAttributes.class);
		//Utility.mf("FILE: "+path.toString()+" DATA ULTIMA MODIFICA: "+pathAttributes.lastModifiedTime().toString());
                
                if(params[2]!=null)//permessi, se uno non è soddisfatto, esce senza fare l'add
                {
                	for(int i=0; i< params[2].length; i++) 
                		if(!eng.matchPermissions(params[2][i], pathAttributes.permissions()))
                			return;
                }
                
                if(params[3]!=null)//data, se uno non è soddisfatto, esce senza fare l'add
                {
                	for(int i=0; i< params[3].length; i++) 
                		if(!eng.matchLastModDate(params[3][i],pathAttributes.lastModifiedTime()))
                			return;
                }
                
                if(params[4]!=null)//dimensione, se uno non è soddisfatto, esce senza fare l'add
                {
                	for(int i=0; i< params[4].length; i++) 
                		if(!eng.matchSize(params[4][i],pathAttributes.size()))
                			return;
                }
                for(Path p : pathList)//per ogni path unico
                {
                	if(path.compareTo(p)==0)//siamo in una delle directory di start ricerca
                		return;
                }
                
                pathResult.add(path);

                string_result.add(path.toString()+"\t\t\t"+((pathAttributes.isDirectory())?"d":"-")+PosixFilePermissions.toString(pathAttributes.permissions())+"\t"+pathAttributes.size()+"\t"+pathAttributes.lastModifiedTime()+"\n");
	}

	@Override
	public void setCommandParameter(CommandParameter[] cpl) {
	}
	
	

    @Override
    public List<Path> getCommandResult() {
        if(pathResult_rec!=null)
            return Collections.unmodifiableList(pathResult_rec);
        else
            return Collections.unmodifiableList(pathResult);
    }

    @Override
    public void usage() {
        Utility.mf("find <file> <path> [<filter>] [exec cmd [<filter>]] ");
    }

    @Override
    public String getCommandStringResult() {
        
        /*  VERIFICARE  se creare 
         * il risultato al momento: necessita ordinare?*/
        return string_result+"\n";
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
    	
        params=cpl;
        if(params[5]!=null)
        	doRecursive=true;
        
        if(params[0]!=null) {
        	Utility.mf("params[0] NON vuoto \n"+params[0][0].getValue());
        	n_file=params[0].length;
        }
        else
        	params[0]=new CommandParameter[]{new CommandParameter(null,pattern,null)};
        
        if(params[1]!=null){ 
        	Utility.mf("params[1] NON vuoto\n"+params[1][0].getValue());
        	n_dir=params[1].length;	
        }
        else
        	params[1]=new CommandParameter[]{new CommandParameter(null,position,null)};
        
        if(params[1]==null && params[0]==null)
            Utility.mf(new ParserException(ParserErrorType.INVALID_NUMBER_PARAMETER, this.getClass().getName(),
                   Thread.currentThread().getStackTrace()[2].getMethodName(), "Find Parameter Exception"));
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        return false;
    }

    @Override
    public void setAdditionalParameters(VarManager manager, ArrayList<String> token_list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
    
    /**
     * A {@code FileVisitor} that finds all files that match the
     * specified pattern.
     */
    public class Finder extends SimpleFileVisitor<Path> {
        private final PathMatcher matcher;
        private int numMatches = 0;

        Finder(String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        //Compares the glob pattern against the file or directory name.
        void find(Path file) throws IOException {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
            	filterAddResult(file);
            	numMatches++;
                System.out.println(file);
            }
        }

        //Prints the total number of matches to standard out.
        void done() {
            Utility.mf("Matched(start included): " + numMatches);
        }

        //Invoke the pattern matching method on each file.
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            try {
                find(file);
            } catch (IOException e) {
                Utility.mf(e);
            }
            return FileVisitResult.CONTINUE;
        }

        //Invoke the pattern matching method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            try {
                find(dir);
            } catch (IOException e) {
                Utility.mf(e);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            Utility.mf(exc);
            return FileVisitResult.CONTINUE;
        }
    }

    


}
