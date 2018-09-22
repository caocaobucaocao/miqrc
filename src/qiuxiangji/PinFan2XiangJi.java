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

import model.Record;
import model.XiangJi;

/**
 * 频繁项集，高属性频繁项集
 * 
 * @author Administrator
 *
 */
public class PinFan2XiangJi {
	/* 项集的最小支持度 */
	private static float minSuppor = (float) 3 / 88162;
	/* 高属性事务支持度 */
	private static float minccof = (float) 3 / 4;
	private Logger logger;

	public PinFan2XiangJi() {
		// TODO Auto-generated constructor stub
		logger = Logger.getLogger(PinFan2XiangJi.class.getName());
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"));
		consoleAppender.setName("控制台");
		consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
		consoleAppender.setThreshold(Level.DEBUG);
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

	/**
	 * @param yunShuJu 未修建原始数据
	 * @return result 频繁二项集,高属性频繁候选项集
	 * @throws InterruptedException
	 */
	public List<HashMap<XiangJi, List<Record>>> pinFan2XiangJi(HashMap<XiangJi, List<Record>> yunShuJu,
			HashMap<XiangJi, List<Record>> gsxShuJu) throws IOException {
		Gson g = new Gson();
		String root = System.getProperty("user.dir") + "\\src\\data\\gsxpf2xj.txt";
		HashMap<XiangJi, List<Record>> yunShuJuCopy = new HashMap<XiangJi, List<Record>>(yunShuJu);
		yunShuJu = null;
		HashMap<XiangJi, List<Record>> gsxShuJuCopy = new HashMap<XiangJi, List<Record>>(gsxShuJu);
		gsxShuJu = null;
		List<HashMap<XiangJi, List<Record>>> result = new ArrayList<HashMap<XiangJi, List<Record>>>();
		ArrayList<Map.Entry<XiangJi, List<Record>>> yunShuJuZhuanHuan = new ArrayList<Map.Entry<XiangJi, List<Record>>>();
		ArrayList<Map.Entry<XiangJi, List<Record>>> gsxZhuanHuan = new ArrayList<Map.Entry<XiangJi, List<Record>>>();
		// 频繁项集
		HashMap<XiangJi, List<Record>> pfxj = new HashMap<XiangJi, List<Record>>();
		// 高属性候选频繁项集
		HashMap<XiangJi, List<Record>> gsxhxxj = new HashMap<XiangJi, List<Record>>();
		HashMap<XiangJi, List<Record>> gsxxj = new HashMap<XiangJi, List<Record>>();
		for (Map.Entry<XiangJi, List<Record>> entry : yunShuJuCopy.entrySet()) {
			yunShuJuZhuanHuan.add(entry);
		}
		// 初始化高属性数据
		for (Map.Entry<XiangJi, List<Record>> entry : gsxShuJuCopy.entrySet()) {
			gsxZhuanHuan.add(entry);
		}
		try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(new File(root)))) {
			for (int i = 0; i < yunShuJuZhuanHuan.size() - 1; i++) {
				for (int j = i + 1; j < yunShuJuZhuanHuan.size(); j++) {
					final int temp = j;
					// 频繁项集
					List<Record> records = yunShuJuZhuanHuan.get(i).getValue().stream()
							.filter(t -> yunShuJuZhuanHuan.get(temp).getValue().contains(t))
							.collect(Collectors.toList());
					logger.debug("频繁项集：" + records);
					// 值的集合大于最小支持度
					float support = (float) records.size() / 88162;
					if (support > minSuppor) {
						logger.debug("support=" + support + ">minSuppor=" + minSuppor);
						XiangJi xiangJi = yunShuJuZhuanHuan.get(i).getKey().union(yunShuJuZhuanHuan.get(j).getKey());
						pfxj.put(xiangJi, records);
						// 高属性候选频繁项集
						List<Record> gsxRecords = gsxZhuanHuan.get(i).getValue().stream()
								.filter(t -> gsxZhuanHuan.get(temp).getValue().contains(t))
								.collect(Collectors.toList());
						gsxhxxj.put(xiangJi, gsxRecords);
						float ccof = (float) gsxRecords.size() / records.size();
						if (ccof > minccof) {
							// 大于高属性事务执行度
							logger.debug("ccof=" + ccof + ">minccof=" + minccof);
							gsxxj.put(xiangJi, gsxRecords);
						}
					} else {
						logger.debug("小于最小支持度");
					}
				}
			}
			bWriter.write(g.toJson(gsxxj));
			logger.info("高属性候选项集共有：" + gsxhxxj.size());
			result.add(pfxj);
			result.add(gsxhxxj);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			if (e instanceof IOException) {
				System.out.println("输出文件问题");
			} else {
				System.out.println("未知继续抛出");
				throw e;
			}
		}
		return result;
	}

	public List<HashMap<List<Integer>, List<Record>>> pin2XiangJi(HashMap<Integer, List<Record>> yunShuJu,
			HashMap<Integer, List<Record>> gsxShuJu) throws IOException {
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
		Gson g = new Gson();
		String root = System.getProperty("user.dir") + "\\src\\data\\gsxpf2xj.txt";
		HashMap<Integer, List<Record>> yunShuJuCopy = new HashMap<Integer, List<Record>>(yunShuJu);
		yunShuJu = null;
		HashMap<Integer, List<Record>> gsxShuJuCopy = new HashMap<Integer, List<Record>>(gsxShuJu);
		gsxShuJu = null;
		List<HashMap<List<Integer>, List<Record>>> result = new ArrayList<HashMap<List<Integer>, List<Record>>>();
		HashMap<List<Integer>, List<Record>> yunShuJuZhuanHuan = new HashMap<List<Integer>, List<Record>>();
		HashMap<List<Integer>, List<Record>> gsxZhuanHuan = new HashMap<List<Integer>, List<Record>>();
		// 频繁项集
		HashMap<List<Integer>, List<Record>> pfxj = new HashMap<List<Integer>, List<Record>>();
		// 高属性候选频繁项集
		HashMap<List<Integer>, List<Record>> gsxhxxj = new HashMap<List<Integer>, List<Record>>();
		HashMap<List<Integer>, List<Record>> needPrint = new HashMap<List<Integer>, List<Record>>();
		for (Map.Entry<Integer, List<Record>> entry : yunShuJuCopy.entrySet()) {
			List<Integer> id = new ArrayList<Integer>();
			id.add(entry.getKey());
			yunShuJuZhuanHuan.put(id, entry.getValue());
		}
		// 初始化高属性数据
		for (Map.Entry<Integer, List<Record>> entry : gsxShuJuCopy.entrySet()) {
			List<Integer> id = new ArrayList<Integer>();
			id.add(entry.getKey());
			gsxZhuanHuan.put(id, entry.getValue());
		}
		// 转化数据处理
		ArrayList<Map.Entry<List<Integer>, List<Record>>> transeferData = new ArrayList<Map.Entry<List<Integer>, List<Record>>>(
				yunShuJuZhuanHuan.entrySet());
		// 转化高属性数据集
		ArrayList<Map.Entry<List<Integer>, List<Record>>> gsxtranseferData = new ArrayList<Map.Entry<List<Integer>, List<Record>>>(
				yunShuJuZhuanHuan.entrySet());
		try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(new File(root)))) {
			for (int i = 0; i < transeferData.size() - 1; i++) {
				for (int j = i + 1; j < transeferData.size(); j++) {
					final int temp = j;
					// 频繁项集
					List<Record> records = transeferData.get(i).getValue().stream()
							.filter(t -> transeferData.get(temp).getValue().contains(t)).collect(Collectors.toList());
					System.out.println("频繁项集：" + records);
					// 值的集合大于最小支持度
					float support = (float) records.size() / 88162;
					if (support > minSuppor) {
						System.out.println("大于最小支持度");
						List<Integer> keys = new ArrayList<>(transeferData.get(i).getKey());
						System.out.println("频繁项集键的集合：" + keys);
						keys.addAll(transeferData.get(j).getKey());
						System.out.println("将频繁项集键的集合求并：" + keys);
						keys = keys.stream().distinct().collect(Collectors.toList());
						System.out.println("频繁项集键集合去重复：" + keys);
						// 放入频繁项集
						pfxj.put(keys, records);
						// 高属性候选频繁项集
						List<Record> gsxRecords = gsxtranseferData.get(i).getValue().stream()
								.filter(t -> transeferData.get(temp).getValue().contains(t))
								.collect(Collectors.toList());
						gsxhxxj.put(keys, gsxRecords);
						if ((float) gsxRecords.size() / records.size() > minccof) {
							// 大于高属性事务执行度
							needPrint.put(keys, gsxRecords);
						}
					} else {
						System.out.println("小于最小支持度");
					}
				}
			}
			System.out.println(gsxhxxj.size());
			bWriter.write(g.toJson((HashMap<List<Integer>, List<Record>>) needPrint.entrySet().stream()
					.sorted(comparator).collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()))));
			// 按键排序
			pfxj = (HashMap<List<Integer>, List<Record>>) pfxj.entrySet().stream().sorted(comparator)
					.collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()));
			result.add(pfxj);
			gsxhxxj = (HashMap<List<Integer>, List<Record>>) gsxhxxj.entrySet().stream().sorted(comparator)
					.collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()));
			result.add(gsxhxxj);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			if (e instanceof IOException) {
				System.out.println("输出文件问题");
			} else {
				System.out.println("未知继续抛出");
				throw e;
			}
		}
		return result;
	}
}
