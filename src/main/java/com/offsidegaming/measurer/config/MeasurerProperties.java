package com.offsidegaming.measurer.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "measurer")
@Data
@Validated
public class MeasurerProperties {

    private List<@Valid UserInfo> users = Collections.emptyList();

    @Data
    public static final class UserInfo {

        @NotNull
        private String username;

        @NotNull
        private String password;

        @NotNull
        private List<String> roles = Collections.emptyList();
    }
}
