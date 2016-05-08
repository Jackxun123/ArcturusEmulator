package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.RoomChatMessage;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.outgoing.inventory.InventoryRefreshComposer;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserWhisperComposer;
import com.eu.habbo.threading.runnables.QueryDeleteHabboItems;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntIntProcedure;

import java.util.ArrayList;

public class RedeemCommand extends Command
{
    public RedeemCommand()
    {
        super.permission = "cmd_redeem";
        super.keys = Emulator.getTexts().getValue("commands.keys.cmd_redeem").split(";");
    }

    @Override
    public boolean handle(final GameClient gameClient, String[] params) throws Exception
    {
        ArrayList<HabboItem> items = new ArrayList<HabboItem>();

        int credits = 0;
        int pixels = 0;

        TIntIntMap points = new TIntIntHashMap();


        for(HabboItem item : gameClient.getHabbo().getHabboInventory().getItemsComponent().getItemsAsValueCollection())
        {
            if (item.getBaseItem().getName().startsWith("CF_") || item.getBaseItem().getName().startsWith("CFC_") || item.getBaseItem().getName().startsWith("DF_") || item.getBaseItem().getName().startsWith("PF_"))
            {
                items.add(item);
                if (item.getBaseItem().getName().startsWith("CF_") || item.getBaseItem().getName().startsWith("CFC_"))
                {
                    try
                    {
                        credits += Integer.valueOf(item.getBaseItem().getName().split("_")[1]);
                    } catch (Exception e)
                    {
                    }

                } else if (item.getBaseItem().getName().startsWith("PF_"))
                {
                    try
                    {
                        pixels += Integer.valueOf(item.getBaseItem().getName().split("_")[1]);
                    } catch (Exception e)
                    {
                    }
                } else if (item.getBaseItem().getName().startsWith("PF_"))
                {
                    int pointsType = 0;
                    int pointsAmount = 0;

                    pointsType = Integer.valueOf(item.getBaseItem().getName().split("_")[1]);
                    pointsAmount = Integer.valueOf(item.getBaseItem().getName().split("_")[2]);

                    points.adjustOrPutValue(pointsType, pointsAmount, pointsAmount);
                }
            }
        }

        TIntObjectHashMap<HabboItem> deleted = new TIntObjectHashMap<HabboItem>();
        for(HabboItem item : items)
        {
            gameClient.getHabbo().getHabboInventory().getItemsComponent().removeHabboItem(item);
            deleted.put(item.getId(), item);
        }

        Emulator.getThreading().run(new QueryDeleteHabboItems(deleted));

        gameClient.sendResponse(new InventoryRefreshComposer());
        gameClient.getHabbo().giveCredits(credits);
        gameClient.getHabbo().givePixels(pixels);

        final String[] message = {Emulator.getTexts().getValue("generic.redeemed")};

        message[0] += Emulator.getTexts().getValue("generic.credits");
        message[0] += ": " + credits;

        if(pixels > 0)
        {
            message[0] += ", " + Emulator.getTexts().getValue("generic.pixels");
            message[0] += ": " + pixels + "";
        }

        if(!points.isEmpty())
        {
            points.forEachEntry(new TIntIntProcedure()
            {
                @Override
                public boolean execute(int a, int b)
                {
                    gameClient.getHabbo().givePoints(a, b);
                    message[0] += " ," + Emulator.getTexts().getValue("seasonal.name." + a) + ": " + b;
                    return true;
                }
            });
        }

        gameClient.sendResponse(new RoomUserWhisperComposer(new RoomChatMessage(message[0], gameClient.getHabbo(), gameClient.getHabbo(), RoomChatMessageBubbles.ALERT)));

        return true;
    }
}
