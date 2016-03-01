import com.zhaoxiaodan.mirserver.db.entities.Character
import com.zhaoxiaodan.mirserver.db.objects.Gender
import com.zhaoxiaodan.mirserver.db.objects.Job
import com.zhaoxiaodan.mirserver.db.objects.MapPoint

class CharacterScript {
    void onCreate(Character c) {
        c.ability.Level = 1;
        c.ability.HP = 100;
        c.ability.MaxHP = 200;
        c.ability.MP = 100;
        c.ability.MaxExp = 200;
        c.ability.AC = 1;
        c.ability.MAC = 5;
        c.ability.DC = 1;
        c.ability.MaxExp = getMaxExp(c.ability.Level + 1);
        c.currMapPoint = getStartPoint();        //设置出生点
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

    Map<String,Integer> getInitItems(Character c){
        Map<String,Integer> items = new HashMap<>()

        items.put("金创药(小)包",5);
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
}
