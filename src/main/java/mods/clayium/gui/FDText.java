package mods.clayium.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class FDText<T>
        extends FDWithHandler<FDText<T>, T> {
    public static FontRenderer renderer = (Minecraft.getMinecraft()).fontRenderer;
    public String defaultString = "";
    public int defaultX = 0;
    public int defaultY = 0;
    public int defaultColor = -16777216;
    public int defaultMaximumLength = -1;

    public FDText(IFunctionalIterable<IFDHandler<FDText<T>, T>, T> defaultHandlers) {
        super(defaultHandlers);
    }

    public FDText(List<IFDHandler<FDText<T>, T>> defaultHandlers) {
        super(defaultHandlers);
    }

    public FDText(IFDHandler<FDText<T>, T> defaultHandler) {
        super(defaultHandler);
    }


    public T render(T param) {
        param = preRender(param);
        int maxLength = getMaximumLength(param);
        if (maxLength <= 0) {
            renderer.drawString(getString(param), getX(param), getY(param), getColor(param), false);
        } else {
            renderer.drawSplitString(getString(param), getX(param), getY(param), maxLength, getColor(param));
        }
        return postRender(param);
    }

    public T preRender(T param) {
        return param;
    }

    public T postRender(T param) {
        return param;
    }

    public String getString(T param) {
        return this.defaultString;
    }

    public int getX(T param) {
        return this.defaultX;
    }

    public int getY(T param) {
        return this.defaultY;
    }

    public int getColor(T param) {
        return this.defaultColor;
    }


    public int getMaximumLength(T param) {
        return this.defaultMaximumLength;
    }

    public FDText<T> preApplyHandler(T param) {
        return this;
    }

    public static class FDTextHandler<T> implements IFDHandler<FDText<T>, T> {
        public String defaultString = "";
        public int defaultX = 0;
        public int defaultY = 0;
        public int defaultColor = -16777216;
        public int defaultMaximumLength = -1;
        public boolean handleString = false;
        public boolean handleX = false;
        public boolean handleY = false;
        public boolean handleColor = false;
        public boolean handleMaximumLength = false;

        public T apply(FDText<T> functional, T param) {
            if (this.handleString) {
                functional.defaultString = applyString(param);
            }
            if (this.handleX) {
                functional.defaultX = applyX(param);
            }
            if (this.handleY) {
                functional.defaultY = applyY(param);
            }
            if (this.handleColor) {
                functional.defaultColor = applyColor(param);
            }
            if (this.handleMaximumLength) {
                functional.defaultMaximumLength = applyMaximumLength(param);
            }
            return param;
        }

        public String applyString(T param) {
            return this.defaultString;
        }

        public int applyX(T param) {
            return this.defaultX;
        }

        public int applyY(T param) {
            return this.defaultY;
        }

        public int applyColor(T param) {
            return this.defaultColor;
        }

        public int applyMaximumLength(T param) {
            return this.defaultMaximumLength;
        }
    }

    public FDText<T> getFDText(List<FDTextHandler<T>> preHandlers, int x, int y, int color, int maxLength, List<FDTextHandler<T>> postHandlers) {
        List<FDTextHandler<T>> list = new ArrayList<FDTextHandler<T>>();
        if (preHandlers != null)
            list.addAll(preHandlers);
        FDTextHandler<T> aHandler = new FDTextHandler<T>();
        aHandler.handleX = aHandler.handleY = aHandler.handleColor = aHandler.handleMaximumLength = true;
        aHandler.defaultX = x;
        aHandler.defaultY = y;
        aHandler.defaultColor = color;
        aHandler.defaultMaximumLength = maxLength;
        list.add(aHandler);
        if (postHandlers != null)
            list.addAll(postHandlers);
        return new FDText((List) list);
    }

    public FDText<T> getFDText(FDTextHandler<T> preHandler, int x, int y, int color, int maxLength) {
        return getFDText(Arrays.asList((FDTextHandler<T>[]) new FDTextHandler[] {preHandler}), x, y, color, maxLength, null);
    }

    public static FDTextHandler<Object> getStringHandler(String string) {
        FDTextHandler<Object> aHandler = new FDTextHandler();
        aHandler.handleString = true;
        aHandler.defaultString = string;
        return aHandler;
    }

    public static FDText<Object> INSTANCE = new FDText();

    public FDText() {}
}
