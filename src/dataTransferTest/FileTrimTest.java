package dataTransferTest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import dataTransfer.FileTrim;
import model.Record;
import model.XiangJi;

public class FileTrimTest {
	public Gson g = new Gson();

	@Test
	public void testCollectionToMemoryLast() {
		FileTrim ft = new FileTrim();
		String root = System.getProperty("user.dir");
		HashMap<XiangJi, List<Record>> data = ft.collectionToMemoryLast(root + "\\src\\data\\retail_utility_spmf1.txt");
		File file = new File(root + "\\src\\data\\retail_utility_spmf2.txt");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(g.toJson(data).getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void testCollectionToMemory() {
		FileTrim ft = new FileTrim();
		String root = System.getProperty("user.dir");
		HashMap<Integer, List<Record>> data = ft.collectionToMemory(root + "\\src\\data\\retail_utility_spmf1.txt");
		File file = new File(root + "\\src\\data\\retail_utility_spmf2.txt");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(g.toJson(data).getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Ignore
	public void testPrint() {
		FileTrim ft = new FileTrim();
		String root = System.getProperty("user.dir");
		ft.printCount(root + "\\src\\data\\retail_utility_spmf1.txt");
		System.out.println(FileTrim.count);
	}
}
