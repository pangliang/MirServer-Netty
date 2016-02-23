import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.debug.MyLoggingHandler;
import com.zhaoxiaodan.mirserver.network.decoder.Bit6BufDecoder;
import com.zhaoxiaodan.mirserver.network.decoder.PacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.Bit6BufEncoder;
import com.zhaoxiaodan.mirserver.network.encoder.PacketEncoder;
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
 * Created by liangwei on 16/2/19.
 */
public class PacketEncodeDecodeTest {
	Map<String, Packet> testList = new HashMap<String, Packet>() {
		{
			try {
//				put("#<<<<<KT=<<<<<<<<!",new Packet(Protocol.SM_ID_NOTFOUND));
				put("#<<<<<=@><<<<<<@<l[gOhG[cjv_MfYO>Go@k!",new Packet(Protocol.LoginSuccSelectServer));
//				put("#2<<<<<I@C<<<<<<<<HODoGo@nHl!", new ClientPackets.Login());
//				put("#2<<<<<I@C<<<<<<<<HODoI?PrInxmH_HpIOTs!",new ClientPackets.Login());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	@Test
	public void test() {
		for (String msg : testList.keySet()) {
			EmbeddedChannel ch = new EmbeddedChannel(
					new MyLoggingHandler(MyLoggingHandler.Type.Both),
					new DelimiterBasedFrameDecoder(1024, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
					new Bit6BufDecoder(false),
					new MyLoggingHandler(MyLoggingHandler.Type.Both),
					new PacketDecoder(ClientPackets.class.getCanonicalName(), false)
			);

			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(msg.getBytes());

			ch.writeInbound(buf);
			ch.finish();

			Packet req = ch.readInbound();

			Packet t = testList.get(msg);
			Assert.assertEquals(t.pid, req.pid );

			ch = new EmbeddedChannel(
					new MyLoggingHandler(MyLoggingHandler.Type.Both),
					new Bit6BufEncoder(false),
					new MyLoggingHandler(MyLoggingHandler.Type.Both),
					new PacketEncoder()
			);

			ch.writeOutbound(req);
			ch.finish();

			ByteBuf out = ch.readOutbound();

			String decodeStr = out.toString(CharsetUtil.UTF_8);

			Assert.assertEquals(msg,decodeStr);
		}
	}
}
