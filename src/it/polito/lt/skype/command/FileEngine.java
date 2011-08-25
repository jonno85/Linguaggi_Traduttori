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
import java.util.GregorianCalendar;
import java.util.Set;

public class FileEngine {
	
	
	
	
	public DirectoryStream<Path> getStreamFromParameter(CommandParameter param) throws IOException{
		DirectoryStream<Path> stream = null;
		Path paramPath = Paths.get("/");
		Path position = Paths.get("/");
		String pattern = "*";
		paramPath = ((Path)Paths.get(param.getValue()).normalize());
    	pattern = paramPath.getFileName().toString();
        position = paramPath.getParent();
        BasicFileAttributes b_attr = null;
        			//OCCHIO SE IL PARAMETRO é NULLO SIGNIFICA CHE CI VA LA CARTELLA CORENTE!
        //QUINDI E DA SISTEMARE
                    if(param!=null) //presenza del nome del file
                    {
                         boolean isRegFolder = false;
                         boolean isFile = false;
                         try{
                        	 b_attr = Files.readAttributes(paramPath,BasicFileAttributes.class);
                        	 isFile=b_attr.isRegularFile();
                         }
                         catch(NoSuchFileException nsfe)
                         {
                        	 System.out.println("NoSuchFileException!");
                        	 isRegFolder=true;
                         }
                         try {
                        	 if(isRegFolder || isFile)
                        		stream = Files.newDirectoryStream(position, pattern);
                        	 else
                                stream = Files.newDirectoryStream(paramPath);                             	
                        	 }	
                             
                        catch (IOException ex) {
                        	System.out.println("il file non esiste!!");
                        	ex.printStackTrace();
                        }
                    }
                    Utility.mf("pos "+position.toString()+"\npattern "+pattern.toString());
		

		return stream;
	}
	
	public boolean matchLastModDate(CommandParameter param, FileTime pft)
	{
		if(param==null)
			return true;
		
		GregorianCalendar paramDateGC;
		FileTime paramLastModTime=FileTime.fromMillis(0);
		try {
			paramDateGC = convertDateString(param.getValue());
			paramLastModTime=FileTime.fromMillis(paramDateGC.getTimeInMillis());
			Utility.mf("PARAMETRO DATA: "+paramLastModTime.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SignType sign=param.getSign();
		int ftMInt = pft.compareTo(paramLastModTime);
		
		if((ftMInt==0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(ftMInt!=0 & sign==SignType.DIV)|
				(ftMInt>0 & sign==SignType.MAG)|(ftMInt<0 & sign==SignType.MIN)){
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
		
		Set<PosixFilePermission> paramPermissions= PosixFilePermissions.fromString(param.getValue());
    	Utility.mf("Parametro PERMESSO: "+PosixFilePermissions.toString(paramPermissions));
		
		SignType sign=param.getSign();
		String pathPerm = PosixFilePermissions.toString(pfp);
		String pivot = PosixFilePermissions.toString(paramPermissions);
		
		if((pathPerm.compareTo(pivot)==0 & (sign==SignType.UG | sign==SignType.MINUG | sign==SignType.MAGUG))|
				(pathPerm.compareTo(pivot)!=0 & sign==SignType.DIV)|
				(pathPerm.compareTo(pivot)>0 & sign==SignType.MAG)|(pathPerm.compareTo(pivot)<0 & sign==SignType.MIN)){
			
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
	
	public GregorianCalendar convertDateString(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		java.util.Date date = sdf.parse(dateString);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
		}

}
