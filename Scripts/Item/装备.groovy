import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.db.types.Ability

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