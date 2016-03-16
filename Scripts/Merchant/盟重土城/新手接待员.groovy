import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.Merchant
import com.zhaoxiaodan.mirserver.gameserver.engine.Engine
import com.zhaoxiaodan.mirserver.gameserver.engine.MagicEngine

public void main(Merchant merchant, Player player) {
    String msg = "欢迎来到 胖梁的传奇测试服务器\\ \\" +
            " <RealodAll/@RealodAll>  \\" +
            " <领药品/@领药品>  \\" +
            " <学习技能/@学习技能>    <遗忘所有技能/@遗忘所有技能> \\" +
            " <离开/@exit>"

    merchant.sayTo(msg, player);
}

void 领药品(Merchant merchant, Player player) {
    player.takeNewItem("超级金创药");
    player.takeNewItem("超级金创药");
    player.takeNewItem("超级金创药");
    player.takeNewItem("超级魔法药");
}

public void RealodAll(Merchant merchant, Player player) {
    Engine.reload();
    player.enterMap(player.currMapPoint);
}

void 新人练级地图(Merchant merchant, Player player) {
    player.takeNewItem("回城卷");
    player.enterMap("G003");
}

public void 学习技能(Merchant merchant, Player player) {
    player.learnMagic(MagicEngine.getStdMagicByName("烈火剑法"));
    player.learnMagic(MagicEngine.getStdMagicByName("刺杀剑术"));
    player.learnMagic(MagicEngine.getStdMagicByName("半月弯刀"));

    String msg = "学习技能成功, 到技能栏查看吧 \\ \\" +
            " <离开/@exit>"
    merchant.sayTo(msg, player);
}

public void 遗忘所有技能(Merchant merchant, Player player) {
    player.deleteAllMagic();
}

