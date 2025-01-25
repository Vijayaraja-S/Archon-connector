package com.p3.service;

import com.p3.beans.D365ConnectionInfo;
import com.p3.beans.metadata.ETLMetaData;
import com.p3.beans.metadata.ETLSchemaMetaData;
import com.p3.beans.metadata.ETLTableMetaData;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.commons.api.edm.provider.*;
import org.springframework.stereotype.Service;

import static com.p3.utils.URLConstants.BASE_URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class D365Processor {
  final ConnectionManager connectionManager;

  public String processPreAnalysis(D365ConnectionInfo info) throws IOException {
    String accessToken = connectionManager.getAccessToken(info);
    String metadataUrl = info.getResourceUrl() + BASE_URL;
    XMLMetadata xmlMetadata = connectionManager.getXMLMetadata(accessToken, metadataUrl);
    processXmlMetadata(xmlMetadata, info);
    return "metadata processed";
  }

  private void processXmlMetadata(XMLMetadata xmlMetadata, D365ConnectionInfo info) {
    List<CsdlSchema> schemas = xmlMetadata.getSchemas();
    ETLMetaData etlMetaData =
        ETLMetaData.builder()
            .type("D365")
            .databaseName("dynamic-365-etl")
            .connectionInfo(info)
            .isFullSelected(true)
            .schemaCount(schemas.size())
            .build();
    etlMetaData.setSchemaMetaDataList(
        schemas.stream()
            .map(
                schema ->
                    ETLSchemaMetaData.builder()
                        .name(schema.getNamespace())
                        .aliasName(schema.getAlias())
                        .archonFormattedName(
                            schema.getNamespace().toUpperCase() + "." + schema.getAlias())
                        .tableCount(schema.getEntityTypes().size())
                        .tableMetaDataList(getTableMetaDatList(schema, info))
                        .build())
            .collect(Collectors.toList()));
  }

  private List<ETLTableMetaData> getTableMetaDatList(CsdlSchema schema, D365ConnectionInfo info) {
    List<CsdlEntityType> entityTypes = schema.getEntityTypes();
    log.info(entityTypes.toString());
    return entityTypes.stream()
        .map(csdlEntityType -> ETLTableMetaData
                .builder()
                .name(csdlEntityType.getName())
                .build())
        .collect(Collectors.toList());
  }
}
