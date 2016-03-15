import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem

void onEat(Player player, PlayerItem playerItem) {
    switch (playerItem.stdItem.attr.name) {
        case "金条":
            金条(player);
            break;
        default:
            player.sendAlarmMsg("未知物品:" + playerItem.stdItem.attr.name);
            break;
    }
}

void 金条(Player player) {
    player.goldChange(10000, 0, 0);
}