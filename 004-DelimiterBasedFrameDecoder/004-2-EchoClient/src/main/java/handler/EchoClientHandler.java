package handler;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zhjk on 2017/3/22.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private int counter;
    private static final String message = "hi hi hi$_";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = null;
        for(int i=0;i<10;i++){
            buf = Unpooled.buffer(message.length());
            buf.writeBytes(message.getBytes());
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String resp = (String)msg;
        System.out.println("client recv : " + resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
