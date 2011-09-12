package it.polito.lt.skype.bot;

import com.skype.ChatMessage;

public class CommandMessage {
	
	 private ChatMessage chatMessage;

	public CommandMessage(ChatMessage cm)
	{
		setChatMessage(cm);
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}
	
	
	
}
