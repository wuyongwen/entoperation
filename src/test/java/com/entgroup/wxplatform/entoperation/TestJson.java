package com.entgroup.wxplatform.entoperation;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class TestJson {
	@Test
	public void test(){
		String s = "{\"url\":  \"http://mmbiz.qpic.cn/mmbiz/gLO17UPS6FS2xsypf378iaNhWacZ1G1UplZYWEYfwvuU6Ont96b1roYs CNFwaRrSaKTPCUdBK9DgEHicsKwWCBRQ/0\"}";
		String val = JSON.parseObject(s).getString("url");
		System.out.println(val);
	}
}
