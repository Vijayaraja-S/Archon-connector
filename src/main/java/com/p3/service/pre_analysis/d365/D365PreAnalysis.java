package com.p3.service.pre_analysis.d365;

import static com.p3.utils.URLConstants.BASE_URL;

import com.p3.beans.request.D365ConnectionInfo;
import com.p3.beans.pre_analysis.*;
import com.p3.beans.pre_analysis.d365.PreAnalysisActionMapperBean;
import com.p3.beans.pre_analysis.d365.PreAnalysisComplexTypeMapperBean;
import com.p3.beans.pre_analysis.d365.PreAnalysisFunctionMapperBean;
import com.p3.beans.pre_analysis.d365.RelationDetails;
import com.p3.beans.pre_analysis.identifiers.ArchonDataCategory;
import com.p3.beans.pre_analysis.identifiers.ColumnType;
import com.p3.utils.Utils;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.commons.api.edm.provider.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class D365PreAnalysis {
  final ConnectionManager connectionManager;
  final OdataProcessor odataProcessor;
  private final Utils utils;

  public String processPreAnalysis(D365ConnectionInfo info) throws IOException {
    String accessToken = connectionManager.getAccessToken(info);
    String metadataUrl = info.getResourceUrl() + BASE_URL;
    XMLMetadata xmlMetadata = odataProcessor.getXMLMetadata(accessToken, metadataUrl);
    processXmlMetadata(xmlMetadata, accessToken, info);
    return "metadata processed";
  }

  private void processXmlMetadata(XMLMetadata xmlMetadata, String token, D365ConnectionInfo info) {
    List<PreAnalysisTableMapperBean> preAnalysisTableMapperBeans = new ArrayList<>();
    String dbId = UUID.randomUUID().toString();
    List<PreAnalysisSchemaMapperBean> preAnalysisSchemaMapperBeans =
        xmlMetadata.getSchemas().stream()
            .map(
                schema -> {
                  String schemaId = UUID.randomUUID().toString();
                  PreAnalysisSchemaMapperBean schemaBean =
                      PreAnalysisSchemaMapperBean.builder()
                          .schemaName(schema.getAlias())
                          .archonFormattedSchemaName(schema.getAlias().toUpperCase())
                          .datasourceId(dbId)
                          .databaseName(schema.getNamespace())
                          .tableCount(schema.getEntityTypes().size())
                          .build();

                  if (!CollectionUtils.isEmpty(schema.getEntityTypes())) {
                    schemaBean.setTableList(
                        getTableMetadataBean(schema, token, info, dbId, schemaId));
                  }

                  if (!CollectionUtils.isEmpty(schema.getComplexTypes())) {
                    List<PreAnalysisComplexTypeMapperBean> complexTypes =
                        schema.getComplexTypes().stream()
                            .map(
                                complexType ->
                                    PreAnalysisComplexTypeMapperBean.builder()
                                        .complexTypeName(complexType.getName())
                                        .properties(
                                            complexType.getProperties().stream()
                                                .map(
                                                    p ->
                                                        buildColumnMapperBean(
                                                            p.getName(),
                                                            p.getType(),
                                                            ColumnType.COMPLEX,
                                                            0, // Adjust ordinal as needed
                                                            "", // No table ID
                                                            schemaId,
                                                            "", // No DB ID
                                                            ""))
                                                .collect(Collectors.toList()))
                                        .build())
                            .collect(Collectors.toList());
                    schemaBean.setComplexTypes(complexTypes);
                  }



                  return schemaBean;
                })
            .collect(Collectors.toList());
  }

  private List<PreAnalysisTableMapperBean> getTableMetadataBean(
      CsdlSchema schema, String token, D365ConnectionInfo info, String dbId, String schemaId) {

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
              String tableId = UUID.randomUUID().toString();
              PreAnalysisTableMapperBean tableMapperBean =
                  PreAnalysisTableMapperBean.builder()
                      .tableName(entityType.getName())
                      .archonFormattedTableName(entityType.getName().toUpperCase())
                      .keywordTableName(entityType.getName().toLowerCase() + "_keyword")
                      .type("TABLE")
                      .schemaId(schemaId)
                      .schemaName(schema.getAlias())
                      .datasourceId(dbId)
                      .databaseName(schema.getNamespace())
                      .columnCount(entityType.getProperties().size())
                      .containsBlob(checkClobAndBlobPresent(entityType, "Edm.Binary"))
                      .containsClob(checkClobAndBlobPresent(entityType, "Edm.String"))
                      .isRelationship(!entityType.getNavigationProperties().isEmpty())
                      .columnList(
                          getColumnMapperBean(
                              entityType,
                              tableId,
                              schemaId,
                              schema.getAlias(),
                              dbId,
                              schema.getNamespace()))
                      .relatedTable(utils.getRelatedTable(entityType))
                      .relationDetails(getRelationDetails(entityType))
                      .entitySetUrl(entitySetUrl)
                      .build();

              // Process Bound Actions
              List<PreAnalysisActionMapperBean> actions =
                  schema.getActions().stream()
                      .filter(action -> isBoundToEntity(action, entityType.getName()))
                      .map(
                          action ->
                              PreAnalysisActionMapperBean.builder()
                                  .actionName(action.getName())
                                  .isBound(action.isBound())
                                  .boundEntity(entityType.getName())
                                  .parameters(
                                      action.getParameters().stream()
                                          .map(p -> p.getName() + ":" + p.getType())
                                          .collect(Collectors.toList()))
                                  .returnType(action.getReturnType().getType())
                                  .build())
                      .collect(Collectors.toList());
              tableMapperBean.setActionList(actions);

              // Process Bound Functions
              List<PreAnalysisFunctionMapperBean> functions =
                  schema.getFunctions().stream()
                      .filter(func -> isBoundToEntity(func, entityType.getName()))
                      .map(
                          func ->
                              PreAnalysisFunctionMapperBean.builder()
                                  .functionName(func.getName())
                                  .isBound(func.isBound())
                                  .boundEntity(entityType.getName())
                                  .parameters(
                                      func.getParameters().stream()
                                          .map(p -> p.getName() + ":" + p.getType())
                                          .collect(Collectors.toList()))
                                  .returnType(func.getReturnType().getType())
                                  .build())
                      .collect(Collectors.toList());
              tableMapperBean.setFunctionList(functions);
              return tableMapperBean;
            })
        .collect(Collectors.toList());
  }

  private boolean isBoundToEntity(CsdlOperation operation, String entityName) {
    return operation.isBound() && operation.getParameters().get(0).getType().contains(entityName);
  }

  private List<RelationDetails> getRelationDetails(CsdlEntityType entityType) {
    return entityType.getNavigationProperties().stream()
        .map(
            np -> {
              RelationDetails relationDetails =
                  RelationDetails.builder()
                      .relationName(np.getName())
                      .partnerName(np.getPartner())
                      .isCollection(np.isCollection())
                      .build();
              if (relationDetails.getIsCollection()
                  && CollectionUtils.isEmpty(np.getReferentialConstraints())) {
                relationDetails.setPkTableName(entityType.getName());
                String[] split = np.getType().split(".");
                relationDetails.setFkTableName(split[split.length - 1]);
              } else if (!relationDetails.getIsCollection()
                  && !CollectionUtils.isEmpty(np.getReferentialConstraints())) {
                relationDetails.setFkTableName(entityType.getName());
                String[] split = np.getType().split(".");
                relationDetails.setPkTableName(split[split.length - 1]);

                // columns
                List<String> properties =
                    np.getReferentialConstraints().stream()
                        .map(CsdlReferentialConstraint::getProperty)
                        .collect(Collectors.toList());

                List<String> referencedProperties =
                    np.getReferentialConstraints().stream()
                        .map(CsdlReferentialConstraint::getReferencedProperty)
                        .collect(Collectors.toList());
                if (properties.size() > 1 && referencedProperties.size() > 1) {
                  relationDetails.setIsComposite(true);
                }
                relationDetails.setPkColumnName(referencedProperties);
                relationDetails.setFkColumnName(properties);
              }
              return relationDetails;
            })
        .collect(Collectors.toList());
  }

  private Boolean checkClobAndBlobPresent(CsdlEntityType entityType, String dataType) {
    return entityType.getProperties().stream()
        .anyMatch(csdlProperty -> csdlProperty.getType().equalsIgnoreCase(dataType));
  }

  private List<PreAnalysisColumnMapperBean> getColumnMapperBean(
      CsdlEntityType entityType,
      String tableId,
      String schemaId,
      String schemaName,
      String dbId,
      String dbName) {
    List<PreAnalysisColumnMapperBean> columnMapperBeans = new ArrayList<>();
    AtomicInteger ordinal = new AtomicInteger(0);

    entityType
        .getKey()
        .forEach(
            property ->
                columnMapperBeans.add(
                    buildColumnMapperBean(
                        property.getName(),
                        "Edm.Guid",
                        ColumnType.PRIMARY,
                        ordinal.getAndIncrement(),
                        tableId,
                        schemaId,
                        dbId,
                        dbName)));
    // Process normal columns
    columnMapperBeans.addAll(
        entityType.getProperties().stream()
            .map(
                property -> {
                  PreAnalysisColumnMapperBean bean =
                      buildColumnMapperBean(
                          property.getName(),
                          property.getType(),
                          ColumnType.NORMAL,
                          ordinal.getAndIncrement(),
                          tableId,
                          schemaId,
                          dbId,
                          dbName);
                  addAdditionalConfig(bean, property);
                  return bean;
                })
            .collect(Collectors.toList()));

    return columnMapperBeans;
  }

  private void addAdditionalConfig(PreAnalysisColumnMapperBean bean, CsdlProperty property) {
    bean.setCollection(property.isCollection());
    bean.setMimeType(property.getMimeType());
    bean.setMapping(property.getMapping() != null ? property.getMapping().toString() : null);
    bean.setDefaultValue(property.getDefaultValue());
    bean.setNullable(property.isNullable());
    bean.setMaxLength(property.getMaxLength());
    bean.setPrecision(property.getPrecision());
    bean.setScale(property.getScale());
    bean.setUnicode(property.isUnicode());

    if (property.getSrid() != null) {
      bean.setSrid(property.getSrid().toString());
    }

    if (property.getAnnotations() != null) {
      bean.setAnnotations(property.getAnnotations().toString());
    }
  }

  private PreAnalysisColumnMapperBean buildColumnMapperBean(
      String columnName,
      String type,
      ColumnType columnType,
      int ordinal,
      String tableId,
      String schemaId,
      String dbId,
      String dbName) {

    return PreAnalysisColumnMapperBean.builder()
        .columnName(columnName)
        .archonFormattedColumnName(columnName.toUpperCase())
        .dataType(type)
        .ordinalPosition(ordinal)
        .columnType(columnType)
        .archonColumntype(ArchonDataCategory.getTypeCategory(type, 0, 0))
        .keywordColumnName(columnName.toUpperCase() + "_keyword")
        .tableId(tableId)
        .schemaId(schemaId)
        .datasourceId(dbId)
        .databaseName(dbName)
        .build();
  }
}
