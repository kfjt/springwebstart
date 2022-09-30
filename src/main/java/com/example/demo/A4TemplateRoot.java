package com.example.demo;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** A4 Template. */
@Data
public class A4TemplateRoot {
  private String basePdf;
  private List<Schema> schemas;
  private List<A4TemplateSampledata> sampledata;

  /** A4 Schema. */
  @Data
  public static class Schema {
    private TextSchema a;
    private TextSchema b;
    private TextSchema c;
  }

  /** CommonSchema. */
  @Data
  public static class CommonSchema {
    private String type;
    private Position position;
    private Integer width;
    private Integer height;
  }

  /** TextSchema. */
  @Data
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class TextSchema extends CommonSchema {
    private String type = "text";
    private String backgroundColor;
  }

  /** A4 Position. */
  @Data
  public static class Position {
    private Integer x;
    private Integer y;
  }
}
