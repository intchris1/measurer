package com.offsidegaming.measurer.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.offsidegaming.measurer.entity.MeasurementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "Измерение")
public class MeasurementDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    @Schema(description = "Идентификатор")
    @JsonView(GetView.class)
    private long id;

    @Schema(description = "Имя пользователя", nullable = true, example = " ")
    @JsonView(PostView.class)
    private String username;

    @NotNull
    @Schema(description = "Тип измерения")
    @JsonView(PostView.class)
    private MeasurementType measurementType;

    @Positive
    @NotNull
    @Schema(description = "Значение измерения", example = "100")
    @JsonView(PostView.class)
    private BigDecimal measurement;

    @Schema(description = "Дата создания измерения", nullable = true)
    @JsonView(GetView.class)
    private Instant createdDate;

    @Schema(description = "Пользователь, создавший измерение", nullable = true)
    @JsonView(GetView.class)
    private String createdBy;

    @Schema(description = "Дата последнего изменения", nullable = true)
    @JsonView(GetView.class)
    private Instant lastModifiedDate;

    @Schema(description = "Пользователь, последний сделавший изменения", nullable = true)
    @JsonView(GetView.class)
    private String lastModifiedBy;

    @NotNull
    @Past
    @Schema(description = "Дата начала измерения", example = "2023-11-22")
    @JsonView(PostView.class)
    private LocalDate startDate;

    @NotNull
    @Past
    @Schema(description = "Дата окончания измерения", example = "2023-12-22")
    @JsonView(PostView.class)
    private LocalDate endDate;

    @AssertTrue(message = "Дата начала измерения должна быть раньше даты окончания измерения")
    @JsonIgnore
    @Schema(hidden = true)
    private boolean isDatesCorrect() {
        return startDate == null || endDate == null || startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }
}
