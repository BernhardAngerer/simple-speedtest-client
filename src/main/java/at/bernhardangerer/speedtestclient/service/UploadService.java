package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.model.UploadSetting;
import at.bernhardangerer.speedtestclient.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public final class UploadService extends TransferService {
  private final static int[] SIZES = new int[]{32768, 65536, 131072, 262144, 524288, 1048576, 7340032};
  private final static String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private final static String CONTENT = "content1=";

  public static TransferTestResult testUpload(String serverUrl, UploadSetting settings, int threads, Callback callback) throws InterruptedException, MissingResultException {
    if (serverUrl != null && settings != null && callback != null) {
      final int[] uploadSizes = Arrays.copyOfRange(SIZES, settings.getRatio() - 1, SIZES.length);
      final int uploadCount = (int) Math.ceil((double) settings.getMaxChunkCount() / (double) uploadSizes.length);
      final List<Integer> sizeList = new ArrayList<>();
      for (int size : uploadSizes) {
        for (int i = 0; i < uploadCount; i++) {
          sizeList.add(size);
        }
      }
      final long timeoutTime = System.currentTimeMillis() + settings.getTestLength() * 1000L;
      final List<Callable<TransferTestResult>> callables = new ArrayList<>();
      for (int size : sizeList) {
        callables.add(new UploadTask(serverUrl, timeoutTime, generateDataString(size), callback));
      }
      return testTransfer(callables, threads);
    } else {
      throw new IllegalArgumentException();
    }
  }

  static String generateDataString(int size) {
    if (size > 0) {
      final int multiplier = (int) Math.ceil(size / (float) CHARS.length());
      final StringBuilder dataString = new StringBuilder(CONTENT);
      dataString.append(CHARS.repeat(Math.max(0, multiplier)));
      return dataString.substring(0, size);
    } else {
      throw new IllegalArgumentException();
    }
  }

}
