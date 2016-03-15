import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.db.entities.StdItem
import com.zhaoxiaodan.mirserver.db.types.Color
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine


class 打捆物品 {
    def config = [
            "超级金创药"  : ["强效金创药": 6]
            , "金创药(中)包": ["金创药(中量)": 6]
            , "金创药(小)包": ["金创药(小量)": 6]
            , "超级魔法药": ["强效魔法药": 6]
            , "魔法药(中)包": ["魔法药(中量)": 6]
            , "魔法药(小)包": ["魔法药(小量)": 6]
            , "随机传送卷包": ["随机传送卷": 6]
            , "回城卷包": ["回城卷": 6]
    ]

    void onEat(Player player, PlayerItem playerItem) {
        def c = config[playerItem.stdItem.attr.name];
        if (c == null) {
            player.sendSysMsg(playerItem.stdItem.attr.name + "解捆物品未知", Color.Red, Color.Blue);
            return;
        }

        c.each {
            String itemName = it.key;
            StdItem stdItem = ItemEngine.getStdItemByName(itemName);
            if (stdItem == null) {
                player.sendSysMsg(itemName + "解捆物品未知", Color.Red, Color.Blue);
                return;
            }
            int num = it.value;
            for (i in 0..num) {
                player.takeNewItem(stdItem);
            }
        }
    }
}
