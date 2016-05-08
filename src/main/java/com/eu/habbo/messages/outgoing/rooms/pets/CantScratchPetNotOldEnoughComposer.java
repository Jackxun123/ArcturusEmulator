package com.eu.habbo.messages.outgoing.rooms.pets;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class CantScratchPetNotOldEnoughComposer extends MessageComposer
{
    private final int currentAge;
    private final int requiredAge;

    public CantScratchPetNotOldEnoughComposer(int currentAge, int requiredAge)
    {
        this.currentAge = currentAge;
        this.requiredAge = requiredAge;
    }

    @Override
    public ServerMessage compose()
    {
        this.response.init(Outgoing.CantScratchPetNotOldEnoughComposer);
        this.response.appendInt32(this.currentAge);
        this.response.appendInt32(this.requiredAge);
        return this.response;
    }
}
