package it.polito.lt.skype.command;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandEnv {
	private String currentPathString=null;
	private Path currentPath=null;
	
	public CommandEnv(String path){
		currentPathString=path;
		currentPath=Paths.get(path);
	}

	public Path getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(Path currentPath) {
		this.currentPath = currentPath;
		currentPathString=currentPath.toString();
	}

	public String getCurrentPathString() {
		return currentPathString;
	}

	public void setCurrentPathString(String currentPathString) {
		this.currentPathString = currentPathString;
		currentPath=Paths.get(currentPathString);
	}
	
}
