package server;

import handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;


/**
 * @author zhjk on 2017/3/22.
 */
public class EchoServer {
    private int port = 45678;

    public void startUp(){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss,worker);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            });
            b.option(ChannelOption.SO_BACKLOG,128);
            b.childOption(ChannelOption.SO_KEEPALIVE,true);

            ChannelFuture f = b.bind(port);
            f.sync();

            f.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        new EchoServer().startUp();
    }
}
