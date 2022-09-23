package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/** pdf. */
@Controller
@RequestMapping("pdf")
public class PdfController {
  public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

  private List<String> ls(String dir) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
      return stream.map(Path::getFileName).map(Path::toString).collect(Collectors.toList());
    }
  }

  /**
   * GET /pdf/ocr.
   *
   * @return pdf/ocr
   */
  @GetMapping("ocr")
  public String getOcr() {
    return "pdf/ocr";
  }

  /**
   * POST /pdf/ocr.
   *
   * @param model model
   * @param file file
   * @return pdf/files
   * @throws IOException ls
   */
  @PostMapping("ocr")
  public String postOcr(Model model, @RequestParam("pdfFile") MultipartFile file)
      throws IOException {
    StringBuilder fileNames = new StringBuilder();
    fileNames.append(file.getOriginalFilename());
    Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
    Files.write(fileNameAndPath, file.getBytes());

    model.addAttribute("filelist", ls(UPLOAD_DIRECTORY));
    return "pdf/files";
  }

  /**
   * GET /pdf/files.
   *
   * @param model model
   * @return pdf/files
   * @throws IOException ls
   */
  @GetMapping("files")
  public String getFiles(Model model) throws IOException {
    model.addAttribute("filelist", ls(UPLOAD_DIRECTORY));
    return "pdf/files";
  }

  /**
   * GET /pdf/ui.
   *
   * @return pdf/ui
   */
  @GetMapping("ui")
  public String getUi(Model model) {
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
    return "pdf/ui";
  }
}
