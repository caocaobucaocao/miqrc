package qiuxiangji;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.google.gson.Gson;

import dataTransfer.DataTrim;
import dataTransfer.FileTrim;
import model.Record;

public class PinFanKXiangji {
	private Gson g = new Gson();

	public void todo(float minSuppor, float minccof, int shangpinzongshu, String filePath, float yuzhi,
			String targetPath) throws IOException {
		// TODO Auto-generated constructor stub
		Comparator<Map.Entry<List<Integer>, List<Record>>> comparator = new Comparator<Map.Entry<List<Integer>, List<Record>>>() {
			@Override
			public int compare(Entry<List<Integer>, List<Record>> o1, Entry<List<Integer>, List<Record>> o2) {
				// TODO Auto-generated method stub
				List<Integer> keys = o1.getKey();
				List<Integer> key2s = o2.getKey();
				int result = 0;
				for (int i = 0; i < keys.size(); i++) {
					if ((int) keys.get(i) != (int) key2s.get(i)) {
						return (int) keys.get(i) - (int) key2s.get(i);
					}
				}
				return result;
			};
		};
		PinFan2XiangJi pinFan2XiangJi = new PinFan2XiangJi();
		FileTrim ft = new FileTrim();
		DataTrim dt = new DataTrim();
		dt.yuzhi = yuzhi;
		Logger log = Logger.getLogger(PinFanKXiangji.class.getName());
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"));
		consoleAppender.setName("控制台");
		consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
		log.addAppender(consoleAppender);
		RollingFileAppender rollingFileAppender = new RollingFileAppender(
				new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"),
				System.getProperty("user.dir") + "\\src\\data\\log4j");
		rollingFileAppender.setName("文件");
		rollingFileAppender.setThreshold(Level.INFO);
		log.addAppender(rollingFileAppender);
		HashMap<Integer, List<Record>> data = ft.collectionToMemory(filePath);
		List<HashMap<List<Integer>, List<Record>>> sumData = pinFan2XiangJi.pin2XiangJi(data, dt.trimByList(data));
		HashMap<List<Integer>, List<Record>> pinFanerXiangJi = sumData.get(0);
		log.info("频繁二项集的总数：" + pinFanerXiangJi.size());
		HashMap<List<Integer>, List<Record>> gxsPinFanerXiangJi = sumData.get(1);
		log.info("候选频繁二项集的总数：" + gxsPinFanerXiangJi.size());
		// 转化频繁项集数据集
		ArrayList<Map.Entry<List<Integer>, List<Record>>> transeferData = new ArrayList<Map.Entry<List<Integer>, List<Record>>>(
				pinFanerXiangJi.entrySet());
		log.info("频繁二项集转化数据结构总数：" + transeferData.size());
		// 转化高属性频繁候选项集数据集
		ArrayList<Map.Entry<List<Integer>, List<Record>>> gsxtranseferData = new ArrayList<Map.Entry<List<Integer>, List<Record>>>(
				gxsPinFanerXiangJi.entrySet());
		log.info("候选频繁二项集转化数据结构总数：" + transeferData.size());
		for (int k = 3; k < 88162; k++) {
			int count = 0;
			HashMap<List<Integer>, List<Record>> printResult = new HashMap<List<Integer>, List<Record>>();
			HashMap<List<Integer>, List<Record>> pfxjTemp = new HashMap<List<Integer>, List<Record>>();
			HashMap<List<Integer>, List<Record>> gsxpfhxxjTemp = new HashMap<List<Integer>, List<Record>>();
			// 输出结果的临时保存值
			try (BufferedWriter bWriter = new BufferedWriter(
					new FileWriter(new File(targetPath + "\\" + k + ".txt")))) {
				log.debug("求第" + k + "项集");
				// 频繁3项集开始，到商品种类集合
				// 在求频繁k项集的时，以（0~k-2）项集作为基础集合的并操作的次数，如果次数为零，则无并集操作，代表无法求频繁k项集，循环结束
				int size = transeferData.size();
				for (int i = 0; i < size;) {
					// 从频繁2项集数据开始
					List<Integer> left = new ArrayList<>(transeferData.get(i).getKey());
					log.info("left商品数目集合：" + g.toJson(left));
					if (i + 1 == size) {
						break;
					}
					for (int j = i + 1; j < size; j++) {
						List<Integer> right = new ArrayList<>(transeferData.get(j).getKey());
						log.info("right商品集合：" + g.toJson(right));
						if (left.subList(0, k - 2).containsAll(right.subList(0, k - 2))) {
							// 可以进行求k项集其中一项的操作
							log.debug("利用 前 k-2项 求 " + k + "项集");
							List<Integer> jihe = new ArrayList<>(left);
							jihe.addAll(right);
							// 频繁K项集的商品并集
							jihe = jihe.stream().distinct().collect(Collectors.toList());
							log.info("去重复商品集合：" + g.toJson(jihe));
							// 去重复
							final int temp = j;
							List<Record> records = transeferData.get(i).getValue().stream()
									.filter(t -> transeferData.get(temp).getValue().contains(t))
									.collect(Collectors.toList());
							log.info("left与right去重复商品事务集合：" + g.toJson(records));
							// 频繁项集事务集（交集）
							if ((float) records.size() / shangpinzongshu > minSuppor) {
								// 大于最小事务支持度
								log.debug("大于最小事务支持度");
								pfxjTemp.put(jihe, records);
								log.debug("收入频繁项集");
								List<Record> record1 = gsxtranseferData.get(i).getValue().stream()
										.filter(t -> gsxtranseferData.get(temp).getValue().contains(t))
										.collect(Collectors.toList());
								log.info("高属性事务集集合：" + g.toJson(record1));
								log.debug("收入高属性事务候选频繁项集事务集（交集）");
								gsxpfhxxjTemp.put(jihe, record1);
								// 收入高属性事务候选频繁项集
								log.debug("高属性事务候选频繁项集共有：" + gsxpfhxxjTemp.size());
								if ((float) record1.size() / records.size() > minccof) {
									count++;
									log.debug("收入高属性事务频繁项集");
									printResult.put(jihe, record1);
									log.debug("高属性事务频繁项集共有：" + printResult.size());
								}
							}
						} else {
							i = j;
							// 从j开始，因为j不是i的同一基础集合的商品集合
							log.debug("一条商品处理完成，重新从" + i + "开始");
							break;
						}
					}
				}
				log.info("输出" + k + "项集");
				bWriter.write(g.toJson((HashMap<List<Integer>, List<Record>>) printResult.entrySet().stream()
						.sorted(comparator).collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()))));
				// 输出结果
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (count == 0) {
				log.info("结束，无法求高相集");
				break;
			}
			log.info(k + "相集结束");
			transeferData.clear();
			pfxjTemp = (HashMap<List<Integer>, List<Record>>) pfxjTemp.entrySet().stream().sorted(comparator)
					.collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()));
			transeferData.addAll(pfxjTemp.entrySet());
			gsxtranseferData.clear();
			gsxpfhxxjTemp = (HashMap<List<Integer>, List<Record>>) gsxpfhxxjTemp.entrySet().stream().sorted(comparator)
					.collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()));
			gsxtranseferData.addAll(gsxpfhxxjTemp.entrySet());
		}
	}

}
