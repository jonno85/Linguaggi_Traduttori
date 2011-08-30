package it.polito.lt.skype.parser;


public enum ParserErrorType {
    COMMMAND_NOT_FOUND(0),
    INVALID_PARAMETER(1),
    INVALID_RANGE(2),
    INVALID_NUMBER_PARAMETER(3);
    
    private int code;
    private ParserErrorType(int n)
    {
        code = n;
    }
}
