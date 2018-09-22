package current;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandleResult {
	public final CyclicBarrier cyclicBarrier;
	public final ExecutorService executor;
	public final UtilMap result;

	public final Runnable[] task;

	public HandleResult(int processors, UtilMap map, Runnable... runnables) {
		// TODO Auto-generated constructor stub
		task = new Runnable[runnables.length];
		for (int i = 0; i < runnables.length; i++) {
			task[i] = runnables[i];
		}
		executor = Executors.newFixedThreadPool(processors);
		result = map;
		cyclicBarrier = new CyclicBarrier(processors, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (result.isEmpty()) {
					// ȡ������
					executor.shutdownNow();
				} else {
					// ����������������,դ������
					result.write();
					cyclicBarrier.reset();
				}
			}
		});
	}

	public void start() {
		for (int i = 0; i < task.length; i++) {
			executor.execute(task[i]);
		}
	}
}
