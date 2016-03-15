import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
import com.zhaoxiaodan.mirserver.db.types.Color
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine
import com.zhaoxiaodan.mirserver.network.Protocol
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket

class 刺杀剑术 extends 默认技能 {

    public int useMagic(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {
        player.metaClass.chishaFlag = false;

        if (!player.chishaFlag) {
            player.sendSysMsg("刺杀剑术还未开启, 不能使用 !!", Color.White, Color.Blue);
        }
        if (!super.chackMp(player, playerMagic))
            return 0;

        ServerPacket packet = new ServerPacket(player.inGameId, Protocol.SM_LONGHIT, player.currMapPoint.x, (short) player.currMapPoint.y, (short) player.direction.ordinal());
        player.broadcast(packet);

        MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(player.currMapPoint.mapId);
        targets.addAll(mapInfo.getObjectsOnLine(player.currMapPoint, player.direction, 2, 1));
        return 0;
    }

    public void spell(Player player, PlayerMagic playerMagic) {
        player.metaClass.chishaFlag = false;
        if (player.chishaFlag) {
            player.chishaFlag = false;
            player.session.sendPacket(new ServerPacket.ActionStatus("+ULNG"));
            player.sendSysMsg("刺杀剑术关闭...", Color.White, Color.Blue);
        } else {
            player.chishaFlag = true;
            player.session.sendPacket(new ServerPacket.ActionStatus("+LNG"));
            player.sendSysMsg("刺杀剑术开启...", Color.White, Color.Blue);
        }
    }
}
