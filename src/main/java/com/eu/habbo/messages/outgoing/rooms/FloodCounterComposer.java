package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class FloodCounterComposer extends MessageComposer
{
    private final int time;

    public FloodCounterComposer(int time)
    {
        this.time = time;
    }

    @Override
    public ServerMessage compose()
    {
        this.response.init(Outgoing.FloodCounterComposer);
        this.response.appendInt32(time);
        return this.response;
    }
}
