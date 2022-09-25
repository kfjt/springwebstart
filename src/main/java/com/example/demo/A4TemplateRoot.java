package com.example.demo;

import java.util.List;
import lombok.Data;

/** A4 Template. */
@Data
public class A4TemplateRoot {
  private String basePdf;
  private List<Schema> schemas;

  /** A4 Schema. */
  @Data
  public static class Schema {
    private Field a;
    private Field b;
    private Field c;
  }

  /** A4 Field. */
  @Data
  public static class Field {
    private String type;
    private Position position;
    private Integer width;
    private Integer height;
  }

  /** A4 Position. */
  @Data
  public static class Position {
    private Integer x;
    private Integer y;
  }
}
