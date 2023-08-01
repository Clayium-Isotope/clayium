package mods.clayium.block.common;

import mods.clayium.item.common.ClayiumMaterial;
import mods.clayium.util.TierPrefix;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class MaterialBlock extends BlockTiered {
    private final ClayiumMaterial material;

    public MaterialBlock(ClayiumMaterial material, int meta, TierPrefix tier) {
        super(Material.IRON, material.getName() + "_block", meta, tier);
        setHardness(2.0F);
        setResistance(2.0F);
        setSoundType(SoundType.METAL);

        this.material = material;

//        if (material == ClayiumMaterial.octupleEnergeticClay)
//            ClayiumCore.jsonHelper.generateSimpleBlockJson(getUnlocalizedName().substring(5), "oec");
//        else if (material == ClayiumMaterial.octuplePureAntimatter)
//            ClayiumCore.jsonHelper.generateSimpleBlockJson(getUnlocalizedName().substring(5), "opa");
//        else
//            ClayiumCore.jsonHelper.generateSimpleBlockJson(getUnlocalizedName().substring(5), material.getName());
    }

    public ClayiumMaterial getMaterial() {
        return material;
    }
}
