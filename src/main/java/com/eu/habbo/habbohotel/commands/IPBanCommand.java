package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.modtool.ModToolBan;
import com.eu.habbo.habbohotel.modtool.ModToolBanType;
import com.eu.habbo.habbohotel.rooms.RoomChatMessage;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserWhisperComposer;

public class IPBanCommand extends Command
{
    public final static int TEN_YEARS = 315569260;
    public IPBanCommand()
    {
        super("cmd_ip_ban", Emulator.getTexts().getValue("commands.keys.cmd_ip_ban").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception
    {
        Habbo habbo = null;
        String reason = "";
        if (params.length >= 2)
        {
            habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(params[1]);
        }

        if (params.length > 2)
        {
            for (int i = 2; i < params.length; i++)
            {
                reason += params[i];
                reason += " ";
            }
        }

        int count = 0;
        if (habbo != null)
        {
            if (habbo == gameClient.getHabbo())
            {
                gameClient.sendResponse(new RoomUserWhisperComposer(new RoomChatMessage(Emulator.getTexts().getValue("commands.error.cmd_ip_ban.ban_self"), gameClient.getHabbo(), gameClient.getHabbo(), RoomChatMessageBubbles.ALERT)));
                return true;
            }

            if (habbo.getHabboInfo().getRank() >= gameClient.getHabbo().getHabboInfo().getRank())
            {
                gameClient.sendResponse(new RoomUserWhisperComposer(new RoomChatMessage(Emulator.getTexts().getValue("commands.error.cmd_ban.target_rank_higher"), gameClient.getHabbo(), gameClient.getHabbo(), RoomChatMessageBubbles.ALERT)));
                return true;
            }

            Emulator.getGameEnvironment().getModToolManager().ban(habbo.getHabboInfo().getId(), gameClient.getHabbo(), reason, TEN_YEARS, ModToolBanType.IP, -1);
            count++;
            for (Habbo h : Emulator.getGameServer().getGameClientManager().getHabbosWithIP(habbo.getHabboInfo().getIpLogin()))
            {
                if (h != null)
                {
                    count++;
                    Emulator.getGameEnvironment().getModToolManager().ban(h.getHabboInfo().getId(), gameClient.getHabbo(), reason, TEN_YEARS, ModToolBanType.IP, -1);
                }
            }
        }
        else
        {
            gameClient.sendResponse(new RoomUserWhisperComposer(new RoomChatMessage(Emulator.getTexts().getValue("commands.error.cmd_ban.user_offline"), gameClient.getHabbo(), gameClient.getHabbo(), RoomChatMessageBubbles.ALERT)));
            return true;
        }

        gameClient.sendResponse(new RoomUserWhisperComposer(new RoomChatMessage(Emulator.getTexts().getValue("commands.succes.cmd_ip_ban").replace("%count%", count + ""), gameClient.getHabbo(), gameClient.getHabbo(), RoomChatMessageBubbles.ALERT)));

        return true;
    }
}
