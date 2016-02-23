package com.zhaoxiaodan.mirserver.db.entities;

import com.alibaba.fastjson.JSON;
public class DAO {
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+":"+JSON.toJSONString(this);
	}
}
