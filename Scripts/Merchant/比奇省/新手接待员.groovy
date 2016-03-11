import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic
import com.zhaoxiaodan.mirserver.db.objects.Merchant
import com.zhaoxiaodan.mirserver.gameserver.engine.MagicEngine
import com.zhaoxiaodan.mirserver.gameserver.engine.MonsterEngine

public void main(Merchant merchant, Player player){
    String msg =  "欢迎来到 {哈哈呵呵/SCOLOR=253}\\ \\ \\" +
            " <刷怪/@刷怪>    <传送到新手练级地图/@传送到新手练级地图> \\ \\" +
            " <学习技能/@学习技能>    <遗忘所有技能/@遗忘所有技能> \\ \\" +
            " <离开/@exit>"

    merchant.sayTo(msg,player);
}

public void 传送到新手练级地图(Merchant merchant, Player player){
    player.enterMap("G003");
}

public void 刷怪(Merchant merchant, Player player){
    MonsterEngine.refresh("0");
}

public void 学习技能(Merchant merchant, Player player){
    player.learnMagic(MagicEngine.getStdMagicByName("烈火剑法"));
}

public void 遗忘所有技能(Merchant merchant, Player player){
    for(PlayerMagic playerMagic : player.magics.values()){
        player.deleteMagic(playerMagic.id);
    }
}

