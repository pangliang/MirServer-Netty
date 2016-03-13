import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
import com.zhaoxiaodan.mirserver.utils.NumUtil

//todo 原版逻辑混乱 ? "使用技能" 和 战士的 "打开技能" 重叠
class 默认技能 {
    public void spell(Player player, PlayerMagic playerMagic) {
        if (isBuffer()) {
            onBufferOnOff(player, playerMagic);
            return;
        }
    }

    public int useBuffer(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {

    }

    // 打开关闭技能
    public boolean onBufferOnOff(Player player, PlayerMagic playerMagic) {

        player.metaClass.lastUseMagicTimes = [:];

        if (player.buffers.containsKey(playerMagic.stdMagic.id)) {
            player.buffers.remove(playerMagic.stdMagic.id);
            return false;
        } else {
            long now = NumUtil.getTickCount();
            if (player.lastUseMagicTimes[playerMagic.stdMagic.id] == null || now > player.lastUseMagicTimes[playerMagic.stdMagic.id] + playerMagic.stdMagic.delay) {
                player.lastUseMagicTimes[playerMagic.stdMagic.id] = now;

                player.buffers.put(playerMagic.stdMagic.id, playerMagic);
                return true;
            } else {
                player.sendSysMsg(playerMagic.stdMagic.name + "正忙!!");
                return false;
            }
        }
    }

    public boolean isBuffer() {
        return false;
    }
}
