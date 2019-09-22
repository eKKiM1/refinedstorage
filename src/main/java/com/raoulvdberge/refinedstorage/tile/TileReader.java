package com.raoulvdberge.refinedstorage.tile;

import com.raoulvdberge.refinedstorage.RSTiles;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.IGuiReaderWriter;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNodeReader;
import com.raoulvdberge.refinedstorage.screen.BaseScreen;
import com.raoulvdberge.refinedstorage.screen.GuiReaderWriter;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileReader extends NetworkNodeTile<NetworkNodeReader> {
    static <T extends NetworkNodeTile> TileDataParameter<String, T> createChannelParameter() {
        return new TileDataParameter<>(DataSerializers.STRING, "", t -> ((IGuiReaderWriter) t.getNode()).getChannel(), (t, v) -> {
            ((IGuiReaderWriter) t.getNode()).setChannel(v);

            t.getNode().markDirty();
        }, (initial, p) -> BaseScreen.executeLater(GuiReaderWriter.class, readerWriter -> readerWriter.setCurrentChannel(p)));
    }

    public static final TileDataParameter<String, TileReader> CHANNEL = createChannelParameter();

    public TileReader() {
        super(RSTiles.READER);

        dataManager.addWatchedParameter(CHANNEL);
    }

    /*
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
        if (super.hasCapability(capability, facing)) {
            return true;
        }

        IReader reader = getNode();

        if (facing != getDirection()) {
            return false;
        }

        for (IReaderWriterHandlerFactory handlerFactory : API.instance().getReaderWriterHandlerRegistry().all()) {
            if (handlerFactory.create(null).hasCapabilityReader(reader, capability)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        T foundCapability = super.getCapability(capability, facing);

        if (foundCapability == null) {
            IReader reader = getNode();

            if (facing != getDirection()) {
                return null;
            }

            Object dummyCap = null;
            for (IReaderWriterHandlerFactory handlerFactory : API.instance().getReaderWriterHandlerRegistry().all()) {
                if (handlerFactory.create(null).hasCapabilityReader(reader, capability)) {
                    dummyCap = handlerFactory.create(null).getNullCapability();
                }
            }

            if (!reader.canUpdate()) {
                return (T) dummyCap;
            }

            IReaderWriterChannel channel = reader.getNetwork().getReaderWriterManager().getChannel(reader.getChannel());

            if (channel == null) {
                return (T) dummyCap;
            }

            for (IReaderWriterHandler handler : channel.getHandlers()) {
                foundCapability = handler.getCapabilityReader(reader, capability);

                if (foundCapability != null) {
                    return foundCapability;
                }
            }
        }

        return foundCapability;
    }*/

    @Override
    @Nonnull
    public NetworkNodeReader createNode(World world, BlockPos pos) {
        return new NetworkNodeReader(world, pos);
    }
}
