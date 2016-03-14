import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.db.types.Ability

void checkAbility(Ability ability, PlayerItem playerItem) {
    ability.DC += playerItem.attr.DC;
    ability.DC2 += playerItem.attr.DC2;
}