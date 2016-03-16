import com.zhaoxiaodan.mirserver.gameserver.entities.Player
import com.zhaoxiaodan.mirserver.gameserver.objects.Merchant
import com.zhaoxiaodan.mirserver.gameserver.engine.Engine

public void main(Merchant merchant, Player player) {
    String msg = "欢迎来到 胖梁的传奇测试服务器\\ \\" +
            " <RealodAll/@RealodAll>  \\ \\" +
            " <领超级金创药/@领超级金创药>    <领超级魔法药/@领超级魔法药>\\ \\" +
//            " <学习技能/@学习技能>    <遗忘所有技能/@遗忘所有技能> \\" +
            " <离开/@exit>"

    merchant.sayTo(msg, player);
}

void 领超级金创药(Merchant merchant, Player player) {
    player.takeNewItem("超级金创药");
    player.takeNewItem("超级金创药");
    player.takeNewItem("超级金创药");
}

void 领超级魔法药(Merchant merchant, Player player) {
    player.takeNewItem("超级魔法药");
    player.takeNewItem("超级魔法药");
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

public void 遗忘所有技能(Merchant merchant, Player player) {
    player.deleteAllMagic();
}

