package com.dmitriav.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(WebVerticle.class);


  @Override
  public void start(Promise<Void> promise) {
    int port = config().getInteger("http.port", 8080);

    vertx.createHttpServer()
        .requestHandler(this::requestHandler)
        .listen(port)
        .onSuccess(server -> {
          int actualPort = server.actualPort();
          logger.debug("Started HTTP Server on {}", actualPort);
          promise.complete();
        })
        .onFailure(promise::fail);
  }

  private void requestHandler(HttpServerRequest request) {
    // TODO: use Router instead

    String path = request.path();
    HttpMethod method = request.method();
    String methodName = method.name();

    JsonObject event = new JsonObject()
        .put("path", path)
        .put("method", methodName);

    vertx.eventBus().publish("web.events", event);

    request.response().end();
  }
}
