package controllers;

import play.Logger;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index; 

public class Application extends Controller {

	private static final String integration_url = "http://localhost:9000/";


	public static Result index() {
		return ok(index.render("Proxy App"));
	}


	@BodyParser.Of(BodyParser.Json.class)
	public static Promise<Result> calc(int x,int y) {
		WSRequestHolder holder = WS.url(integration_url+"calc")
				.setQueryParameter("x", String.valueOf(x))
				.setQueryParameter("y", String.valueOf(y))
				.setTimeout(5000);
		Logger.info(holder.getUrl());

		final Promise<Result> resultPromise = holder.get().map(
				new Function<WSResponse, Result>() {
					public Result apply(WSResponse response) {
						return ok(response.asJson());
					}
				}
				);

		return resultPromise;
	}


	//       			@BodyParser.Of(BodyParser.Json.class)
	//    			public static Promise<Result> calcJson(int x,int y) {
	//    				WSRequestHolder holder = WS.url(integration_url+"calcJson")
	//    						.setQueryParameter("x", String.valueOf(x))
	//    	        			.setQueryParameter("y", String.valueOf(y));
	//    				System.out.println(holder.getUrl());
	//    			    final Promise<Result> resultPromise = holder.get().map(
	//    			            new Function<WSResponse, Result>() {
	//    			                public Result apply(WSResponse response) {
	//    			                	JsonNode jsonNode = response.asJson().findPath("calcResult");
	//    			                	ObjectNode result = Json.newObject();
	//    			                	result.put("calcResult",jsonNode);
	//    			                    return ok(result);
	//    			                }
	//    			            }
	//    			    );
	//    			    return resultPromise;
	//    			}

}
