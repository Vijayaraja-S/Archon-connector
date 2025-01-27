package com.p3.service;

import static com.p3.utils.URLConstants.BASE_URL;

import com.p3.beans.D365ConnectionInfo;
import java.io.*;

import com.p3.beans.pre_analysis.PreAnalysisTableMapperBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.client.api.edm.xml.XMLMetadata;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class D365Processor {
  final ConnectionManager connectionManager;
  final OdataProcessor odataProcessor;

  public String processPreAnalysis(D365ConnectionInfo info) throws IOException {
    String accessToken = connectionManager.getAccessToken(info);
    String metadataUrl = info.getResourceUrl() + BASE_URL;
    XMLMetadata xmlMetadata = odataProcessor.getXMLMetadata(accessToken, metadataUrl);
    processXmlMetadata(xmlMetadata, info);
    return "metadata processed";
  }

  private void processXmlMetadata(XMLMetadata xmlMetadata, D365ConnectionInfo info) {
    xmlMetadata.getSchemas().forEach(this::initPreAnalysis);
  }

  private void initPreAnalysis(CsdlSchema schema) {
    schema.getEntityTypes().forEach(entityType -> {
      PreAnalysisTableMapperBean
              .builder()
              .tableName(entityType.getName())
              .archonFormattedTableName(entityType.getName().toUpperCase())
              .keywordTableName(entityType.getName().toLowerCase()+"_keyword")
              .type("Table")
              .schemaName(schema.getAlias())
              .build();
    });
  }
}
