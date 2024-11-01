package mods.clayium.item.common;

import mods.clayium.util.TierPrefix;
import mods.clayium.util.UtilLocale;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ClayiumShapedMaterial extends ItemTiered {

    private final ClayiumMaterial material;
    private final ClayiumShape shape;
    private final boolean useGeneralIcon;

    public ClayiumShapedMaterial(ClayiumMaterial material, ClayiumShape shape, TierPrefix tier) {
        this(material, shape, tier, false);
    }

    public ClayiumShapedMaterial(ClayiumMaterial material, ClayiumShape shape, TierPrefix tier, boolean useGeneralIcon) {
        super(material, shape, tier);
        this.material = material;
        this.shape = shape;
        this.useGeneralIcon = useGeneralIcon;
    }

    @Nullable
    public String getTempFile() {
        if (!useGeneralIcon) return null;

        switch (shape) {
            case plate:
                return "plate";
            case largePlate:
                return "large_plate";
            case dust:
                return "dust";
            case ingot:
                return "ingot";
            case gem:
                switch (material) {
                    case antimatter:
                    case pureAntimatter:
                    case compressedPureAntimatter_1:
                        return "matter";
                    case compressedPureAntimatter_2:
                    case compressedPureAntimatter_3:
                        return "matter2";
                    case compressedPureAntimatter_4:
                    case compressedPureAntimatter_5:
                        return "matter3";
                    case compressedPureAntimatter_6:
                    case compressedPureAntimatter_7:
                        return "matter4";
                    case octuplePureAntimatter:
                        return "matter5";
                }
        }
        return null;
    }

    public ClayiumMaterial getMaterial() {
        return material;
    }

    public boolean useGeneralIcon() {
        return useGeneralIcon;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        final String defaultKey = this.getUnlocalizedNameInefficiently(stack) + ".name";
        if (UtilLocale.canLocalize(defaultKey)) {
            return UtilLocale.localizeAndFormat(defaultKey);
        }

        final String shapeKey = ClayiumShape.getLocalizeKey(this.shape);
        if (!UtilLocale.canLocalize(shapeKey)) {
            return "[shaped material]";
        }

        return UtilLocale.localizeAndFormat(shapeKey,
                UtilLocale.localizeAndFormat(ClayiumMaterial.getLocalizeKey(this.material))
        );
    }
}
