package com.ada.kafka.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Book {
    private Integer id;
    private String name;
    private String author;
}
