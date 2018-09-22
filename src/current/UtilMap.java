package current;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.google.gson.Gson;

import model.Record;

public class UtilMap {
	private final HashMap<List<Integer>, List<Record>> data;
	private final Logger log = Logger.getLogger(UtilMap.class.getName());
	private final String rootPath = System.getProperty("user.dir") + "\\src\\data\\";
	private final AtomicInteger k = new AtomicInteger(1);
	public final AtomicInteger cursor = new AtomicInteger(0);

	public UtilMap() {
		// TODO Auto-generated constructor stub
		data = new HashMap<List<Integer>, List<Record>>();
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("[%-5p] --> [%t] %l: %m %n <--%d"));
		consoleAppender.setName("控制台");
		consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
	}

	public void write() {
		// TODO Auto-generated constructor stub
		k.incrementAndGet();
		try (FileWriter fWriter = new FileWriter(new File(rootPath + k + ".txt"))) {
			Gson gson = new Gson();
			synchronized (data) {
				fWriter.write(gson.toJson(data));
				data.clear();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.trace("结果输出到文件问题");
			e.printStackTrace();
		}
	}

	public synchronized boolean isEmpty() {
		return data.isEmpty();
	}

	public synchronized void addAll(HashMap<List<Integer>, List<Record>> map) {
		data.putAll(map);
	}

	public synchronized void put(List<Integer> key, List<Record> value) {
		data.put(key, value);
	}

	public synchronized int size() {
		return data.size();
	}
}
