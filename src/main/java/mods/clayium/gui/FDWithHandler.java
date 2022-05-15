package mods.clayium.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class FDWithHandler<S, T>
        implements IFunctionalDrawer<T> {
    protected IFunctionalIterable<IFDHandler<S, T>, T> defaultHandlers = null;

    public FDWithHandler(IFunctionalIterable<IFDHandler<S, T>, T> defaultHandlers) {
        this.defaultHandlers = defaultHandlers;
    }

    public FDWithHandler(List<IFDHandler<S, T>> defaultHandlers) {
        this(new FunctionalList<IFDHandler<S, T>, T>(defaultHandlers));
    }

    public FDWithHandler(IFDHandler<S, T> defaultHandler) {
        this(Arrays.asList((IFDHandler<S, T>[]) new IFDHandler[] {defaultHandler}));
    }

    public FDWithHandler() {
        this((IFDHandler<S, T>) null);
    }


    public T draw(T param) {
        return render(applyHandler(param));
    }

    public abstract T render(T paramT);

    public T applyHandler(T param) {
        IFunctionalIterable<IFDHandler<S, T>, T> iterable = getHandlerIterator(param);
        if (iterable == null) return param;
        IFunctionalIterator<IFDHandler<S, T>, T> iterator = iterable.iterator(param);
        if (iterator == null) return param;
        while (iterator.hasNext(param)) {
            IFDHandler<S, T> handler = iterator.next(param);
            param = handler.apply(preApplyHandler(param), param);
        }
        return param;
    }

    public abstract S preApplyHandler(T paramT);

    public IFunctionalIterable<IFDHandler<S, T>, T> getHandlerIterator(T param) {
        return this.defaultHandlers;
    }

    public static interface IFDHandler<S, T> {
        T apply(S param1S, T param1T);
    }

    public static interface IFunctionalIterable<S, T> {
        IFunctionalIterator<S, T> iterator(T param1T);
    }

    public static interface IFunctionalIterator<S, T> {
        boolean hasNext(T param1T);

        S next(T param1T);
    }

    public static class FunctionalList<S, T> implements IFunctionalIterable<S, T> {
        Iterable<S> iterable;

        public FunctionalList(Iterable<S> iterable) {
            this.iterable = iterable;
        }

        public IFunctionalIterator<S, T> iterator(T param) {
            return new FunctionalIterator<S, T>(this.iterable.iterator());
        }
    }

    public static class FunctionalIterator<S, T> implements IFunctionalIterator<S, T> {
        Iterator<S> iterator;

        public FunctionalIterator(Iterator<S> iterator) {
            this.iterator = iterator;
        }

        public boolean hasNext(T param) {
            return this.iterator.hasNext();
        }

        public S next(T param) {
            return this.iterator.next();
        }
    }
}
