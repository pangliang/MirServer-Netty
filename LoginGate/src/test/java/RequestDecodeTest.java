import com.zhaoxiaodan.mirserver.logingate.Config;
import com.zhaoxiaodan.mirserver.logingate.decoder.Bit6BufDecoder;
import com.zhaoxiaodan.mirserver.logingate.decoder.RequestDecoder;
import com.zhaoxiaodan.mirserver.logingate.request.Protocol;
import com.zhaoxiaodan.mirserver.logingate.request.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangwei on 16/2/17.
 */
public class RequestDecodeTest {
	EmbeddedChannel ch = new EmbeddedChannel(
			new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
			new Bit6BufDecoder(),
			new RequestDecoder(CharsetUtil.UTF_8));

	Map<String, Request> testList = new HashMap<String, Request>() {
		{
			put("#2<<<<<I@C<<<<<<<<HO@mHO@nH^xoHoHoHoHoHoHo!", new Request(Protocol.CM_IDPASSWORD, (byte) 2, "1111122/333333333"));
		}
	};


	@Test
	public void testDecode() {
		for (String msg : testList.keySet()) {
			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(msg.getBytes());

			ch.writeInbound(buf);
			ch.finish();

			Request req = ch.readInbound();

			Assert.assertEquals(req, testList.get(msg));
		}
	}
}
