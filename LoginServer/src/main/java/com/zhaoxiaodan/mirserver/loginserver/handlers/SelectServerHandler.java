package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.LoginClientPackets;
import com.zhaoxiaodan.mirserver.loginserver.LoginServerPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

import java.util.List;
import java.util.Random;

public class SelectServerHandler extends UserHandler {

    @Override
    public void onPacket(ClientPacket packet, User user) throws Exception {
        LoginClientPackets.SelectServer selectServer = (LoginClientPackets.SelectServer) packet;

        List<ServerInfo> list = DB.query(ServerInfo.class);//, Restrictions.eq("name", selectServer.serverName));
        if (1 != list.size()) {
            session.sendPacket(new ServerPacket(Protocol.SM_ID_NOTFOUND));
            return;
        } else {
            ServerInfo info = list.get(0);

            user.certification = (byte) new Random().nextInt(200);
            user.selectServer = info;
            DB.update(user);

            session.sendPacket(new LoginServerPackets.SelectServerOk(info.loginServerIp, info.loginServerPort, user.certification));
        }
    }

}
