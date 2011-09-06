package it.polito.lt.skype.manager;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

public class VarManager{

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
			
			
			System.out.println("dentro");
			if(chkType(a,b)){
				System.out.println("dentro2");
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
	
}
