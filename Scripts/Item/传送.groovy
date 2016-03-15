import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem

void onEat(Player player, PlayerItem playerItem) {
    switch (playerItem.stdItem.attr.name) {
        case "回城卷":
        case "行会回城卷":
            goHome(player);
            break;
        default:
            player.sendAlarmMsg("未知传送物品:" + playerItem.stdItem.attr.name);
            break;
    }
}

void goHome(Player player) {
    player.enterMap(player.homeMapPoint);
}