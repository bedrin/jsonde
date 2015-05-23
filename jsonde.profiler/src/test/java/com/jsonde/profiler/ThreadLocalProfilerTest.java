package com.jsonde.profiler;

import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.api.methodCall.MethodCallSummaryDto;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class ThreadLocalProfilerTest extends TestCase {

    private class TestProfiler extends ProfilerImpl {

        int processCalled;

        List<MethodCallDto> methodCallDtos;

        @Override
        protected void processMethodCall(List<MethodCallDto> methodCallDtos, MethodCallSummaryDto methodCallSummaryDto, boolean complete) {
            processCalled++;
            this.methodCallDtos = new ArrayList<MethodCallDto>(methodCallDtos);
        }
    }

    public void testThreadLocalProfiler() throws Exception {

        TestProfiler testProfiler = new TestProfiler();

        ThreadLocalProfiler tlp = new ThreadLocalProfiler(testProfiler);

        tlp.enterMethodImpl(1);
            tlp.enterMethodImpl(2);

                tlp.enterMethodImpl(3);
                tlp.leaveMethodImpl(null);

            tlp.leaveMethodImpl(null);

            tlp.enterMethodImpl(2);
            tlp.leaveMethodImpl(null);

            tlp.enterMethodImpl(2);
            tlp.leaveMethodImpl(null);

            tlp.enterMethodImpl(2);
            tlp.leaveMethodImpl(null);

        tlp.leaveMethodImpl(null);

        assertEquals(1, testProfiler.processCalled);
        assertEquals(6, testProfiler.methodCallDtos.size());

    }

}
