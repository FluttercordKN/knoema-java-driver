package com.knoema.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("WeakerAccess")
public class Utils {
    @SuppressWarnings("WeakerAccess")
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static SimpleDateFormat getUTCDateFormat(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(UTC);
        return dateFormat;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private static class ConvertIterator<B, T extends B> implements Iterator<B> {

        private final Iterator<T> source;
        public ConvertIterator(Iterator<T> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public B next() {
            return source.next();
        }

        @Override
        public void remove() {
            source.remove();
        }
    }

    public static <B, T extends B> Iterator<B> castIterator(Iterator<T> source) {
        return new ConvertIterator<>(source);
    }

    private static class FutureCallbackChain<T> implements FutureCallback<T> {

        private final FutureCallback<T> first;
        private final FutureCallback<T> second;

        public FutureCallbackChain(FutureCallback<T> first, FutureCallback<T> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public void completed(T t) {
            first.completed(t);
            second.completed(t);
        }

        @Override
        public void failed(Exception e) {
            first.failed(e);
            second.failed(e);
        }

        @Override
        public void cancelled() {
            first.cancelled();
            second.cancelled();
        }
    }

    public static <T> FutureCallback<T> chainCallbacks(FutureCallback<T> first, FutureCallback<T> second) {
        if (first == null)
            return second;
        if (second == null)
            return first;
        return new FutureCallbackChain<>(first, second);
    }

    public static String toString(Object value) {

        return value != null ? value.toString() : null;
    }

    public static <T> int indexOf(T[] array, T value, int startIndex) {
        if (value != null) {
            for (int i = startIndex; i < array.length; i++) {
                if (value.equals(array[i]))
                    return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public static <T1, T> T1[] toArray(T[] from, T1[] to) {
        return (T1[])(Arrays.asList(from).toArray((Object[])to));
    }

    private static ObjectMapper createPropertyObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    private final static ObjectMapper propertyObjectMapper = createPropertyObjectMapper();

    public static Map<String, Object> toPropertySet(Object value) {

        if (value instanceof PropSet) {
            return ((PropSet)value);
        }

        String content = null;
        if (value instanceof String) {
            content = (String)value;
        }
        else {
            Class<?> valueClass = value.getClass();
            if (valueClass != null) {
                PropSet result = new PropSet();
                Field[] fields = valueClass.getFields();
                for (Field field : fields) {
                    String name = field.getName();
                    field.setAccessible(true);
                    Object val = null;
                    try {
                        val = field.get(value);
                    }
                    catch (final IllegalAccessException e) {
                        name = null;
                    }
                    if (name != null)
                        result.put(name, val);
                }
                return result;
            }

            final StringWriter sw = new StringWriter();
            try {
                propertyObjectMapper.writeValue(sw, value);
                content = sw.toString();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        PropSet data = null;
        if (!Utils.isEmpty(content)) {
            try {
                data = propertyObjectMapper.readValue(content, PropSet.class);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        if (data == null) {
            data = new PropSet();
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    public static List<String> toStringList(Object val) {

        List<String> result = null;
        String str = null;
        if (val instanceof String) {
            str = ((String)val);
        }
        else if (val instanceof String[]) {
            result = Arrays.asList((String[])val);
        }
        else if (val instanceof List) {
            result = (List<String>)val;
        }
        else if (val instanceof Iterable) {
            result = new ArrayList<>();
            for (Object o : ((Iterable)val)) {
                result.add(Utils.toString(o));
            }
        }
        else {
             str = Utils.toString(val);
        }
        if (str != null) {
            result = Arrays.asList(str.split(";"));
        }
        return result;
    }
}
