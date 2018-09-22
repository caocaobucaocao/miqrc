package qiuxiangji.test;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import dataTransfer.DataTrim;
import dataTransfer.FileTrim;
import model.Record;
import model.XiangJi;
import qiuxiangji.PinFan2XiangJi;

public class PinFan2XiangJiTest {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	PinFan2XiangJi xaingji = new PinFan2XiangJi();
	String root = System.getProperty("user.dir");
	Gson g = new Gson();

	@Test
	public void pin2XiangJiTest1() {
		FileTrim ft = new FileTrim();
		DataTrim dt = new DataTrim();
		dt.yuzhi = (float) 1 / 4;
		String root = System.getProperty("user.dir");
		LocalDateTime start = LocalDateTime.now();
		HashMap<XiangJi, List<Record>> data = ft.collectionToMemoryLast(root + "\\src\\data\\retail_utility_spmf1.txt");
		try (FileOutputStream fos = new FileOutputStream(new File(root + "\\src\\data\\retail_utility_spmf4.txt"))) {
			fos.write(g.toJson(xaingji.pinFan2XiangJi(data, dt.trimByMap(data))).getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logger.info("运行时间：" + ChronoUnit.MILLIS.between(LocalDateTime.now(), start) + "s");
	}

//	@Test
//	@Ignore
	public void pin2XiangJiTest() {
		FileTrim ft = new FileTrim();
		DataTrim dt = new DataTrim();
		dt.yuzhi = (float) 1 / 4;
		String root = System.getProperty("user.dir");
		LocalDateTime start = LocalDateTime.now();
		HashMap<Integer, List<Record>> data = ft.collectionToMemory(root + "\\src\\data\\retail_utility_spmf1.txt");
		try (FileOutputStream fos = new FileOutputStream(new File(root + "\\src\\data\\retail_utility_spmf3.txt"))) {
			fos.write(g.toJson(xaingji.pin2XiangJi(data, dt.trimByList(data))).getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logger.info("运行时间：" + ChronoUnit.MILLIS.between(LocalDateTime.now(), start) + "s");
	}
}
