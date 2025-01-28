package com.p3.service;

import static com.p3.utils.URLConstants.BASE_URL;

import com.p3.beans.D365ConnectionInfo;
import com.p3.beans.pre_analysis.PreAnalysisColumnMapperBean;
import com.p3.beans.pre_analysis.PreAnalysisRelationMapperBean;
import com.p3.beans.pre_analysis.PreAnalysisTableMapperBean;
import com.p3.utils.Utils;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.edm.xml.Reference;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.commons.api.edm.provider.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class D365Processor {
  final ConnectionManager connectionManager;
  final OdataProcessor odataProcessor;
  private final Utils utils;

  private static void accept(CsdlProperty csdlProperty) {}

  public String processPreAnalysis(D365ConnectionInfo info) throws IOException {
    String accessToken = connectionManager.getAccessToken(info);
    String metadataUrl = info.getResourceUrl() + BASE_URL;
    XMLMetadata xmlMetadata = odataProcessor.getXMLMetadata(accessToken, metadataUrl);
      String edmVersion = xmlMetadata.getEdmVersion();
      List<List<String>> schemaNamespaces = xmlMetadata.getSchemaNamespaces();
      List<CsdlSchema> schemas = xmlMetadata.getSchemas();
      List<Reference> references = xmlMetadata.getReferences();
      Map<String, CsdlSchema> schemaByNsOrAlias = xmlMetadata.getSchemaByNsOrAlias();
      processXmlMetadata(xmlMetadata, accessToken, info);
    return "metadata processed";
  }

  private void processXmlMetadata(XMLMetadata xmlMetadata, String token, D365ConnectionInfo info) {
    List<PreAnalysisTableMapperBean> preAnalysisTableMapperBeans = new ArrayList<>();
    xmlMetadata
        .getSchemas()
        .forEach(
            csdlSchema -> {
              String dataBaseName = "dataBaseName";
              preAnalysisTableMapperBeans.addAll(
                  initPreAnalysis(csdlSchema, token, dataBaseName, info));
            });
    log.info("preAnalysisTableMapperBeans: {}", preAnalysisTableMapperBeans);
  }

  private List<PreAnalysisTableMapperBean> initPreAnalysis(
      CsdlSchema schema, String token, String dataBaseName, D365ConnectionInfo info) {
    String dbId = UUID.randomUUID().toString();
    return schema.getEntityTypes().stream()
        .map(
            entityType -> {
              CsdlEntityContainer entityContainer = schema.getEntityContainer();
              List<CsdlEntitySet> entitySets = entityContainer.getEntitySets();
              String entitySetUrl =
                  utils.getEntitySetUrl(entityType, entitySets, info.getResourceUrl());

              List<CsdlFunctionImport> functionImports = entityContainer.getFunctionImports();
              List<CsdlActionImport> actionImports = entityContainer.getActionImports();
              List<CsdlAnnotation> annotations = entityContainer.getAnnotations();
              List<CsdlSingleton> singletons = entityContainer.getSingletons();
              return PreAnalysisTableMapperBean.builder()
                  .tableName(entityType.getName())
                  .archonFormattedTableName(entityType.getName().toUpperCase())
                  .keywordTableName(entityType.getName().toLowerCase() + "_keyword")
                  .type("TABLE")
                  .schemaId(UUID.randomUUID().toString())
                  .schemaName(schema.getAlias())
                  .datasourceId(dbId)
                  .databaseName(dataBaseName)
                  .columnCount(entityType.getProperties().size())
//                  .rowCount(odataProcessor.getRowCount(token, entitySetUrl))
                  .containsBlob(checkClobAndBlobPresent(entityType, "Edm.Binary"))
                  .containsClob(checkClobAndBlobPresent(entityType, "Edm.String"))
                  .isRelationship(!entityType.getNavigationProperties().isEmpty())
                  .columnList(getColumnMapperBean(entityType))
                  .relations(getRelationMapperBeans(entityType))
                  .relatedTable(getRelatedTables(entityType))
                  .entitySetUrl(entitySetUrl)
                  .build();
            })
        .collect(Collectors.toList());
  }

  private Boolean checkClobAndBlobPresent(CsdlEntityType entityType, String dataType) {
    return entityType.getProperties().stream()
        .anyMatch(csdlProperty -> csdlProperty.getType().equalsIgnoreCase(dataType));
  }


  private Set<String> getRelatedTables(CsdlEntityType entityType) {
    return null;
  }

  private List<PreAnalysisRelationMapperBean> getRelationMapperBeans(CsdlEntityType entityType) {
    return null;
  }

  private List<PreAnalysisColumnMapperBean> getColumnMapperBean(CsdlEntityType entityType) {
    return null;
  }
}
