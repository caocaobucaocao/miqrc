package model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ModelTest {
	@Test
	public void XiangJiTest() {

		List<Integer> data = new ArrayList<>(5);
		for (int i = 0; i < 4; i++) {
			data.add(i, i);
		}
		XiangJi xiangJi = new XiangJi(data);
		System.out.println(xiangJi);
		XiangJi xiangJi2 = new XiangJi(data);
		xiangJi2.setProductIds(data);
		System.out.println("---" + xiangJi2.compareTo(xiangJi));
		System.out.println("---" + xiangJi.intersect(xiangJi2));
		List<Integer> data1 = new ArrayList<>(2);
		data1.add(2);
		data1.add(3);
		xiangJi2.setProductIds(data1);
		System.out.println("---" + xiangJi.intersect(xiangJi2));
	}

	@Test
	@Ignore
	public void test() {
		Record r1 = new Record(1, 1);
		Record r2 = new Record(1, 1);
		Assert.assertEquals(r1, r2);
		Assert.assertEquals(r1.hashCode(), r2.hashCode());
		Record r3 = new Record(1, 2);
		Assert.assertNotEquals(r1, r3);
		Assert.assertNotEquals(r1.hashCode(), r3.hashCode());
		List<Record> ff = new ArrayList<>();
		ff.add(r1);
		ff.add(r2);
		ff.add(r3);
		System.out.println(ff);
	}

}
