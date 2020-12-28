package rmqexample;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.HashMap;

import com.rabbitmq.tools.json.JSONReader;

public class Stats {
	private double loadAverage;
	private long usedHeap;
	private long usedNoHeap;
	private String timestamp;
	
	public void Update() {
		loadAverage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		MemoryUsage muHeap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
		MemoryUsage muNoHeap = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
		Date date = new Date();
		usedHeap = muHeap.getUsed();
		usedNoHeap = muNoHeap.getUsed();
		timestamp = date.toString();
	}

	public double getLoadAverage() {
		return loadAverage;
	}

	public long getUsedHeap() {
		return usedHeap;
	}

	public long getUsedNoHeap() {
		return usedNoHeap;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public static Stats loadFromJSON(String JSONString) {
		JSONReader jsonreader = new JSONReader();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> hash=  (HashMap<String, Object>) jsonreader.read(JSONString);
		
		Stats stats = new Stats();
		stats.loadAverage = Double.valueOf(hash.get("loadAverage").toString());
		stats.usedHeap    = Long.valueOf(hash.get("usedHeap").toString());
		stats.usedNoHeap  = Long.valueOf(hash.get("usedNoHeap").toString());
		stats.timestamp   = hash.get("timestamp").toString();
		return stats;
	}
	
	public String toString() {
		return timestamp + ": memory=" + Long.toString(usedHeap) + "/" + Long.toString(usedNoHeap) +
				" cpu=" + Double.toString(loadAverage);
	}
}
