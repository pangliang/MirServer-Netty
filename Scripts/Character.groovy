import com.zhaoxiaodan.mirserver.db.entities.Character
import com.zhaoxiaodan.mirserver.db.objects.MapPoint;

class CharacterScript {
    void onCreate(Character c) {
        c.ability.Level = 1;
        c.ability.HP = 100;
        c.ability.MaxHP = 100;
        c.ability.AC = 1;
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
}
