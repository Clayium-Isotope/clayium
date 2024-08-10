package mods.clayium.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class SIFormat extends NumberFormat {
    protected static final DecimalFormat integerDF = new DecimalFormat("##0");
    protected static final DecimalFormat decimalDF = new DecimalFormat("##0.000");
    protected static final DecimalFormat cutZeroDF = new DecimalFormat("##0.###");
    protected static final DecimalFormat exponentDF = new DecimalFormat("##0.000E0");
    protected static final String[] SINumerals = new String[] { "q", "r", "y", "z", "a", "f", "p", "n", "μ", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };
    protected static final int SI_ONE = 10;

    protected final boolean showTrailingZero;
    protected final int expOffset;

    public SIFormat(boolean showTrailingZero, int expOffset) {
        this.showTrailingZero = showTrailingZero;
        this.expOffset = expOffset;
    }

    /**
     * 0
     * -123.456k
     * -123.456E-30
     */
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        if (number == 0) {
            // number=1,expOffset=-5 すなわち単位量が 10μCE でも、表示時には 0CE と表示される
            if (pos.getFieldAttribute() == Field.INTEGER) {
                pos.setBeginIndex(0);
                pos.setEndIndex(1);
            }

            return toAppendTo.append(0);
        }

        if (number < 0) {
            if (pos.getFieldAttribute() == Field.SIGN) {
                pos.setBeginIndex(0);
                pos.setEndIndex(1);
            }
            toAppendTo.append('-');
            number *= -1;
        }

        number *= Math.pow(10, this.expOffset);

        int exp = (int) Math.floor(Math.log10(number));
        int exp_of_1k = Math.floorDiv(exp, 3);

        if (exp_of_1k == 0) {
            int fieldStart = toAppendTo.length();
            toAppendTo.append(integerDF.format(number));
            if (pos.getFieldAttribute() == Field.INTEGER) {
                pos.setBeginIndex(fieldStart);
                pos.setEndIndex(toAppendTo.length());
            }
            return toAppendTo;
        }

        if (exp_of_1k < -SI_ONE || exp_of_1k >= SINumerals.length - SI_ONE) {
            return exponentDF.format(number, toAppendTo, pos);
        }

        // toAppendTo が変化する副作用があるので、Returnは受けなくてよい。
        (this.showTrailingZero ? decimalDF : cutZeroDF)
                .format(number / Math.pow(1000, exp_of_1k), toAppendTo, pos);

        int fieldStart = toAppendTo.length();
        int numSI = exp_of_1k + SI_ONE;
        toAppendTo.append(SINumerals[numSI]);

        // Field.SUFFIX のほうが適切か?
        if (pos.getFieldAttribute() == Field.EXPONENT) {
            pos.setBeginIndex(fieldStart);
            pos.setEndIndex(toAppendTo.length());
        }
        return toAppendTo;
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
