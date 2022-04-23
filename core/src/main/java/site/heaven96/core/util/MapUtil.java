package site.heaven96.core.util;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map相关工具类
 *
 * @author Looly
 * @since 3.1.1
 */
public class MapUtil {
	/**
	 * Map是否为空
	 *
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return null == map || map.isEmpty();
	}

	/**
	 * Map是否为非空
	 *
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return null != map && false == map.isEmpty();
	}


	/**
	 * 排序已有Map，Key有序的Map，使用默认Key排序方式（字母顺序）
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map Map
	 * @return TreeMap
	 * @see #newTreeMap(Map, Comparator)
	 * @since 4.0.1
	 */
	public static <K, V> TreeMap<K, V> sort(Map<K, V> map) {
		return sort(map, null);
	}

	public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
		if (null == map) {
			return null;
		}

		TreeMap<K, V> result;
		if (map instanceof TreeMap) {
			// 已经是可排序Map，此时只有比较器一致才返回原map
			result = (TreeMap<K, V>) map;
			if (null == comparator || comparator.equals(result.comparator())) {
				return result;
			}
		} else {
			result = newTreeMap(map, comparator);
		}

		return result;
	}

	/**
	 * 新建TreeMap，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param map        Map
	 * @param comparator Key比较器
	 * @return TreeMap
	 * @since 3.2.3
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
		final TreeMap<K, V> treeMap = new TreeMap<>(comparator);
		if (false == isEmpty(map)) {
			treeMap.putAll(map);
		}
		return treeMap;
	}
}
