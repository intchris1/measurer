package com.offsidegaming.measurer.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.offsidegaming.measurer.service.MeasurementService;
import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import com.offsidegaming.measurer.service.dto.PostView;
import com.offsidegaming.measurer.service.dto.SearchMeasurementCriteria;
import com.offsidegaming.measurer.util.PageableWithoutSortAsDefault;
import com.offsidegaming.measurer.util.PaginationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/measurements")
@SecurityRequirement(name = "user-auth")
public class MeasurementResource {

    private static final String CONTROLLER_TAG = "Методы для работы с измерениями (воды, газа)";

    private final MeasurementService measurementService;

    @Operation(
            description = "Добавление нового измерения",
            tags = CONTROLLER_TAG,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен",
                            content = @Content(schema = @Schema(implementation = MeasurementDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "409", description = "Указанные даты конфликтуют с ранее созданными измерениями пользователя",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "403", description = "Указанное имя пользователя не соответствует имени пользователя из аутентификации",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<MeasurementDTO> createMeasurement(@RequestBody @Valid @JsonView(PostView.class) MeasurementDTO request) {
        return ResponseEntity.ok(measurementService.createMeasurement(request));
    }

    @Operation(
            description = "Поиск измерений по критерии с пагинацией",
            tags = CONTROLLER_TAG,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/search")
    @PageableWithoutSortAsDefault
    public ResponseEntity<List<MeasurementDTO>> searchMeasurements(@Parameter(hidden = true) Pageable pageable, @RequestBody SearchMeasurementCriteria criteria) {
        Page<MeasurementDTO> measurements = measurementService.searchMeasurements(pageable, criteria);
        return ResponseEntity.ok()
                .headers(PaginationUtils.generatePaginationHttpHeaders(measurements, "/api/measurement/search"))
                .body(measurements.getContent());
    }
}
