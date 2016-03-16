import com.zhaoxiaodan.mirserver.gameserver.entities.PlayerItem
import com.zhaoxiaodan.mirserver.gameserver.types.Ability

void checkAbility(Ability ability, PlayerItem playerItem) {
    ability.AC += playerItem.attr.AC;
    ability.AC2 += playerItem.attr.AC2;
    ability.MAC += playerItem.attr.MAC;
    ability.MAC2 += playerItem.attr.MAC2;
    ability.DC += playerItem.attr.DC;
    ability.DC2 += playerItem.attr.DC2;
    ability.MC += playerItem.attr.MC;
    ability.MC2 += playerItem.attr.MC2;
    ability.SC += playerItem.attr.SC;
    ability.SC2 += playerItem.attr.SC2;
}

void onCompose(PlayerItem playerItem, int level) {
    playerItem.attr.AC += playerItem.attr.AC * 0.1 + 0.9;
    playerItem.attr.AC2 += playerItem.attr.AC2 * 0.1 + 1.9;
    playerItem.attr.DC += playerItem.attr.DC * 0.1 + 0.9;
    playerItem.attr.DC2 += playerItem.attr.DC2 * 0.1 + 1.9;
    playerItem.attr.MAC += playerItem.attr.MAC * 0.1 + 0.9;
    playerItem.attr.MAC2 += playerItem.attr.MAC2 * 0.1 + 1.9;
    playerItem.attr.MC += playerItem.attr.MC * 0.1 + 0.9;
    playerItem.attr.MC2 += playerItem.attr.MC2 * 0.1 + 1.9;
    playerItem.attr.SC += playerItem.attr.SC * 0.1 + 0.9;
    playerItem.attr.SC2 += playerItem.attr.SC2 * 0.1 + 1.9;
}