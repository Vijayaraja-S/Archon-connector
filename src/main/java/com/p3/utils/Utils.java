package com.p3.utils;

import static com.p3.utils.URLConstants.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Utils {

  public String getEntitySetUrl(
      CsdlEntityType entityType, List<CsdlEntitySet> entitySets, String resourceUrl) {
    String entitysetName =
        entitySets.stream()
            .filter(
                entitySet ->
                    entitySet
                        .getType()
                        .split("\\.")[entitySet.getType().split("\\.").length - 1]
                        .equalsIgnoreCase(entityType.getName()))
            .map(CsdlEntitySet::getName)
            .findFirst()
            .orElse("");
    if (entitysetName.isEmpty()) {
      log.error("No entity set found for entity type {}", entityType.getName());
      return null;
    }
    return resourceUrl + BASE_URL + SEPARATOR + entitysetName;
  }
}
