package com.raoulvdberge.refinedstorage.tile;

import com.raoulvdberge.refinedstorage.RSTiles;
import com.raoulvdberge.refinedstorage.api.network.grid.IGrid;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNodeCrafterManager;
import com.raoulvdberge.refinedstorage.screen.BaseScreen;
import com.raoulvdberge.refinedstorage.screen.GuiCrafterManager;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCrafterManager extends NetworkNodeTile<NetworkNodeCrafterManager> {
    public static final TileDataParameter<Integer, TileCrafterManager> SIZE = new TileDataParameter<>(DataSerializers.VARINT, IGrid.SIZE_STRETCH, t -> t.getNode().getSize(), (t, v) -> {
        if (IGrid.isValidSize(v)) {
            t.getNode().setSize(v);
            t.getNode().markDirty();
        }
    }, (initial, p) -> BaseScreen.executeLater(GuiCrafterManager.class, BaseScreen::init));
    public static final TileDataParameter<Integer, TileCrafterManager> SEARCH_BOX_MODE = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getSearchBoxMode(), (t, v) -> {
        if (IGrid.isValidSearchBoxMode(v)) {
            t.getNode().setSearchBoxMode(v);
            t.getNode().markDirty();
        }
    }, (initial, p) -> BaseScreen.executeLater(GuiCrafterManager.class, crafterManager -> crafterManager.getSearchField().setMode(p)));

    public TileCrafterManager() {
        super(RSTiles.CRAFTER_MANAGER);

        dataManager.addWatchedParameter(SIZE);
        dataManager.addWatchedParameter(SEARCH_BOX_MODE);
    }

    @Override
    public NetworkNodeCrafterManager createNode(World world, BlockPos pos) {
        return new NetworkNodeCrafterManager(world, pos);
    }
}
