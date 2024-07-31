package mods.clayium.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class SIFormat extends NumberFormat {
    protected static final DecimalFormat integerDF = new DecimalFormat("##0");
    protected static final DecimalFormat decimalDF = new DecimalFormat("##0.000");
    protected static final DecimalFormat cutZeroDF = new DecimalFormat("##0.###");
    protected static final String[] SINumerals = new String[] { "q", "r", "y", "z", "a", "f", "p", "n", "μ", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };
    protected static final int SI_ONE = 10;

    protected final boolean showTrailingZero;
    protected final int expOffset;

    public SIFormat(boolean showTrailingZero, int expOffset) {
        this.showTrailingZero = showTrailingZero;
        this.expOffset = expOffset;
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        if (pos.getField() == NumberFormat.INTEGER_FIELD) {
            pos.setBeginIndex(0);
            pos.setEndIndex(3);
        } else if (pos.getField() == NumberFormat.FRACTION_FIELD) {
            pos.setBeginIndex(4);
            pos.setEndIndex(6);
        }

        int baseSI = Math.floorDiv(expOffset, 3) + SI_ONE;
        if (number == 0) {
            // number=1,expOffset=-5 すなわち単位量が 10μCE でも、表示時には 0μCE と表示される
            return toAppendTo.append(0).append(SINumerals[baseSI]);
        }

        if (number < 0) {
            toAppendTo.append('-');
            number *= -1;
        }

        int exp = (int) Math.floor(Math.log10(number) + expOffset);
        int exp_of_1k = Math.floorDiv(exp, 3);
        int numSI = exp_of_1k + SI_ONE;

        String mantissa = (numSI == baseSI ? integerDF : this.showTrailingZero ? decimalDF : cutZeroDF)
                .format(number / Math.pow(1000, exp_of_1k));

        if (numSI < 0 || numSI >= SINumerals.length) {
            return toAppendTo.append(mantissa).append("e").append(exp_of_1k * 3);
        }

        return toAppendTo.append(mantissa).append(SINumerals[SI_ONE]);
    }

    /**
     * 関心があるのは上位の桁なので、{@code double}へのキャストをしても意に反しない
     */
    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.format((double) number, toAppendTo, pos);
    }

    /**
     * 必要になったら実装します。
     */
    @Override
    @Deprecated
    public Number parse(String source, ParsePosition parsePosition) {
        return Double.NaN;
    }
}
