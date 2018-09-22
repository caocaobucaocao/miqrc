package dataTransferTest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import dataTransfer.DataTrim;
import dataTransfer.FileTrim;
import model.Record;
import model.XiangJi;

public class DataTrimTest {
	public DataTrim dt = new DataTrim();
	public Gson g = new Gson();
	public FileTrim ft = new FileTrim();
	public String root = System.getProperty("user.dir");
	public HashMap<Integer, TreeMap<Integer, Integer>> data;

	@BeforeClass
	public void init() {
		System.out.println("start");
	}

	@Test
	public void testTrimByMap() {
		HashMap<XiangJi, List<Record>> data = ft.collectionToMemoryLast(root + "\\src\\data\\retail_utility_spmf1.txt");
		dt.yuzhi = (float) 1 / 4;
		File file = new File(System.getProperty("user.dir") + "\\src\\data\\retail_utility_spmf3.txt");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(g.toJson(dt.trimByMap(data)).getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

//	@Test
//	@Ignore
	public void testTrim1() {
		HashMap<Integer, List<Record>> data = ft.collectionToMemory(root + "\\src\\data\\retail_utility_spmf1.txt");
		dt.yuzhi = (float) 1 / 4;
		File file = new File(System.getProperty("user.dir") + "\\src\\data\\retail_utility_spmf3.txt");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(g.toJson(dt.trimByList(data)).getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
