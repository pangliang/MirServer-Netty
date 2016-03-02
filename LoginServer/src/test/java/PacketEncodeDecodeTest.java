import com.zhaoxiaodan.mirserver.network.Bit6Coder;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.debug.MyLoggingHandler;
import com.zhaoxiaodan.mirserver.network.decoder.ClientPacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.ServerPacketEncoder;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PacketEncodeDecodeTest {
	Map<String, Packet> testList = new HashMap<String, Packet>() {
		{
			try {
//				create("#<<<<<KT=<<<<<<<<!",new Packet(Protocol.SM_ID_NOTFOUND));

				put("#2<<<<<KAQ<<<<<<<<<<<<<<<<<<<WBa]WbXmGrmeTRucHNxrTo@mIbTnUO<qIoa^IOY^URPmHOQ]HbHsUO<uJ?`pJ<!",new Packet(Protocol.SM_PASSOK_SELECTSERVER));
//				create("#2<<<<<I@C<<<<<<<<HODoGo@nHl!", new ClientPacket.CM_IDPASSWORD());
//				create("#2<<<<<I@C<<<<<<<<HODoI?PrInxmH_HpIOTs!",new ClientPacket.CM_IDPASSWORD());
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
					new MyLoggingHandler(MyLoggingHandler.Type.Both),
					new ClientPacketDecoder()
			);

			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(msg.getBytes());

			ch.writeInbound(buf);
			ch.finish();

			Packet req = ch.readInbound();

			Packet t = testList.get(msg);
			Assert.assertEquals(t.protocol, req.protocol);

			ch = new EmbeddedChannel(
					new MyLoggingHandler(MyLoggingHandler.Type.Both),
					new MyLoggingHandler(MyLoggingHandler.Type.Both),
					new ServerPacketEncoder()
			);

			ch.writeOutbound(req);
			ch.finish();

			ByteBuf out = ch.readOutbound();

			String decodeStr = out.toString(CharsetUtil.UTF_8);

			Assert.assertEquals(msg,decodeStr);
		}
	}

	@Test
	public void mytest()  throws Exception{
		String            data    = "#2<<<<<KAQ<<<<<<<<<<<<<<<<<<<WBa]WbXmGrmeTRucHNxrTo@mIbTnUO<qIoa^IOY^URPmHOQ]HbHsUO<uJ?`pJ<!";

		System.out.println(new String(Bit6Coder.decode6BitBuf("WBa]WbXmGrmeTRucHNxrTo@mIbTnUO<qIoa^IOY^URPmHOQ]HbHsUO<uJ?`pJ<".getBytes())));
	}
}
