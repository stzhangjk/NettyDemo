package client;

import handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author zhjk on 2017/3/22.
 */
public class EchoClient {
    public static void main(String[] args){
        String host = "localhost";
        int port = 45678;
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new EchoClientHandler());
                }
            });

            ChannelFuture f = b.connect(host,port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
