import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.db.types.Ability
import com.zhaoxiaodan.mirserver.utils.NumUtil

void checkAbility(Ability ability, PlayerItem playerItem) {

    if(5 == playerItem.attr.stdMode || 6 == playerItem.attr.stdMode){
        // 武器
        ability.DC += NumUtil.makeLong(playerItem.attr.dc, playerItem.attr.dc2);
    }else{
        ability.AC += NumUtil.makeLong(playerItem.attr.ac, playerItem.attr.ac2);
        ability.MAC += NumUtil.makeLong(playerItem.attr.mac, playerItem.attr.mac2);
        ability.DC += NumUtil.makeLong(playerItem.attr.dc, playerItem.attr.dc2);
        ability.MC += NumUtil.makeLong(playerItem.attr.mc, playerItem.attr.mc2);
        ability.SC += NumUtil.makeLong(playerItem.attr.sc, playerItem.attr.sc2);
    }


}