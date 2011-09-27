package it.polito.lt.skype.manager;

public enum ManagerErrorType {
	TYPE_MISMATCH(0),
	UNDECLARED(1), 
	OUT_OF_BOUND(2);
	
	private int code;
	private ManagerErrorType(int a)
	{
		code=a;
	}
}