package it.polito.lt.skype.manager;

import it.polito.lt.skype.command.CommandException;
import it.polito.lt.skype.command.CommandParameter;
import it.polito.lt.skype.command.ICommand;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.parser.ParserErrorType;
import it.polito.lt.skype.parser.ParserException;
import java.nio.file.Path;
import java.util.List;


public class Operation implements ICommand{
	private myVar result;
	private boolean bool_result;
        private myVar op1 = null;
        private myVar op2 = null;
        private String sign = null;
        private myVar bool_ris_inter;
        
        public Operation(myVar a, myVar b, String segno)
        {
            op1 = a;
            op2 = b;
            sign = segno;
        }
        
        public void setArgument(myVar a, myVar b, String segno)
        {
            op1 = a;
            op2 = b;
            sign = segno;
        }
        
        public myVar getResult(){
        	return result;
        }
        
        
        public myVar makeOper() throws CommandException, ParserException{
        	Utility.mf("Lanciato makeOPER: "+sign);
        	if(sign=="<"||sign==">"||sign=="<="||sign==">="||sign.equalsIgnoreCase("==")||sign=="!=")
        		return makeLogicOper(op1, op2, sign);
        	else {
        		op1.exec();
        		op2.exec();
        		return makeNumOper(op1, op2, sign);
        	}
        }
        
       
        public myVar makeNumOper(myVar a, myVar b, String segno) throws ParserException{
        	Utility.mf("MAKE NUM OPERATION "+a.toString()+" "+segno+" "+b.toString() );
				switch(a.getType()){
                                    case 1: {
                                            Utility.mf("intero");
                                             result = Ioper(a,b,segno);
                                             break;
                                            }
                                    case 2:  {
                                    	Utility.mf("float");
                                    	result = Foper(a,b,segno);
                                    			break;
                                    		}
                                    		
                                    case 3: {
                                            Utility.mf("concatenazione stringa");
                                    	result = Soper(a,b,segno);
                                            break;
                                            }
                                    case 5:{
                                    	Utility.mf("boolean");
                                    	result = Boper(a,b,segno);
                                		//Utility.mf(ris_inter.toString());
                                    	break;
                                    }
                                    default: return null;
				
			}
			if(result==null)
				throw new ParserException(ParserErrorType.INVALID_PARAMETER, this.getClass().getName(),
                   Thread.currentThread().getStackTrace()[2].getMethodName(), "Opeation problem");
				result.setOperation(new Operation(a,b,segno));
			return result;
        }
        
        //metodo magico parte2: va accorpato poi, qui si trattano i confronti
        public myVar makeLogicOper(myVar a, myVar b, String segno){
        	switch(a.getType()){
	            case 1: {
	            		bool_ris_inter = LogicIoper(a,b,segno);
	            			break;
	            		}
	            case 2:  {
	            		bool_ris_inter = LogicFoper(a,b,segno);
	            			break;
	            		}
	            		
	            case 3: {
	                    //Utility.mf("confronto stringa");
	                    bool_ris_inter = LogicSoper(a,b,segno);
	                    break;
	                    }
	            case 5:{
	            	bool_ris_inter = LogicBoper(a,b,segno);	
	            	break;
	            }
	            default: return null;
        	}
        		bool_ris_inter.setOperation(new Operation(a,b,segno));
        		return bool_ris_inter;
        }
        
        
        
        
        
        public String toString(){
            return " ("+op1.toString()+"["+sign+"]"+op2.toString()+")";
        }
        
        //restituisce l'operando 1 o 2
        public myVar getOp(int i){
            if(i==1)
                return op1;
            if(i==2)
                return op2;
            return null;
        }
        
	public Operation(){
		result = null;
	}
        
