import com.zhaoxiaodan.mirserver.gameserver.entities.Config
import com.zhaoxiaodan.mirserver.gameserver.entities.Player
import com.zhaoxiaodan.mirserver.gameserver.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.gameserver.objects.BaseObject
import com.zhaoxiaodan.mirserver.gameserver.types.Color
import com.zhaoxiaodan.mirserver.gameserver.GameServerPackets
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket
import com.zhaoxiaodan.mirserver.network.Protocol

class 烈火剑法 extends 默认技能 {

    public int useMagic(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {
        player.metaClass.liehuoFlag = false;
        if (!player.liehuoFlag)
            return 0;

        player.liehuoFlag = false;
        ServerPacket packet = new ServerPacket(player.inGameId, Protocol.SM_FIREHIT, player.currMapPoint.x, (short) player.currMapPoint.y, (short) player.direction.ordinal());
        player.broadcast(packet);
        return power;
    }

    public void spell(Player player, PlayerMagic playerMagic) {

        if (!super.chackMp(player, playerMagic))
            return;

        player.metaClass.liehuoFlag = false;
        if (!player.checkLastActionTime("烈火剑法", (int)(Config.OBJECT_SPEED_BASE / 3), 0)) {
            player.liehuoFlag = false;
            return;
        }

        player.liehuoFlag = true;
        player.session.sendPacket(new GameServerPackets.ActionStatus("+FIR"));
        player.sendSysMsg("你的武器因烈火剑法而炙热", Color.White, Color.Blue);
    }
}
