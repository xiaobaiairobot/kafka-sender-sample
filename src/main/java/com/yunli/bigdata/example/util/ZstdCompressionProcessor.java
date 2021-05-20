package com.yunli.bigdata.example.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.util.StringUtils;

import com.github.luben.zstd.Zstd;

/**
 * @author : yunli
 */
public class ZstdCompressionProcessor {
  public byte[] compress(byte[] input) {
    if (input == null || input.length == 0) {
      return input;
    }
    return Zstd.compress(input);
  }

  public String compressPojoToBase64String(Object obj) {
    if (obj == null) {
      return null;
    }
    return new String(
        Base64.getEncoder().encode(compress(JsonUtil.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8))),
        StandardCharsets.UTF_8);
  }

  public byte[] compressPojoToBytes(Object obj) {
    if (obj == null) {
      return null;
    }
    return compress(JsonUtil.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8));
  }

  public byte[] decompress(byte[] input) {
    if (input == null || input.length == 0) {
      return input;
    }
    long len = Zstd.decompressedSize(input);
    return Zstd.decompress(input, (int) len);
  }

  public <T> T decompressPojoFromBase64String(String data, Class<T> type) {
    if (StringUtils.isEmpty(data)) {
      return null;
    }
    return JsonUtil.readValue(new String(decompress(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8))),
        StandardCharsets.UTF_8), type);
  }

  public <T> T decompressPojoFromBytes(byte[] data, Class<T> type) {
    if (CollectionUtil.isNullOrEmpty(data)) {
      return null;
    }
    return JsonUtil.readValue(new String(decompress(data), StandardCharsets.UTF_8), type);
  }

  public <T> List<T> decompressPojoListFromBytes(byte[] data, Class<T> type) {
    if (CollectionUtil.isNullOrEmpty(data)) {
      return null;
    }
    return JsonUtil.readListValue(new String(decompress(data), StandardCharsets.UTF_8), type);
  }
}
