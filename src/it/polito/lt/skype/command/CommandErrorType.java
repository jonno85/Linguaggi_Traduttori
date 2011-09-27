package it.polito.lt.skype.command;

public enum CommandErrorType {
	COPY_ERROR(0),
	FIND_ERROR(1),
	LIST_ERROR(2),
	MOVE_ERROR(3),
	REMOVE_ERROR(4),
	MAKE_DIR_ERROR(5),
    STATEMENT_ERROR(6),
    ASSIG_ERROR(7), 
    PRINT_ERROR(8);
	
	private int code;
	private CommandErrorType(int a)
	{
		code=a;
	}
}