package dataTransfer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import model.Record;
import model.XiangJi;

/**
 * 客户端设置阈值，删除低属性事务集
 * 
 * @author Administrator
 *
 */

public class DataTrim {
	/* 日志记录器 */
	private Logger logger;
	/* 阈值 */
	public float yuzhi;

	/**
	 * @param data 原始数据
	 * @return 根据阈值修剪过的数据
	 */

	public DataTrim() {
		// TODO Auto-generated constructor stub
		logger = Logger.getLogger(DataTrim.class.getName());
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

	public HashMap<XiangJi, List<Record>> trimByMap(HashMap<XiangJi, List<Record>> data) {
		HashMap<XiangJi, List<Record>> copy = new HashMap<XiangJi, List<Record>>(data);
		copy.forEach((k, v) -> {
			// 价格数目
			int size = v.size();
			// 末尾的位置
			int position = (int) Math.floor(size * yuzhi);
			v.removeAll(v.subList(position, v.size() - 1));
			logger.info("原始事务数：" + size + ",当前事务数：" + v.size() + "修剪事务数" + (size - v.size()));
		});
		return copy;
	}

	public HashMap<Integer, List<Record>> trimByList(HashMap<Integer, List<Record>> data) {
		HashMap<Integer, List<Record>> copy = new HashMap<Integer, List<Record>>(data);
		copy.forEach((k, v) -> {
			// 价格数目
			int size = v.size();
			// 末尾的位置
			int position = (int) Math.floor(size * yuzhi);
			v.removeAll(v.subList(position, v.size() - 1));
			logger.info("原始事务数：" + size + ",当前事务数：" + v.size() + "修剪事务数" + (size - v.size()));
		});
		return copy;
	}

	public HashMap<List<Integer>, List<Record>> trimByList1(HashMap<List<Integer>, List<Record>> data) {
		HashMap<List<Integer>, List<Record>> copy = new HashMap<List<Integer>, List<Record>>(data);
		copy.forEach((k, v) -> {
			// 价格数目
			int size = v.size();
			// 末尾的位置
			int position = (int) Math.floor(size * yuzhi);
			v.removeAll(v.subList(position, v.size() - 1));
			logger.info("原始事务数：" + size + ",当前事务数：" + v.size() + "修剪事务数" + (size - v.size()));
		});
		return copy;
	}
}
