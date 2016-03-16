import com.zhaoxiaodan.mirserver.db.entities.Config
import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem
import com.zhaoxiaodan.mirserver.db.objects.Merchant
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine

public void main(Merchant merchant, Player player) {
    String msg = "要合成的装备\\ \\" +
            " <选择要合成的装备/@buy> \\ \\" +
            " <领3把裁决/@领3把裁决> \\ \\" +
            " <离开/@exit>"

    merchant.sayTo(msg, player);
}

public void 领3把裁决(Merchant merchant, Player player) {
    player.takeNewItem("裁决之杖");
    player.takeNewItem("裁决之杖");
    player.takeNewItem("裁决之杖");
}

public void buy(Merchant merchant, Player player) {
    merchant.openSellDialog(player);
}

public boolean sell(Merchant merchant, Player player, int itemId) {
    PlayerItem playerItem = player.items.get(itemId);
    if (playerItem == null) {
        merchant.sayTo("你选择的物品不在你的背包里, 你变了什么戏法? \\ \\ <返回/@main>", player);
        return false;
    }

    String itemName = playerItem.stdItem.attr.name;
    int level = playerItem.level;
    List<PlayerItem> sameItems = new ArrayList<>();
    for (PlayerItem i : player.items.values()) {
        if (i.stdItem.attr.name.equals(itemName) && i.level == level) {
            sameItems.add(i);
        }
    }

    if (sameItems.size() < Config.COMPOSE_REQURE_NUMBER) {
        merchant.sayTo("你的背包里没有 " + Config.COMPOSE_REQURE_NUMBER + "个等级为 " + level + " 的 " + itemName + " \\ \\ <返回/@main>", player);
        return false;
    }

    if (!player.goldChange(-Config.COMPOSE_REQURE_GOLD, 0, 0)) {
        merchant.sayTo("合成装备需要 " + Config.COMPOSE_REQURE_GOLD + " 金币, 你的金币不足 \\ \\ <返回/@main>", player);
        return false;
    }

    if (!player.deleteItems(sameItems)) {
        merchant.sayTo("服务器异常, 如果装备消失, 请重新登录 \\ \\ <返回/@main>", player);
        return false;
    }

    ScriptEngine.exce(playerItem.stdItem.scriptName, "onCompose", playerItem, level + 1);
    playerItem.level = level + 1;
    playerItem.attr.name = playerItem.stdItem.attr.name + " +" + playerItem.level;

    player.takeNewItem(playerItem);

    return true;
}




