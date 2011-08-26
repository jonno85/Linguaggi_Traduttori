package it.polito.lt.skype.command;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
                ICommand cmd = null;
                CommandParameter[][] p = null;
                Path current =Paths.get("/home/robizz/prf/");
            /*  RMCommand
		cmd = new RMCommand();
		p = new CommandParameter[1];
                p[0] = new CommandParameter(ParamType.FILE,"/home/jo/lt/e2*",null);
             * 
             
            //CPCommand
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
                
                cmd=new FINDCommand();
                p = new CommandParameter[6][];
                
                p[0] = new CommandParameter[]{new CommandParameter(ParamType.FILE, "*.txt", null),
                		new CommandParameter(ParamType.FILE, "*.htm", null)};
                p[1] = new CommandParameter[]{new CommandParameter(ParamType.FILE, "/home/robizz/prf", null)};
                p[2] = new CommandParameter[]{new CommandParameter(ParamType.FILE, "001", SignType.MAGUG)};
                p[3] = new CommandParameter[]{new CommandParameter(ParamType.FILE, "23082011", SignType.MAGUG)};
                p[4] = null;//new CommandParameter[]{new CommandParameter(ParamType.FILE, "35", SignType.MINUG)};
		cmd.setCommandParameter(p);
                try{
                    cmd.exec();
                }catch(CommandException ce)
                {
                    System.err.println(ce.getCause());
                }
                
                System.out.println(cmd.getCommandStringResult());

	}

}