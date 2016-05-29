package com.rs.game.player;

import com.rs.game.player.ChatMessage;

public class PublicChatMessage extends ChatMessage {

    private int effects;

    public PublicChatMessage(String message, int effects) {
	super(message);
	this.effects = effects;
    }

    public int getEffects() {
	return effects;
    }

}
