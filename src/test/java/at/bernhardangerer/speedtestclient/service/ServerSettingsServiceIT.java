package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.model.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public final class ServerSettingsServiceIT {

  @Test
  public void requestServerList() throws MissingResultException {
    final List<Server> result = ServerSettingsService.requestServerList(4);
    Assertions.assertNotNull(result);
    Assertions.assertFalse(result.isEmpty());
    Assertions.assertTrue(result.size() > 5);
  }

}
