package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.pojo.card.CardCreationRes;
//import gss.workshop.testing.pojo.card.CardUpdateRes;
import gss.workshop.testing.pojo.list.ListCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

//import java.nio.ReadOnlyBufferException;

public class TrelloTests extends TestBase {

  @Test
  public void trelloWorkflowTest() {
    // 1. Create new board without default list
    String boardName = OtherUtils.randomBoardName();
    Response resBoardCreation = RequestFactory.createBoard(boardName, false);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardCreation, 200);

    // VP. Validate a board is created: Board name and permission level
    BoardCreationRes board =
        ConvertUtils.convertRestResponseToPojo(resBoardCreation, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, board.getName());
    ValidationUtils.validateStringEqual("private", board.getPrefs().getPermissionLevel());

    // -> Store board Id
    String boardId = board.getId();
    System.out.println(String.format("Board Id of the new Board: %s", boardId));

    // 2. Create a TODO list
    Response resTODOList = RequestFactory.createList(boardId, "TODO");
    // VP. Validate status code
    ValidationUtils.validateStatusCode(resTODOList, HttpStatus.SC_OK);
    // VP. Validate a list is created: name of list, closed attribute
    ListCreationRes todoPojo = ConvertUtils.convertRestResponseToPojo(resTODOList, ListCreationRes.class);
    ValidationUtils.validateStringEqual(boardId, todoPojo.getIdBoard());
    // VP. Validate the list was created inside the board: board Id
    String todoIdList = todoPojo.getId();
    // 3. Create a DONE list
    Response resDoneList = RequestFactory.createList(boardId, "Done");

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resTODOList, HttpStatus.SC_OK);
    // VP. Validate a list is created: name of list, "closed" property
    ListCreationRes donePojo = ConvertUtils.convertRestResponseToPojo(resDoneList, ListCreationRes.class);
    ValidationUtils.validateStringEqual(boardId, todoPojo.getIdBoard());
    // VP. Validate the list was created inside the board: board Id
    String DoneIdList = donePojo.getId();
    // 4. Create a new Card in TODO list
    String taskName = OtherUtils.randomTaskName();
    Response resCard = RequestFactory.createCard(taskName, todoIdList);
    // VP. Validate status code
    ValidationUtils.validateStatusCode(resCard, HttpStatus.SC_OK);
    CardCreationRes cardCreationPojo = ConvertUtils.convertRestResponseToPojo(resCard, CardCreationRes.class);

    // Store cardId
    String cardId = cardCreationPojo.getId();
    // VP. Validate a card is created: task name, list id, board id
    // VP. Validate the card should have no votes or attachments

    // 5. Move the card to DONE list
    Response resUpdateCard = RequestFactory.updateCard(cardId, DoneIdList);
    // VP. Validate status code
    ValidationUtils.validateStatusCode(resUpdateCard, HttpStatus.SC_OK);
    //CardUpdateRes cardUpdatePojo = ConvertUtils.convertRestResponseToPojo(resUpdateCard, CardUpdateRes.class)
    // VP. Validate the card should have new list: list id

    // VP. Validate the card should preserve properties: name task, board Id, "closed" property

    // 6. Delete board
    Response resDeleteBoard = RequestFactory.deleteBoard(boardId);
    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDeleteBoard, HttpStatus.SC_OK);
    Response resGetBoard = RequestFactory.getBoardById(boardId);
    ValidationUtils.validateStatusCode(resGetBoard, HttpStatus.SC_NOT_FOUND);
  }
}
