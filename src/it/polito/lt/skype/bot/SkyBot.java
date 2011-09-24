/*******************************************************************************
 * Copyright (c) 2006-2007 Koji Hisano <hisano@gmail.com> - UBION Inc. Developer
 * Copyright (c) 2006-2007 UBION Inc. <http://www.ubion.co.jp/>
 * 
 * Copyright (c) 2006-2007 Skype Technologies S.A. <http://www.skype.com/>
 * 
 * Skype4Java is licensed under either the Apache License, Version 2.0 or
 * the Eclipse Public License v1.0.
 * You may use it freely in commercial and non-commercial products.
 * You may obtain a copy of the licenses at
 *
 *   the Apache License - http://www.apache.org/licenses/LICENSE-2.0
 *   the Eclipse Public License - http://www.eclipse.org/legal/epl-v10.html
 *
 * If it is possible to cooperate with the publicity of Skype4Java, please add
 * links to the Skype4Java web site <https://developer.skype.com/wiki/Java_API> 
 * in your web site or documents.
 * 
 * Contributors:
 * Koji Hisano - initial API and implementation
 ******************************************************************************/
//package com.skype.sample;
package it.polito.lt.skype.bot;



import it.polito.lt.skype.command.Utility;
import it.polito.lt.skype.generated.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.Runtime;
import java.lang.Process;
import java.io.*;


import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Profile;
import com.skype.Skype;
import com.skype.SkypeException;


public class SkyBot {
    public static void main(String[] args) throws Exception {
    	final Profile profilo = Skype.getProfile();
    	Skype.setDeamon(false); // to prevent exiting from this program
        profilo.setMoodMessage("SHELL MODE: ON");
        Utility.mf("bot settato e pronto");
        
        
        ChatMessageAdapter meslis= new ChatMessageAdapter()  {
    	  
        	//ChatMessageAdapter class begin
        	public void chatMessageReceived(ChatMessage received) throws SkypeException {
				       
				       	Utility.mf("\n"+received.getType());
				       	if (received.getType().equals(ChatMessage.Type.SAID)) {
				       		CommandMessage cm=new CommandMessage(received);
				       		
				       		String mes=received.getContent();
				      		  Utility.mf("messaggio ricevuto: "+mes);
				       		Utility.mf("messaggio ricevuto da"+cm.getChatMessage().getSenderId());
				       		
				       		/*String mes=received.getContent();
				       		 *Utility.mf("messaggio ricevuto: "+mes);
				       		 *received.getSender().send(">>>hai detto: "+mes);
				       		 *received.getSender().send("risultati comando:\n");
				       		 **/
//				       		InputStream is = parseStringToIS(mes);
//				       		
//				       		InputStreamReader reader = new InputStreamReader (is);
//				            BufferedReader myInput = new BufferedReader (reader);
				       		
				           

				       		Lexer l = new Lexer(new java.io.StringReader(mes+"\n")  );
							
							/* Istanzio il parser */
							parser p = new parser(l);
							p.setScanner(l);
							
							String output ="···";
							p.se
							/* Avvio il parser */
							try {
								Object result = p.debug_parse();
								
							} 
							catch (Exception e) 
							{
								
								// TODO Auto-generated catch block
								Utility.mf("FALLITO: "+e.getMessage());
								//System.out.println(myInput.toString());
								e.printStackTrace();
							}
							//p.debug_parse();
							Utility.mf("finito");
				      	     
				           	
				           	//parsing comando
				           	
				           	//stampa risultato sull chat del client con                	
				           	//received.getSender().send("risultati comando:\n");
				   	     
				           }
				       }
        	
			        	 //from string to input stream
			    public java.io.InputStream parseStringToIS(String str){
			    	  if(str==null) return null;
			    	  //str = str.trim();
			    	  java.io.InputStream in = null;
			    	  try{
			    		  in = new java.io.ByteArrayInputStream(str.getBytes("UTF-8"));
			    	  }catch(Exception ex){}
			    	  return in;      
			    	 }
        	
        	
        	
        	
				   };//fine chatmessage adapter;
				   
				   
        Skype.addChatMessageListener(meslis);
        //Skype.removeChatMessageListener(meslis);
        //System.out.println("bot eliminato");
    }
    
   

    
    
}

