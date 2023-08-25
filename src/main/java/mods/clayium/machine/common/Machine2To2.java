package mods.clayium.machine.common;

public interface Machine2To2 extends MachineSomeToSome {
    int MATERIAL_1 = 0;
    int MATERIAL_2 = 1;
    int PRODUCT_1 = 2;
    int PRODUCT_2 = 3;

    @Override
    default int getResultSlotCount() {
        return 2;
    }
}

