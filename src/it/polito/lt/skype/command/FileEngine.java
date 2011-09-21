package it.polito.lt.skype.command;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

public class FileEngine {
	
	
	public static ICommand iCommandFromString(String c, String current)
	{
		ICommand ret= null;
		if(c.compareToIgnoreCase("cp")==0) ret=new CPCommand(current);
		if(c.compareToIgnoreCase("mv")==0) ret=new MVCommand(current);
		if(c.compareToIgnoreCase("ls")==0) ret=new LSCommand(current);
                if(c.compareToIgnoreCase("rm")==0) ret=new RMCommand(current);
		if(c.compareToIgnoreCase("find")==0) ret=new FINDCommand(current);
		return ret;
	}
	
	public DirectoryStream<Path> getStreamFromString(String param) throws IOException{
		DirectoryStream<Path> stream = null;
		DirectoryStream<Path> streamd = null;
		DirectoryStream<Path> streamp = null;
		//Path currentPath = Paths.get(current);
		Path paramPath = (Paths.get(param).normalize());
		//paramPath=currentPath.resolve(paramPath);
		//Utility.mf("GETSTREAM RESOLVED: "+paramPath.toString());
		Path position = Paths.get("/");
		String pattern = "*";
		
		Utility.mf("GETSTREAM paramPath "+paramPath.toString());
    	pattern = paramPath.getFileName().toString();
    	Utility.mf("GETSTREAM pattern "+pattern);
        position = paramPath.getParent();
        Utility.mf("GETSTREAM position "+position.toString());
        BasicFileAttributes b_attr = null;
        			
                    if(param!=null) //presenza del nome del file
                    {
                         boolean isRegFolder = false;
                         boolean isFile = false;
                         try{
                        	 b_attr = Files.readAttributes(paramPath,BasicFileAttributes.class);
                        	 isRegFolder=b_attr.isDirectory();
                        	 if(!isRegFolder)
                        		 isFile=true;
                         }
                         catch(NoSuchFileException nsfe)
                         {
                        	 Utility.mf("NoSuchFileException!");

                         }
                         try 
                         {
	                        	 if(isRegFolder || isFile){
	                        		 Utility.mf("file o cartella secchi");
	                        		 stream = Files.newDirectoryStream(paramPath);       
	                        	 }
	                        	 else{
	                        		 Utility.mf("regexp");  
	                        		 stream = Files.newDirectoryStream(position, pattern);
	                        	 }	
                         }
                             
                        catch (IOException ex) {
                        	System.out.println("il file non esiste!!");
                        	ex.printStackTrace();
                        }
                   
                        /* try {
                        		 //streamd = Files.newDirectoryStream(paramPath);       
                        		 streamp = Files.newDirectoryStream(position, pattern);
                        		/* if(streamd!=null)
                        		 {
                                     Utility.mf("SENZA PATTERN");
                        			 stream= streamd;
                        		 }
                        		 else{
	                        		 if(streamp!=null)
	                        		 {
	                        			 
	                        			 stream =  streamp;
	                        			 Utility.mf("PATTERN"+ streamp.toString());
	                        			 
	                        			 for(Path p : stream)
	                        				 Utility.mf("RISULTATI"+ p.toString());
	                        			 
	                        		 }
                        		 }
                        	 }	
                             
                        catch (IOException ex) {
                        	System.out.println("il file non esiste!!");
                        	ex.printStackTrace();
                        }*/
                         
	
                   
                    }

		return stream;
	}
	
