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



import java.io.IOException;
import java.lang.Runtime;
import java.lang.Process;
import java.io.*;

import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Profile;
import com.skype.Skype;
import com.skype.SkypeException;


public class AutoAnswering {
    public static void main(String[] args) throws Exception {
    	final Profile profilo = Skype.getProfile();
    	Skype.setDeamon(false); // to prevent exiting from this program
        profilo.setMoodMessage("AUTOANSWER");
        System.out.println("bot settato e pronto");
       ChatMessageAdapter meslis= new ChatMessageAdapter() {
            public void chatMessageReceived(ChatMessage received) throws SkypeException {
            	System.out.println(received.getType()+"\n");
            	if (received.getType().equals(ChatMessage.Type.SAID)) {
                	String mes=received.getContent();
                	System.out.println("messaggio ricevuto: "+mes);
                	received.getSender().send("hai detto: "+mes);
                	//received.getSender().send("Risposta Automatica: Non ci sono :P");
                	//esecuzione comando nella shell
                	Process p=null;
                	String line=null;
                	try {
						p = Runtime.getRuntime().exec(mes);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					//ricezione output comando
					InputStream inputS = p.getInputStream();
			        InputStreamReader inputSRead = new InputStreamReader(inputS);
			        BufferedReader buffRead = new BufferedReader(inputSRead);

			        try {
						p.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        try {//stampa risultati comando
			        	received.getSender().send("risultati comando:\n");
						while ((line = buffRead.readLine()) != null) {
						    System.out.println("output console: "+line);
						    received.getSender().send(line);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
                	
                }
            }
        };
        Skype.addChatMessageListener(meslis);
        //Skype.removeChatMessageListener(meslis);
        //System.out.println("bot eliminato");
    }


}