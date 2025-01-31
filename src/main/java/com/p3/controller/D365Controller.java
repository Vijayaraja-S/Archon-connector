package com.p3.controller;

import java.io.IOException;
import javax.validation.Valid;

import com.p3.beans.request.DatasourceProfileRequestDTO;
import com.p3.service.connection.ConnectionManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/d365")
public class D365Controller {
  final ConnectionManagerService preAnalysis;

  @PostMapping("/start-pre-analysis")
  public String startPreAnalysis(@Valid @RequestBody DatasourceProfileRequestDTO datasourceProfileRequestDTO) throws IOException {
    return "";
  }
}
