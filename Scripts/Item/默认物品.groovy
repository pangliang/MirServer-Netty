import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.db.types.Ability
import com.zhaoxiaodan.mirserver.utils.NumUtil

void checkAbility(Ability ability,PlayerItem playerItem){
    ability.AC += NumUtil.makeLong(playerItem.attr.ac,playerItem.attr.ac2);
    ability.DC += NumUtil.makeLong(playerItem.attr.dc,playerItem.attr.dc2);
}