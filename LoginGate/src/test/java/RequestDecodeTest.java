import com.zhaoxiaodan.mirserver.core.Config;
import com.zhaoxiaodan.mirserver.core.SocketMessage;
import com.zhaoxiaodan.mirserver.core.decoder.Bit6BufDecoder;
import com.zhaoxiaodan.mirserver.core.decoder.RequestDecoder;
import com.zhaoxiaodan.mirserver.logingate.LoginGateProtocols;
import com.zhaoxiaodan.mirserver.logingate.decoder.ProcessRequestDecoder;
import com.zhaoxiaodan.mirserver.logingate.request.LoginRequest;
import com.zhaoxiaodan.mirserver.logingate.request.ProcessRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangwei on 16/2/17.
 */
public class RequestDecodeTest {


	Map<String, SocketMessage> testList = new HashMap<String, SocketMessage>() {
		{
			try {
				put("#1PROCESS!", new ProcessRequest((byte) 1));
				put("#2<<<<<I@C<<<<<<<<HO@mHO@nH^xoHoHoHoHoHoHo!", new LoginRequest((byte) 2, "1111122", "333333333"));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	@Test
	public void testDecode() {
		for (String msg : testList.keySet()) {
			EmbeddedChannel ch = new EmbeddedChannel(new LoggingHandler(LogLevel.INFO), new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})), new ProcessRequestDecoder(CharsetUtil.UTF_8), new Bit6BufDecoder(), new LoggingHandler(LogLevel.INFO), new RequestDecoder(new LoginGateProtocols()));

			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(msg.getBytes());

			ch.writeInbound(buf);
			ch.finish();

			SocketMessage req = ch.readInbound();

			Assert.assertEquals(req, testList.get(msg));
		}
	}
}
