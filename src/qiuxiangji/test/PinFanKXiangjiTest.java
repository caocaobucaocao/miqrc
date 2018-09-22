package qiuxiangji.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import qiuxiangji.PinFanKXiangji;

class PinFanKXiangjiTest {
	public PinFanKXiangji target = new PinFanKXiangji();

	@Before
	public void init() {
	}

	@Test
	void test() throws IOException {
		target.todo((float) 3 / 88162, (float) 2 / 7761, 88162,
				System.getProperty("user.dir") + "\\src\\data\\retail_utility_spmf1.txt", (float) 1 / 4,
				System.getProperty("user.dir") + "\\src\\data");
	}
}
