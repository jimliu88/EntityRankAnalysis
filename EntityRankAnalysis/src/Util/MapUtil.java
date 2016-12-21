package Util;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class MapUtil {

	public static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue( Map<K, V> map) {
	    HashMap<K, V> result = new LinkedHashMap<>();
	    Stream<Map.Entry<K, V>> st = map.entrySet().stream();
	    
	    Comparator ok = new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        };

	    st.sorted(ok).forEachOrdered(e->result.put(((Entry<K, V>) e).getKey(), ((Entry<K, V>) e).getValue()));
	    return result;
	}
}