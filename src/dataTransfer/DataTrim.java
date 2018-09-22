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
 * �ͻ���������ֵ��ɾ������������
 * 
 * @author Administrator
 *
 */

public class DataTrim {
	/* ��־��¼�� */
	private Logger logger;
	/* ��ֵ */
	public float yuzhi;

	/**
	 * @param data ԭʼ����
	 * @return ������ֵ�޼���������
	 */

	public DataTrim() {
		// TODO Auto-generated constructor stub
		logger = Logger.getLogger(DataTrim.class.getName());
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"));
		consoleAppender.setName("����̨");
		consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
		consoleAppender.setThreshold(Level.DEBUG);
		logger.addAppender(consoleAppender);
		RollingFileAppender rollingFileAppender;
		try {
			rollingFileAppender = new RollingFileAppender(new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"),
					System.getProperty("user.dir") + "\\src\\data\\log4j");
			rollingFileAppender.setName("�ļ�");
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
			// �۸���Ŀ
			int size = v.size();
			// ĩβ��λ��
			int position = (int) Math.floor(size * yuzhi);
			v.removeAll(v.subList(position, v.size() - 1));
			logger.info("ԭʼ��������" + size + ",��ǰ��������" + v.size() + "�޼�������" + (size - v.size()));
		});
		return copy;
	}

	public HashMap<Integer, List<Record>> trimByList(HashMap<Integer, List<Record>> data) {
		HashMap<Integer, List<Record>> copy = new HashMap<Integer, List<Record>>(data);
		copy.forEach((k, v) -> {
			// �۸���Ŀ
			int size = v.size();
			// ĩβ��λ��
			int position = (int) Math.floor(size * yuzhi);
			v.removeAll(v.subList(position, v.size() - 1));
			logger.info("ԭʼ��������" + size + ",��ǰ��������" + v.size() + "�޼�������" + (size - v.size()));
		});
		return copy;
	}

	public HashMap<List<Integer>, List<Record>> trimByList1(HashMap<List<Integer>, List<Record>> data) {
		HashMap<List<Integer>, List<Record>> copy = new HashMap<List<Integer>, List<Record>>(data);
		copy.forEach((k, v) -> {
			// �۸���Ŀ
			int size = v.size();
			// ĩβ��λ��
			int position = (int) Math.floor(size * yuzhi);
			v.removeAll(v.subList(position, v.size() - 1));
			logger.info("ԭʼ��������" + size + ",��ǰ��������" + v.size() + "�޼�������" + (size - v.size()));
		});
		return copy;
	}
}
