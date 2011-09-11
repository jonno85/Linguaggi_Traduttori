package it.polito.lt.skype.bot;

import java.util.concurrent.CopyOnWriteArrayList;

import it.polito.lt.skype.command.Utility;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ChatMessageAdapter {
	private CopyOnWriteArrayList<CommandMessage> CMCList;

	public ChatMessageAdapter()
	{
		CMCList = new CopyOnWriteArrayList<CommandMessage>();
	}
	
    public void chatMessageReceived(ChatMessage received) throws SkypeException {
    	Utility.mf(received.getType()+"\n");
    	
    	if (received.getType().equals(ChatMessage.Type.SAID)) {
    		CMCList.add(new CommandMessage(received));
    		
    		String mes=received.getContent();
        	Utility.mf("messaggio ricevuto: "+mes);
        	received.getSender().send(">>>hai detto: "+mes);
        	
        	
        	//parsing comando
        	
        	//stampa risultato sull chat del client con                	
        	received.getSender().send("risultati comando:\n");
	     
			
			
			
        	
        }
    }
};//fine chatmessage adapter

