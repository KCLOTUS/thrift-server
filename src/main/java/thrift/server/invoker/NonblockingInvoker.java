package thrift.server.invoker;

import org.apache.thrift.TProcessor;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.*;
import thrift.server.service.HelloService;
import thrift.server.service.HelloServiceImpl;
import thrift.server.service.User;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 非阻塞I/O服务调用示例
 *
 * @author flytoyou
 * @version 1.0.0
 */
public class NonblockingInvoker {

    public static void main(String[] args) {

    }

    /**
     * 启动服务
     */
    public void startServer() throws TTransportException{
        int port = 8091;
        //创建processor
        TProcessor tProcessor = new HelloService.Processor<HelloService.Iface>(new HelloServiceImpl());
        //创建transport非阻塞nonblocking
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
        //创建protocol数据传输协议
        TCompactProtocol.Factory protocol = new TCompactProtocol.Factory();
        //创建transport数据传输方式，非阻塞需要这种方式传输
        TFramedTransport.Factory transport = new TFramedTransport.Factory();
        TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);
        args.processor(tProcessor);
        args.transportFactory(transport);
        args.protocolFactory(protocol);
        //创建服务器，类型是非阻塞
        TServer server = new TNonblockingServer(args);
        //开启服务
        server.serve();
    }

    /**
     * 客户端调用服务端
     */
    public void startCilent() throws Exception{
        String ip = "127.0.0.1";
        int port = 8091;
        int timeOut = 1000;

        TAsyncClientManager clientManager = new TAsyncClientManager();
        TNonblockingTransport transport = new TNonblockingSocket(ip,port,timeOut);
        TProtocolFactory tProtocol = new TCompactProtocol.Factory();
        HelloService.AsyncClient asyncClient = new HelloService.AsyncClient(tProtocol,clientManager,transport);
        //客户端异步调用
        User user = new User();
        user.setName("flytoyou");
        user.setEmail("g15179156337@gmail.com");
        CountDownLatch latch = new CountDownLatch(1);
        //设置回调接口实现
        //AsynInvokerCallback callback = new AsynInvokerCallback(latch);
        AsyncMethodCallback callback = new AsynInvokerCallback(latch);
        asyncClient.sayHello(user,callback);
        //等待调用返回
        latch.await(5, TimeUnit.SECONDS);
    }

}
