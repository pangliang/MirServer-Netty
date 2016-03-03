package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.objects.*;

import javax.persistence.*;
import java.util.List;

@Entity
public class Player extends BaseObject {

	@Id
	@GeneratedValue
	public int      id;
	@ManyToOne
	@JoinColumn(name = "userId")
	public User     user;
	@Column(unique = true)
	public String   name;
	/**
	 * 头发
	 */
	public byte     hair;
	public Job      job;
	public Ability ability = new Ability();
	/**
	 * 性别
	 */
	public Gender   gender;
	/**
	 * 当前所在地图点位
	 */
	@AttributeOverrides({
			@AttributeOverride(name="mapName", column=@Column(name="currMapName")),
			@AttributeOverride(name="x", column=@Column(name="currX")),
			@AttributeOverride(name="y", column=@Column(name="currY"))
	})
	public MapPoint currMapPoint;
	/**
	 * 回城点
	 */
	@AttributeOverrides({
			@AttributeOverride(name="mapName", column=@Column(name="homeMapName")),
			@AttributeOverride(name="x", column=@Column(name="homeX")),
			@AttributeOverride(name="y", column=@Column(name="homeY"))
	})
	public MapPoint homeMapPoint;

	/**
	 * 金币
	 */
	public int      gold;
	/**
	 * 游戏币
	 */
	public int      gameGold;
	/**
	 * 游戏点数
	 */
	public int      gamePoint;

	@OneToMany(mappedBy = "player",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	@MapKey(name = "id")
	public List<PlayerItem> items;

}
