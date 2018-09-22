package dataTransfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import model.Record;
import model.XiangJi;

public class FileTrim {
	private Logger logger;
	public static int count;

	public FileTrim() {
		// TODO Auto-generated constructor stub
		logger = Logger.getLogger(FileTrim.class.getName());
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"));
		consoleAppender.setName("控制台");
		consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
		logger.addAppender(consoleAppender);
		RollingFileAppender rollingFileAppender;
		try {
			rollingFileAppender = new RollingFileAppender(new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"),
					System.getProperty("user.dir") + "\\src\\data\\log4j");
			rollingFileAppender.setName("文件");
			rollingFileAppender.setThreshold(Level.INFO);
			logger.addAppender(rollingFileAppender);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteSum(String filePath) {
		File f = new File(filePath);
		StringBuilder temp = new StringBuilder(filePath);
		File target = new File(temp.insert(temp.indexOf("."), "1").toString());
		try (BufferedReader br = new BufferedReader(new FileReader(f));
				BufferedWriter bw = new BufferedWriter(new FileWriter(target))) {
			StringBuilder builder = new StringBuilder();
			while (!builder.append(br.readLine()).toString().equals("null")) {
				System.out.println(builder);
				builder.delete(builder.indexOf(":") + 1, builder.lastIndexOf(":") + 1);
				bw.write(builder.toString());
				bw.write('\n');
				builder.delete(0, builder.capacity() - 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void printCount(String filePath) {
		File f = new File(filePath);
		int count = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			StringBuilder builder = new StringBuilder();
			while (!builder.append(br.readLine()).toString().equals("null")) {
				count++;
				logger.info(builder.toString());
				builder.delete(0, builder.capacity() - 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		logger.info("总事务数：" + count);
		FileTrim.count = count;
	}

	public HashMap<XiangJi, List<Record>> collectionToMemoryLast(String filePath) {
		File f = new File(filePath);
		// 数据存储映射
		HashMap<XiangJi, List<Record>> data = new HashMap<XiangJi, List<Record>>();
		// 事务号
		int tranNum = 1;
		// 读取
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String faker = null;
			while ((faker = br.readLine()) != null) {// 读取内容不为空
				faker.replaceAll(" ", "");
				logger.info(faker);
				String one = faker.substring(0, faker.indexOf(':'));
				String two = faker.substring(faker.indexOf(':') + 1);
				// 以任意空白字符串进行分割
				String[] left = one.split("\\s+");
				String[] right = two.split("\\s+");
				for (int i = 0; i < left.length; i++) {
					Integer id = Integer.valueOf(left[i]);
					Integer sumPrice = Integer.valueOf(right[i]);
					List<Integer> key = new ArrayList<>();
					key.add(id);
					XiangJi xiangJi = new XiangJi(key);
					// 是否包含id集合为 ｛i｝ 的商品的事务与此次事务中值的信息
					if (data.containsKey(xiangJi)) {
						List<Record> records = data.get(xiangJi);
						records.add(new Record(tranNum, sumPrice));
					} else {
						// 创建列表
						List<Record> records = new ArrayList<Record>();
						records.add(new Record(tranNum, sumPrice));
						data.put(xiangJi, records);
					}
					logger.info("商品号：" + left[i] + "消费纪录:" + tranNum + "," + "单条纪录商品总值：" + right[i]);
				}
				tranNum++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 数据倒序排列
		data.forEach((k, v) -> Collections.sort(v, Comparator.comparing(Record::getPriceSum).reversed()));
		return data;
	}

	public HashMap<Integer, List<Record>> collectionToMemory(String filePath) {
		File f = new File(filePath);
		// 数据存储映射
		HashMap<Integer, List<Record>> data = new HashMap<Integer, List<Record>>();
		// 事务号
		int tranNum = 1;
		// 读取
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String faker;
			while ((faker = br.readLine()) != null) {// 读取内容不为空
				faker.replaceAll(" ", "");
				logger.info(faker);
				String one = faker.substring(0, faker.indexOf(':'));
				String two = faker.substring(faker.indexOf(':') + 1);
				// 以任意空白字符串进行分割
				String[] left = one.split("\\s+");
				String[] right = two.split("\\s+");
				for (int i = 0; i < left.length; i++) {
					Integer id = Integer.valueOf(left[i]);
					Integer sum = Integer.valueOf(right[i]);
					// 是否包含id为 i 的商品的事务与此次事务中值的信息
					if (data.containsKey(id)) {
						List<Record> records = data.get(id);
						records.add(new Record(tranNum, sum));
					} else {
						// 创建列表
						List<Record> records = new ArrayList<Record>();
						records.add(new Record(tranNum, sum));
						data.put(id, records);
					}
					logger.info("商品号：" + left[i] + "消费纪录:" + tranNum + "," + "单条纪录商品总值：" + right[i]);
				}
				tranNum++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 数据倒序排列
		data.forEach((k, v) -> Collections.sort(v, Comparator.comparing(Record::getPriceSum).reversed()));
		return data;
	}

	public HashMap<List<Integer>, List<Record>> collectionToMemory1(String filePath) {
		File f = new File(filePath);
		// 数据存储映射
		HashMap<List<Integer>, List<Record>> data = new HashMap<List<Integer>, List<Record>>();
		// 事务号
		int tranNum = 1;
		// 读取
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String faker;
			while ((faker = br.readLine()) != null) {// 读取内容不为空
				faker.replaceAll(" ", "");
				logger.info(faker);
				String one = faker.substring(0, faker.indexOf(':'));
				String two = faker.substring(faker.indexOf(':') + 1);
				// 以任意空白字符串进行分割
				String[] left = one.split("\\s+");
				String[] right = two.split("\\s+");
				ArrayList<List<Integer>> ids = new ArrayList<>(data.keySet());
				for (int i = 0; i < left.length; i++) {
					Integer id = Integer.valueOf(left[i]);
					Integer sumPrice = Integer.valueOf(right[i]);
					List<Integer> key = new ArrayList<>();
					key.add(id);
					// 是否包含id集合为 ｛i｝ 的商品的事务与此次事务中值的信息
					if (ids.contains(key)) {
						List<Record> records = data.get(key);
						records.add(new Record(tranNum, sumPrice));
					} else {
						// 创建列表
						List<Record> records = new ArrayList<Record>();
						records.add(new Record(tranNum, sumPrice));
						data.put(key, records);
					}
					logger.info("商品号：" + left[i] + "消费纪录:" + tranNum + "," + "单条纪录商品总值：" + right[i]);
				}
				tranNum++;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 数据倒序排列
		data.forEach((k, v) -> Collections.sort(v, Comparator.comparing(Record::getPriceSum).reversed()));
		return data;
	}
}
