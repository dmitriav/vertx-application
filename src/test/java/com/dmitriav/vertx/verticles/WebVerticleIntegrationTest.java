package com.dmitriav.vertx.verticles;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class WebVerticleIntegrationTest {

  @BeforeAll
  static void setup(Vertx vertx, VertxTestContext context) {
    vertx.deployVerticle(WebVerticle.class.getCanonicalName(), context.succeedingThenComplete());
  }

  @Test
  void test(VertxTestContext context) {
    // TODO: connect to endpoints with WebClient
    // TODO: test responses
    context.completeNow();
  }
}
