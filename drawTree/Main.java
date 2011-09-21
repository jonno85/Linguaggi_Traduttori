/* Esercitazione 2 - Esercizio 1 (Main) */
import java.io.*;
   
public class Main {
  static public void main(String argv[]) {    
    try {
      /* Istanzio lo scanner aprendo il file di ingresso argv[0] */
      Lexer l = new Lexer(new FileReader(argv[0]));
      /* Istanzio il parser */
      parser p = new parser(l);
      /* Avvio il parser */
      Object result = p.parse();      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}


