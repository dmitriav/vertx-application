package com.dmitriav.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(WebVerticle.class);


  @Override
  public void start(Promise<Void> promise) {
    Router router = setupRouter();
    int port = config().getInteger("http.port", 8080);

    vertx.createHttpServer()
        .requestHandler(router)
        .listen(port)
        .onSuccess(server -> {
          int actualPort = server.actualPort();
          logger.debug("Started HTTP Server on {}", actualPort);
          promise.complete();
        })
        .onFailure(promise::fail);
  }

  private Router setupRouter() {
    Router rootRouter = Router.router(vertx);
    Router paymentRouter = Router.router(vertx);
    BodyHandler bodyHandler = BodyHandler.create();

    paymentRouter.post()
        .handler(bodyHandler);
    paymentRouter.put()
        .handler(bodyHandler);

    paymentRouter.post("/")
        .handler(this::makePayment)
        .handler(this::endRequest);
    paymentRouter.put("/:paymentId")
        .handler(this::updatePayment)
        .handler(this::endRequest);
    paymentRouter.delete("/:paymentId")
        .handler(this::cancelPayment)
        .handler(this::endRequest);
    paymentRouter.get("/")
        .handler(this::getPayments)
        .handler(this::endRequest);

    rootRouter.mountSubRouter("/v1/payments", paymentRouter);

    return rootRouter;
  }

  private void makePayment(RoutingContext context) {
    logger.debug("Making payment");
    JsonObject payment = context.getBodyAsJson();
    publishPayment(payment);
    context.next();
  }

  private void updatePayment(RoutingContext context) {
    String paymentId = context.pathParam("paymentId");
    logger.debug("Updating payment, ID: {}", paymentId);
    context.next();
  }

  private void cancelPayment(RoutingContext context) {
    String paymentId = context.pathParam("paymentId");
    logger.debug("Cancelling payment, ID: {}", paymentId);
    context.next();
  }

  private void getPayments(RoutingContext context) {
    logger.debug("Getting payments");
    context.next();
  }

  private void endRequest(RoutingContext context) {
    JsonObject response = new JsonObject()
        .put("status", "OK");
    context.json(response);
  }

  private void publishPayment(JsonObject payment) {
    vertx.eventBus().publish("web.payments", payment);
  }
}
