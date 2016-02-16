import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by liangwei on 16/2/16.
 */
public class RequestDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final Charset charset;

    public RequestDecoder(Charset charset)
    {
        this.charset = charset;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int size = in.readableBytes();
        if(size < Request.DEFAULT_HEADER_SIZE + 2)
            throw new Request.WrongFormatException("packet size < " + Request.DEFAULT_HEADER_SIZE + 2);

        if('#' != in.getByte(0) || '!' != in.getByte(size - 1))
            throw new Request.WrongFormatException("start or end flag not found");

        in = in.order(ByteOrder.LITTLE_ENDIAN);

        byte sFlag = in.readByte();

        byte cmdIndex = (byte)(in.readByte() - '0');
        int recog = in.readInt();
        short type = in.readShort();
        short p1 = in.readShort();
        short p2 = in.readShort();
        short p3 = in.readShort();
        String body=in.toString(in.readerIndex(), in.readableBytes()-1,charset).trim();

        out.add(new Request(cmdIndex,recog,type,p1,p2,p3,body));
    }
}
