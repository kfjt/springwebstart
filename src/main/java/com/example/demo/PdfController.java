package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** pdf. */
@Controller
public class PdfController {

  /**
   * GET /pdf.
   *
   * @return pdf
   */
  @GetMapping("/pdf")
  public String getMapPdf() {
    return "pdf";
  }
}
