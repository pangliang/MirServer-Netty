import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
import com.zhaoxiaodan.mirserver.db.types.Color
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine
import com.zhaoxiaodan.mirserver.network.Protocol
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket

class 半月弯刀 extends 默认技能{

    public void spell(Player player, PlayerMagic playerMagic) {
        player.metaClass.banyueFlag = false;
        if(player.banyueFlag){
            player.banyueFlag = false;
            player.session.sendPacket(new ServerPacket.ActionStatus("+UWID"));
            player.sendSysMsg("半月弯刀关闭...", Color.White, Color.Blue);
        }else{
            player.banyueFlag = true;
            player.session.sendPacket(new ServerPacket.ActionStatus("+WID"));
            player.sendSysMsg("半月弯刀开启...", Color.White, Color.Blue);
        }
    }

    public int useMagic(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {
        player.metaClass.banyueFlag = false;

        if(!player.banyueFlag){
            player.sendSysMsg("半月弯刀还未开启, 不能使用 !!", Color.White, Color.Blue);
        }

        if (!super.chackMp(player, playerMagic))
            return 0;

        ServerPacket packet = new ServerPacket(player.inGameId, Protocol.SM_WIDEHIT, player.currMapPoint.x, (short) player.currMapPoint.y, (short) player.direction.ordinal());
        player.broadcast(packet);

        MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(player.currMapPoint.mapId);

        // 半月, 取前方半个扇面
        targets.addAll(mapInfo.getObjectsOnLine(player.currMapPoint, player.direction.turn(-2), 1, 1 ));
        targets.addAll(mapInfo.getObjectsOnLine(player.currMapPoint, player.direction.turn(-1), 1, 1 ));
        targets.addAll(mapInfo.getObjectsOnLine(player.currMapPoint, player.direction, 1, 1 ));
        targets.addAll(mapInfo.getObjectsOnLine(player.currMapPoint, player.direction.turn(1), 1, 1 ));
        targets.addAll(mapInfo.getObjectsOnLine(player.currMapPoint, player.direction.turn(2), 1, 1 ));
        return -power/2;
    }
}
