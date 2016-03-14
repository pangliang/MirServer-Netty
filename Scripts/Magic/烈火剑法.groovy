import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
import com.zhaoxiaodan.mirserver.db.types.Color
import com.zhaoxiaodan.mirserver.network.Protocol
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket

class 烈火剑法 {

    public int useMagic(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {
        player.metaClass.liehuoFlag = false;
        if(!player.liehuoFlag)
            return 0;

        player.liehuoFlag = false;
        ServerPacket packet = new ServerPacket(player.inGameId, Protocol.SM_FIREHIT, player.currMapPoint.x, (short) player.currMapPoint.y, (short) player.direction.ordinal());
        player.broadcast(packet);
        return power;
    }

    public void spell(Player player, PlayerMagic playerMagic) {
        player.metaClass.liehuoFlag = false;
        player.liehuoFlag = true;
        player.session.sendPacket(new ServerPacket.ActionStatus("+FIR"));
        player.sendSysMsg("你的武器因烈火剑法而炙热", Color.White, Color.Blue);
    }
}
