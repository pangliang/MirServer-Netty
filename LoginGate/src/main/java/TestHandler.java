import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * Created by liangwei on 16/2/16.
 */
public class TestHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx)
    {
        System.out.println(ctx.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Request req = (Request)msg;
        System.out.println(this.getClass().getName() + " Read >> " + req);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
