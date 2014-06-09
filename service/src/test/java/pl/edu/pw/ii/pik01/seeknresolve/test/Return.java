package pl.edu.pw.ii.pik01.seeknresolve.test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class Return<T> implements Answer<T> {

    private int paramNumber = 0;

    public static <Z> Return<Z> firstParameter() {
        Return<Z> r = new Return<>();
        r.paramNumber = 0;
        return r;
    }

    @Override
    public T answer(InvocationOnMock invocation) throws Throwable {
        return (T) invocation.getArguments()[paramNumber];
    }
}