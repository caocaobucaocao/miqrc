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
		consoleAppender.setName("����̨");
		consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
		log.addAppender(consoleAppender);
		RollingFileAppender rollingFileAppender = new RollingFileAppender(
				new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"),
				System.getProperty("user.dir") + "\\src\\data\\log4j");
		rollingFileAppender.setName("�ļ�");
		rollingFileAppender.setThreshold(Level.INFO);
		log.addAppender(rollingFileAppender);
		HashMap<Integer, List<Record>> data = ft.collectionToMemory(filePath);
		List<HashMap<List<Integer>, List<Record>>> sumData = pinFan2XiangJi.pin2XiangJi(data, dt.trimByList(data));
		HashMap<List<Integer>, List<Record>> pinFanerXiangJi = sumData.get(0);
		log.info("Ƶ�������������" + pinFanerXiangJi.size());
		HashMap<List<Integer>, List<Record>> gxsPinFanerXiangJi = sumData.get(1);
		log.info("��ѡƵ�������������" + gxsPinFanerXiangJi.size());
		// ת��Ƶ������ݼ�
		ArrayList<Map.Entry<List<Integer>, List<Record>>> transeferData = new ArrayList<Map.Entry<List<Integer>, List<Record>>>(
				pinFanerXiangJi.entrySet());
		log.info("Ƶ�����ת�����ݽṹ������" + transeferData.size());
		// ת��������Ƶ����ѡ����ݼ�
		ArrayList<Map.Entry<List<Integer>, List<Record>>> gsxtranseferData = new ArrayList<Map.Entry<List<Integer>, List<Record>>>(
				gxsPinFanerXiangJi.entrySet());
		log.info("��ѡƵ�����ת�����ݽṹ������" + transeferData.size());
		for (int k = 3; k < 88162; k++) {
			int count = 0;
			HashMap<List<Integer>, List<Record>> printResult = new HashMap<List<Integer>, List<Record>>();
			HashMap<List<Integer>, List<Record>> pfxjTemp = new HashMap<List<Integer>, List<Record>>();
			HashMap<List<Integer>, List<Record>> gsxpfhxxjTemp = new HashMap<List<Integer>, List<Record>>();
			// ����������ʱ����ֵ
			try (BufferedWriter bWriter = new BufferedWriter(
					new FileWriter(new File(targetPath + "\\" + k + ".txt")))) {
				log.debug("���" + k + "�");
				// Ƶ��3���ʼ������Ʒ���༯��
				// ����Ƶ��k���ʱ���ԣ�0~k-2�����Ϊ�������ϵĲ������Ĵ������������Ϊ�㣬���޲��������������޷���Ƶ��k���ѭ������
				int size = transeferData.size();
				for (int i = 0; i < size;) {
					// ��Ƶ��2����ݿ�ʼ
					List<Integer> left = new ArrayList<>(transeferData.get(i).getKey());
					log.info("left��Ʒ��Ŀ���ϣ�" + g.toJson(left));
					if (i + 1 == size) {
						break;
					}
					for (int j = i + 1; j < size; j++) {
						List<Integer> right = new ArrayList<>(transeferData.get(j).getKey());
						log.info("right��Ʒ���ϣ�" + g.toJson(right));
						if (left.subList(0, k - 2).containsAll(right.subList(0, k - 2))) {
							// ���Խ�����k�����һ��Ĳ���
							log.debug("���� ǰ k-2�� �� " + k + "�");
							List<Integer> jihe = new ArrayList<>(left);
							jihe.addAll(right);
							// Ƶ��K�����Ʒ����
							jihe = jihe.stream().distinct().collect(Collectors.toList());
							log.info("ȥ�ظ���Ʒ���ϣ�" + g.toJson(jihe));
							// ȥ�ظ�
							final int temp = j;
							List<Record> records = transeferData.get(i).getValue().stream()
									.filter(t -> transeferData.get(temp).getValue().contains(t))
									.collect(Collectors.toList());
							log.info("left��rightȥ�ظ���Ʒ���񼯺ϣ�" + g.toJson(records));
							// Ƶ������񼯣�������
							if ((float) records.size() / shangpinzongshu > minSuppor) {
								// ������С����֧�ֶ�
								log.debug("������С����֧�ֶ�");
								pfxjTemp.put(jihe, records);
								log.debug("����Ƶ���");
								List<Record> record1 = gsxtranseferData.get(i).getValue().stream()
										.filter(t -> gsxtranseferData.get(temp).getValue().contains(t))
										.collect(Collectors.toList());
								log.info("���������񼯼��ϣ�" + g.toJson(record1));
								log.debug("��������������ѡƵ������񼯣�������");
								gsxpfhxxjTemp.put(jihe, record1);
								// ��������������ѡƵ���
								log.debug("�����������ѡƵ������У�" + gsxpfhxxjTemp.size());
								if ((float) record1.size() / records.size() > minccof) {
									count++;
									log.debug("�������������Ƶ���");
									printResult.put(jihe, record1);
									log.debug("����������Ƶ������У�" + printResult.size());
								}
							}
						} else {
							i = j;
							// ��j��ʼ����Ϊj����i��ͬһ�������ϵ���Ʒ����
							log.debug("һ����Ʒ������ɣ����´�" + i + "��ʼ");
							break;
						}
					}
				}
				log.info("���" + k + "�");
				bWriter.write(g.toJson((HashMap<List<Integer>, List<Record>>) printResult.entrySet().stream()
						.sorted(comparator).collect(Collectors.toMap(t -> t.getKey(), t -> t.getValue()))));
				// ������
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (count == 0) {
				log.info("�������޷�����༯");
				break;
			}
			log.info(k + "�༯����");
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
