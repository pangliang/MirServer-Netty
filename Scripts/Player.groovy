import com.zhaoxiaodan.mirserver.db.entities.Player
import com.zhaoxiaodan.mirserver.db.types.Gender
import com.zhaoxiaodan.mirserver.db.types.Job
import com.zhaoxiaodan.mirserver.db.types.MapPoint
import com.zhaoxiaodan.mirserver.utils.NumUtil

class PlayerScript {
    void onCreate(Player c) {
        c.ability.Level = 1;
        c.ability.HP = 100;
        c.ability.MaxHP = 200;
        c.ability.MP = 10;
        c.ability.MaxMP = 10;
        c.ability.MaxExp = 200;
        c.ability.AC = NumUtil.makeLong(1,1000);;
        c.ability.MAC = NumUtil.makeLong(2,20);;
        c.ability.DC = NumUtil.makeLong(3000,20000);
        c.ability.MC = NumUtil.makeLong(4,5);
        c.ability.MaxExp = getMaxExp(c.ability.Level + 1);
//        c.currMapPoint = getStartPoint();        //设置出生点
        c.gold = 10000;
        c.gameGold = 1234;
        c.gamePoint = 5678;
    }

    int getMaxExp(int nextLevel) {
        return 300 * nextLevel;
    }

    MapPoint getStartPoint() {
        MapPoint startPoint = new MapPoint();
        startPoint.mapName = "0";
        startPoint.x = 289;
        startPoint.y = 618;

        return startPoint;
    }

    Map<String,Integer> getInitItems(Player c){
        Map<String,Integer> items = new HashMap<>()

        items.put("金创药(小)包",5);
        items.put("八荒",1);
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
}
