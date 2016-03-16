import com.zhaoxiaodan.mirserver.gameserver.entities.Player
import com.zhaoxiaodan.mirserver.gameserver.entities.PlayerItem

void onEat(Player player, PlayerItem playerItem) {
    player.healthSpellChange(playerItem.stdItem.attr.AC, playerItem.stdItem.attr.MAC);
}