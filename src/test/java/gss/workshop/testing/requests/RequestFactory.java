package gss.workshop.testing.requests;

import static gss.workshop.testing.utils.RestUtils.addParams;

import gss.workshop.testing.tests.TestBase;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RequestFactory extends TestBase {

  private static final Logger logger = Logger.getLogger(String.valueOf(RequestFactory.class));

  // -------------------Board-------------------

  /**
   * Send request to create a new board
   *
   * @param boardName expected board name
   * @return Response of the request
   */
  public static Response createBoard(String boardName) {
    logger.info("Creating a new board.");
    HashMap<String, String> params = setCommonQueryParams();
    params.putAll(addParams(Map.of("name", boardName)));
    String path = String.format(prop.getProperty("boardCreationPath"), version);
    Response res =
        RestClient.doPostRequestWithParamsAndNoPayload(
            path,
            params); // it calls a method of RestClient "doPostRequestWithParamsAndNoPayload" to
    // perform the Post request with specific info was prepared.
    logger.info(String.format("The request for creating the %s board completed", boardName));
    return res;
  }

  /**
   * Send request to create a new board without defaultList
   *
   * @param boardName expected board name
   * @param defaultList a board without/with default list
   * @return Response of the request
   */
  public static Response createBoard(String boardName, boolean defaultList) {
    logger.info("Creating a new board.");
    HashMap<String, String> params = setCommonQueryParams();
    params.putAll(addParams(Map.of("name", boardName, "defaultLists", false)));
    String requestPath = String.format(prop.getProperty("boardCreationPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info(String.format("The request for creating the %s board completed", boardName));
    return res;
  }

  /**
   * Get info of an existing board by its Id
   *
   * @param boardId the Id of an existing board
   * @return Response of the request
   */
  public static Response getBoardById(String boardId) {
    logger.info((String.format("Getting a board by id %s.",boardId)));
    HashMap<String, String> params = setCommonQueryParams();
    String requestPath = String.format(prop.getProperty("getBoardPath"), version, boardId);
    Response res = RestClient.doGetRequestWithParams(requestPath, params);
    logger.info(String.format("The request for get the %s board completed", boardId));
    return res;
  }

  /**
   * Delete an existing board by Id
   *
   * @param boardId the Id of an existing board
   * @return Response of the request
   */
  public static Response deleteBoard(String boardId) {
    logger.info((String.format("Delete a board by id %s.",boardId)));
    HashMap<String, String> params = setCommonQueryParams();
    String requestPath = String.format(prop.getProperty("deleteBoardPath"), version, boardId);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info(String.format("The request for delete the %s board completed", boardId));
    return res;
  }
  // -------------------List-------------------

  /**
   * Create a new list in an existing board
   *
   * @param boardId the board id which to be added more list
   * @param listName name of the new list created
   * @return Response of the request
   */
  public static Response createList(String boardId, String listName) {
    logger.info((String.format("Create a list %s.",listName)));
    HashMap<String, String> params = setCommonQueryParams();
    setCommonQueryParams().putAll(Map.of("name",listName,"idBoard", boardId));
    String requestPath = String.format(prop.getProperty("createListPath"), version, boardId);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info(String.format("The request for create the %s list completed", listName));
    return res;
  }

  // -------------------Card-------------------

  /**
   * Create a new card in an existing list
   *
   * @param taskName
   * @param listId
   * @return Response of the request
   */
  public static Response createCard(String taskName, String listId) {
    logger.info((String.format("Create a card %s.",taskName)));
    HashMap<String, String> params = setCommonQueryParams();
    setCommonQueryParams().putAll(Map.of("name",taskName,"idList", listId));
    String requestPath = String.format(prop.getProperty("createCardPath"), version);
    Response res = RestClient.doGetRequestWithParams(requestPath, params);
    logger.info(String.format("The request for create the %s card completed", taskName));
    return res;
  }

  /**
   * Update an existing card
   *
   * @param cardId
   * @param listId
   * @return Response of the request
   */
  public static Response updateCard(String cardId, String listId) {
    logger.info((String.format("Update a card %s.",cardId)));
    HashMap<String, String> params = setCommonQueryParams();
    setCommonQueryParams().putAll(Map.of("idList",listId));
    String requestPath = String.format(prop.getProperty("updateCardPath"), version, cardId);
    Response res = RestClient.doGetRequestWithParams(requestPath, params);
    logger.info(String.format("The request for update the %s list completed", cardId));
    return res;
  }

  private static HashMap setCommonQueryParams() {
    HashMap<String, String> params = addParams(Map.of("key", key, "token", token));
    return params;
  }
}
