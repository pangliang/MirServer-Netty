import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
import com.zhaoxiaodan.mirserver.db.types.Color
import com.zhaoxiaodan.mirserver.network.Protocol
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket

class 烈火剑法 extends 默认技能 {

    public int useBuffer(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {
        player.buffers.remove(playerMagic.stdMagic.id);
        ServerPacket packet = new ServerPacket(player.inGameId, Protocol.SM_FIREHIT, player.currMapPoint.x, (short) player.currMapPoint.y, (short) player.direction.ordinal());
        player.broadcast(packet);
        return power;
    }

    public boolean onBufferOnOff(Player player, PlayerMagic playerMagic) {
        if (super.onBufferOnOff(player, playerMagic)) {
            player.session.sendPacket(new ServerPacket.ActionStatus("+FIR"));
            player.sendSysMsg("你的武器因烈火剑法而炙热", Color.White, Color.Blue);
        }
    }

    public boolean isBuffer() {
        return true;
    }
}
