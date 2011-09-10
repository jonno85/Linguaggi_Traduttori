package it.polito.lt.skype.command;

import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
                ICommand cmd = null;
                CommandParameter[] p = null;
                CommandParameter[][] pm = null;
                String current ="/home/robizz/lt2";
           /*
                cmd = new LSCommand("/home/robizz/lt2");
                p = new CommandParameter[7];
                p[0] = null;//new CommandParameter(ParamType.FILE, "desc", SignType.MAG);
                p[1] = null;//new CommandParameter(ParamType.FILE, "file", SignType.MAG);
                p[2] = new CommandParameter(ParamType.FILE, "lt/c*", SignType.MAG);
                p[3] = null;
                p[4] = null;
            
           
             cmd = new RMCommand("/home/robizz/lt2");
             p = new CommandParameter[7];
             p[2] = new CommandParameter(ParamType.FILE,"lt/c*",null);
             
              
            //CPCommand
                */
                cmd = new MVCommand(current);
                p = new CommandParameter[7];
                p[2] = new CommandParameter(ParamType.FILE, "g*", null);
                p[3] = new CommandParameter(ParamType.FILE, "e", null);//(ParamType.NULL, null, SignType.MAG)//null;//new CommandParameter(ParamType.FILE, "/home/jo/lt/.", null);
                
                /*
                //find command senza exec
                
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
                
        /*     // MVCommand
				cmd = new MVCommand(current);
				p = new CommandParameter[7];
	                p[2] = new CommandParameter(ParamType.FILE,"g",null);
	                p[3] = new CommandParameter(ParamType.FILE,"e",null);
            */
             /*
                cmd=new FINDCommand(current);
                pm = new CommandParameter[7][];
                
                pm[0] =new CommandParameter[]{new CommandParameter(ParamType.FILE, "*.htm", null)};
                		//new CommandParameter(ParamType.FILE, "*", null)};
                pm[1] = new CommandParameter[]{new CommandParameter(ParamType.FILE, "/home", null)};
                		//,new CommandParameter(ParamType.FILE, "/home/robizz/prf/ci/", null)
                		//,new CommandParameter(ParamType.FILE, "/home/jo/lt/ci", null)};
                pm[2] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "001", SignType.MAGUG)};
                pm[3] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "23082011", SignType.MAGUG)};
                pm[4] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "35", SignType.MINUG)};
                pm[5] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "rm", SignType.MINUG)};
                pm[6] = null;//new CommandParameter[]{new CommandParameter(ParamType.NULL, "asc", SignType.MAG),null,null,null,null};//new CommandParameter[]{new CommandParameter(ParamType.FILE, "/home/jo/lt/h", SignType.MINUG)};
         
                
               cmd = new CPCommand(current);
                p = new CommandParameter[2];
                p[0] = new CommandParameter(ParamType.FILE,"/home/jo/lt/e/er33",null);
                p[1] = new CommandParameter(ParamType.FILE,"/home/jo/lt/h12",null);*/
                try {
                    cmd.setCommandParameter(p);
                } catch (ParserException ex) {
                    ex.printStackTrace();
                }
                try{
                    cmd.exec();
                }catch(CommandException ce)
                {
                    System.err.println(ce.getCause());
                }
                
                System.out.println(cmd.getCommandStringResult());

	}

}
