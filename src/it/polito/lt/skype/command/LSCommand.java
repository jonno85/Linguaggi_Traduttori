package it.polito.lt.skype.command;

import it.polito.lt.skype.parser.ParserErrorType;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;




public class LSCommand implements ICommand {

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
        
        
        public LSCommand(String current)
        {
                pathResult = new ArrayList<>();       
                string_result = new ArrayList<>();
                
                paramPath = position = currentPath = Paths.get(current);
                pattern = "*";
                goAsc=true;
                includeFolders=true;
                params = new CommandParameter[6];
                
        }
        
	@Override
	public boolean exec() throws CommandException {
		
            DirectoryStream<Path> stream = null;
            Boolean isDir = false;
            synchronized(this){
                try{
                    //System.out.printf("lunghezza vetto para "+params.length);
                    if(params[2]!=null){
                        stream=eng.getStreamFromParameter(params[2]);
                    }else{
                        stream = eng.getStreamFromParameter(new CommandParameter(ParamType.FILE, position.toString(), SignType.MAG));
                    }

                    for (Path file: stream) {
                        PosixFileAttributes attr = Files.readAttributes(file, PosixFileAttributes.class);
                        isDir = attr.isDirectory();
                        
                        if(params[1]==null || (includeFolders && isDir) || (!includeFolders && !isDir))
                        {
                            filterAddResult(file);
                        }
        	    }          
		} 
		catch (IOException | DirectoryIteratorException ex) {
		    throw new CommandException(CommandErrorType.LIST_ERROR,this.getClass().getName(),Thread.currentThread().getStackTrace()[2].getMethodName(), "LS recursive Exception: "+ex.getMessage(), null);
		}
		//sorting risultati
                Collections.sort(string_result);
                if(!goAsc){
                    Collections.reverse(string_result);
                }
            }
            return true;
	}
	
	
	private void filterAddResult(Path path) throws IOException
	{
            PosixFileAttributes pathAttributes = Files.readAttributes(path, PosixFileAttributes.class);
            //Utility.mf("FILE: "+path.toString()+" DATA ULTIMA MODIFICA: "+pathAttributes.lastModifiedTime().toString());
            if(eng.matchLastModDate(params[4],pathAttributes.lastModifiedTime()) 
                    && eng.matchPermissions(params[5],pathAttributes.permissions()) 
                    && eng.matchSize(params[6],pathAttributes.size())){
                pathResult.add(path);
                string_result.add(path.getFileName()+"\t\t\t"+((pathAttributes.isDirectory())?"d":"-")+PosixFilePermissions.toString(pathAttributes.permissions())+"\t"+pathAttributes.size()+"\t"+pathAttributes.lastModifiedTime()+"\n");
            }
	}

	@Override
	public void setCommandParameter(CommandParameter[] cpl) {
			
            if (cpl.length == 7)
            {
                params = cpl;
                n_par = cpl.length;
                
                if(params[0] != null)
                {//ascendente o discendente
                    if(params[0].getValue().compareToIgnoreCase("asc")==0)
                	goAsc = true;
                    else
                	goAsc = false;
                }
                
                if(params[1] != null)
                {//file o folders
                    if(params[1].getValue().compareToIgnoreCase("dir")==0)
                        includeFolders = true;
                    else
                        includeFolders = false;
                }
                
                if(params[2] != null)
                {//estrazione del path
                	paramPath = ((Path)Paths.get(params[2].getValue()).normalize());
                    if(paramPath.getFileName()!=null)
                    	pattern = paramPath.getFileName().toString();
                    if(paramPath.getParent()!=null)
                    	position = paramPath.getParent();
                    else position=Paths.get("/");
                    Utility.mf("ESTRATTI: "+pattern+" "+position.toString());
	        }              
            }
            else
                Utility.mf(new ParserException(ParserErrorType.INVALID_NUMBER_PARAMETER, this.getClass().getName(),
                   Thread.currentThread().getStackTrace()[2].getMethodName(), "LS Parameter Exception"));
	}
	
	

    @Override
    public List<Path> getCommandResult() {
            return Collections.synchronizedList(pathResult);
    }

    @Override
    public void usage() {
        Utility.mf("ls <path> <flags>");
    }

    @Override
    public String getCommandStringResult() {
    	string_result.add(0,"Name\t\t\tPermissions\tSize\tLast Modified\n");
    	return string_result.toString();
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        Boolean isDir = false;
        for (Path file: stream) {
            PosixFileAttributes attr;
            try {
                attr = Files.readAttributes(file, PosixFileAttributes.class);
                isDir = attr.isDirectory();
                if(params[1] == null || (includeFolders && isDir) || (!includeFolders && !isDir))
                {
                    filterAddResult(file);
                }
            } catch (IOException ex) {
                throw new CommandException(CommandErrorType.LIST_ERROR,this.getClass().getName(),Thread.currentThread().getStackTrace()[2].getMethodName(), ex.getMessage(), null);
            }
        }
        Collections.sort(string_result);
        if(!goAsc)
            Collections.reverse(string_result);
        return true;
    }
}