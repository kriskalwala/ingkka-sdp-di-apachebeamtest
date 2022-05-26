package com.ingka.sbp.di.poslogparse.xml;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Article {
  private Long id;
  private String title;
  private List<String> tags;
}