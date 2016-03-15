import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.Merchant
import com.zhaoxiaodan.mirserver.gameserver.engine.Engine
import com.zhaoxiaodan.mirserver.gameserver.engine.MagicEngine

public void main(Merchant merchant, Player player) {
    String msg = "欢迎来到 胖梁的传奇测试服务器\\ \\" +
            " <RealodAll/@RealodAll>  \\ \\" +
            " <刷怪/@刷怪>\\ \\" +
            " <学习技能/@学习技能>    <遗忘所有技能/@遗忘所有技能> \\ \\" +
            " <离开/@exit>"

    merchant.sayTo(msg, player);
}

public void RealodAll(Merchant merchant, Player player) {
    Engine.reload();
    player.enterMap(player.currMapPoint);
}

public void 学习技能(Merchant merchant, Player player) {
    player.learnMagic(MagicEngine.getStdMagicByName("烈火剑法"));
    player.learnMagic(MagicEngine.getStdMagicByName("刺杀剑术"));
    player.learnMagic(MagicEngine.getStdMagicByName("半月弯刀"));
}

public void 遗忘所有技能(Merchant merchant, Player player) {
    player.deleteAllMagic();
}

