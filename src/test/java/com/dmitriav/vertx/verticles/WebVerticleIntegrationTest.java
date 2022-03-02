package com.dmitriav.vertx.verticles;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class WebVerticleIntegrationTest {

  @BeforeAll
  static void deployWebVerticle(Vertx vertx, VertxTestContext context) {
    vertx.deployVerticle(WebVerticle.class.getCanonicalName(), context.succeedingThenComplete());
  }

  @Test
  void testDelete(Vertx vertx, VertxTestContext context) {
    Checkpoint responseCheckpoint = context.checkpoint();
    HttpClient client = vertx.createHttpClient();

    Handler<AsyncResult<Buffer>> responseHandler = context.succeeding(buffer ->
      context.verify(() -> {
        JsonObject payload = buffer.toJsonObject();
        Assertions.assertNotNull(payload);
        String status = payload.getString("status");
        Assertions.assertEquals("OK", status);
        responseCheckpoint.flag();
      })
    );

    client.request(HttpMethod.DELETE, 8080, "localhost", "/v1/payments/ID-1")
        .compose(request -> request.send().compose(HttpClientResponse::body))
        .onComplete(responseHandler);
  }
}
