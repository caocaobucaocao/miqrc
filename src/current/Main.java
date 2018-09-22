package current;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataTransfer.DataTrim;
import dataTransfer.FileTrim;
import model.Record;

public class Main {
	private static UtilMap map = new UtilMap();
	private static int processors = Runtime.getRuntime().availableProcessors();
	private static ExecutorService executor = Executors.newFixedThreadPool(processors);
	private static CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (map.isEmpty()) {
				// 取消任务
				executor.shutdownNow();
			} else {
				// 计算结果，输出，清空,栅栏重置
				map.write();
				cyclicBarrier.reset();
			}
		}
	});

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileTrim ft = new FileTrim();
		DataTrim dt = new DataTrim();
		dt.yuzhi = (float) 1 / 4;
		String root = System.getProperty("user.dir");
		HashMap<List<Integer>, List<Record>> data = ft
				.collectionToMemory1(root + "\\src\\data\\retail_utility_spmf1.txt");
		HashMap<List<Integer>, List<Record>> data2 = dt.trimByList1(data);
		Runnable[] runnables = new Runnable[3];
		for (int i = 0; i < 3; i++) {
			runnables[i] = new Task(data, data2, map, cyclicBarrier);
		}
		HandleResult handleResult = new HandleResult(processors, map, runnables);
		handleResult.start();
	}
}
