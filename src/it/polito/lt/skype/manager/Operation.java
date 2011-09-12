package it.polito.lt.skype.manager;


public class Operation{
	private myVar result;
	public Operation(){
		result = null;
		}
	public myVar Ioper(myVar a, myVar b, String segno){
		result = null;
		Integer op1 = (Integer)(a.getValue());
		Integer op2 = (Integer)(b.getValue());
		System.out.println("valori "+op1+" "+op2);
		int type = a.getType(); 
		if(segno.compareTo("+")==0){
			System.out.println("operazione somma");
			result = new myVar("",type,new Integer(op1.intValue()+op2.intValue()));
		}
		if(segno.compareTo("-")==0) 
			result =  new myVar("",type,new Integer(op1.intValue()-op2.intValue()));
		if(segno.compareTo("*")==0)
			result =  new myVar("",type,new Integer(op1.intValue()*op2.intValue()));
		if(segno.compareTo("/")==0)
			result =  new myVar("",type,new Integer(op1.intValue()+op2.intValue()));
		return result;

	}
	public myVar Foper(myVar a, myVar b, String segno){
		result = null;
		Float op1 = (Float)(a.getValue());
		Float op2 = (Float)(b.getValue());
		int type = a.getType();
		if(segno.compareTo("+")==0)
			result = new myVar("",type,new Float(op1.floatValue()+op2.floatValue()));
		if(segno.compareTo("-")==0) 
			result = new myVar("",type,new Float(op1.floatValue()-op2.floatValue()));
		if(segno.compareTo("*")==0)
			result = new myVar("",type,new Float(op1.floatValue()*op2.floatValue()));
		if(segno.compareTo("/")==0)
			result = new myVar("",type,new Float(op1.floatValue()/op2.floatValue()));
		return result;
	}
	public myVar Soper(myVar a, myVar b, String segno){
		result = null;
		int type = a.getType();
		if(segno.compareTo("+")==0) //rimosso costruttore String - check!
			result = new myVar("",type,(String)(a.getValue()+(String)(b.getValue())));	
		return result;
	}
	public myVar Boper(myVar a, myVar b, String segno){
		result = null;
		int type = a.getType();
		if(segno.compareTo("&")==0 | segno.compareTo("&&")==0) //rimosso costruttore boolean - check!
			result = new myVar("",type, ((Boolean)(a.getValue())&((Boolean)(b.getValue()))));
		if(segno.compareTo("|")==0 || segno.compareTo("||")==0) 
			result = new myVar("",type, ((Boolean)(a.getValue())|((Boolean)(b.getValue()))));
		return result;	
	}
	public myVar BNoper(myVar a, String segno){
		int type = a.getType();
		if(segno.compareTo("!")==0) 
			result = new myVar("",type, (!((Boolean)(a.getValue()))));
		return result;	
	}
	public myVar Neg(myVar a){
		result = a;//null;
		int type = a.getType();
		if(type==1)
			result = new myVar("",type,new Integer(-(Integer)a.getValue()));
		if(type==2)
			result = new myVar("",type,new Float(-(Float)a.getValue()));
		return result;
	}
        
}
