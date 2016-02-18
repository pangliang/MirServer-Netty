import com.zhaoxiaodan.mirserver.core.Config;
import com.zhaoxiaodan.mirserver.core.network.SocketMessage;
import com.zhaoxiaodan.mirserver.core.network.decoder.Bit6BufDecoder;
import com.zhaoxiaodan.mirserver.core.network.decoder.RequestDecoder;
import com.zhaoxiaodan.mirserver.core.network.encoder.Bit6BufEncoder;
import com.zhaoxiaodan.mirserver.core.network.encoder.RequestEncoder;
import com.zhaoxiaodan.mirserver.logingate.LoginGateProtocols;
import com.zhaoxiaodan.mirserver.logingate.decoder.ProcessRequestDecoder;
import com.zhaoxiaodan.mirserver.logingate.request.LoginRequest;
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
public class RequestEncodeDecodeTest {


	Map<String, SocketMessage> testList = new HashMap<String, SocketMessage>() {
		{
			try {
				put("#2<<<<<I@C<<<<<<<<HODoGo@nHl!", new LoginRequest((byte) 2, "123", "123"));
				put("#2<<<<<I@C<<<<<<<<HODoI?PrInxmH_HpIOTs<!",new LoginRequest((byte) 2, "1234567", "1234567"));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	@Test
	public void testDecode() {
		for (String msg : testList.keySet()) {
			EmbeddedChannel ch = new EmbeddedChannel(
					new LoggingHandler(LogLevel.INFO),
					new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
					new ProcessRequestDecoder(CharsetUtil.UTF_8),
					new Bit6BufDecoder(true),
					new LoggingHandler(LogLevel.INFO),
					new RequestDecoder(new LoginGateProtocols())
			);

			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(msg.getBytes());

			ch.writeInbound(buf);
			ch.finish();

			SocketMessage req = ch.readInbound();

			ch = new EmbeddedChannel(
					new LoggingHandler(LogLevel.INFO),
					new Bit6BufEncoder(true),
					new LoggingHandler(LogLevel.INFO),
					new RequestEncoder()
			);

			ch.writeOutbound(testList.get(msg));
			ch.finish();

			ByteBuf out = ch.readOutbound();

			ch = new EmbeddedChannel(
					new LoggingHandler(LogLevel.INFO),
					new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
					new ProcessRequestDecoder(CharsetUtil.UTF_8),
					new Bit6BufDecoder(true),
					new LoggingHandler(LogLevel.INFO),
					new RequestDecoder(new LoginGateProtocols())
			);

			ch.writeInbound(out);
			ch.finish();

			Assert.assertEquals(req, ch.readInbound());
		}
	}

	@Test
	public void testEncode() {
		for (String msg : testList.keySet()) {
			EmbeddedChannel ch = new EmbeddedChannel(
					new LoggingHandler(LogLevel.INFO),
					new Bit6BufEncoder(true),
					new LoggingHandler(LogLevel.INFO),
					new RequestEncoder()
			);

			ch.writeOutbound(testList.get(msg));
			ch.finish();

			ByteBuf out = ch.readOutbound();


			ch = new EmbeddedChannel(
					new LoggingHandler(LogLevel.INFO),
					new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
					new ProcessRequestDecoder(CharsetUtil.UTF_8),
					new Bit6BufDecoder(true),
					new LoggingHandler(LogLevel.INFO),
					new RequestDecoder(new LoginGateProtocols())
			);

			ch.writeInbound(out);
			ch.finish();

			Assert.assertEquals(testList.get(msg), ch.readInbound());


		}
	}
}
