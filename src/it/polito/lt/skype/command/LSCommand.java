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
         *          2 = path
         *          3 = permessi
         *          4 = data
         */
        
        
        public LSCommand(Path current)
        {
                pathResult = new ArrayList<>();       
                string_result = new ArrayList<>();
                string_result.add(0,"Name\t\t\tPermissions\tSize\tLast Modified\n");
                paramPath = position = currentPath = current;
                pattern = "*";
                goAsc=true;
                includeFolders=true;
                
        }
        
	@Override
	public boolean exec() throws CommandException {
		
                DirectoryStream<Path> stream = null;
        
                    Boolean isDir = false;
       try{
    	   			stream=eng.getStreamFromParameter(params[2]);
                    for (Path file: stream) {
                        PosixFileAttributes attr = Files.readAttributes(file, PosixFileAttributes.class);
                        isDir = attr.isDirectory();
                        
                        if(params[1]==null || (includeFolders && isDir) || (!includeFolders && !isDir))
                        {
                        	filterAddResult(file);
                        }
        	    }          
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
		PosixFileAttributes pathAttributes =Files.readAttributes(path, PosixFileAttributes.class);
		Utility.mf("FILE: "+path.toString()+" DATA ULTIMA MODIFICA: "+pathAttributes.lastModifiedTime().toString());
		if(eng.matchLastModDate(params[4],pathAttributes.lastModifiedTime()) && eng.matchPermissions(params[3],pathAttributes.permissions()))
		{
			pathResult.add(path);
			string_result.add(path.getFileName()+"\t\t\t"+((pathAttributes.isDirectory())?"d":"-")+PosixFilePermissions.toString(pathAttributes.permissions())+"\t"+pathAttributes.size()+"\t"+pathAttributes.lastModifiedTime()+"\n");
		}
	
	}
	
	
	
	
	

	@Override
	public void setCommandParameter(CommandParameter[] cpl) {

            if (cpl.length==5)
            {
                params = cpl;
                n_par = cpl.length;
                
                if(params[0]!=null)
                {//ascendente o discendente
                	if(params[0].getValue().compareToIgnoreCase("asc")==0)
                		goAsc=true;
                	else
                		goAsc=false;
                }
                
                if(params[1]!=null)
                {//file o folders
	                if(params[0].getValue().compareToIgnoreCase("d")==0)
	                	includeFolders=true;
	                else
	                	includeFolders=false;
                }
                
                if(params[2]!=null)
                {//estrazione del path
	                paramPath = ((Path)Paths.get(params[2].getValue()).normalize());
	           	 	pattern = paramPath.getFileName().toString();
	                position = paramPath.getParent();
	            }
                
                /*if(params[3]!=null)
                {//permessi
                	paramPermissions= PosixFilePermissions.fromString(params[3].getValue());
                	Utility.mf("Parametro PERMESSO: "+PosixFilePermissions.toString(paramPermissions));
                }// fatta nel file Engine*/
                
               /* if(params[4]!=null)
                {
                	GregorianCalendar paramDateGC;
					try {
						paramDateGC = convertDateString(params[4].getValue());
						paramLastModTime=FileTime.fromMillis(paramDateGC.getTimeInMillis());
						Utility.mf("PARAMETRO DATA: "+paramLastModTime.toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                }//fatto in file engine*/
            }
	}
	
	

    @Override
    public List<Path> getCommandResult() {
            return pathResult;
    }

    @Override
    public void usage() {
        System.err.println("ls <path> <flags>");
        System.exit(-1);
    }

    @Override
    public String getCommandStringResult() {
        return string_result.toString();
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) {
    }
    
    

}
