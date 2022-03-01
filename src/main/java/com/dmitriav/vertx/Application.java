package com.dmitriav.vertx;

import com.dmitriav.vertx.verticles.LoggingVerticle;
import com.dmitriav.vertx.verticles.WebVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

  private static final Logger logger = LoggerFactory.getLogger(Application.class);


  public static void main(String... arguments) {
    JsonObject webConfiguration = new JsonObject();
    webConfiguration.put("http.port", 8080);

    DeploymentOptions webOptions = new DeploymentOptions()
        .setConfig(webConfiguration)
        .setInstances(1)
        .setWorker(false);

    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(WebVerticle.class.getCanonicalName(), webOptions);
    vertx.deployVerticle(LoggingVerticle.class.getCanonicalName());

    logger.info("Application started");
  }
}
