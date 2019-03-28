package com.redpig.common.enums;

/**
 * 常用数据枚举
 */
public enum Nums {
	TWO(2), ONE(1), ZERO(0), NEGETIVE_ONE(-1);
	
	private Nums(int num) {
		this.num = num;
	}
	
	private int num;
	public int getNum() {
		return num;
	}

	public static Nums parse(int num) {
		Nums[] values = Nums.values();
		for (Nums value : values) {
			if (value.getNum() == num) {
				return value;
			}
		}
		return null;
	}
}
