package test.project1;

// import io.vertx.core.AbstractVerticle;
// import io.vertx.core.Promise;
import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.*;
import io.vertx.ext.web.handler.BodyHandler;


public class Server extends AbstractVerticle {

  private Router router;
  private HttpServer server;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    router = Router.router(vertx);
    router.post("/analyze").handler(BodyHandler.create()).handler(this::analyzeWord);

    vertx.createHttpServer().requestHandler(router)
      .listen(config().getInteger("http.port", 8080))
      .onSuccess(server -> {
        this.server = server;
        startPromise.complete();
      })
      .onFailure(startPromise::fail);
  }

  void analyzeWord(RoutingContext context) {
    JsonObject data = new JsonObject(context.getBody());
    HttpServerResponse response = context.response();
    response.putHeader("content-type", "application/json")
      .end(data.getString("text"));
  }
}

