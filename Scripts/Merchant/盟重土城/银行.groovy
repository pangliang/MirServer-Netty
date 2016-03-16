import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.Merchant

public void main(Merchant merchant, Player player) {
    String msg = "要合成的装备\\ \\" +
//            " <领3个金条/@领3个金条> \\ \\" +
            " <离开/@exit>"

    merchant.sayTo(msg, player);
}

public void 领3个金条(Merchant merchant, Player player) {
    player.takeNewItem("金条");
    player.takeNewItem("金条");
    player.takeNewItem("金条");
}





