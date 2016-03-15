import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.types.Gender
import com.zhaoxiaodan.mirserver.db.types.Job
import com.zhaoxiaodan.mirserver.db.types.MapPoint
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine

void onCreate(Player c) {
    setBaseAbility(c);
    giveItems(c);
}

void setBaseAbility(Player c) {
    c.levelUp(1);

    c.baseAbility.HP = 100;
    c.baseAbility.MaxHP = 100;
    c.baseAbility.MP = 10;
    c.baseAbility.MaxMP = 10;

    c.baseAbility.AC = 1;
    c.baseAbility.AC2 = 1;
    c.baseAbility.MAC = 1;
    c.baseAbility.MAC2 = 1;
    c.baseAbility.DC = 1;
    c.baseAbility.DC2 = 1;
    c.baseAbility.MC = 1;
    c.baseAbility.MC2 = 1;
    c.baseAbility.SC = 1;
    c.baseAbility.SC2 = 1;

    c.baseAbility.MaxExp = getMaxExp(c.Level + 1);


    c.gold = 10000;
    c.gameGold = 1234;
    c.gamePoint = 5678;

    c.homeMapPoint = getStartPoint();        //设置出生点
}

void onLevelUp(Player c) {
    c.baseAbility.MaxExp = getMaxExp(c.Level + 1);
}

int getMaxExp(int nextLevel) {
    return 300 * nextLevel;
}

MapPoint getStartPoint() {
    MapPoint startPoint = new MapPoint();
    startPoint.mapId = "0";
    startPoint.x = 273;
    startPoint.y = 590;

    return startPoint;
}

void giveItems(Player c) {

    c.takeNewItem(ItemEngine.getStdItemByName("金创药(小)包"));
    if (c.job == Job.Warrior) {
        c.takeNewItem(ItemEngine.getStdItemByName("裁决之杖"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战头盔"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战项链"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战手镯"));
        c.takeNewItem(ItemEngine.getStdItemByName("圣战戒指"));
    }
    if (c.gender == Gender.MALE) {
        c.takeNewItem(ItemEngine.getStdItemByName("天魔神甲"));
    } else {
        c.takeNewItem(ItemEngine.getStdItemByName("圣战宝甲"));
    }
}
