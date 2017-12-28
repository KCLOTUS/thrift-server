package thrift.server.invoker;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import thrift.server.service.HelloService;
import thrift.server.service.HelloServiceImpl;
import thrift.server.service.User;

/**
 * 启动服务
 *
 * @author flytoyou
 * @version 1.0.0
 */
public class SimpleInvoker {

    public void startServer() throws TTransportException {
        //创建processor
        TProcessor tProcessor = new HelloService.Processor<HelloService.Iface>(new HelloServiceImpl());
        //服务端口
        int port = 8091;
        //创建transport 阻塞通信
        TServerSocket serverTransport =  new TServerSocket(port);
        //创建protocol
        TBinaryProtocol.Factory protocol = new TBinaryProtocol.Factory();
        //将processor transport protocol 设置到服务器server中
        TServer.Args args = new TServer.Args(serverTransport);
        args.processor(tProcessor);
        args.protocolFactory(protocol);
        //定义服务器类型设定参数
        TServer server = new TSimpleServer(args);
        //开启服务
        server.serve();
    }

    /**
     * 客户端调用服务端
     */
    public void startClient() throws TException {
        String ip ="127.0.0.1";
        int port = 8091;
        int timeOut = 1000;
        //创建Transport
        TTransport transport  = new TSocket(ip, port, timeOut);
        //创建TProtocol
        TProtocol protocol = new TBinaryProtocol(transport);
        //基于TTransport和TProtocol创建Client
        HelloService.Client client = new HelloService.Client(protocol);
        transport.open();

        //调用client方法
        User user = new User();
        user.setName("flytoyou");
        user.setEmail("g15179156337@gmail.com");
        String content = client.sayHello(user);
        System.out.println("content:"+content);
    }
}
