package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.pojo.card.CardCreationRes;
import gss.workshop.testing.pojo.card.CardUpdateRes;
import gss.workshop.testing.pojo.list.ListCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class TrelloTests extends TestBase {

  @Test
  public void trelloWorkflowTest() {
    // 1. Create new board without default list
    String boardName = OtherUtils.randomBoardName();
    Response resBoardCreation = RequestFactory.createBoard(boardName, false);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardCreation, HttpStatus.SC_OK);

    // VP. Validate a board is created: Board name and permission level
    BoardCreationRes boardPojo =
        ConvertUtils.convertRestResponseToPojo(resBoardCreation, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, boardPojo.getName());
    ValidationUtils.validateStringEqual("private", boardPojo.getPrefs().getPermissionLevel());

    // -> Store board Id
    String boardId = boardPojo.getId();
    System.out.println(String.format("Board Id of the new Board: %s", boardId));

    // 2. Create a TODO list
    Response resTodoListCreation = RequestFactory.createList(boardId, "TODO");

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resTodoListCreation, HttpStatus.SC_OK);

    // VP. Validate a list is created: name of list, closed attribute
    ListCreationRes todoListPojo =
        ConvertUtils.convertRestResponseToPojo(resTodoListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual("TODO", todoListPojo.getName());
    ValidationUtils.validateStringEqual(false, todoListPojo.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, todoListPojo.getIdBoard());
    // -> Store todo lstId
    String lstTodoId = todoListPojo.getId();

    // 3. Create a DONE list
    Response resDoneListCreation = RequestFactory.createList(boardId, "DONE");

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDoneListCreation, HttpStatus.SC_OK);

    // VP. Validate a list is created: name of list, "closed" property
    ListCreationRes doneListPojo =
        ConvertUtils.convertRestResponseToPojo(resDoneListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual("DONE", doneListPojo.getName());
    ValidationUtils.validateStringEqual(false, doneListPojo.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, doneListPojo.getIdBoard());
    // -> Store todo lstId
    String lstDoneId = doneListPojo.getId();

    // 4. Create a new Card in TODO list
    String taskName = OtherUtils.randomTaskName();
    Response resCreatedCard = RequestFactory.createCard(taskName, lstTodoId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resCreatedCard, 200);

    // VP. Validate a card is created: task name, list id, board id
    CardCreationRes cardPojo =
        ConvertUtils.convertRestResponseToPojo(resCreatedCard, CardCreationRes.class);
    ValidationUtils.validateStringEqual(taskName, cardPojo.getName());
    ValidationUtils.validateStringEqual(boardId, cardPojo.getIdBoard());
    ValidationUtils.validateStringEqual(lstTodoId, cardPojo.getIdList());

    // VP. Validate the card should have no votes or attachments
    ValidationUtils.validateStringEqual(0, cardPojo.getBadges().getVotes());
    ValidationUtils.validateStringEqual(
        0, cardPojo.getBadges().getAttachmentsByType().getTrello().getCard());
    // -> Store todo lstId
    String cardId = cardPojo.getId();

    // 5. Move the card to DONE list
    Response resUpdatedCard = RequestFactory.updateCard(cardId, lstDoneId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resUpdatedCard, HttpStatus.SC_OK);

    // VP. Validate the card should have new list: list id
    CardUpdateRes updatedCardPojo =
        ConvertUtils.convertRestResponseToPojo(resUpdatedCard, CardUpdateRes.class);
    ValidationUtils.validateStringEqual(lstDoneId, updatedCardPojo.getIdList());

    // VP. Validate the card should preserve properties: name task, board Id, "closed" property
    ValidationUtils.validateStringEqual(taskName, updatedCardPojo.getName());
    ValidationUtils.validateStringEqual(boardId, updatedCardPojo.getIdBoard());
    ValidationUtils.validateStringEqual(false, updatedCardPojo.getClosed());

    // 6. Delete board
    Response resDeletedBoard = RequestFactory.deleteBoard(boardId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDeletedBoard, HttpStatus.SC_OK);

    // VP. Validate the board is not available
    Response resBoard = RequestFactory.getBoardById(boardId);
    ValidationUtils.validateStatusCode(resBoard, HttpStatus.SC_NOT_FOUND);
  }
}
