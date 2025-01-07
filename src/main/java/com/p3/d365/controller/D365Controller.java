package com.p3.d365.controller;

import com.p3.d365.beans.D365ConnectionInfo;
import com.p3.d365.service.D365Processor;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/d365")
public class D365Controller {
  private final D365Processor d365Processor;

  @PostMapping("/start-pre-analysis")
  public String startPreAnalysis(@Valid @RequestBody D365ConnectionInfo info) throws IOException {
    return d365Processor.processPreAnalysis(info);
  }
}
