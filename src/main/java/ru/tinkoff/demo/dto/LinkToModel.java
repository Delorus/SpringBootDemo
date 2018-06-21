package ru.tinkoff.demo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO that contains only id of related entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LinkToModel {

    @NotNull
    @Min(1)
    private Long id;
}
