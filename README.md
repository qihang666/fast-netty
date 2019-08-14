# fast-netty
java服务器快速上手的网络IO框架，基于netty， google protobuf 数据传输协议，disruptor队列，zookeeper服务注册发现，用于服务器间长连接通讯，可搭建分布式服务器以及分布式游戏。

# Server
<pre>
<code>
public class DemoServer extends TcpServer {
       
      	public static void main(String[] args) {
		                                       
            MessageDispatcher.register(DomeServerHandler.class);
		
            new DemoServer().listen(9000).start();
		
       }
}
    </code>
</pre>
# client
<pre>
<code>
public class DemoClient extends TcpClient {

	public DemoClient(String hosts, int port) {
		super(hosts, port);
	}
	
  public static void main(String[] args) {
     
        MessageDispatcher.register(DomeClientHandler.class);
		 
        DemoClient client = new DemoClient("127.0.0.1",9000);
	   
        client.start();
        
	      Protobuf.TestData.Builder data = Protobuf.TestData.newBuilder();
	      data.setName("我是客户端");
		 
        client.writeAndFlush(new Message((short)11,data.build().toByteArray()));
    
  }

   </code>
</pre>
