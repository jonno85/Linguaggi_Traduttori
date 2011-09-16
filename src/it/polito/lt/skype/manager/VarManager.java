package it.polito.lt.skype.manager;

import it.polito.lt.skype.command.Utility;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;


public class VarManager{

	private HashMap var_tb;
 //       private HashMap temp_var_tb;    //conterra le variabili temporanee per le operazioni intermedie
	private myVar app = null;
	private myVar ris_inter = null;
	private Operation mkOper;
	private String unknowVarName = null;
	public static Collection c;
        private int tmp_name = 1;
		private myVar bool_ris_inter;
	
	public VarManager(){
		var_tb = new HashMap();
   //             temp_var_tb = new HashMap();
		ris_inter = new myVar();
		mkOper = new Operation();
		unknowVarName = "QQSYSTEM";
	}

    //Hash table in cui vengono memorizzati tutte le variabili dichiarate nel programma: nome,myVar
//public void newVar(String name)    
     
   /*     public void add_tmp_var(myVar var)
        {
        var.setName("_tmp_"+tmp_name++);
        var_tb.put(var.getName(),var);	
		System.out.println("add_tmp_var(): "+var.getName()+" = "+var.getStringValue());
	}*/

        
	public void add_var(myVar var){
		  
		var_tb.put(var.getName(),var);	
            //System.out.println("Dichiarazione: "+var.getName()+" = "+var.getStringValue());
            //if(var.getName().equals(""));
            	//var.setName("_tmp_"+tmp_name++);
            //var_tb.put(var.getName(),var);	
            //System.out.println("add_var(): "+var.getName()+" = "+var.getStringValue());
            
			//valutare la necessita di ricorsivita multilivello
                myVar tmpOp1, tmpOp2;
                if(var.getOperation()!=null){
                    tmpOp1 = var.getOperation().getOp(1);
                    tmpOp2 = var.getOperation().getOp(2);
                    if("".equals(tmpOp1.getName())){
                        tmpOp1.setName("_tmp_"+tmp_name++);
                        add_var(tmpOp1);
                    }
                    if("".equals(tmpOp2.getName())){
                        tmpOp2.setName("_tmp_"+tmp_name++);
                        add_var(tmpOp2);
                    }
                }
            //***************************************************/
                Utility.mf(var.getName()+" stored"); 
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
                        if(app.getOperation()!=null)
                            System.out.println("operazione interna trovta: "+app.getOperation().toString());
			if(chkType(app,var)||app.getType()==myVar._notInit){
				add_var(var);
				System.out.println("Assegnazione: "+var.getName()+" = "+var.getStringValue());
			}
			else
				System.out.println("variabile: "+var.getName()+" DICHIARATA MA TIPO NON CORRISPONDENTE");
		}
                else{    //variabile non dichiarata o senza nome
			System.out.println("variabile: "+var.getName()+" NON DICHIARATA");
                }
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
		a.toString();
		if(chkVar(a)){
			System.out.println("dentro");
			switch(a.getType()){
				//case 1: {
					//System.out.println("intero");
					//return ris_inter = mkOper.Neg(a);
					//}
				//case 2: return ris_inter = mkOper.Neg(a);
				 case 5: {
					 System.out.println("booleano");
					 ris_inter = mkOper.BNoper(a,segno);
					 break;
                 }
				 default: return null;
			}
			ris_inter.setOperation(new Operation(a,null,segno));
		}
		//System.out.println("VALORE VARIABILE INVALIDO");
		return ris_inter;
	}

	public myVar makeOper(myVar a, myVar b, String segno){
		a.toString();
		b.toString();
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
                                            //System.out.println("intero");
                                             ris_inter = mkOper.Ioper(a,b,segno);
                                             break;
                                            }
                                    case 2:  {
                                    			ris_inter = mkOper.Foper(a,b,segno);
                                    			break;
                                    		}
                                    		
                                    case 3: {
                                            //System.out.println("concatenazione stringa");
                                            ris_inter = mkOper.Soper(a,b,segno);
                                            break;
                                            }
                                    case 5:{
                                    	ris_inter = mkOper.Boper(a,b,segno);	
                                    	break;
                                    }
                                    default: return null;
				}
				ris_inter.setOperation(new Operation(a,b,segno));
				
				
			}
		}
		//System.out.println("VALORE VARIABILE INVALIDO");
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
        
    	public myVar makeLogicOper(myVar a, myVar b, String segno){
    		a.toString();
    		b.toString();
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
                                        		bool_ris_inter = mkOper.LogicIoper(a,b,segno);
                                        			break;
                                        		}
                                        case 2:  {
                                        		bool_ris_inter = mkOper.LogicFoper(a,b,segno);
                                        			break;
                                        		}
                                        		
                                        case 3: {
                                                System.out.println("confronto stringa");
                                                bool_ris_inter = mkOper.LogicSoper(a,b,segno);
                                                break;
                                                }
                                        case 5:{
                                        	bool_ris_inter = mkOper.LogicBoper(a,b,segno);	
                                        	break;
                                        }
                                        default: return null;
    				}
    				bool_ris_inter.setOperation(new Operation(a,b,segno));
    				
    				
    			}
    		}
    		//System.out.println("VALORE VARIABILE INVALIDO");
    		return null;
    	}
        
       
	
}
