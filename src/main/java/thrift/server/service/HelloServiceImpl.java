package thrift.server.service;

import org.apache.thrift.TException;

public class HelloServiceImpl implements HelloService.Iface {
    @Override
    public String sayHello(User user) throws TException {
        return "hello," + user.getName() + user.getEmail();
    }
}
