import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.db.entities.StdItem
import com.zhaoxiaodan.mirserver.db.types.Color
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine


class 打捆物品 {
    def config = ["超级金创药":["强效金创药" : 6]]

    void onEat(Player player, PlayerItem playerItem) {
        def c = config[playerItem.stdItem.attr.name];
        if(c == null){
            player.sendSysMsg(playerItem.stdItem.attr.name + "解捆物品未知",Color.Red,Color.Blue);
            return;
        }

        c.each {
            String itemName = it.key;
            StdItem stdItem = ItemEngine.getStdItemByName(itemName);
            if(stdItem == null){
                player.sendSysMsg(itemName + "解捆物品未知",Color.Red,Color.Blue);
                return;
            }
            int num = it.value;
            for( i in 0 .. num){
                player.takeNewItem(stdItem);
            }
        }
    }
}
