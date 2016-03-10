import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.types.Gender
import com.zhaoxiaodan.mirserver.db.types.Job
import com.zhaoxiaodan.mirserver.db.types.MapPoint
import com.zhaoxiaodan.mirserver.utils.NumUtil

void onCreate(Player c) {
    c.levelUp(1);
    c.baseAbility.HP = 100;
    c.baseAbility.MaxHP = 100;
    c.baseAbility.MP = 10;
    c.baseAbility.MaxMP = 10;
    c.baseAbility.AC = NumUtil.makeLong(1,1);;
    c.baseAbility.MAC = NumUtil.makeLong(1,1);;
    c.baseAbility.DC = NumUtil.makeLong(1,1);
    c.baseAbility.MC = NumUtil.makeLong(1,1);
    c.baseAbility.MaxExp = getMaxExp(c.baseAbility.Level + 1);
//        c.currMapPoint = getStartPoint();        //设置出生点
    c.gold = 10000;
    c.gameGold = 1234;
    c.gamePoint = 5678;
}

void onLevelUp(Player c){
    c.baseAbility.MaxExp = getMaxExp(c.baseAbility.Level + 1);
}

int getMaxExp(int nextLevel) {
    return 300 * nextLevel;
}

MapPoint getStartPoint() {
    MapPoint startPoint = new MapPoint();
    startPoint.mapId = "0";
    startPoint.x = 289;
    startPoint.y = 618;

    return startPoint;
}

Map<String,Integer> getInitItems(Player c){
    Map<String,Integer> items = new HashMap<>()

    items.put("金创药(小)包",5);
    items.put("八荒",1);
    items.put("木剑",1);
    if (c.job != Job.Warrior){
        items.put("魔法药(小)包",3);
    }

    if (c.gender == Gender.MALE){
        items.put("布衣(男)",1);
    }else{
        items.put("布衣(女)",1);
    }

    return items;
}
