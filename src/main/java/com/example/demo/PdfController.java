package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/** pdf. */
@Controller
@RequestMapping("pdf")
public class PdfController {
  private Set<String> ls(Path dir, String ext) throws IOException {
    try (Stream<Path> stream = Files.list(dir)) {
      return stream
          .filter(file -> !Files.isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .filter(path -> path.endsWith(ext))
          .collect(Collectors.toSet());
    }
  }

  private A4TemplateRoot.TextSchema fieldText(int x, int y, int width, int height) {
    A4TemplateRoot.Position position = new A4TemplateRoot.Position();
    position.setX(x);
    position.setY(y);
    A4TemplateRoot.TextSchema field = new A4TemplateRoot.TextSchema();
    field.setPosition(position);
    field.setWidth(width);
    field.setHeight(height);
    field.setBackgroundColor("#ffffff");
    return field;
  }

  private A4TemplateRoot.Schema schmaTypeA() {
    A4TemplateRoot.TextSchema fieldA = fieldText(0, 0, 30, 40);
    A4TemplateRoot.TextSchema fieldB = fieldText(50, 50, 30, 40);
    A4TemplateRoot.TextSchema fieldC = fieldText(100, 100, 30, 40);
    A4TemplateRoot.Schema schema = new A4TemplateRoot.Schema();
    schema.setA(fieldA);
    schema.setB(fieldB);
    schema.setC(fieldC);
    return schema;
  }

  private A4TemplateSampledata sampledataTypeA(String a, String b, String c) {
    A4TemplateSampledata input = new A4TemplateSampledata();
    input.setA(a);
    input.setB(b);
    input.setC(c);
    return input;
  }

  private Set<String> lsPdf(Path dir) throws IOException {
    return ls(dir, ".pdf");
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
    String id = UUID.randomUUID().toString();
    Path fileNameAndPath = DemoApplication.UPLOAD_DIRECTORY.resolve(Paths.get(id + ".pdf"));
    Files.write(fileNameAndPath, file.getBytes());

    model.addAttribute("id", id);
    return "pdf/saved";
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
    Set<String> filelist =
        lsPdf(DemoApplication.UPLOAD_DIRECTORY).stream()
            .map(StringUtils::stripFilenameExtension)
            .collect(Collectors.toSet());
    model.addAttribute("filelist", filelist);
    return "pdf/files";
  }

  /**
   * GET /pdf/ui/{id}.
   *
   * @return pdf/ui
   * @throws IOException readBase64
   */
  @GetMapping("ui/{id}")
  public String getUi(Model model, @PathVariable("id") String id) throws IOException {
    A4TemplateRoot template = new A4TemplateRoot();
    template.setSchemas(Arrays.asList(schmaTypeA()));
    template.setBasePdf(readPdfDataUri(id));
    A4TemplateSampledata sampledata = sampledataTypeA("a1", "b10\nb20", "c1");
    template.setSampledata(Arrays.asList(sampledata));
    model.addAttribute("template", template);
    return "pdf/ui";
  }

  /**
   * readBase64.
   *
   * @param id id
   * @return data uri
   * @throws IOException Files.readAllBytes
   */
  private String readPdfDataUri(String id) throws IOException {
    Path fileNameAndPath = DemoApplication.UPLOAD_DIRECTORY.resolve(Paths.get(id + ".pdf"));
    String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(fileNameAndPath));
    return "data:application/pdf;base64," + base64;
  }
}
