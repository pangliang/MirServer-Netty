package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.LoginClientPackets;
import com.zhaoxiaodan.mirserver.loginserver.LoginServerPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class SelectCharacterHandler extends UserHandler {

    @Override
    public void onPacket(ClientPacket packet, User user) throws Exception {

        LoginClientPackets.SelectCharacter request = (LoginClientPackets.SelectCharacter) packet;

        if (null == user.selectServer) {
            session.sendPacket(new ServerPacket(Protocol.SM_STARTFAIL));
            return;
        }

        for (Player player : user.players) {
            if (player.name.equals(request.characterName)) {
                session.sendPacket(new LoginServerPackets.StartPlay(user.selectServer.gameServerIp, user.selectServer.gameServerPort));
                return;
            }
        }

        session.sendPacket(new ServerPacket(Protocol.SM_STARTFAIL));
        return;
    }

}
