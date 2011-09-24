package it.polito.lt.skype.parser;


import it.polito.lt.skype.command.CommandEnv;
import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.generated.parser.*;

import java.io.*;
	
	   
	public class ParserMain {
	  static public void main(String argv[]) {
	    try {
	      /* Istanzio lo scanner 
	       * NB: dato che JFlex genera la classe e i costruttori omettendo "public" 
	       * e dato che l'ant-clean deve pulire anche i generati, 
	       * ricordarsi ad ogni generazione dello scanner di aggiungere public 
	       * al nome della classe Lexer ai costruttori!
	       * NB2: risolto nel nome della classe NON RISOLTO nei costruttori!
	       * */
	    	Utility.mf("scrivi un comando: ");
	    	
	    	InputStreamReader reader = new InputStreamReader (System.in);
            BufferedReader myInput = new BufferedReader (reader);
           
	    	//reader.
            //myInput.readLine();
            //reader.read(myInput.readLine().toCharArray());
            Utility.mf("LETTO: "+ reader.toString());
           
           // Yylex l = new Yylex(System.in);    	  
			Lexer l = new Lexer(System.in);
			/* Istanzio il parser */
			parser p = new parser(l);
			p.setEnviroment(new CommandEnv("/home/robizz/lt2"));
			/* Avvio il parser */
			Object result = p.parse();
			//p.debug_parse();
			Utility.mf("finito");
			
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	}



