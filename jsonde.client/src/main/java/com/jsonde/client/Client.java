package com.jsonde.client;

import com.jsonde.api.Message;
import com.jsonde.api.MessageListener;
import com.jsonde.api.function.heap.ClassHeapDataDto;
import com.jsonde.api.function.heap.DumpHeapFunctionRequest;
import com.jsonde.api.function.heap.DumpHeapFunctionResponse;
import com.jsonde.api.methodCall.*;
import com.jsonde.api.telemetry.TelemetryDataDto;
import com.jsonde.api.telemetry.TelemetryDataMessage;
import com.jsonde.client.dao.*;
import com.jsonde.client.domain.*;
import com.jsonde.client.network.NetworkClient;
import com.jsonde.client.network.NetworkClientException;
import com.jsonde.client.network.NetworkClientImpl;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class Client implements MessageListener {

    private NetworkClient networkClient;

    private Vector<MethodCallListener> methodCallListeners = new Vector<MethodCallListener>();
    private Vector<ClassListener> classListeners = new Vector<ClassListener>();

    public void addMethodCallListener(MethodCallListener methodCallListener) {
        methodCallListeners.add(methodCallListener);
    }

    public void addClassListener(ClassListener classListener) {
        classListeners.add(classListener);
    }

    private void fireMethodCallEvent(MethodCall methodCall) {

        for (MethodCallListener listener : methodCallListeners) {
            listener.onMethodCall(methodCall);
        }

    }

    private void fireRegisterClassEvent(Clazz clazz) {

        for (ClassListener classListener : classListeners) {
            classListener.onRegisterClass(clazz);
        }

    }

    public void loadMethodCalls() {

        try {
            for (TopMethodCall topMethodCall :
                    DaoFactory.getTopMethodCallDao().getAll()) {
                fireMethodCallEvent(
                        DaoFactory.getMethodCallDao().get(topMethodCall.getMethodCallId())
                );
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    public void dumpHeap() {

        try {

            DumpHeapFunctionResponse dumpHeapFunctionResponse =
                    networkClient.invokeFunction(new DumpHeapFunctionRequest());

            for (ClassHeapDataDto classHeapDataDto : dumpHeapFunctionResponse.getClassHeapDataDtos()) {

                Method method = DaoFactory.getMethodDao().get(classHeapDataDto.getConstructorId());

                long classId = method.getClassId();

                ClazzDao clazzDao = DaoFactory.getClazzDao();

                Clazz clazz = clazzDao.get(classId);

                clazz.setCreateCounter(classHeapDataDto.getCreateCounter());
                clazz.setCollectCounter(classHeapDataDto.getCollectCounter());
                clazz.setTotalCurrentSize(classHeapDataDto.getTotalCurrentSize());

                clazzDao.update(clazz);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    private final boolean online;

    private final JdbcConnectionPool jdbcConnectionPool;

    private final static String DB_CONNECTION_MODIFIERS = "LOCK_MODE=0;LOG=0;UNDO_LOG=0;CACHE_SIZE=65536";

    public Client(String databaseFileName, String address, int port) {

        online = true;

        jdbcConnectionPool = JdbcConnectionPool.create(
                "jdbc:h2:" + databaseFileName + ";" + DB_CONNECTION_MODIFIERS,
                "sa", "sa");
        jdbcConnectionPool.setMaxConnections(100);
        jdbcConnectionPool.setLoginTimeout(0);

        try {

            DaoFactory.initialize(
                    jdbcConnectionPool
            );

            DaoFactory.createSchema();

        } catch (DaoException e) {
            e.printStackTrace();
        }
        networkClient = new NetworkClientImpl(address, port);
    }

    public Client(String databaseFileName) {

        online = false;

        jdbcConnectionPool = JdbcConnectionPool.create(
                "jdbc:h2:" + databaseFileName + ";" + DB_CONNECTION_MODIFIERS,
                "sa", "sa");
        jdbcConnectionPool.setMaxConnections(100);
        jdbcConnectionPool.setLoginTimeout(0);

        try {

            DaoFactory.initialize(
                    jdbcConnectionPool
            );

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline() {
        return online;
    }

    public void start() {

        networkClient.addMessageListener(this);

        try {
            networkClient.start();
        } catch (NetworkClientException e) {
            e.printStackTrace();
        }

    }

    public void stop() {

        try {
            networkClient.stop();
        } catch (NetworkClientException e) {
            e.printStackTrace();
        }

        networkClient.removeMessageListener(this);

        /*try {
            jdbcConnectionPool.dispose();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

    }

    public void sendMessage(Message message) {
        networkClient.sendMessage(message);
    }

    public long getClassCount() {
        try {
            return DaoFactory.getClazzDao().count();
        } catch (DaoException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void onMessage(Message message) {


        if (message instanceof RegisterClassMessage) {

            RegisterClassMessage registerClassMessage = (RegisterClassMessage) message;

            Clazz clazz = new Clazz();
            clazz.setId(registerClassMessage.getClassId());
            clazz.setName(registerClassMessage.getName());

            try {
                DaoFactory.getClazzDao().insert(clazz);
            } catch (DaoException e) {
                e.printStackTrace();
            }

            fireRegisterClassEvent(clazz);

        } else if (message instanceof RegisterMethodMessage) {

            RegisterMethodMessage registerMethodMessage =
                    (RegisterMethodMessage) message;

            Method method = new Method();
            method.setId(registerMethodMessage.getMethodId());
            method.setClassId(registerMethodMessage.getClassId());
            method.setName(registerMethodMessage.getName());

            try {
                DaoFactory.getMethodDao().insert(method);
            } catch (DaoException e) {
                e.printStackTrace();
            }

        } else if (message instanceof MethodCallMessage) {

            MethodCallMessage methodCallMessage =
                    (MethodCallMessage) message;

            boolean complete = methodCallMessage.isComplete();

            MethodCallDto[] methodCallDtos =
                    methodCallMessage.getMethodCallDtos();

            MethodCall methodCall = null;

            try {
                methodCall = DaoFactory.getMethodCallDao().
                        persistMethodCallDtos(methodCallDtos);
            } catch (DaoException e) {
                e.printStackTrace();
            }

            MethodCallSummaryDto methodCallSummaryDto = methodCallMessage.getMethodCallSummaryDto();

            try {
                DaoFactory.getMethodCallSummaryDao().processMethodCallSummaryDto(methodCallSummaryDto);
            } catch (DaoException e) {
                e.printStackTrace();
            }

            if (complete) {
                createTopMethodCall(methodCall);
            }

        } else if (message instanceof TelemetryDataMessage) {

            TelemetryDataMessage telemetryDataMessage =
                    (TelemetryDataMessage) message;

            TelemetryDataDto dto = telemetryDataMessage.getTelemetryData();

            TelemetryData telemetryData = new TelemetryData();

            telemetryData.setId(telemetryDataIdGenerator.getAndIncrement());
            telemetryData.setTime(dto.time);

            {
                // memory
                telemetryData.setFreeMemory(dto.freeMemory);
                telemetryData.setMaxMemory(dto.maxMemory);
                telemetryData.setTotalMemory(dto.totalMemory);
            }

            {
                // class loading
                telemetryData.setLoadedClassCount(dto.loadedClassCount);
                telemetryData.setClassCount(dto.classCount);
                telemetryData.setUnloadedClassCount(dto.unloadedClassCount);
            }

            {
                // compilation
                telemetryData.setTotalCompilationTime(dto.totalCompilationTime);
            }

            try {
                DaoFactory.getTelemetryDataDao().insert(telemetryData);
            } catch (DaoException e) {
                e.printStackTrace();
            }


        } else if (message instanceof DescribeClassMessage) {

            DescribeClassMessage describeClassMessage =
                    (DescribeClassMessage) message;

            try {

                long classId;

                if (describeClassMessage.isClassRedefined()) {

                    classId = describeClassMessage.getClassId();

                } else {

                    long staticConstructorMethodId = describeClassMessage.getMethodId();
                    Method method = DaoFactory.getMethodDao().get(staticConstructorMethodId);
                    classId = method.getClassId();

                }

                ClazzDao clazzDao = DaoFactory.getClazzDao();
                Clazz clazz = clazzDao.get(classId);

                {
                    ClazzLoaderDao clazzLoaderDao = DaoFactory.getClazzLoaderDao();

                    ClazzLoader clazzLoader = clazzLoaderDao.get(describeClassMessage.getClassLoaderId());

                    if (null == clazzLoader) {
                        clazzLoader = new ClazzLoader();
                        clazzLoader.setId(describeClassMessage.getClassLoaderId());
                        clazzLoaderDao.insert(clazzLoader);
                    }

                    clazz.setClassLoaderId(clazzLoader.getId());

                }

                {
                    CodeSourceDao codeSourceDao =
                            DaoFactory.getCodeSourceDao();

                    CodeSource codeSource;

                    List<CodeSource> codeSources =
                            null == describeClassMessage.getCodeLocation() ?
                                    codeSourceDao.getByCondition("source is null") :
                                    codeSourceDao.getByCondition("source = ?", describeClassMessage.getCodeLocation());

                    if (codeSources.isEmpty()) {

                        codeSource = new CodeSource();
                        codeSource.setId(codeSourceIdGenerator.getAndIncrement());
                        codeSource.setSource(describeClassMessage.getCodeLocation());

                        codeSourceDao.insert(codeSource);

                    } else {
                        codeSource = codeSources.get(0);
                    }

                    clazz.setCodeSourceId(codeSource.getId());

                }

                clazzDao.update(clazz);

            } catch (DaoException e) {
                e.printStackTrace();
            }

        }

    }

    private void createTopMethodCall(MethodCall methodCall) {

        //todo move this logic to thread local profiler

        TopMethodCallBuilder builder = new TopMethodCallBuilder();
        try {
            builder.visitMethodCall(methodCall);
        } catch (DaoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TopMethodCallDao topMethodCallDao = DaoFactory.getTopMethodCallDao();

            List<TopMethodCall> topMethodCalls =
                    topMethodCallDao.getByCondition(
                            "HASHCODE = ? AND COUNT = ?",
                            builder.getHashCode(), builder.getCount());

            if (0 == topMethodCalls.size()) {

                fireMethodCallEvent(methodCall);

                TopMethodCall topMethodCall = new TopMethodCall();

                topMethodCall.setId(topMethodCallIdGenerator.getAndIncrement());
                topMethodCall.setMethodCallId(methodCall.getId());
                topMethodCall.setHashCode(builder.getHashCode());
                topMethodCall.setCount(builder.getCount());

                topMethodCallDao.insert(topMethodCall);

            }

        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    private static class TopMethodCallBuilder {

        private int hashCode;
        private ByteArrayOutputStream outputStream;
        private int count;

        private TopMethodCallBuilder() {
            hashCode = 1;
            outputStream = new ByteArrayOutputStream();
        }

        public void visitMethodCall(MethodCall methodCall) throws IOException, DaoException {

            count++;

            writeByte(0);

            long value = methodCall.getMethodId();

            byte[] bytes = new byte[8];

            for (int i = 0; i < bytes.length; ++i) {
                int offset = (bytes.length - i - 1) * 8;
                bytes[i] = (byte) ((value & (0xff << offset)) >>> offset);
            }

            writeByte(bytes);

            for (MethodCall callee :
                    DaoFactory.getMethodCallDao().getByCondition("CALLERID = ?", methodCall.getMethodId())) {
                visitMethodCall(callee);
            }

            // traverse children

            writeByte(1);

        }

        private void writeByte(int b) {

            outputStream.write(b);
            hashCode = 31 * hashCode + (byte) b;

        }

        private void writeByte(byte[] bs) {
            for (byte b : bs)
                writeByte(b);
        }

        public int getHashCode() {
            return hashCode;
        }

        public ByteArrayOutputStream getOutputStream() {
            return outputStream;
        }

        public int getCount() {
            return count;
        }
    }

    private AtomicLong codeSourceIdGenerator = new AtomicLong();
    private AtomicLong topMethodCallIdGenerator = new AtomicLong();
    private AtomicLong telemetryDataIdGenerator = new AtomicLong();

}
