package serializer;

import thrift.server.invoker.NonblockingInvoker;

public class ThriftSerializer {

    public static void main(String[] args) throws Exception{
        NonblockingInvoker nonblockingInvoker = new NonblockingInvoker();
        nonblockingInvoker.startCilent();
    }

}
