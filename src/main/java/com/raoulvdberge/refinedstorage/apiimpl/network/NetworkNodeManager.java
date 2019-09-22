package com.raoulvdberge.refinedstorage.apiimpl.network;

import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeFactory;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeManager;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkNodeManager extends WorldSavedData implements INetworkNodeManager {
    public static final String NAME = "refinedstorage_nodes";

    private static final String NBT_NODES = "Nodes";
    private static final String NBT_NODE_ID = "Id";
    private static final String NBT_NODE_DATA = "Data";
    private static final String NBT_NODE_POS = "Pos";

    private final World world;

    private ConcurrentHashMap<BlockPos, INetworkNode> nodes = new ConcurrentHashMap<>();

    public NetworkNodeManager(String name, World world) {
        super(name);

        this.world = world;
    }

    @Override
    public void read(CompoundNBT tag) {
        if (tag.contains(NBT_NODES)) {
            ListNBT nodesTag = tag.getList(NBT_NODES, Constants.NBT.TAG_COMPOUND);

            this.nodes.clear();

            for (int i = 0; i < nodesTag.size(); ++i) {
                CompoundNBT nodeTag = nodesTag.getCompound(i);

                String id = nodeTag.getString(NBT_NODE_ID);
                CompoundNBT data = nodeTag.getCompound(NBT_NODE_DATA);
                BlockPos pos = BlockPos.fromLong(nodeTag.getLong(NBT_NODE_POS));

                INetworkNodeFactory factory = API.instance().getNetworkNodeRegistry().get(id);

                if (factory != null) {
                    INetworkNode node = null;

                    try {
                        node = factory.create(data, world, pos);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }

                    if (node != null) {
                        this.nodes.put(pos, node);
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        ListNBT list = new ListNBT();

        for (INetworkNode node : all()) {
            try {
                CompoundNBT nodeTag = new CompoundNBT();

                nodeTag.putString(NBT_NODE_ID, node.getId());
                nodeTag.putLong(NBT_NODE_POS, node.getPos().toLong());
                nodeTag.put(NBT_NODE_DATA, node.write(new CompoundNBT()));

                list.add(nodeTag);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        tag.put(NBT_NODES, list);

        return tag;
    }

    @Nullable
    @Override
    public INetworkNode getNode(BlockPos pos) {
        return nodes.get(pos);
    }

    @Override
    public void removeNode(BlockPos pos) {
        if (pos == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        nodes.remove(pos);
    }

    @Override
    public void setNode(BlockPos pos, INetworkNode node) {
        if (pos == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        if (node == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }

        nodes.put(pos, node);
    }

    @Override
    public Collection<INetworkNode> all() {
        return nodes.values();
    }

    @Override
    public void markForSaving() {
        markDirty();
    }
}
