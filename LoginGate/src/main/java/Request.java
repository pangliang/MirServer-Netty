import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

/**
 * Created by liangwei on 16/2/16.
 */
public class Request {
    public static final int DEFAULT_HEADER_SIZE = 16;

    public final byte cmdIndx;  // #号后面紧跟的序号, 响应包的序号要跟请求一直
    public final int recog;     // 未知
    public final short type;      // 请求类型
    public final short p1;        //
    public final short p2;
    public final short p3;
    public final String body;

    public Request(byte cmdIndx, int recog, short type, short p1, short p2, short p3, String body) {
        this.cmdIndx = cmdIndx;
        this.recog = recog;
        this.type = type;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Request{" +
                "cmdIndx=" + cmdIndx +
                ", recog=" + recog +
                ", type=" + type +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                ", body='" + body + '\'' +
                '}';
    }

    public static class WrongFormatException extends Exception{
        public WrongFormatException(String msg){
            super(msg);
        }
    };

}
