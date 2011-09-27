package it.polito.lt.skype.manager;

import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.parser.ParserException;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;


public class VarManager{

	private HashMap<String, myVar> var_tb;
 //       private HashMap temp_var_tb;    //conterra le variabili temporanee per le operazioni intermedie
	private myVar app = null;
	private myVar ris_inter = null;
	private Operation mkOper;
	private Collection c;
	private VarManager mainManager=null;
    private int tmp_num = 1;
    private String tmp_name = "_tmp_";
	private myVar bool_ris_inter;
	
	public VarManager(){
		var_tb = new HashMap<String, myVar>();
   //             temp_var_tb = new HashMap();
		ris_inter = new myVar();
		mkOper = new Operation();
                mkOper.setVM(this);

	}
        
        public void setTmpName(String tmp){
            tmp_name = tmp;
        }

    //Hash table in cui vengono memorizzati tutte le variabili dichiarate nel programma: nome,myVar
//public void newVar(String name)    
     
   /*     public void add_tmp_var(myVar var)
        {
        var.setName("_tmp_"+tmp_name++);
        var_tb.put(var.getName(),var);	
		Utility.mf("add_tmp_var(): "+var.getName()+" = "+var.getStringValue());
	}*/

    public String getTempName(){
    	return tmp_name+tmp_num++;
    }
	
	public void add_var(myVar var){
                
                //    Utility.mf("valore PRIMA di aggiornare " + (var_tb.get(var.getName())).getStringValue());
		var_tb.put(var.getName(),var);	
                //if(var_tb.get(var.getName())!=null)
                //    Utility.mf("valore DOPO di aggiornare " + (var_tb.get(var.getName())).getStringValue());
            //Utility.mf("Dichiarazione: "+var.getName()+" = "+var.getStringValue());
            //if(var.getName().equals(""));
            	//var.setName("_tmp_"+tmp_name++);
            //var_tb.put(var.getName(),var);	
            //Utility.mf("add_var(): "+var.getName()+" = "+var.getStringValue());
            
            
            //***************************************************/
                //Utility.mf(var.getName()+" stored"); 
	}
        
	public void getListVar(){
		c = var_tb.values();
		Iterator itr = c.iterator();
		myVar app;
		while(itr.hasNext()){
			app=(myVar)itr.next();
			Utility.mf("Variabile " +app.getName()+" Tipo " +app.getType()+" value: "+app.getStringValue());
		}
	}
	public void assig(myVar var) throws ManagerException{
		if((app = extractVar(var.getName()))!=null){
                    if(chkType(app,var)||app.getType()==myVar._notInit){
                            add_var(var);
                            //Utility.mf("Assegnazione: "+var.getName()+" = "+var.getStringValue());
                    }
                    else{
                        Utility.mf("variabile: "+var.getName()+" DICHIARATA MA TIPO NON CORRISPONDENTE");
                        throw new ManagerException(ManagerErrorType.TYPE_MISMATCH, this.getClass().getName(),
                                Thread.currentThread().getStackTrace()[2].getMethodName(),
                                "["+var.getName()+"]: RIGHT MEMBER TYPE MISMATCH", null);
                    }
                }
                else{    //variabile non dichiarata o senza nome
				Utility.mf("variabile: "+var.getName()+" NON DICHIARATA");
				throw new ManagerException(ManagerErrorType.UNDECLARED, this.getClass().getName(),
	                    Thread.currentThread().getStackTrace()[2].getMethodName(),
	                    "["+var.getName()+"]: UNDECLARED", null);
                }
	}

	public myVar extractVar(String name) throws ManagerException{
		//Utility.mf("ExctractVar(): "+name);
		//restituisce la variabile che contiene il risultato intermedio
		//if(name.compareToIgnoreCase("")==0)
		//	return ris_inter;			
		//restituisce la variabile se definita nella tabella delle variabili
		myVar e =null;
		e=var_tb.get(name);
		if(e==null){
			if(mainManager!=null){
	              e = mainManager.extractVar(name);
				}
				else
					throw new ManagerException(ManagerErrorType.UNDECLARED, this.getClass().getName(),
		                    Thread.currentThread().getStackTrace()[2].getMethodName(),
		                    "["+name+"]: UNDECLARED",null);
		}
		return e;
         
	}

        public boolean isPos(String name) throws ManagerException{
             myVar a = extractVar(name);
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
			try {
				if(extractVar(a.getName())==null)
					if(mainManager!=null)
						if(mainManager.extractVar(a.getName())==null)
							c = false;
			} catch (ManagerException e) {
				Utility.mf("Check var failed");
				c=false;
			}
		return c;
	}
        
        public myVar getIndexResult(myVar a) throws ManagerException{
        	myVar var = null;
        		try {
					var = extractVar("result_"+a.getStringValue());
				} catch (ManagerException e) {
					// TODO Auto-generated catch block
					throw new ManagerException(ManagerErrorType.OUT_OF_BOUND, this.getClass().getName(),
		                    Thread.currentThread().getStackTrace()[2].getMethodName(),
		                    "$result: out of bound exception",null);
				}
    		return var;
        }

	public myVar makeSOper(myVar a, String segno) throws ManagerException{
            if(chkVar(a)){
                switch(a.getType()){
                    case 1: 
                            if(segno.equalsIgnoreCase("[]"))
                                return ris_inter = getIndexResult(a);
                            if(segno.equalsIgnoreCase("--"))
                                return ris_inter = mkOper.Neg(a);
                    case 2: return ris_inter = mkOper.Neg(a);
                    case 5: ris_inter = mkOper.BNoper(a,segno);
                            break;
                    default: return null;
                }
            }
            return ris_inter;
	}

	public myVar makeOper(myVar a, myVar b, String segno) {
            myVar x = new myVar();
			myVar y = new myVar();
			x.set(a);
			y.set(b);
			x.setType(3);
			y.setType(3);
            if( chkVar(x)&&chkVar(y)){
                if((a.getType()==3 && b.getType()!=3||a.getType()!=3 && b.getType()==3) && segno.equalsIgnoreCase("+"))
                	ris_inter = mkOper.makeOper(x, y, segno);
                else{
		                if(chkType(a,b))
		                    ris_inter = mkOper.makeOper(a, b, segno);
		                
                }
            }
            //Utility.mf("VALORE VARIABILE INVALIDO");
            return ris_inter;
	}
        
        public String Auto_Neg(String name,String segno) throws ManagerException{
                myVar a =extractVar(name);                		
        		
                if(segno.equalsIgnoreCase("-"))
                 a=   mkOper.Neg(extractVar(name));
                add_var(a);
                return name;
        }
        
        /*
         * Logic Operation >
         */
        
    	public myVar makeLogicOper(myVar a, myVar b, String segno){
    		a.toString();
    		b.toString();
    		if( chkVar(a)&&chkVar(b)){
    			
    			if(chkType(a,b)){
    				bool_ris_inter = mkOper.makeLogicOper(a, b, segno);
    				
    			}
    		}
    		//Utility.mf("VALORE VARIABILE INVALIDO");
    		return bool_ris_inter;
    	}

		public VarManager getMainManager() {
			return mainManager;
		}

		public void setMainManager(VarManager mainManager) {
			this.mainManager = mainManager;
		}
        
       
	
}
