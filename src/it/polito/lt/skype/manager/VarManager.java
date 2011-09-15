package it.polito.lt.skype.manager;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class VarManager implements ICommand{

	private HashMap var_tb;
	private myVar app = null;
	private myVar ris_inter = null;
	private Operation mkOper;
	private String unknowVarName = null;
	public static Collection c;
	
	public VarManager(){
		var_tb = new HashMap();
		ris_inter = new myVar();
		mkOper = new Operation();
		unknowVarName = "QQSYSTEM";
	}

    //Hash table in cui vengono memorizzati tutte le variabili dichiarate nel programma: nome,myVar
//public void newVar(String name)    


	public void add_var(myVar var){
		var_tb.put(var.getName(),var);	
		System.out.println("Dichiarazione: "+var.getName()+" = "+var.getStringValue());
	}
	public void getListVar(){
		c = var_tb.values();
		Iterator itr = c.iterator();
		myVar app;
		while(itr.hasNext()){
			app=(myVar)itr.next();
			System.out.println("Variabile " +app.getName()+" Tipo " +app.getType());
		}
	}
	public void assig(myVar var){
		if((app = extractVar(var.getName()))!=null){
			if(chkType(app,var)||app.getType()==myVar._notInit){
				add_var(var);
				System.out.println("Assegnazione: "+var.getName()+" = "+var.getStringValue());
			}
			else
				System.out.println("variabile: "+var.getName()+" DICHIARATA MA TIPO NON CORRISPONDENTE");
		}
		else
			System.out.println("variabile: "+var.getName()+" NON DICHIARATA");
	}

	public myVar extractVar(String name){
		//restituisce la variabile che contiene il risultato intermedio
		if(name.compareToIgnoreCase("")==0)
			return ris_inter;			
		//restituisce la variabile se definita nella tabella delle variabili
		return ((myVar)var_tb.get(name));
	}

        public boolean isPos(myVar a){
                if(a.getType()==1){
                    if((Integer)a.getValue()>=0)  
                        return true;
                }
                if(a.getType()==2){
                    if((Float)a.getValue()>=0.0)    
                        return true;
                }
		return false;
	}
        
	public boolean chkType(myVar a, myVar b){
		if(a.getType()==b.getType())        
			return true;
		else
			return false;
	}
	public boolean chkVar(myVar a){
		boolean c = true;
		if(a.getName()!=null)
			if (extractVar(a.getName())==null)
				c = false;
		return c;
	}

	public myVar makeSOper(myVar a, String segno){
		a.printVar();
		if(chkVar(a)){
			System.out.println("dentro");
			switch(a.getType()){
				case 1: {
					System.out.println("intero");
					return ris_inter = mkOper.Neg(a);
					}
				case 2: return ris_inter = mkOper.Neg(a);

				case 5: {System.out.println("booleano");
					return ris_inter = mkOper.BNoper(a,segno);}		
			}
		}
		System.out.println("VALORE VARIABILE INVALIDO");
		return null;
	}

	public myVar makeOper(myVar a, myVar b, String segno){
		a.printVar();
		b.printVar();
		if( chkVar(a)&&chkVar(b)){
			
			if(a.getType()==3 & b.getType()!=3 & segno.equalsIgnoreCase("+")){
				b.setType(3);
			}
			if(a.getType()!=3 & b.getType()==3 & segno.equalsIgnoreCase("+")){
				a.setType(3);
			}
			if(chkType(a,b)){
				
				switch(a.getType()){
                                    case 1: {
                                            System.out.println("intero");
                                            return ris_inter = mkOper.Ioper(a,b,segno);
                                            }
                                    case 2: return ris_inter = mkOper.Foper(a,b,segno);

                                    case 3: {
                                            System.out.println("concatenazione stringa");
                                            return ris_inter = mkOper.Soper(a,b,segno);
                                            }
                                    case 5: return ris_inter = mkOper.Boper(a,b,segno);		
				}
			}
		}
		System.out.println("VALORE VARIABILE INVALIDO");
		
		return null;
	}
        
        public myVar Auto_Neg(myVar a,String segno){
                int type = a.getType();
                if(segno.equalsIgnoreCase("-"))
                    return mkOper.Neg(a);
                return a;
        }
        
        /*
         * Logic Operation >
         */
        public boolean makeLOMag(myVar x, myVar y)
        {
            boolean result = false;
            if(x.getType()==y.getType())
            {
                switch(x.getType())
                {
                    case 1:
                        if((Integer)x.getValue()>(Integer)y.getValue())
                            result = true;
                        break;
                    case 2:
                        if((Float)x.getValue()>(Float)y.getValue())
                            result = true;
                        break;
                    case 3:
                        if(x.getStringValue().length()>y.getStringValue().length())
                            result = true;
                        break;
                }
            }
            return result;
        }
         /*
         * Logic Operation >=
         */
        public boolean makeLOMaU(myVar x, myVar y)
        {
            boolean result = false;
            if(x.getType()==y.getType())
            {
                switch(x.getType())
                {
                    case 1:
                        if((Integer)x.getValue()>=(Integer)y.getValue())
                            result = true;
                        break;
                    case 2:
                        if((Float)x.getValue()>=(Float)y.getValue())
                            result = true;
                        break;
                    case 3:
                        if(x.getStringValue().length()>=y.getStringValue().length())
                            result = true;
                        break;
                }
            }
            return result;
        }
         /*
         * Logic Operation <
         */
        public boolean makeLOMin(myVar x, myVar y)
        {
            boolean result = false;
            if(x.getType()==y.getType())
            {
                switch(x.getType())
                {
                    case 1:
                        if((Integer)x.getValue()<(Integer)y.getValue())
                            result = true;
                        break;
                    case 2:
                        if((Float)x.getValue()<(Float)y.getValue())
                            result = true;
                        break;
                    case 3:
                        if(x.getStringValue().length()<y.getStringValue().length())
                            result = true;
                        break;
                }
            }
            return result;
        }
         /*
         * Logic Operation <=
         */
        public boolean makeLOMiU(myVar x, myVar y)
        {
            boolean result = false;
            if(x.getType()==y.getType())
            {
                switch(x.getType())
                {
                    case 1:
                        if((Integer)x.getValue()<=(Integer)y.getValue())
                            result = true;
                        break;
                    case 2:
                        if((Float)x.getValue()<=(Float)y.getValue())
                            result = true;
                        break;
                    case 3:
                        if(x.getStringValue().length()<=y.getStringValue().length())
                            result = true;
                        break;
                }
            }
            return result;
        }
         /*
         * Logic Operation ==
         */
        public boolean makeLOUg(myVar x, myVar y)
        {
            boolean result = false;
            if(x.getType()==y.getType())
            {
                switch(x.getType())
                {
                    case 1:
                        if((Integer)x.getValue()==(Integer)y.getValue())
                            result = true;
                        break;
                    case 2:
                        if((Float)x.getValue()==(Float)y.getValue())
                            result = true;
                        break;
                    case 3:
                        if(x.getStringValue().length()==y.getStringValue().length())
                            result = true;
                        break;
                }
            }
            return result;
        }
         /*
         * Logic Operation !=
         */
        public boolean makeLODiv(myVar x, myVar y)
        {
            boolean result = false;
            if(x.getType()==y.getType())
            {
                switch(x.getType())
                {
                    case 1:
                        if((Integer)x.getValue()!=(Integer)y.getValue())
                            result = true;
                        break;
                    case 2:
                        if((Float)x.getValue()!=(Float)y.getValue())
                            result = true;
                        break;
                    case 3:
                        if(x.getStringValue().length()!=y.getStringValue().length())
                            result = true;
                        break;
                }
            }
            return result;
        }

    @Override
    public boolean exec() throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exec_from_prev_result(List<Path> stream) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommandParameter(CommandParameter[] cpl) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCommandParameter(CommandParameter[][] cpl) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Path> getCommandResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCommandStringResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void usage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
}
