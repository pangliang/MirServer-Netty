import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.BaseObject
//todo 原版逻辑混乱 ? "使用技能" 和 战士的 "打开技能" 重叠
class 默认技能 {

    public void spell(Player player, PlayerMagic playerMagic) {
        player.sendSysMsg("默认技能spell(), PlayerMagic:"+playerMagic.stdMagic.name);
    }

    public int useMagic(Player player, PlayerMagic playerMagic, int power, List<BaseObject> targets) {
        player.sendSysMsg("useMagic(), PlayerMagic:"+playerMagic.stdMagic.name);
        return 0;
    }
}
