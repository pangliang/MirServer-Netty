package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.objects.Ability;
import com.zhaoxiaodan.mirserver.db.objects.Gender;
import com.zhaoxiaodan.mirserver.db.objects.Job;
import com.zhaoxiaodan.mirserver.db.objects.MapPoint;

import javax.persistence.*;

@Entity
public class Character extends DAO {

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
	 * 能力
	 */
	public Ability  alility;
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

}
