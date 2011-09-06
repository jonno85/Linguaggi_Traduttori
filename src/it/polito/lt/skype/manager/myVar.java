package it.polito.lt.skype.manager;

public class myVar{
	private String name;
	private int type;
	private Object value;
	public static final int _notInit = 0;
	public static final int _int = 1;
	public static final int _float = 2;
	public static final int _string = 3;
	public static final int _data = 4;
	public static final int _bool = 5;

	public myVar(){}

	public myVar(String name, int type, Object value){
		this.name = name;
		this.type = type;
		this.value = value;
	}


	public myVar(int type, Object value){
		this.name = "";
		this.type = type;
		this.value = value;
	}

	public myVar(String name){
		this.name = name;
		this.type = _notInit;
		this.value = null;
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

}
