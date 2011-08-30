/* Esercitazione 2 - Esercizio 1 (Main) */

import it.polito.lt.skype.command.Utility;
import java.io.*;
import java_cup.parser;
import java_cup.runtime.*;


   
public class Main {
  static public void main(String argv[]) {    
    try {
      /* Istanzio lo scanner aprendo il file di ingresso argv[0] */
        
      JFlex.Main.generate(new File("parser/scanner.jflex"));       
      
      Utility.mf("inserisci comando:\n ");
      
      
      Lexer l = new Lexer(System.in);
      /* Istanzio il parser */
      parser p = new parser(l);
      /* Avvio il parser */
      Object result = p.parse();      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


