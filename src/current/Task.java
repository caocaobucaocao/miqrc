package current;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.google.gson.Gson;

import model.Record;

/**
 * 每个线程的任务逻辑
 * 
 * @author Administrator
 */
public class Task implements Runnable {
	// 数据
	private final HashMap<List<Integer>, List<Record>> pfxj;
	private final HashMap<List<Integer>, List<Record>> gsxpfhxxj;
	private final Gson g = new Gson();
	private static float minSuppor;
	private static int shangpinzongshu;
	private static float minccof;
	private static int count = 0;
	private final int renWu;
	private final Logger log;
	private final UtilMap result;
	private final CyclicBarrier cyclicBarrier;

	/**
	 * @param data 一项集数据
	 */
	public Task(HashMap<List<Integer>, List<Record>> pfxj, HashMap<List<Integer>, List<Record>> gsxpfhxxj, UtilMap map,
			CyclicBarrier cyclicBarrier) {
		// TODO Auto-generated constructor stub
		this.cyclicBarrier = cyclicBarrier;
		this.result = map;
		this.pfxj = pfxj;
		this.gsxpfhxxj = gsxpfhxxj;
		this.renWu = count++;
		log = Logger.getLogger(Task.class.getName() + ":" + renWu);
		try {
			RollingFileAppender rollingFileAppender = new RollingFileAppender(
					new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"),
					System.getProperty("user.dir") + "\\src\\data\\log4j");
			rollingFileAppender.setName("文件");
			rollingFileAppender.setThreshold(Level.INFO);
			log.addAppender(rollingFileAppender);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		handler(pfxj, gsxpfhxxj);
	}

	/**
	 * @param pfxj      数据
	 * @param gsxpfhxxj 高属性数据
	 */
	public void handler(HashMap<List<Integer>, List<Record>> pfxj, HashMap<List<Integer>, List<Record>> gsxpfhxxj) {
		List<Map.Entry<List<Integer>, List<Record>>> pfxjList = new ArrayList<>(pfxj.entrySet());
		List<Map.Entry<List<Integer>, List<Record>>> gsxpfhxxjList = new ArrayList<>(gsxpfhxxj.entrySet());
		int basicProductIdCollection = pfxjList.get(0).getKey().size() - 1;
		HashMap<List<Integer>, List<Record>> faker1 = new HashMap<List<Integer>, List<Record>>();
		HashMap<List<Integer>, List<Record>> faker2 = new HashMap<List<Integer>, List<Record>>();
		for (int i = result.cursor.incrementAndGet(); i < pfxjList.size();) {
			List<Integer> left = new ArrayList<>(pfxjList.get(i).getKey());
			if (i + 1 == pfxjList.size()) {
				break;
			}
			for (int j = i + 1; j < pfxjList.size(); j++) {
				List<Integer> right = new ArrayList<>(pfxjList.get(j).getKey());
				if (basicProductIdCollection >= 1 && left.subList(0, basicProductIdCollection)
						.containsAll(right.subList(0, basicProductIdCollection))) {
					List<Integer> jihe = new ArrayList<>(left);
					jihe.addAll(right);
					// 去重复
					jihe = jihe.stream().distinct().collect(Collectors.toList());
					log.info("去重复商品集合：" + g.toJson(jihe));
					final int temp = j;
					// 频繁项集事务集（交集）去重复
					List<Record> records = pfxjList.get(i).getValue().stream()
							.filter(t -> pfxjList.get(temp).getValue().contains(t)).collect(Collectors.toList());
					log.info("left与right去重复商品事务集合：" + g.toJson(records));
					// 大于最小事务支持度
					if ((float) (records.size() / shangpinzongshu) > minSuppor) {
						System.out.println("大于最小事务支持度");
						// 收入频繁项集
						faker1.put(jihe, records);
						log.info("频繁项集：" + g.toJson(faker1));
						// 高属性事务候选频繁项集事务集（交集）
						List<Record> record1 = gsxpfhxxjList.get(i).getValue().stream()
								.filter(t -> gsxpfhxxjList.get(temp).getValue().contains(t))
								.collect(Collectors.toList());
						// 收入高属性事务候选频繁项集
						faker2.put(jihe, record1);
						log.info("高属性事务候选频繁项集共有：" + faker2.size());
						if ((float) record1.size() / records.size() > minccof) {
							result.put(left, record1);
							log.info("高属性事务频繁项集共有：" + result.size());
						}
					}
				} else if (basicProductIdCollection == 0) {
					final int temp = j;
					// 频繁项集事务集
					List<Record> records = pfxjList.get(i).getValue().stream()
							.filter(t -> pfxjList.get(temp).getValue().contains(t)).collect(Collectors.toList());
					// 值的集合大于最小支持度
					float support = (float) records.size() / shangpinzongshu;
					if (support > minSuppor) {
						left.addAll(pfxjList.get(j).getKey());
						left = left.stream().distinct().collect(Collectors.toList());
						log.info("频繁项集商品Id集合：" + left);
						faker1.put(left, records);
						List<Record> gsxhxRecords = gsxpfhxxjList.get(i).getValue().stream()
								.filter(t -> pfxjList.get(temp).getValue().contains(t)).collect(Collectors.toList());
						faker2.put(left, gsxhxRecords);
						if ((float) gsxhxRecords.size() / records.size() > minccof) {
							log.info("\"" + g.toJson(left) + "\"" + ":" + g.toJson(gsxhxRecords) + ",");
							result.put(left, gsxhxRecords);
						}
					} else {
						log.info("小于最小支持度");
					}
				} else {
					i = j;
					// 从j开始，因为j不是i的同一基础集合的商品集合
					log.info("一条商品处理完成，重新从" + i + "开始");
					break;
				}
			}
		}
		try {
			cyclicBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			log.info("栅栏被打破");
			e.printStackTrace();
		}
		handler(faker1, faker2);
	}
}
