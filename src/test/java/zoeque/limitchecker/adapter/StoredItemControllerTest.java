package zoeque.limitchecker.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import zoeque.limitchecker.application.dto.record.StoredItemJsonDto;
import zoeque.limitchecker.domain.entity.StoredItem;
import zoeque.limitchecker.domain.entity.factory.StoredItemFactory;
import zoeque.limitchecker.domain.model.AlertStatusFlag;
import zoeque.limitchecker.domain.model.ItemTypeModel;
import zoeque.limitchecker.domain.repository.IStoredItemRepository;
import zoeque.limitchecker.testtool.DatabaseDropService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StoredItemControllerTest {
  private MockMvc mvc;
  @Autowired
  IStoredItemRepository repository;
  @Autowired
  StoredItemFactory factory;
  @Autowired
  DatabaseDropService dropService;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  StoredItemController controller;


  @BeforeEach
  public void clearTable() {
    dropService.deleteAllData();
    mvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void sendNewItemWithJson_viaRestApi_returns200() throws Exception {
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    String testDate = format.format(date);

    StoredItemJsonDto jsonDto
            = new StoredItemJsonDto(1000L, "test", "others", testDate);
    String json = objectMapper.writeValueAsString(jsonDto);

    mvc.perform(post("/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk());
  }

  @Test
  public void sendRequestToFindAllItem_thenReturn200() throws Exception {
    mvc.perform(get("/find")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  public void sendDeleteRequest_thenReturn200() throws Exception {
    String name = "test";
    StoredItem item = factory.createStoredItem(factory.createItemDetail(name, ItemTypeModel.DAIRY, LocalDateTime.now()).get(), AlertStatusFlag.NOT_REPORTED);
    Long id = item.getIdentifier();

    repository.save(item);
    Assertions.assertEquals(1, repository.findAll().size());

    StoredItemJsonDto jsonDto
            = new StoredItemJsonDto(id, "test", "others", LocalDateTime.now().toString());
    String json = objectMapper.writeValueAsString(jsonDto);

    mvc.perform(post("/drop").contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isOk());

    Assertions.assertEquals(0, repository.findAll().size());
  }

  @Test
  public void sendDeleteRequest_whenItemDoesNotExist_thenReturn400() throws Exception {
    StoredItemJsonDto jsonDto
            = new StoredItemJsonDto(1000L, "test", "others", LocalDateTime.now().toString());
    String json = objectMapper.writeValueAsString(jsonDto);

    mvc.perform(post("/drop").contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isBadRequest());
  }
}
