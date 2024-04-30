package mods.clayium.machine.ClayWorkTable;

public enum KneadingMethod {

    Roll,       // 転がす
    Press,      // つぶす
    Bend,       // 引き延ばす
    CutRect,    // 矩形に切る
    CutCircle,  // 円形に切る
    Slice,      // 断面を切る
    UNKNOWN;    // 未知

    public static KneadingMethod fromId(int id) {
        switch (id) {
            case 0:
                return Roll;
            case 1:
                return Press;
            case 2:
                return Bend;
            case 3:
                return CutRect;
            case 4:
                return CutCircle;
            case 5:
                return Slice;
            default:
                return UNKNOWN;
        }
    }

    public String toBeSuffix() {
        switch (this) {
            case Roll:
                return "by_roll";
            case Press:
                return "by_press";
            case Bend:
                return "by_bend";
            case CutRect:
                return "by_cut_r";
            case CutCircle:
                return "by_cut_c";
            case Slice:
                return "by_slice";
            default:
                return "";
        }
    }
}
