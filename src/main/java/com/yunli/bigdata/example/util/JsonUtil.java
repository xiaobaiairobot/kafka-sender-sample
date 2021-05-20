package com.yunli.bigdata.example.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author : yunli
 */
public class JsonUtil {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public static String writeValueAsString(Object obj) {
    if (obj == null) {
      return null;
    }
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static <T> T readValue(String json, Class<T> type) {
    if (StringUtil.isEmpty(json)) {
      return null;
    }
    try {
      return MAPPER.readValue(json, type);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> List<T> readListValue(String json, Class<T> type) {
    if (StringUtil.isEmpty(json)) {
      return null;
    }
    try {
      JavaType javaType = MAPPER.getTypeFactory().constructParametricType(ArrayList.class, type);
      return MAPPER.readValue(json, javaType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> Set<T> readSetValue(String json, Class<T> type) {
    if (StringUtil.isEmpty(json)) {
      return null;
    }
    try {
      JavaType javaType = MAPPER.getTypeFactory().constructParametricType(HashSet.class, type);
      return MAPPER.readValue(json, javaType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <K, V> Map<K, V> readMapValue(String json, Class<K> keyType, Class<V> valueType) {
    if (StringUtil.isEmpty(json)) {
      return null;
    }
    try {
      JavaType javaType = MAPPER.getTypeFactory().constructMapType(HashMap.class, keyType, valueType);
      return MAPPER.readValue(json, javaType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <K, V> List<Map<K, V>> readListMapValue(String json, Class<K> keyType, Class<V> valueType) {
    if (StringUtil.isEmpty(json)) {
      return null;
    }
    try {
      JavaType javaType = MAPPER.getTypeFactory().constructMapType(HashMap.class, keyType, valueType);
      javaType = MAPPER.getTypeFactory().constructParametricType(ArrayList.class, javaType);
      return MAPPER.readValue(json, javaType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
