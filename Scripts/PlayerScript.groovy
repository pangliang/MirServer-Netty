import com.zhaoxiaodan.mirserver.gameserver.entities.Player
import com.zhaoxiaodan.mirserver.gameserver.types.Gender
import com.zhaoxiaodan.mirserver.gameserver.types.Job
import com.zhaoxiaodan.mirserver.gameserver.types.MapPoint
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine
import com.zhaoxiaodan.mirserver.gameserver.engine.MagicEngine

void onCreate(Player c) {
    setBaseAbility(c);
    giveItems(c);
    学习技能(c);
}

void setBaseAbility(Player player) {

    player.hp = 100;
    player.maxHp = 100;
    player.mp = 10;
    player.maxMp = 10;

    player.baseAbility.AC = 1;
    player.baseAbility.AC2 = 1;
    player.baseAbility.MAC = 1;
    player.baseAbility.MAC2 = 1;
    player.baseAbility.DC = 1;
    player.baseAbility.DC2 = 1;
    player.baseAbility.MC = 1;
    player.baseAbility.MC2 = 1;
    player.baseAbility.SC = 1;
    player.baseAbility.SC2 = 1;

    player.levelUp(28);

    player.gold = 10000;
    player.gameGold = 100;
    player.gamePoint = 10;

    player.homeMapPoint = getStartPoint();        //设置出生点
}

void onLevelUp(Player player) {
    player.MaxExp = getMaxExp(player.Level + 1);

    switch (player.job) {
        case Job.Warrior:
            player.maxHp = 100 + 10 * player.Level;
            player.maxMp = 10 + 2 * player.Level;
            break;
    }
}

int getMaxExp(int nextLevel) {
    return Math.pow(nextLevel, 3) * 100;
}

MapPoint getStartPoint() {
    MapPoint startPoint = new MapPoint();
    startPoint.mapId = "0";
    startPoint.x = 273;
    startPoint.y = 590;

    return startPoint;
}

void giveItems(Player c) {

    c.takeNewItem(ItemEngine.getStdItemByName("超级金创药"));
    if (c.job == Job.Warrior) {
        c.takeNewItem(ItemEngine.getStdItemByName("裁决之杖"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战头盔"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战项链"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战手镯"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战手镯"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战戒指"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战戒指"));
    }
    if (c.gender == Gender.MALE) {
        c.takeNewItem(ItemEngine.getStdItemByName("天魔神甲"));
    } else {
        c.takeNewItem(ItemEngine.getStdItemByName("圣战宝甲"));
    }
}

public void 学习技能(Player player) {
    player.learnMagic(MagicEngine.getStdMagicByName("烈火剑法"));
    player.learnMagic(MagicEngine.getStdMagicByName("刺杀剑术"));
    player.learnMagic(MagicEngine.getStdMagicByName("半月弯刀"));
}
