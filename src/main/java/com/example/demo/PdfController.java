package com.example.demo;

import java.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  public String getMapPdf(Model model) {
    A4TemplateRoot.Position positionA = new A4TemplateRoot.Position();
    positionA.setX(0);
    positionA.setY(0);
    A4TemplateRoot.Field fieldA = new A4TemplateRoot.Field();
    fieldA.setType("text");
    fieldA.setPosition(positionA);
    fieldA.setWidth(30);
    fieldA.setHeight(40);
    A4TemplateRoot.Position positionB = new A4TemplateRoot.Position();
    positionB.setX(50);
    positionB.setY(50);
    A4TemplateRoot.Field fieldB = new A4TemplateRoot.Field();
    fieldB.setType("text");
    fieldB.setPosition(positionB);
    fieldB.setWidth(30);
    fieldB.setHeight(40);
    A4TemplateRoot.Position positionC = new A4TemplateRoot.Position();
    positionC.setX(100);
    positionC.setY(100);
    A4TemplateRoot.Field fieldC = new A4TemplateRoot.Field();
    fieldC.setType("text");
    fieldC.setPosition(positionC);
    fieldC.setWidth(30);
    fieldC.setHeight(40);
    A4TemplateRoot.Schema schema = new A4TemplateRoot.Schema();
    schema.setA(fieldA);
    schema.setB(fieldB);
    schema.setC(fieldC);
    A4TemplateRoot template = new A4TemplateRoot();
    template.setSchemas(Arrays.asList(schema));
    model.addAttribute("template", template);
    A4TemplateInput input = new A4TemplateInput();
    input.setA("a1");
    input.setB("b10\nb20");
    input.setC("c1");
    model.addAttribute("inputs", Arrays.asList(input));
    return "pdf";
  }
}
