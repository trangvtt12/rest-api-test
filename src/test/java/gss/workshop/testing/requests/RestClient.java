package gss.workshop.testing.requests;

import static io.restassured.RestAssured.*;

import gss.workshop.testing.tests.TestBase;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

/**
 * Generic RestClient containing some Rest-Assured methods.
 *
 * @author vihoang Date 1-Jun-2022
 */
public class RestClient extends TestBase {

  /**
   * Perform Get request with params
   *
   * @param requestPath the endpoint
   * @param params list of params
   * @return the response of the Get request
   */
  public static Response doGetRequestWithParams(String requestPath, Map<String, String> params) {
    return given()
        .queryParams(params)
        .contentType(ContentType.ANY)
        .when()
        .get(requestPath)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  /**
   * Perform Post request with params and no request payload
   *
   * @param requestPath the endpoint
   * @param params list of params
   * @return the response of the Post request
   */
  public static Response doPostRequestWithParamsAndNoPayload(
      String requestPath, Map<String, String> params) {
    return given()
        .queryParams(params)
        .contentType(ContentType.ANY)
        .when()
        .post(requestPath)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  /**
   * Perform Put request with params and no request payload
   *
   * @param requestPath the endpoint
   * @param params list of params
   * @return the response of the Put request
   */
  public static Response doPutRequestWithParamsAndNoPayload(
      String requestPath, Map<String, String> params) {
    return given()
        .queryParams(params)
        .contentType(ContentType.ANY)
        .when()
        .put(requestPath)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  /**
   * Perform Delete request with params and headers
   *
   * @param requestPath the endpoint
   * @param params list of params
   * @return the response of the Delete request
   */
  public static Response doDeleteRequestWithParams(String requestPath, Map<String, String> params) {
    return given()
        .queryParams(params)
        .contentType(ContentType.ANY)
        .when()
        .delete(requestPath)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }
}
