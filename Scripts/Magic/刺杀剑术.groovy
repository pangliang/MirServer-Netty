import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
import com.zhaoxiaodan.mirserver.db.types.Color
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine
import com.zhaoxiaodan.mirserver.network.Protocol
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket

class 刺杀剑术 extends 默认技能 {

    public int useBuffer(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {

        ServerPacket packet = new ServerPacket(player.inGameId, Protocol.SM_LONGHIT, player.currMapPoint.x, (short) player.currMapPoint.y, (short) player.direction.ordinal());
        player.broadcast(packet);

        MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(player.currMapPoint.mapId);
        targets.addAll(mapInfo.getObjectsOnLine(player.currMapPoint, player.direction, 2, 1 ));
        return 0;
    }

    public boolean onBufferOnOff(Player player, PlayerMagic playerMagic) {
        if (super.onBufferOnOff(player, playerMagic)) {
            player.session.sendPacket(new ServerPacket.ActionStatus("+LNG"));
            player.sendSysMsg("攻杀剑术开启", Color.White, Color.Blue);
        } else {
            player.session.sendPacket(new ServerPacket.ActionStatus("+ULNG"));
            player.sendSysMsg("攻杀剑术关闭", Color.White, Color.Blue);
        }
    }

    public boolean isBuffer() {
        return true;
    }
}
