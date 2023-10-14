package mods.clayium.machine.StorageContainer;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

/*package-private*/ enum StorageContainerSize implements IStringSerializable {
    NORMAL("normal", 65536),
    CLAY_CORE("clay_core", Integer.MAX_VALUE);

    public static final PropertyEnum<StorageContainerSize> STORAGE_SIZE = PropertyEnum.create("storage_size", StorageContainerSize.class);
    private final String name;
    public final int maxSize;

    StorageContainerSize(String name, int size) {
        this.name = name;
        this.maxSize = size;
    }

    public static StorageContainerSize getByID(int id) {
        if (id == 1) return CLAY_CORE;
        return NORMAL;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
