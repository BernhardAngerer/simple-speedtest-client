package at.bernhardangerer.speedtestclient.service;

import at.bernhardangerer.speedtestclient.exception.MissingResultException;
import at.bernhardangerer.speedtestclient.exception.ParsingException;
import at.bernhardangerer.speedtestclient.exception.ServerRequestException;
import at.bernhardangerer.speedtestclient.model.ConfigSetting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class ConfigSettingsServiceIT {

  @Test
  public void requestSettings() throws MissingResultException, ServerRequestException, ParsingException {
    final ConfigSetting result = ConfigSettingsService.requestSetting();
    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getClient());
    Assertions.assertNotNull(result.getDownload());
  }

}
