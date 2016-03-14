import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem

void onEat(Player player, PlayerItem playerItem) {
    player.healthSpellChange(playerItem.stdItem.attr.AC, playerItem.stdItem.attr.MAC);
}