package it.polito.lt.skype.manager;

import java.util.ArrayList;
import java.util.List;

public class myVar{
private String name;
private int type;
private Object value;
private List<myVar> pre_op = null;
public static final int _notInit = 0;
public static final int _int = 1;
public static final int _float = 2;
public static final int _string = 3;
public static final int _data = 4;
public static final int _bool = 5;

	public myVar(){
            pre_op = new ArrayList<>();
        }

	public myVar(String name, int type, Object value, myVar temp_res){
		this.name = name;
		this.type = type;
		this.value = value;
                pre_op = new ArrayList<>();
                pre_op.add(temp_res);
        }
        
        public myVar(String name, int type, Object value){
		this.name = name;
		this.type = type;
		this.value = value;
                pre_op = new ArrayList<>();
        }
        
        /**
         * si suppone che sia intero
         * @param name
         * @param value 
         */
        public myVar(String name, Object value){
		this.name = name;
		this.type = 1;
		this.value = value;
                pre_op = new ArrayList<>();
        }
        

	public myVar(int type, Object value){
		this.name = "";
		this.type = type;
		this.value = value;
                pre_op = new ArrayList<>();
	}

	public myVar(String name){
		this.name = name;
		this.type = _notInit;
		this.value = null;
                pre_op = new ArrayList<>();
	}
        
	public void setName(String name){	
		this.name = name;
	}
        
	public void printVar(){
		System.out.println("\n name: "+name+"\ntype: "+type+"\nvalue: "+value);	
	}
        
	public String getName(){
		return name;	
	}
        
	public Integer getType(){
		return type;	
	}
        
	public void setType(int type){
		this.type=type;
	}
        
        public void setValue(Object value){
            this.value = value;
        }
        
	public String getStringValue(){
		String valore = null;
		if(this.type==0)
			valore = new String("notInit");
		else
			valore = ""+value;		
		return valore;
	}
        
	public Object getValue(){
		return this.value;
	}
        
        /**
         * [Aggiunge la operazione alla lista interna di operazioni per ricalcolare 
         * la variabile a run-time  ]NO
         * 
         * aggiungiamo la variabile temporanea che contiene il risultato temporale 
         * dell'operazione intermedia
         * @param op 
         */
        public void addTempRes(myVar temp_res){
            pre_op.add(temp_res);
        }

}
