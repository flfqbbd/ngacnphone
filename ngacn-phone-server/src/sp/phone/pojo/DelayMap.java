package sp.phone.pojo;

import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class DelayMap<K, V> {

	public static final int DEFAULT_SIZE = 500;
	public static final int DEFAULT_LIFE = 10;
	public static final int DEFAULT_FREQUENCY = 1;

	private int size = DEFAULT_SIZE;
	private int life = DEFAULT_LIFE;
	private int frequency = DEFAULT_FREQUENCY;

	public String name;

	public DelayMap(int frequency, int life, int size, String name) {
		this.name = name;
		this.life = life;
		this.size = size;
		this.frequency = frequency;
		poll();
	}

	public DelayMap() {
		poll();
	}

	ConcurrentHashMap<K, V> map = new ConcurrentHashMap<K, V>();

	ConcurrentHashMap<K, Integer> map_time = new ConcurrentHashMap<K, Integer>();

	public synchronized void put(K key, V value) {
		// System.out.println(name + "  size:" + map.size());
		if (map.get(key) == null) {
			map.put(key, value);
			map_time.put(key, life);
		}
	}

	public void update(K key, V value) {
		remove(key);
		put(key, value);
	}

	public void remove(K key) {
		map.remove(key);
		map_time.remove(key);
	}

	public V get(K key) {

		return map.get(key);
	}

	public int size() {
		return map.size();
	}

	private void poll() {
		Timer time = new Timer();
		time.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					Iterator it = map_time.entrySet().iterator();
					if (map_time.size() >= size) {// 维持400个对象
						while (it.hasNext()) {
							Entry<K, Integer> entry = (Entry<K, Integer>) it
									.next();
							K key = entry.getKey();
							Integer value = entry.getValue();
							if (value != null) {
								entry.setValue(value - 1);
								if (value < 0) {
									it.remove();
									map.remove(key);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Date(), frequency * 60000);// 1分钟一次
	}
}
