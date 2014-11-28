package com.jsonde.test;

import com.jsonde.api.configuration.AgentConfigurationMessage;
import com.jsonde.api.configuration.ClassFilterDto;
import com.jsonde.client.Client;
import com.jsonde.client.dao.DaoFactory;
import com.jsonde.samples.BigMethodCallApplication;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class BigMethodCallApplicationTest extends AgentTestCase {

    public void test1() throws Exception {

        executeWithApplicationAndAgent(BigMethodCallApplication.class, new Callable() {

            public Object call() throws Exception {

                Client client = new Client("target/test-database/dbBigMethodCallApplication", "127.0.0.1", 60001);

                waitForAgent();

                client.start();

                AgentConfigurationMessage message = new AgentConfigurationMessage();

                ClassFilterDto excludeAll = new ClassFilterDto(false, "*");
                ClassFilterDto includeSample = new ClassFilterDto(true, "com.jsonde.*");

                message.setClassFilters(Arrays.asList(excludeAll, includeSample));

                client.sendMessage(message);

                Thread.sleep(3000);
                client.stop();

                long methodCallCount = DaoFactory.getMethodCallDao().count();
                long topMethodCallCount = DaoFactory.getTopMethodCallDao().count();
                long methodCallSummaryCount = DaoFactory.getMethodCallSummaryDao().count();

                System.out.println(methodCallCount);
                System.out.println(topMethodCallCount);
                System.out.println(methodCallSummaryCount);

                System.out.println(
                        DaoFactory.getMethodCallSummaryDao().getAll()
                );

                return this;
            }

        });

    }

}