	public boolean matchLastModDate(CommandParameter param, FileTime pft)
	{
		if(param==null)
			return true;
		
		GregorianCalendar paramDateGC;
		FileTime paramLastModTime=FileTime.fromMillis(0);
		try {
			//paramDateGC = 
			paramLastModTime=FileTime.fromMillis(convertDateString(param.getValue()));//FileTime.fromMillis(paramDateGC.getTimeInMillis());
			Utility.mf("PARAM DATA: "+paramLastModTime.toString());
			Utility.mf("ELEME DATA: "+pft.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SignType sign=param.getSign();
		int ftMInt = pft.compareTo(paramLastModTime);
		
		if((ftMInt==0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(ftMInt!=0 & sign==SignType.DIV)|
				(ftMInt>0 & (sign==SignType.MAG|sign==SignType.MAGUG))|
				(ftMInt<0 & (sign==SignType.MIN | sign==SignType.MINUG))){
			return true;//match
		}
		if((ftMInt!=0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(ftMInt==0 & sign==SignType.DIV)){
			return false;//no match
		}
		
		return false;
	
	}//fine match

	public boolean matchPermissions(CommandParameter param, Set<PosixFilePermission> pfp)
	{
		if(param==null)
			return true;
		
		SignType sign=param.getSign();
		Integer pathPerm = new Integer(permissionsToOctal(PosixFilePermissions.toString(pfp)));
		Integer pivot = Integer.parseInt(param.getValue());//param
		
		Utility.mf("Param PERMESSO: "+pivot.toString());
    	Utility.mf("Eleme PERMESSO: "+pathPerm.toString());
    	
		if((pathPerm.compareTo(pivot)==0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(pathPerm.compareTo(pivot)!=0 & sign==SignType.DIV)|
				(pathPerm.compareTo(pivot)>0 & (sign==SignType.MAG|sign==SignType.MAGUG))|
				(pathPerm.compareTo(pivot)<0 & (sign==SignType.MIN|sign==SignType.MINUG))){
			
			Utility.mf("match permesso");
			return true;
		}
		if((pathPerm.compareTo(pivot)!=0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(pathPerm.compareTo(pivot)==0 & sign==SignType.DIV)){
			
			Utility.mf("no match permesso");
			return false;
		}
		return false;
		
		
	}//fine match
	
	public boolean matchSize(CommandParameter param, long ps)
	{
		if(param==null)
			return true;
		
		//Set<PosixFilePermission> paramPermissions= PosixFilePermissions.fromString(param.getValue());
    	Utility.mf("Param SIZE: "+param.getValue());
    	Utility.mf("Eleme SIZE: "+ps);
		
		SignType sign=param.getSign();
		//String pathPerm = PosixFilePermissions.toString(pfp); //del file attuale
		//String pivot = PosixFilePermissions.toString(paramPermissions); //del param
		Long pivot=Long.parseLong(param.getValue());//del param
		Long psobj = new Long(ps);
		if((psobj.compareTo(pivot)==0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(psobj.compareTo(pivot)!=0 & sign==SignType.DIV)|
				(psobj.compareTo(pivot)>0 & (sign==SignType.MAG|sign==SignType.MAGUG)|
				(psobj.compareTo(pivot)<0 & (sign==SignType.MIN|sign==SignType.MINUG)))){
			
			Utility.mf("match size");
			return true;
		}
		if((psobj.compareTo(pivot)!=0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(psobj.compareTo(pivot)==0 & sign==SignType.DIV)){
			
			Utility.mf("no match size");
			return false;
		}
		return false;
		
		
	}//fine match
	
	public long convertDateString(String dateString) throws ParseException {
		//Utility.mf("ds result: "+ dateString);
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		java.util.Date date = sdf.parse(dateString);		
		return date.getTime();
		}
	
	/**
	 * trasforma la stringa dei permessi rwx in forma ottale
	 * @param attributes
	 * @return
	 */
	private int permissionsToOctal(String attributes){
		int j, d=100, value=0;
		String s;
		for(int i=0;i<3;i++){
			j = i*3;
			s = attributes.substring(j, j+3);
			if(s.contains("r"))
				value+=4*d;
			if(s.contains("w"))
				value+=2*d;
			if(s.contains("x"))
				value+=1*d;
			d/=10;
		}
		return value;
	}
}