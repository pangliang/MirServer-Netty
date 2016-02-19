package com.zhaoxiaodan.mirserver.core.network;

import com.zhaoxiaodan.mirserver.db.entities.User;
import io.netty.buffer.ByteBuf;

/**
 * Created by liangwei on 16/2/19.
 */
//TODO client 包现在写服务器部分,只实现读, 不管写
//TODO 按道理这里是网络封包模块不应该关联 DB 的 DAO, 当时包含的属性其实基本一致; 偷懒就这么用了
public class ClientPackets {

	public static final class Process extends IndexPacket {

		public Process(byte cmdIndex) {
			super(Protocol.CM_PROTOCOL.id, cmdIndex);
		}
	}

	public static final class Login extends IndexPacket {

		private static final char PARAM_SPLIT_CHAR = '/';

		public User user;

		@Override
		public void readPacket(ByteBuf in) {
			super.readPacket(in);

			user = new User();
			String remain = readString(in);
			int    pos    = 0;
			if (remain != null && (pos = remain.indexOf(PARAM_SPLIT_CHAR)) > 0) {
				user.userId = remain.substring(0, pos);
				user.password = remain.substring(pos + 1);
			}
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(user.userId.getBytes());
			out.writeByte('/');
			out.writeBytes(user.password.getBytes());
		}
	}

	public static final class NewUser extends IndexPacket {

		public User user;

		@Override
		public void readPacket(ByteBuf in) {
			super.readPacket(in);

			user = new User();
			user.userId = readString(in);
			user.password = readString(in);
			user.username = readString(in);

		}
	}
}
