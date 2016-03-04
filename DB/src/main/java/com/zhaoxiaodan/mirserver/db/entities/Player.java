package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.objects.BaseObject;
import com.zhaoxiaodan.mirserver.db.types.*;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

import javax.persistence.*;
import java.util.List;

@Entity
public class Player extends BaseObject {

	public Player(){
		race = Race.Player;
	}

	@Id
	@GeneratedValue
	public int  id;
	@ManyToOne
	@JoinColumn(name = "userId")
	public User user;

	/**
	 * 头发
	 */
	public byte hair;
	public Job  job;
	public Ability ability = new Ability();
	/**
	 * 性别
	 */
	public Gender gender;

	/**
	 * 回城点
	 */
	@AttributeOverrides({
			@AttributeOverride(name = "mapName", column = @Column(name = "homeMapName")),
			@AttributeOverride(name = "x", column = @Column(name = "homeX")),
			@AttributeOverride(name = "y", column = @Column(name = "homeY"))
	})
	public MapPoint homeMapPoint;

	/**
	 * 金币
	 */
	public int gold;
	/**
	 * 游戏币
	 */
	public int gameGold;
	/**
	 * 游戏点数
	 */
	public int gamePoint;

	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@MapKey(name = "id")
	public List<PlayerItem> items;

	/**
	 * 最后动作时间, 用来防止加速
	 */
	@Transient
	public long lastActionTime = 0;

	@Transient
	public Session session;

	public boolean checkAndIncActionTime(int interval) {
		long now = NumUtil.getTickCount();
		if (now - lastActionTime < interval) {
			return false;
		}
		lastActionTime = now;
		return true;
	}

	@Override
	public void onTick() {

	}

}
