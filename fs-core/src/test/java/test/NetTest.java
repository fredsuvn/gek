package test;

import org.testng.Assert;
import org.testng.annotations.Test;
import xyz.srclab.annotations.Nullable;
import xyz.srclab.common.base.Fs;
import xyz.srclab.common.base.FsLogger;
import xyz.srclab.common.data.FsData;
import xyz.srclab.common.io.FsIO;
import xyz.srclab.common.net.*;
import xyz.srclab.common.net.handlers.LengthBasedTcpChannelHandler;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NetTest {

    @Test
    public void testTcp() {
        testTcp0(6, 5, 10);
        testTcp0(1024, 8, 20);
        testTcp0(22, 8, 50);
    }

    private void testTcp0(int bufferSize, int serverThreads, int clientThreads) {
        Map<String, AtomicInteger> data = new ConcurrentHashMap<>();

        //server: hlo
        //client: hlo
        //client: abc * 10
        //server: qwe * 10
        //client: bye
        //server bye

        FsTcpServer server = FsTcpServer.newBuilder()
            .channelBufferSize(bufferSize)
            .executor(Executors.newFixedThreadPool(serverThreads))
            .serverHandler(new FsTcpServerHandler() {
                @Override
                public void onException(FsNetServerException exception) {
                    FsLogger.defaultLogger().info("server.onException: ", exception);
                }

                @Override
                public void onOpen(FsTcpChannel channel) {
                    channel.send(FsData.wrap("hlo"));
                    TestUtil.count("hlo", data);
                    channel.flush();
                    TestUtil.count("server-onOpen", data);
                }

                @Override
                public void onClose(FsTcpChannel channel, ByteBuffer buffer) {
                    TestUtil.count("server-onClose", data);
                }

                @Override
                public void onException(FsTcpChannel channel, Throwable throwable, ByteBuffer buffer) {
                    TestUtil.count("server-channel.onException", data);
                    FsLogger.defaultLogger().info("server-channel.onException: ", throwable);
                }
            })
            .addChannelHandler(new LengthBasedTcpChannelHandler(3))
            .addChannelHandler(new FsTcpChannelHandler<List<ByteBuffer>>() {
                @Override
                public @Nullable Object onMessage(FsTcpChannel channel, List<ByteBuffer> message) {
                    for (ByteBuffer buffer : message) {
                        String str = FsIO.getString(buffer);
                        TestUtil.count(str, data);
                        switch (str) {
                            case "abc": {
                                channel.sendAndFlush(FsData.wrap("qwe"));
                                break;
                            }
                            case "bye": {
                                channel.sendAndFlush(FsData.wrap("bye"));
                                //channel.flush();
                                channel.closeNow();
                                break;
                            }
                        }
                    }
                    //channel.flush();
                    return null;
                }
            })
            .build();
        server.start(false);
        CountDownLatch latch = new CountDownLatch(clientThreads);
        List<FsTcpClient> clients = new LinkedList<>();
        for (int i = 0; i < clientThreads; i++) {
            FsTcpClient client = FsTcpClient.newBuilder()
                .channelBufferSize(bufferSize)
                .clientHandler(new FsTcpClientHandler() {
                    @Override
                    public void onOpen(FsTcpChannel channel) {
                        TestUtil.count("client-onOpen", data);
                    }

                    @Override
                    public void onClose(FsTcpChannel channel, ByteBuffer buffer) {
                        TestUtil.count("client-onClose", data);
                    }

                    @Override
                    public void onException(FsTcpChannel channel, Throwable throwable, ByteBuffer buffer) {
                        TestUtil.count("client-channel.onException", data);
                        FsLogger.defaultLogger().info("client-channel.onException: ", throwable);
                    }
                })
                .addChannelHandler(new LengthBasedTcpChannelHandler(3))
                .addChannelHandler(new FsTcpChannelHandler<List<ByteBuffer>>() {
                    @Override
                    public @Nullable Object onMessage(FsTcpChannel channel, List<ByteBuffer> message) {
                        for (ByteBuffer buffer : message) {
                            String str = FsIO.getString(buffer);
                            TestUtil.count(str, data);
                            switch (str) {
                                case "hlo": {
                                    new Thread(() -> {
                                        channel.sendAndFlush(FsData.wrap("a"));
                                        Fs.sleep(200);
                                        channel.sendAndFlush(FsData.wrap("bc"));
                                        Fs.sleep(200);
                                        channel.sendAndFlush(FsData.wrap("abc"));
                                        Fs.sleep(200);
                                        channel.sendAndFlush(FsData.wrap("ab"));
                                        Fs.sleep(200);
                                        channel.sendAndFlush(FsData.wrap("ca"));
                                        Fs.sleep(200);
                                        channel.sendAndFlush(FsData.wrap("bca"));
                                        Fs.sleep(200);
                                        channel.sendAndFlush(FsData.wrap("bc"));
                                        Fs.sleep(200);
                                        channel.sendAndFlush(FsData.wrap("abcabcabcabcabc"));
                                        Fs.sleep(500);
                                        channel.sendAndFlush(FsData.wrap("bye"));
                                    }).start();
                                    break;
                                }
                                case "bye": {
                                    //channel.flush();
                                    channel.closeNow();
                                    break;
                                }
                            }
                        }
                        channel.flush();
                        return null;
                    }
                })
                .build();
            clients.add(client);
        }
        for (FsTcpClient client : clients) {
            new Thread(() -> {
                try {
                    client.start("localhost", server.getPort());
                } catch (Exception e) {
                    System.out.println(e);
                }
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        server.close();
        //server: hlo
        //client: hlo
        //client: abc * 10
        //server: qwe * 10
        //client: bye
        //server bye
        Assert.assertEquals(data.get("server-onOpen").get(), clientThreads);
        Assert.assertEquals(data.get("client-onOpen").get(), clientThreads);
        Assert.assertEquals(data.get("server-onClose").get(), clientThreads);
        Assert.assertEquals(data.get("client-onClose").get(), clientThreads);
        Assert.assertEquals(data.get("hlo").get(), clientThreads * 2);
        Assert.assertEquals(data.get("bye").get(), clientThreads * 2);
        Assert.assertEquals(data.get("abc").get(), clientThreads * 10);
        Assert.assertEquals(data.get("qwe").get(), clientThreads * 10);
    }
}
