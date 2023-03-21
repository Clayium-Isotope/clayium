package mods.clayium.util;

import javax.annotation.Nullable;

public class UtilTier {
    public enum BufferTransport {
        _0(8, 8, 1, 1),
        _1(8, 8, 1, 1),
        _2(8, 8, 1, 1),
        _3(8, 8, 1, 1),
        _4(8, 8, 1, 1),
        _5(4, 4, 4, 4),
        _6(2, 2, 16, 16),
        _7(1, 1, 64, 64),
        _8(1, 1, 128, 128),
        _9(1, 1, 192, 192),
        _10(1, 1, 256, 256),
        _11(1, 1, 512, 512),
        _12(1, 1, 1024, 1024),
        _13(1, 1, 6400, 6400);

        public final int autoInsertInterval;
        public final int autoExtractInterval;
        public final int maxAutoInsertDefault;
        public final int maxAutoExtractDefault;

        BufferTransport(int insertInterval, int extractInterval, int maxInsert, int maxExtract) {
            this.autoInsertInterval = insertInterval;
            this.autoExtractInterval = extractInterval;
            this.maxAutoInsertDefault = maxInsert;
            this.maxAutoExtractDefault = maxExtract;
        }

        @Nullable
        public static BufferTransport getByTier(int tier) {
            switch(tier) {
                case 0:     return _0;
                case 1:     return _1;
                case 2:     return _2;
                case 3:     return _3;
                case 4:     return _4;
                case 5:     return _5;
                case 6:     return _6;
                case 7:     return _7;
                case 8:     return _8;
                case 9:     return _9;
                case 10:    return _10;
                case 11:    return _11;
                case 12:    return _12;
                case 13:    return _13;
                default:    return null;
            }
        }
    }

    public enum MachineTransport {
        _0(20, 20, 8, 8),
        _1(20, 20, 8, 8),
        _2(20, 20, 8, 8),
        _3(20, 20, 8, 8),
        _4(20, 20, 8, 8),
        _5(2, 2, 16, 16),
        _6(1, 1, 64, 64),
        _7(1, 1, 64, 64),
        _8(1, 1, 64, 64),
        _9(1, 1, 64, 64),
        _10(1, 1, 64, 64),
        _11(1, 1, 64, 64),
        _12(1, 1, 64, 64),
        _13(1, 1, 64, 64);

        public final int autoInsertInterval;
        public final int autoExtractInterval;
        public final int maxAutoInsertDefault;
        public final int maxAutoExtractDefault;

        MachineTransport(int insertInterval, int extractInterval, int maxInsert, int maxExtract) {
            this.autoInsertInterval = insertInterval;
            this.autoExtractInterval = extractInterval;
            this.maxAutoInsertDefault = maxInsert;
            this.maxAutoExtractDefault = maxExtract;
        }

        @Nullable
        public static MachineTransport getByTier(int tier) {
            switch(tier) {
                case 0:     return _0;
                case 1:     return _1;
                case 2:     return _2;
                case 3:     return _3;
                case 4:     return _4;
                case 5:     return _5;
                case 6:     return _6;
                case 7:     return _7;
                case 8:     return _8;
                case 9:     return _9;
                case 10:    return _10;
                case 11:    return _11;
                case 12:    return _12;
                case 13:    return _13;
                default:    return null;
            }
        }
    }

    public static boolean canAutoTransfer(int tier) {
        return tier >= 3;
    }

    /**
     * @return true: Pushing the button is the only way to produce CE<p>
     * false: kinds of Clay Energy Block are the only way to produce CE
     */
    public static boolean canManufactureCraft(int tier) {
        return tier <= 2;
    }

    public static boolean acceptWaterWheel(int tier) {
        return tier == 2 || tier == 3;
    }

    public static boolean acceptEnergyClay(int tier) {
        return tier >= 4;
    }
}
