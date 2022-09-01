package com.example.servingwebcontent;

import java.io.Serializable;
import lombok.Data;

/** SampleElement. */
@Data
public class SampleElement implements Serializable {
  private String value;
  private Boolean editable;
  private Integer top;
  private Integer left;
}
