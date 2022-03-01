package com.dmitriav.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(LoggingVerticle.class);


  @Override
  public void start() {
    vertx.eventBus().consumer("web.events", this::messageHandler);
  }

  private void messageHandler(Message<JsonObject> message) {
    JsonObject event = message.body();
    logger.info("{}", event);
  }
}
