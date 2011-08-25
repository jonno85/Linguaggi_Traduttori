package it.polito.lt.skype.command;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
                ICommand cmd = null;
                CommandParameter[] p = null;
            
            /*  RMCommand
		cmd = new RMCommand();
		p = new CommandParameter[1];
                p[0] = new CommandParameter(ParamType.FILE,"/home/jo/lt/e2*",null);
             * 
             */
            //CPCommand
                cmd = new CPCommand();
                p = new CommandParameter[2];
                p[0] = new CommandParameter(ParamType.FILE, "/home/jo/lt/e/", null);
                p[1] = null;//(ParamType.NULL, null, SignType.MAG)//null;//new CommandParameter(ParamType.FILE, "/home/jo/lt/.", null);
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
