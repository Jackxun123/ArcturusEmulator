package com.eu.habbo.messages.outgoing.unknown;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;

public class SnowWarsGenericErrorComposer extends MessageComposer
{
    @Override
    public ServerMessage compose()
    {
        this.response.init(3702);
        this.response.appendInt32(1);
        return this.response;
    }
}
