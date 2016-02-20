package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.PacketHandler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 *        +-------------------------------------------------+
          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 +--------+-------------------------------------------------+----------------+
 |00000000| 23 31 3c 3c 3c 3c 3c 49 44 43 3c 3c 3c 3c 3c 3c |#1<<<<<IDC<<<<<<|
 |00000010| 3c 3c 3d 63 51 6f 55 53 45 65 55 3c 3c 3c 3c 3c |<<=cQoUSEeU<<<<<|
 |00000020| 3c 44 58 42 41 6f 58 73 59 6b 58 62 4c 3c 3c 3c |<DXBAoXsYkXbL<<<|
 |00000030| 5d 71 58 72 51 6e 57 62 41 69 55 4c 3c 3c 3c 3c |]qXrQnWbAiUL<<<<|
 |00000040| 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 74 72 49 4f |<<<<<<<<<<<<trIO|
 |00000050| 3c 6d 48 3f 40 69 48 4f 4c 71 49 4f 40 6d 48 4c |<mH?@iHOLqIO@mHL|
 |00000060| 5c 70 48 3f 3c 6d 4a 3f 5c 74 4a 3c 3c 3c 3c 3c |\pH?<mJ?\tJ<<<<<|
 |00000070| 3c 3c 3c 3c 45 6d 48 4c 3c 3c 3c 3c 3c 3c 3c 3c |<<<<EmHL<<<<<<<<|
 |00000080| 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c |<<<<<<<<<<<<<<<<|
 |00000090| 45 5d 48 4c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c |E]HL<<<<<<<<<<<<|
 |000000a0| 3c 4c 58 42 41 6a 55 72 6d 65 54 52 75 63 4c 43 |<LXBAjUrmeTRucLC|
 |000000b0| 41 6d 47 62 49 6b 57 4c 3c 3c 3c 3c 3c 3c 3c 3c |AmGbIkWL<<<<<<<<|
 |000000c0| 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c |<<<<<<<<<<<<<<<<|
 |000000d0| 3c 3c 3c 3c 3c 3c 3c 3c 3c 63 40 6e 3c 3c 3c 3c |<<<<<<<<<c@n<<<<|
 |000000e0| 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c |<<<<<<<<<<<<<<<<|
 |000000f0| 3c 3c 3c 3c 3c 62 40 6e 3c 3c 3c 3c 3c 3c 3c 3c |<<<<<b@n<<<<<<<<|
 |00000100| 3c 3c 3c 3c 3c 3c 64 6d 4a 4f 5c 74 47 6f 3c 6d |<<<<<<dmJO\tGo<m|
 |00000110| 47 6f 3c 6d 3e 6f 40 6f 4a 3f 5c 74 4a 3f 5c 74 |Go<m>o@oJ?\tJ?\t|
 |00000120| 4a 3f 5c 74 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c |J?\t<<<<<<<<<<<<|
 |00000130| 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c |<<<<<<<<<<<<<<<<|
 |00000140| 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c |<<<<<<<<<<<<<<<<|
 |00000150| 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 3c 21 |<<<<<<<<<<<<<<<!|
 +--------+-------------------------------------------------+----------------+

		  +-------------------------------------------------+
		  |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 +--------+-------------------------------------------------+----------------+
 |00000000| 23 31 00 00 00 00 d2 07 00 00 00 00 00 00 06 75 |#1.............u|
 |00000010| 73 65 72 69 64 00 00 00 00 08 70 61 73 73 77 6f |serid.....passwo|
 |00000020| 72 64 00 00 08 75 73 65 72 6e 61 6d 65 00 00 00 |rd...username...|
 |00000030| 00 00 00 00 00 00 00 00 00 0e 36 35 30 31 30 31 |..........650101|
 |00000040| 2d 31 34 35 35 31 31 31 08 34 30 30 31 38 38 38 |-1455111.4001888|
 |00000050| 38 00 00 00 00 00 00 02 71 31 00 00 00 00 00 00 |8.......q1......|
 |00000060| 00 00 00 00 00 00 00 00 00 00 00 00 02 61 31 00 |.............a1.|
 |00000070| 00 00 00 00 00 00 00 00 00 10 70 61 6e 67 6c 69 |..........pangli|
 |00000080| 61 6e 67 40 71 71 2e 63 6f 6d 00 00 00 00 00 00 |ang@qq.com......|
 |00000090| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |000000a0| 00 00 00 27 13 20 00 00 00 00 00 00 00 00 00 00 |...'. ..........|
 |000000b0| 00 00 00 00 00 00 00 00 26 13 20 00 00 00 00 00 |........&. .....|
 |000000c0| 00 00 00 00 00 a3 13 93 83 82 f3 03 12 f3 03 10 |................|
 |000000d0| b3 13 33 83 83 83 83 83 83 83 83 80 00 00 00 00 |..3.............|
 |000000e0| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |000000f0| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |00000100| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |00000110| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |00000120| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |00000130| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |00000140| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |................|
 |00000150| 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 21 |...............!|
 +--------+-------------------------------------------------+----------------+

 part : userid
 part : password
 part : username
 part : 650101-145511140018888
 part : q1
 part : a1
 part : pangliang@qq.com
 part : '
 part : &
 part : ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ3￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ￯﾿ﾽ

 */
public class NewUserHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.NewUser newUser = (ClientPackets.NewUser) packet;

		Session  session  = DB.getSession();
		Criteria criteria = session.createCriteria(User.class);

		List<User> list = criteria.add(Restrictions.eq("userId", newUser.user.userId)).list();
		if(list.size() > 0)
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
			return ;
		}else{
			try{
				session.save(newUser.user);
				session.flush();

				ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_SUCCESS));
			}catch (Exception e)
			{
				ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
				return ;
			}
		}
	}

}
