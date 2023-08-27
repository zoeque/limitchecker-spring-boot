package zoeque.limitchecker.application.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zoeque.limitchecker.domain.model.ItemTypeModel;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class ExpireAlertDefinitionMap {
  Map<ItemTypeModel, Integer> expireAlertDefinitionMap;

  @Autowired
  public ExpireAlertDefinitionMap(Map<ItemTypeModel, Integer> expireAlertDefinitionMap) {
    this.expireAlertDefinitionMap = new HashMap<>();
  }
}
