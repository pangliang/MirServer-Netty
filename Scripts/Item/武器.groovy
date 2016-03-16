import com.zhaoxiaodan.mirserver.gameserver.entities.PlayerItem
import com.zhaoxiaodan.mirserver.gameserver.types.Ability

void checkAbility(Ability ability, PlayerItem playerItem) {
    ability.DC += playerItem.attr.DC;
    ability.DC2 += playerItem.attr.DC2;
}

void onCompose(PlayerItem playerItem, int level) {
    playerItem.attr.DC += playerItem.attr.DC * 0.1 + 0.9;
    playerItem.attr.DC2 += playerItem.attr.DC2 * 0.1 + 1.9;
}