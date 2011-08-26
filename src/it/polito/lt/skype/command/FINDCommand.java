package it.polito.lt.skype.command;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Set;
import java.nio.file.NoSuchFileException;



public class FINDCommand implements ICommand {

		private Path currentPath = null;		
	
		private CommandParameter[][] params = null;
        private FileEngine eng = null;
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
         *          0 = [] file | reg exp
         *          1 = [] directories
         *          2 = [] permessi
         *          3 = [] data
         *          4 = [] dimensione
         *          5 = esegui 
         *          6 = [] parametri (esegui)
         */
        
        
        public FINDCommand(Path current)
        {
                pathResult = new ArrayList<>();       
                string_result = new ArrayList<>();
                string_result.add(0,"Name\t\t\tPermissions\tSize\tLast Modified\n");
                paramPath = position = currentPath= current;
                pattern = "*";
                goAsc=true;
                includeFolders=true;
                eng = new FileEngine();
        }
        
	@Override
	public boolean exec() throws CommandException {
		
                DirectoryStream<Path> stream = null;
                Boolean isDir = false;
                try{
                    int n_file = params[0].length;
                    int n_dir = params[1].length;
                    Path curr_find = null;
                    
                    /*
                     * Matching di tutte le possibili combinazioni 
                     * path e file | reg_exp
                     */
                    while(n_dir>0)
                    {
                        n_dir--;
                        while(n_file>0)
                        {
                            n_file--;
                            Utility.mf(params[1][n_dir].getValue()+"/"+params[0][n_file].getValue());
                            stream = eng.getStreamFromParameter(new CommandParameter(ParamType.FILE, params[1][n_dir].getValue()+"/"+params[0][n_file].getValue(), SignType.MAG));
                            
                            for(Path file : stream)
                            {
                                filterAddResult(file);
                            }
                        }
                    }
                     
                    Utility.mf(pathResult.toString());
                    
                    
                              
		} 
		catch (IOException | DirectoryIteratorException x) {
		    //IOException can never be thrown by the iteration.
		    //In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
                    return false;
		}
		//sorting risultati
        Collections.sort(string_result);
        if(!goAsc)
            Collections.reverse(string_result);

		return true;
	}
	
	
	private void filterAddResult(Path path) throws IOException
	{
		PosixFileAttributes pathAttributes = Files.readAttributes(path, PosixFileAttributes.class);
		Utility.mf("FILE: "+path.toString()+" DATA ULTIMA MODIFICA: "+pathAttributes.lastModifiedTime().toString());
                
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
                pathResult.add(path);
    			string_result.add(path.getFileName()+"\t\t\t"+((pathAttributes.isDirectory())?"d":"-")+PosixFilePermissions.toString(pathAttributes.permissions())+"\t"+pathAttributes.size()+"\t"+pathAttributes.lastModifiedTime()+"\n");
	}

	@Override
	public void setCommandParameter(CommandParameter[] cpl) {
	}
	
	

    @Override
    public List<Path> getCommandResult() {
            return pathResult;
    }

    @Override
    public void usage() {
        System.err.println("find <file> <path> [<filter>] [cmd [<filter>]] ");
        System.exit(-1);
    }

    @Override
    public String getCommandStringResult() {
        return string_result.toString();
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
       if (cpl.length>=1)
       {
            params = cpl;
       }
    }
    
    

}