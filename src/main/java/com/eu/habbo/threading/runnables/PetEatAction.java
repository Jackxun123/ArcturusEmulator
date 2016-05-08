package com.eu.habbo.threading.runnables;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.achievements.AchievementManager;
import com.eu.habbo.habbohotel.items.interactions.InteractionPetFood;
import com.eu.habbo.habbohotel.pets.Pet;
import com.eu.habbo.habbohotel.pets.PetTask;
import com.eu.habbo.messages.outgoing.rooms.items.FloorItemUpdateComposer;
import com.eu.habbo.messages.outgoing.rooms.items.RemoveFloorItemComposer;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserStatusComposer;

public class PetEatAction implements Runnable
{
    private final Pet pet;
    private final InteractionPetFood food;

    public PetEatAction(Pet pet, InteractionPetFood food)
    {
        this.pet = pet;
        this.food = food;
    }

    @Override
    public void run()
    {
        if(this.pet.getRoomUnit() != null && this.pet.getRoom() != null)
        {
            if (this.pet.levelHunger >= 20 && this.food != null && Integer.valueOf(this.food.getExtradata()) < this.food.getBaseItem().getStateCount())
            {
                this.pet.addHunger(-20);
                this.pet.setTask(PetTask.EAT);
                this.pet.getRoomUnit().setCanWalk(false);

                this.food.setExtradata(Integer.valueOf(this.food.getExtradata()) + 1 + "");
                this.pet.getRoom().updateItem(this.food);

                AchievementManager.progressAchievement(Emulator.getGameEnvironment().getHabboManager().getHabbo(this.pet.getUserId()), Emulator.getGameEnvironment().getAchievementManager().achievements.get("PetFeeding"), 20);

                Emulator.getThreading().run(this, 1000);
            } else
            {
                if (this.food != null && Integer.valueOf(this.food.getExtradata()) == this.food.getBaseItem().getStateCount())
                {
                    Emulator.getThreading().run(new QueryDeleteHabboItem(this.food), 500);
                    if (this.pet.getRoom() != null)
                    {
                        this.pet.getRoom().removeHabboItem(this.food);
                        this.pet.getRoom().sendComposer(new RemoveFloorItemComposer(this.food, true).compose());
                    }
                }

                this.pet.setTask(null);
                this.pet.getRoomUnit().getStatus().remove("eat");
                this.pet.getRoomUnit().setCanWalk(true);
                this.pet.getRoom().sendComposer(new RoomUserStatusComposer(this.pet.getRoomUnit()).compose());
            }
        }
    }
}
