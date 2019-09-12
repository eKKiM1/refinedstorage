package com.raoulvdberge.refinedstorage.inventory.item;

import com.raoulvdberge.refinedstorage.inventory.item.validator.ItemValidatorBasic;
import com.raoulvdberge.refinedstorage.item.ItemUpgrade;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ItemHandlerUpgrade extends ItemHandlerBase {
    public ItemHandlerUpgrade(int size, @Nullable Consumer<Integer> listener, ItemUpgrade... supportedUpgrades) {
        super(size, listener, new ItemValidatorBasic[supportedUpgrades.length]);

        for (int i = 0; i < supportedUpgrades.length; ++i) {
            this.validators[i] = new ItemValidatorBasic(supportedUpgrades[i]);
        }
    }

    public int getSpeed() {
        return getSpeed(9, 2);
    }

    public int getSpeed(int speed, int speedIncrease) {
        for (int i = 0; i < getSlots(); ++i) {
            /*TODO if (!getStackInSlot(i).isEmpty() && getStackInSlot(i).getItemDamage() == ItemUpgrade.TYPE_SPEED) {*/
            if (false) {
                speed -= speedIncrease;
            }
        }

        return speed;
    }

    public boolean hasUpgrade(int type) {
        return getUpgradeCount(type) > 0;
    }

    public int getUpgradeCount(int type) {
        int upgrades = 0;

        for (int i = 0; i < getSlots(); ++i) {
            /*TODO if (!getStackInSlot(i).isEmpty() && getStackInSlot(i).getItemDamage() == type) {*/
            if (false) {
                upgrades++;
            }
        }

        return upgrades;
    }

    public int getEnergyUsage() {
        int usage = 0;

        for (int i = 0; i < getSlots(); ++i) {
            // TODO usage += ItemUpgrade.getEnergyUsage(getStackInSlot(i));
        }

        return usage;
    }

    public int getFortuneLevel() {
        int maxFortune = 0;

        for (int i = 0; i < getSlots(); ++i) {
            if (!getStackInSlot(i).isEmpty()) {
                // TODO int fortune = ItemUpgrade.getFortuneLevel(getStackInSlot(i));
                int fortune = 0;

                if (fortune > maxFortune) {
                    maxFortune = fortune;
                }
            }
        }

        return maxFortune;
    }

    public int getItemInteractCount() {
        return hasUpgrade(ItemUpgrade.TYPE_STACK) ? 64 : 1;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
