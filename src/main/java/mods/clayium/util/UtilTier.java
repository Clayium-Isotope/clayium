package mods.clayium.util;

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

        public static BufferTransport getByTier(TierPrefix tier) {
            switch (tier) {
                case none:              return _0;
                case clay:              return _1;
                case denseClay:         return _2;
                case simple:            return _3;
                case basic:             return _4;
                case advanced:          return _5;
                case precision:         return _6;
                case claySteel:         return _7;
                case clayium:           return _8;
                case ultimate:          return _9;
                case antimatter:        return _10;
                case pureAntimatter:    return _11;
                case OEC:               return _12;
                case OPA:               return _13;
            }

            throw new IllegalTierException();
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

        public static MachineTransport getByTier(TierPrefix tier) {
            switch (tier) {
                case unknown:
                case none:              return _0;
                case clay:              return _1;
                case denseClay:         return _2;
                case simple:            return _3;
                case basic:             return _4;
                case advanced:          return _5;
                case precision:         return _6;
                case claySteel:         return _7;
                case clayium:           return _8;
                case ultimate:          return _9;
                case antimatter:        return _10;
                case pureAntimatter:    return _11;
                case OEC:               return _12;
                case OPA:               return _13;
            }

            throw new IllegalTierException();
        }
    }

    public static boolean canAutoTransfer(TierPrefix tier) {
        return TierPrefix.comparator.compare(tier, TierPrefix.denseClay) > 0;
    }

    /**
     * @return true: Pushing the button is the only way to produce CE<p>
     * false: kinds of Clay Energy Block are the only way to produce CE
     */
    public static boolean canManufactureCraft(TierPrefix tier) {
        return TierPrefix.comparator.compare(tier, TierPrefix.simple) < 0;
    }

    public static boolean acceptWaterWheel(TierPrefix tier) {
        switch (tier) {
            case clay:
            case denseClay:
                return true;
            default:
                return false;
        }
    }

    public static boolean acceptEnergyClay(TierPrefix tier) {
        return TierPrefix.comparator.compare(tier, TierPrefix.simple) > 0;
    }
}
