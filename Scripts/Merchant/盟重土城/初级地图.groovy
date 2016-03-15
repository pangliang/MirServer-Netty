import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.Merchant

public void main(Merchant merchant, Player player) {
    String msg = "欢迎来到 胖梁的传奇测试服务器\\ \\" +
            " <野猪营/@野猪营> \\ \\" +
            " <离开/@exit>"

    merchant.sayTo(msg, player);
}

public void 野猪营(Merchant merchant, Player player) {
    player.takeNewItem("回城卷")
    player.enterMap("D2006");
}


