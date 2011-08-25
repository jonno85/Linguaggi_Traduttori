package it.polito.lt.skype.command;


public class CommandParameter {
	private ParamType tipo=null;
	private String value=null;
	private SignType segno=null;
	private Boolean extValue=null; 
	//sta ad indicare che il parametro fa farte di una ricerca range con valori esterni Xi < X0 & Xi > X1

	public CommandParameter(ParamType type, String value, SignType segno){
		this.tipo = type;
                this.value = value;
		this.segno = segno;
	}

	public Boolean getExtValueFlag(){
		return extValue;
	}
	
	public SignType getSign(){
		return segno;
	}
	
	public String getValue() {
            return value;
	}
	
	public ParamType getParamType() {
		return tipo;
	}

}
