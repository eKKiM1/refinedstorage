package com.raoulvdberge.refinedstorage.item;

import com.raoulvdberge.refinedstorage.RS;
import com.raoulvdberge.refinedstorage.apiimpl.autocrafting.CraftingPattern;
import com.raoulvdberge.refinedstorage.item.info.ItemInfo;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemPattern extends ItemBase/* implements ICraftingPatternProvider */ {
    private static Map<ItemStack, CraftingPattern> PATTERN_CACHE = new HashMap<>();

    private static final String NBT_VERSION = "Version";
    public static final String NBT_INPUT_SLOT = "Input_%d";
    public static final String NBT_OUTPUT_SLOT = "Output_%d";
    private static final String NBT_FLUID_INPUT_SLOT = "FluidInput_%d";
    private static final String NBT_FLUID_OUTPUT_SLOT = "FluidOutput_%d";
    private static final String NBT_OREDICT = "Oredict";
    public static final String NBT_PROCESSING = "Processing";

    public ItemPattern() {
        super(new ItemInfo(RS.ID, "pattern"));
    }
/*
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(IModelRegistration modelRegistration) {
        modelRegistration.setModel(this, 0, new ModelResourceLocation(info.getId(), "inventory"));

        modelRegistration.addBakedModelOverride(info.getId(), BakedModelPattern::new);

        modelRegistration.addItemColor(this, new ItemColorPattern());
    }

    public static CraftingPattern getPatternFromCache(World world, ItemStack stack) {
        if (!PATTERN_CACHE.containsKey(stack)) {
            PATTERN_CACHE.put(stack, new CraftingPattern(world, null, stack));
        }

        return PATTERN_CACHE.get(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);

        if (!stack.hasTagCompound()) {
            return;
        }

        ICraftingPattern pattern = getPatternFromCache(world, stack);

        if (pattern.isValid()) {
            if (GuiScreen.isShiftKeyDown() || isProcessing(stack)) {
                tooltip.add(TextFormatting.YELLOW + I18n.format("misc.refinedstorage:pattern.inputs") + TextFormatting.RESET);

                RenderUtils.addCombinedItemsToTooltip(tooltip, true, pattern.getInputs().stream().map(i -> i.size() > 0 ? i.get(0) : ItemStack.EMPTY).collect(Collectors.toList()));
                RenderUtils.addCombinedFluidsToTooltip(tooltip, true, pattern.getFluidInputs());

                tooltip.add(TextFormatting.YELLOW + I18n.format("misc.refinedstorage:pattern.outputs") + TextFormatting.RESET);
            }

            RenderUtils.addCombinedItemsToTooltip(tooltip, true, pattern.getOutputs());
            RenderUtils.addCombinedFluidsToTooltip(tooltip, true, pattern.getFluidOutputs());

            if (isOredict(stack)) {
                tooltip.add(TextFormatting.BLUE + I18n.format("misc.refinedstorage:pattern.oredict") + TextFormatting.RESET);
            }

            if (isProcessing(stack)) {
                tooltip.add(TextFormatting.BLUE + I18n.format("misc.refinedstorage:processing") + TextFormatting.RESET);
            }
        } else {
            tooltip.add(TextFormatting.RED + I18n.format("misc.refinedstorage:pattern.invalid") + TextFormatting.RESET);
        }
    }

    public static void setInputSlot(ItemStack pattern, int slot, ItemStack stack) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new CompoundNBT());
        }

        pattern.getTagCompound().put(String.format(NBT_INPUT_SLOT, slot), stack.serializeNBT());
    }

    @Nullable
    public static ItemStack getInputSlot(ItemStack pattern, int slot) {
        String id = String.format(NBT_INPUT_SLOT, slot);

        if (!pattern.hasTagCompound() || !pattern.getTagCompound().hasKey(id)) {
            return null;
        }

        ItemStack stack = new ItemStack(pattern.getTagCompound().getCompound(id));
        if (stack.isEmpty()) {
            return null;
        }

        return stack;
    }

    public static void setOutputSlot(ItemStack pattern, int slot, ItemStack stack) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new CompoundNBT());
        }

        pattern.getTagCompound().put(String.format(NBT_OUTPUT_SLOT, slot), stack.serializeNBT());
    }

    @Nullable
    public static ItemStack getOutputSlot(ItemStack pattern, int slot) {
        String id = String.format(NBT_OUTPUT_SLOT, slot);

        if (!pattern.hasTagCompound() || !pattern.getTagCompound().hasKey(id)) {
            return null;
        }

        ItemStack stack = new ItemStack(pattern.getTagCompound().getCompound(id));
        if (stack.isEmpty()) {
            return null;
        }

        return stack;
    }

    public static void setFluidInputSlot(ItemStack pattern, int slot, FluidStack stack) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new CompoundNBT());
        }

        pattern.getTagCompound().put(String.format(NBT_FLUID_INPUT_SLOT, slot), stack.writeToNBT(new CompoundNBT()));
    }

    @Nullable
    public static FluidStack getFluidInputSlot(ItemStack pattern, int slot) {
        String id = String.format(NBT_FLUID_INPUT_SLOT, slot);

        if (!pattern.hasTagCompound() || !pattern.getTagCompound().hasKey(id)) {
            return null;
        }

        return FluidStack.loadFluidStackFromNBT(pattern.getTagCompound().getCompound(id));
    }

    public static void setFluidOutputSlot(ItemStack pattern, int slot, FluidStack stack) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new CompoundNBT());
        }

        pattern.getTagCompound().put(String.format(NBT_FLUID_OUTPUT_SLOT, slot), stack.writeToNBT(new CompoundNBT()));
    }

    @Nullable
    public static FluidStack getFluidOutputSlot(ItemStack pattern, int slot) {
        String id = String.format(NBT_FLUID_OUTPUT_SLOT, slot);

        if (!pattern.hasTagCompound() || !pattern.getTagCompound().hasKey(id)) {
            return null;
        }

        return FluidStack.loadFluidStackFromNBT(pattern.getTagCompound().getCompound(id));
    }

    public static boolean isProcessing(ItemStack pattern) {
        return pattern.hasTagCompound() && pattern.getTagCompound().hasKey(NBT_PROCESSING) && pattern.getTagCompound().getBoolean(NBT_PROCESSING);
    }

    public static void setProcessing(ItemStack pattern, boolean processing) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new CompoundNBT());
        }

        pattern.getTagCompound().putBoolean(NBT_PROCESSING, processing);
    }

    public static boolean isOredict(ItemStack pattern) {
        return pattern.hasTagCompound() && pattern.getTagCompound().hasKey(NBT_OREDICT) && pattern.getTagCompound().getBoolean(NBT_OREDICT);
    }

    public static void setOredict(ItemStack pattern, boolean oredict) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new CompoundNBT());
        }

        pattern.getTagCompound().putBoolean(NBT_OREDICT, oredict);
    }

    public static void setVersion(ItemStack pattern) {
        if (!pattern.hasTagCompound()) {
            pattern.setTagCompound(new CompoundNBT());
        }

        pattern.getTagCompound().setString(NBT_VERSION, RS.VERSION);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, EnumHand hand) {
        if (!world.isRemote && player.isSneaking()) {
            return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(RSItems.PATTERN, player.getHeldItem(hand).getCount()));
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    @Nonnull
    public ICraftingPattern create(World world, ItemStack stack, ICraftingPatternContainer container) {
        return new CraftingPattern(world, container, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        super.onUpdate(stack, world, entity, slot, isSelected);

        if (!world.isRemote) {
            API.instance().getOneSixMigrationHelper().migratePattern(stack);
        }
    }*/
}
