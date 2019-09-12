package com.raoulvdberge.refinedstorage.item.itemblock;

import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.block.BlockController;
import com.raoulvdberge.refinedstorage.item.info.ItemInfo;

public class ItemBlockController extends ItemBlockBase {
    public ItemBlockController(BlockController block) {
        super(block, new ItemInfo(RS.ID, "controller"));

        // setMaxStackSize(1);
    }
/* TODO
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1D - ((double) getEnergyStored(stack) / (double) RS.INSTANCE.config.controllerCapacity);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.hsvToRGB(Math.max(0.0F, (float) getEnergyStored(stack) / (float) RS.INSTANCE.config.controllerCapacity) / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getMetadata() != ControllerType.CREATIVE.getId();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        if (stack.getMetadata() != ControllerType.CREATIVE.getId()) {
            tooltip.add(I18n.format("misc.refinedstorage:energy_stored", getEnergyStored(stack), RS.INSTANCE.config.controllerCapacity));
        }
    }

    public static int getEnergyStored(ItemStack stack) {
        return (stack.hasTagCompound() && stack.getTagCompound().hasKey(TileController.NBT_ENERGY)) ? stack.getTagCompound().getInteger(TileController.NBT_ENERGY) : 0;
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity player) {
        super.onCreated(stack, world, player);

        createStack(stack, 0);
    }

    public static ItemStack createStack(ItemStack stack, int energy) {
        CompoundNBT tag = stack.getTagCompound();

        if (tag == null) {
            tag = new CompoundNBT();
        }

        tag.putInt(TileController.NBT_ENERGY, stack.getMetadata() == ControllerType.CREATIVE.getId() ? RS.INSTANCE.config.controllerCapacity : energy);

        stack.setTagCompound(tag);

        return stack;
    }*/
}
