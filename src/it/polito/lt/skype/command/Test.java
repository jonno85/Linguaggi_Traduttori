package it.polito.lt.skype.command;

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
                Path current =Paths.get("/home/jo/lt");
            /* LSCommand
                cmd = new LSCommand(current);
                p = new CommandParameter[5];
                p[0] = new CommandParameter(ParamType.FILE, "desc", SignType.MAG);
                p[1] = null;
                p[2] = null;
                p[3] = null;
                p[4] = null;
            */  
          
            
            /* RMCommand
		cmd = new RMCommand();
		p = new CommandParameter[1];
                p[0] = new CommandParameter(ParamType.FILE,"/home/jo/lt/z",null);
              
             
            /*CPCommand
                cmd = new CPCommand();
                p = new CommandParameter[2];
                p[0] = new CommandParameter(ParamType.FILE, "/home/jo/lt/e/", null);
                p[1] = null;//(ParamType.NULL, null, SignType.MAG)//null;//new CommandParameter(ParamType.FILE, "/home/jo/lt/.", null);
		cmd.setCommandParameter(p);
                */
                
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
		p = new CommandParameter[2];
                p[0] = new CommandParameter(ParamType.FILE,"/home/jo/lt/e/*",null);
                p[1] = new CommandParameter(ParamType.FILE,"/home/jo/lt/h",null);
             */
            
                cmd=new FINDCommand(current);
                pm = new CommandParameter[7][];
                
                pm[0] =new CommandParameter[]{new CommandParameter(ParamType.FILE, "*.zip", null)};
                		//new CommandParameter(ParamType.FILE, "*", null)};
                pm[1] = new CommandParameter[]{new CommandParameter(ParamType.FILE, "/home/jo/lt/h/h2", null)};
                		//,new CommandParameter(ParamType.FILE, "/home/robizz/prf/ci/", null)
                		//,new CommandParameter(ParamType.FILE, "/home/jo/lt/ci", null)};
                pm[2] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "001", SignType.MAGUG)};
                pm[3] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "23082011", SignType.MAGUG)};
                pm[4] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "35", SignType.MINUG)};
                pm[5] = new CommandParameter[]{new CommandParameter(ParamType.FILE, "ls", SignType.MINUG)};
                pm[6] = new CommandParameter[]{new CommandParameter(ParamType.NULL, "asc", SignType.MAG),null,null,null,null};//new CommandParameter[]{new CommandParameter(ParamType.FILE, "/home/jo/lt/h", SignType.MINUG)};

		cmd.setCommandParameter(pm);
                try{
                    cmd.exec();
                }catch(CommandException ce)
                {
                    System.err.println(ce.getCause());
                }
                
                System.out.println(cmd.getCommandStringResult());

	}

}
