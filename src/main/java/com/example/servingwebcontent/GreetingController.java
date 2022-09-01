package com.example.servingwebcontent;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/** GreetingController. */
@Controller
public class GreetingController {
  static final Logger logger = Logger.getLogger(GreetingController.class.getName());
  private Map<String, SampleElement> sampleElementMap;

  /**
   * get greeting.
   *
   * @param name name.
   * @param model model.
   * @return greeting
   */
  @GetMapping("/greeting")
  public String greeting(
      @RequestParam(name = "name", required = false, defaultValue = "World") String name,
      Model model) {
    SampleElement data1 = new SampleElement();
    data1.setValue("name1");
    data1.setEditable(true);
    data1.setTop(10);
    data1.setLeft(20);
    SampleElement data2 = new SampleElement();
    data2.setValue("name2");
    data2.setEditable(false);
    data2.setTop(30);
    data2.setLeft(40);
    Map<String, SampleElement> map = new HashMap<>();
    map.put("target1", data1);
    map.put("target2", data2);
    this.sampleElementMap = map;
    model.addAttribute("name", name);
    model.addAttribute("sampleElementMap", this.sampleElementMap);
    return "greeting";
  }

  /**
   * post updateFrag.
   *
   * @param sampleParam editableLine.
   * @param model model.
   * @return greeting :: testfrag
   */
  @PostMapping("/updateFrag")
  public String updateFrag(@RequestBody SampleParam sampleParam, Model model) {
    String id = sampleParam.getId();
    logger.info(id);
    this.sampleElementMap.entrySet().stream()
        .filter(e -> !id.equals(e.getKey()))
        .forEach(e -> e.getValue().setEditable(false));
    toggleEditable(id);
    model.addAttribute("sampleElementMap", this.sampleElementMap);
    return "greeting :: samplefrag";
  }

  private void toggleEditable(String id) {
    SampleElement element = this.sampleElementMap.get(id);
    element.setEditable(!element.getEditable());
    this.sampleElementMap.replace(id, element);
  }
}
