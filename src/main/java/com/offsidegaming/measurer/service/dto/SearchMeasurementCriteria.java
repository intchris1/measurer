package com.offsidegaming.measurer.service.dto;

import com.offsidegaming.measurer.service.dto.filter.InstantFilter;
import com.offsidegaming.measurer.service.dto.filter.LocalDateFilter;
import com.offsidegaming.measurer.service.dto.filter.StringFilter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "Критерий поиска измерений")
@Data
@Accessors(chain = true)
public class SearchMeasurementCriteria implements Serializable {

    @Serial
    private static final long serialVersionUID = 43L;

    @Schema(description = "Фильтр по дате создания", nullable = true)
    private InstantFilter createdDate;

    @Schema(description = "Фильтр по дате начала измерения", nullable = true)
    private LocalDateFilter startDate;

    @Schema(description = "Фильтр по дате окончания измерения", nullable = true)
    private LocalDateFilter endDate;

    @Schema(description = "Фильтр по идентификатору (полное совпадение)", nullable = true)
    private Long id;

    @Schema(description = "Фильтр по имени пользователя (полное совпадение)", nullable = true)
    private String username;

    @Schema(description = "Фильтр по типу измерения", nullable = true)
    private StringFilter measurementType;
}
