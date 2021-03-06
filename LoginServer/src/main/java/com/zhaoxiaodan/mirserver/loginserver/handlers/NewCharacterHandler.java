package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.LoginClientPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

/**
 * SM_NEWCHR_FAIL: begin
 * case Msg.Recog of
 * 0: FrmDlg.DMessageDlg('[错误信息] 输入的角色名称包含非法字符！ 错误代码 = 0', [mbOk]);
 * 2: FrmDlg.DMessageDlg('[错误信息] 创建角色名称已被其他人使用！ 错误代码 = 2', [mbOk]);
 * 3: FrmDlg.DMessageDlg('[错误信息] 您只能创建二个游戏角色！ 错误代码 = 3', [mbOk]);
 * 4: FrmDlg.DMessageDlg('[错误信息] 创建角色时出现错误！ 错误代码 = 4', [mbOk]);
 * else FrmDlg.DMessageDlg('[错误信息] 创建角色时出现未知错误！', [mbOk]);
 * end;
 * end;
 */
public class NewCharacterHandler extends UserHandler {

	@Override
	public void onPacket(ClientPacket packet, User user) throws Exception {
		LoginClientPackets.NewCharacter request = (LoginClientPackets.NewCharacter) packet;
		if (user.players.size() >= 2) {
			session.sendPacket(new ServerPacket(Protocol.SM_NEWCHR_FAIL));
			return;
		}
		try {
			Player player = request.player;
			player.user = user;
			DB.save(player);
			user.players.add(player);
			session.sendPacket(new ServerPacket(Protocol.SM_NEWCHR_SUCCESS));
		} catch (Exception e) {
			session.sendPacket(new ServerPacket(Protocol.SM_NEWCHR_FAIL));
			throw e;
		}
	}
}
