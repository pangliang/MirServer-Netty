import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.objects.Merchant
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine

public void main(Merchant merchant, Player player){
    String msg =  "欢迎来到 {哈哈呵呵/SCOLOR=253}\\ \\ \\" +
            " <领取新手药包/@getNewPlayerItems>    <传送到新手练级地图/@moveToNewPlayerMap> \\ \\" +
            " <离开/@exit>"

    merchant.sayTo(msg,player);
}

public void moveToNewPlayerMap(Merchant merchant, Player player){
    MapEngine.enter(player,"G003");
}
