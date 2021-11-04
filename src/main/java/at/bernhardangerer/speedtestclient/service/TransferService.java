package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.model.TransferTestResult;
import at.bernhardangerer.speedtestclient.util.Util;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TransferService {

  public static TransferTestResult testTransfer(List<Callable<TransferTestResult>> callables, int threats)
      throws InterruptedException, MissingResultException {
    if (callables != null && !callables.isEmpty() && threats > 0) {
      final List<TransferTestResult> results = Executors.newWorkStealingPool(threats).invokeAll(callables)
          .stream()
          .map(future -> {
            try {
              return future.get();
            } catch (Exception e) {
              throw new IllegalStateException(e);
            }
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      if (results.isEmpty()) {
        throw new MissingResultException("Empty list for transfer results");
      }
      final int bytes = results.stream().map(TransferTestResult::getBytes).mapToInt(Integer::intValue).sum();
      final long durationInMs = results.stream().map(TransferTestResult::getDurationInMs).mapToLong(Long::longValue).sum() / threats;
      return new TransferTestResult(Util.calculateMbps(bytes, durationInMs), bytes, durationInMs);
    } else {
      throw new IllegalArgumentException();
    }
  }
}
