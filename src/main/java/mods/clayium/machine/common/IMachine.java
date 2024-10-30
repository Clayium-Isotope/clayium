package mods.clayium.machine.common;

import mods.clayium.component.teField.FieldComponent;
import mods.clayium.machine.EnumMachineKind;
import mods.clayium.util.UsedFor;
import mods.clayium.util.UtilTransfer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

@UsedFor(UsedFor.Type.TileEntity)
public interface IMachine extends FieldComponent {

    EnumMachineKind getKind();

    /**
     * by IInventory
     */
    int getField(int id);

    static IItemHandler createItemHandler(TileEntity te, int ...at) {
        return UtilTransfer.getItemHandler(te, null, at);
    }
}
