package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author zhjk on 2017/3/22.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private int counter;
    private String separator = System.getProperty("line.separator");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*
        原来的写法
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8").substring(0,req.length-separator.length());
        */
        //现在的写法
        String body = (String)msg;
        System.out.println("The Time Server recv order : " + body + "; counter = " + ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        currentTime += separator;
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
