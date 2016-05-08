package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class RoomScoreComposer extends MessageComposer {

    private final int score;
    private final boolean canVote;

    public RoomScoreComposer(int score, boolean canVote)
    {
        this.score = score;
        this.canVote = canVote;
    }

    @Override
    public ServerMessage compose() {
        this.response.init(Outgoing.RoomScoreComposer);
        this.response.appendInt32(score);
        this.response.appendBoolean(this.canVote);
        return this.response;
    }
}
