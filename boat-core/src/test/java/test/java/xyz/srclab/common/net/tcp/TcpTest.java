package test.java.xyz.srclab.common.net.tcp;

import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.srclab.common.base.BLog;
import xyz.srclab.common.io.BBuffer;
import xyz.srclab.common.net.BSocket;
import xyz.srclab.common.net.tcp.TcpListener;
import xyz.srclab.common.net.tcp.TcpClient;
import xyz.srclab.common.net.tcp.TcpContext;
import xyz.srclab.common.net.tcp.TcpServer;
import xyz.srclab.common.run.AsyncRunner;
import xyz.srclab.common.run.RunLatch;
import xyz.srclab.common.run.Runner;

import java.io.InputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpTest {

    private static final String SERVER_OPEN = "SERVER_OPEN";
    private static final String SERVER_RECEIVE = "SERVER_RECEIVE";
    private static final String CLIENT_MESSAGE = "CLIENT_RECEIVE";
    private static final String SERVER_CLOSE = "SERVER_CLOSE";

    private static String serverSentMessage = "";
    private static String serverReceivedMessage = "";
    private static String clientReceivedMessage = "";

    @Test
    public void testTcp() throws Exception {
        int port = BSocket.availablePort();
        String address = "localhost:" + port;
        RunLatch latch = RunLatch.newRunLatch(0);
        TcpServer tcpServer = TcpServer.nioServer(
            BSocket.parseInetSocketAddress(address),
            new ServerHandler(latch)
        );

        TcpClient nioClient = TcpClient.nioClient(address);
        testTcp0(nioClient, tcpServer, latch);

        TcpClient bioClient = TcpClient.bioClient(address);
        testTcp0(bioClient, tcpServer, latch);
    }

    private void testTcp0(TcpClient tcpClient, TcpServer tcpServer, RunLatch latch) throws Exception {
        serverSentMessage = "";
        serverReceivedMessage = "";
        clientReceivedMessage = "";

        tcpServer.start();
        tcpClient.connect();

        latch.lockUp();
        BLog.info("Send message1: {}", CLIENT_MESSAGE);
        tcpClient.send(CLIENT_MESSAGE);
        latch.await();
        String received1 = tcpClient.receiveString();
        BLog.info("Client receive1: {}", received1);
        clientReceivedMessage += received1;
        Assert.assertEquals(
            serverReceivedMessage,
            CLIENT_MESSAGE
        );
        Assert.assertEquals(
            clientReceivedMessage,
            SERVER_OPEN + SERVER_RECEIVE
        );

        latch.lockUp();
        BLog.info("Send message2: {}", CLIENT_MESSAGE);
        tcpClient.send(CLIENT_MESSAGE);
        latch.await();
        String received2 = tcpClient.receiveString();
        BLog.info("Client receive2: {}", received2);
        clientReceivedMessage += received2;
        Assert.assertEquals(
            serverReceivedMessage,
            CLIENT_MESSAGE + CLIENT_MESSAGE
        );
        Assert.assertEquals(
            clientReceivedMessage,
            SERVER_OPEN + SERVER_RECEIVE + SERVER_RECEIVE
        );

        latch.lockUp();
        BLog.info("Send message3: {}", CLIENT_MESSAGE);
        tcpClient.send(CLIENT_MESSAGE);
        latch.await();
        InputStream input3 = tcpClient.receive();
        byte[] r3Bytes = new byte[SERVER_RECEIVE.getBytes().length];
        input3.read(r3Bytes);
        String received3 = new String(r3Bytes);
        BLog.info("Client receive3: {}", received3);
        clientReceivedMessage += received3;
        Assert.assertEquals(
            serverReceivedMessage,
            CLIENT_MESSAGE + CLIENT_MESSAGE + CLIENT_MESSAGE
        );
        Assert.assertEquals(
            clientReceivedMessage,
            SERVER_OPEN + SERVER_RECEIVE + SERVER_RECEIVE + SERVER_RECEIVE
        );

        latch.lockUp();
        BLog.info("Send message4: {}", CLIENT_MESSAGE);
        tcpClient.send(CLIENT_MESSAGE);
        latch.await();
        InputStream input4 = tcpClient.receive();
        byte[] r4Bytes = new byte[SERVER_RECEIVE.getBytes().length + 100];
        input4.read(r4Bytes, 44, SERVER_RECEIVE.getBytes().length);
        String received4 = new String(r4Bytes, 44, SERVER_RECEIVE.getBytes().length);
        BLog.info("Client receive4: {}", received4);
        clientReceivedMessage += received4;
        Assert.assertEquals(
            serverReceivedMessage,
            CLIENT_MESSAGE + CLIENT_MESSAGE + CLIENT_MESSAGE + CLIENT_MESSAGE
        );
        Assert.assertEquals(
            clientReceivedMessage,
            SERVER_OPEN + SERVER_RECEIVE + SERVER_RECEIVE + SERVER_RECEIVE + SERVER_RECEIVE
        );

        latch.lockUp();
        tcpClient.disconnect();
        latch.await();

        Assert.assertEquals(
            serverSentMessage,
            SERVER_OPEN + SERVER_RECEIVE + SERVER_RECEIVE + SERVER_RECEIVE + SERVER_RECEIVE + SERVER_CLOSE
        );

        tcpServer.stop();
    }

    @Test
    public void testOpenClose() throws Exception {
        SocketAddress address = BSocket.availableAddress();
        System.out.println("address: " + address);
        int clientCount = 1000;
        List<TcpClient> nioClients = new LinkedList<>();
        for (int i = 0; i < clientCount; i++) {
            TcpClient tcpClient = TcpClient.nioClient(address);
            nioClients.add(tcpClient);
        }
        List<TcpClient> bioClients = new LinkedList<>();
        for (int i = 0; i < clientCount; i++) {
            TcpClient tcpClient = TcpClient.bioClient(address);
            bioClients.add(tcpClient);
        }
        testClientClose0(address, nioClients);
        testClientClose0(address, bioClients);

        //address = new InetSocketAddress(18088);
        nioClients = new LinkedList<>();
        for (int i = 0; i < clientCount; i++) {
            TcpClient tcpClient = TcpClient.nioClient(address);
            nioClients.add(tcpClient);
        }
        bioClients = new LinkedList<>();
        for (int i = 0; i < clientCount; i++) {
            TcpClient tcpClient = TcpClient.bioClient(address);
            bioClients.add(tcpClient);
        }
        testServerClose0(address, nioClients);
        testServerClose0(address, bioClients);
    }

    private void testClientClose0(
        SocketAddress address,
        Collection<TcpClient> tcpClients
    ) throws Exception {

        AtomicInteger openCount = new AtomicInteger(0);
        AtomicInteger closeCount = new AtomicInteger(0);
        AtomicInteger receiveCount = new AtomicInteger(0);
        RunLatch latch = RunLatch.newRunLatch(0);

        latch.lockTo(tcpClients.size() * 4L);
        openCount.set(0);
        closeCount.set(0);
        receiveCount.set(0);

        TcpServer tcpServer = TcpServer.nioServer(
            address,
            new TcpListener() {
                @Override
                public void onOpen(@NotNull TcpContext context) {
                    openCount.incrementAndGet();
                    latch.lockDown();
                }

                @Override
                public void onReceive(@NotNull TcpContext context, @NotNull ByteBuffer data) {
                    receiveCount.incrementAndGet();
                    latch.lockDown();
                }

                @Override
                public void onClose(@NotNull TcpContext context) {
                    closeCount.incrementAndGet();
                    latch.lockDown();
                }
            }
        );
        tcpServer.start();

        Runner runner = AsyncRunner.INSTANCE;
        for (TcpClient tcpClient : tcpClients) {
            runner.run(() -> {
                tcpClient.connect();
                tcpClient.send("123");
                tcpClient.disconnect();
                latch.lockDown();
            });
        }
        latch.await();
        BLog.info("openCount: {}", openCount);
        BLog.info("closeCount: {}", closeCount);
        BLog.info("receiveCount: {}", receiveCount);
        Assert.assertEquals(openCount.get(), tcpClients.size());
        Assert.assertEquals(closeCount.get(), tcpClients.size());
        Assert.assertEquals(receiveCount.get(), tcpClients.size());

        tcpServer.stop();
        tcpServer.await();
    }

    private void testServerClose0(
        SocketAddress address,
        Collection<TcpClient> tcpClients
    ) throws Exception {

        AtomicInteger openCount = new AtomicInteger(0);
        AtomicInteger closeCount = new AtomicInteger(0);
        AtomicInteger receiveCount = new AtomicInteger(0);
        RunLatch latch = RunLatch.newRunLatch(0);

        latch.lockTo(tcpClients.size());
        openCount.set(0);
        closeCount.set(0);
        receiveCount.set(0);

        TcpServer tcpServer = TcpServer.nioServer(
            address,
            new TcpListener() {
                @Override
                public void onOpen(@NotNull TcpContext context) {
                }

                @Override
                public void onReceive(@NotNull TcpContext context, @NotNull ByteBuffer data) {
                    context.disconnect();
                    receiveCount.incrementAndGet();
                    latch.lockDown();
                }

                @Override
                public void onClose(@NotNull TcpContext context) {
                }
            }
        );
        tcpServer.start();

        latch.lockTo(tcpClients.size());
        openCount.set(0);
        closeCount.set(0);
        receiveCount.set(0);

        Runner runner = AsyncRunner.INSTANCE;
        for (TcpClient tcpClient : tcpClients) {
            runner.run(() -> {
                tcpClient.connect();
                tcpClient.send("123");
            });
        }
        latch.await();
        BLog.info("openCount: {}", openCount);
        BLog.info("closeCount: {}", closeCount);
        BLog.info("receiveCount: {}", receiveCount);
        Assert.assertEquals(openCount.get(), 0);
        Assert.assertEquals(closeCount.get(), 0);
        Assert.assertEquals(receiveCount.get(), tcpClients.size());

        for (TcpClient tcpClient : tcpClients) {
            InputStream inputStream = tcpClient.receive();
            Assert.assertEquals(inputStream.read(), -1);
            ByteBuffer buffer = ByteBuffer.allocate(999);
            int bufferCount = tcpClient.receive(buffer);
            Assert.assertEquals(bufferCount, -1);
        }

        tcpServer.stop();
        tcpServer.await();
    }

    private static final class ServerHandler implements TcpListener {

        private final RunLatch latch;

        public ServerHandler(RunLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onOpen(@NotNull TcpContext context) {
            serverSentMessage += SERVER_OPEN;
            context.send(SERVER_OPEN.getBytes());
            BLog.info("Server open: {}", context.getRemoteAddress());
        }

        @Override
        public void onReceive(@NotNull TcpContext context, @NotNull ByteBuffer data) {
            String read = BBuffer.getString(data);
            serverSentMessage += SERVER_RECEIVE;
            serverReceivedMessage += read;
            context.send(SERVER_RECEIVE.getBytes());
            BLog.info("Server receive from {}: {}", context.getRemoteAddress(), read);
            latch.lockDown();
        }

        @Override
        public void onClose(@NotNull TcpContext context) {
            serverSentMessage += SERVER_CLOSE;
            //context.write(SERVER_CLOSE.getBytes());
            BLog.info("Server close: {}", context.getRemoteAddress());
            latch.lockDown();
        }
    }
}
