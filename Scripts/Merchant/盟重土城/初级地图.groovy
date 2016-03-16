import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.Merchant

public void main(Merchant merchant, Player player) {
    String msg = "欢迎来到 胖梁的传奇测试服务器\\ \\" +
            " <新人练级地图/@新人练级地图>   \\ \\" +
            " <野猪营/@野猪营>    <BOSS之家/@BOSS之家> \\ \\" +
            " <离开/@exit>"

    merchant.sayTo(msg, player);
}

public void 新人练级地图(Merchant merchant, Player player) {
    player.takeNewItem("回城卷")
    player.enterMap("新人练级");
}

public void 野猪营(Merchant merchant, Player player) {
    player.takeNewItem("回城卷")
    player.enterMap("野猪营");
}

public void BOSS之家(Merchant merchant, Player player) {
    player.takeNewItem("回城卷")
    player.enterMap("BOSS之家");
}