	public myVar Ioper(myVar a, myVar b, String segno){
		result = null;
                setArgument(a, b, segno);
		//Integer op1 = (Integer)(a.getValue());
		//Integer op2 = (Integer)(b.getValue());
		
		int type = a.getType(); 
		if(segno.compareTo("+")==0){
			//Utility.mf("operazione somma");
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    + ((Integer)op2.getValue()).intValue()));
		}
		if(segno.compareTo("-")==0) 
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    - ((Integer)op2.getValue()).intValue()));
		if(segno.compareTo("*")==0)
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    * ((Integer)op2.getValue()).intValue()));
		if(segno.compareTo("/")==0)
			result = new myVar("",type,new Integer(((Integer)op1.getValue()).intValue()
                                                                    / ((Integer)op2.getValue()).intValue()));
		return result;

	}
	public myVar Foper(myVar a, myVar b, String segno){
		result = null;
                setArgument(a, b, segno);
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
		//Utility.mf(result.toString());
		return result;	
	}
	
	public myVar BNoper(myVar a, String segno){
		int type = a.getType();
		if(segno.compareTo("!")==0) 
			result = new myVar("",type, (!((Boolean)(a.getValue()))));
		return result;	
	}
	
	public myVar BNoper(){
			result = new myVar("",myVar._bool, (!((Boolean)(op1.getValue()))));
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
	
	public myVar LogicIoper(myVar a, myVar b, String segno){
		result = null;
                setArgument(a, b, segno);
		Integer op1 = (Integer)(a.getValue());
		Integer op2 = (Integer)(b.getValue());
		int type = a.getType();
		switch(segno){
			case">":{if(op1>op2)bool_result=true;else bool_result=false; break;}
			case"<":{if(op1<op2)bool_result=true;else bool_result=false; break;}
			case">=":{if(op1>=op2)bool_result=true;else bool_result=false; break;}
			case"<=":{if(op1<=op2)bool_result=true;else bool_result=false; break;}
			case"==":{if(op1==op2)bool_result=true;else bool_result=false; break;}
			case"!=":{if(op1!=op2)bool_result=true;else bool_result=false; break;}
			default: return null;
		}
		myVar tmp = new myVar("",myVar._bool,new Boolean(bool_result));
		return tmp;
	}
	
	public myVar LogicFoper(myVar a, myVar b, String segno){
		
                setArgument(a, b, segno);
		Float op1 = (Float)(a.getValue());
		Float op2 = (Float)(b.getValue());
		int type = a.getType();
		switch(segno){
			case">":{if(op1>op2)bool_result=true;else bool_result=false; break;}
			case"<":{if(op1<op2)bool_result=true;else bool_result=false; break;}
			case">=":{if(op1>=op2)bool_result=true;else bool_result=false; break;}
			case"<=":{if(op1<=op2)bool_result=true;else bool_result=false; break;}
			case"==":{if(op1==op2)bool_result=true;else bool_result=false; break;}
			case"!=":{if(op1!=op2)bool_result=true;else bool_result=false; break;}
			default: return null;
		}
		myVar tmp = new myVar("",myVar._bool,new Boolean(bool_result));
		return tmp;
	}
	
	public myVar LogicBoper(myVar a, myVar b, String segno){
			
	        setArgument(a, b, segno);
	Boolean op1 = (Boolean)(a.getValue());
	Boolean op2 = (Boolean)(b.getValue());
	int type = a.getType();
	switch(segno){
		case"==":{if(op1==op2)bool_result=true;else bool_result=false; break;}
		case"!=":{if(op1!=op2)bool_result=true;else bool_result=false; break;}
		default: return null;
	}
	myVar tmp = new myVar("",myVar._bool,new Boolean(bool_result));
	return tmp;
	}
	
	public myVar LogicSoper(myVar a, myVar b, String segno){
		result = null;
		int type = a.getType();
		switch(segno){
			case"==":{if(((String)a.getValue()).equals((String)b.getValue()))bool_result=true;else bool_result=false; break;}
			case"!=":{if(!((String)a.getValue()).equals((String)b.getValue()))bool_result=true;else bool_result=false; break;}
			default: return null;
		}
		myVar tmp = new myVar("",myVar._bool,new Boolean(bool_result));
		return tmp;
	}
	
	 /*public boolean makeLOMag(myVar x, myVar y)
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
      
       //Logic Operation >=
      
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
     
      // Logic Operation <
     
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
     
    //Logic Operation <=
      
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
      
      // Logic Operation ==
      
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
     
      //Logic Operation !=
      
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
     }*/

    @Override
    public boolean exec() throws CommandException {
       if(op2==null){
    	   Utility.mf("EXEC: valori "+op1.toString());
    	   result=BNoper();
       }
       else{
    	   Utility.mf("EXEC2op: valori "+op1.toString()+" "+op2.toString());
    	   try {
			makeOper();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
       return true;
    	   
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
