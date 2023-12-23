
package com.offsidegaming.measurer.service.mapper;

import com.offsidegaming.measurer.entity.Measurement;
import com.offsidegaming.measurer.service.dto.MeasurementDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {

    MeasurementDTO toMeasurementDto(Measurement measurement);

    default Page<MeasurementDTO> toMeasurementDtoPage(Page<Measurement> measurementEntities) {
        if (measurementEntities == null) {
            return Page.empty();
        }
        return new PageImpl<>(toMeasurementDtoList(measurementEntities.toList()));
    }

    List<MeasurementDTO> toMeasurementDtoList(List<Measurement> measurementDTOS);

    Measurement toMeasurementEntity(MeasurementDTO measurementEntity);

}
