import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine

void onEat(Player player, PlayerItem playerItem) {
    switch (playerItem.stdItem.attr.name) {
        case "回城卷":
        case "行会回城卷":
            回城卷(player);
            break;
        default:
            player.sendAlarmMsg("未知传送物品:" + playerItem.stdItem.attr.name);
            break;
    }
}

void 回城卷(Player player) {

    player.enterMap(MapEngine.getStartPoint());
}