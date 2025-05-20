package ru.pin36bik.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YandexWeatherResponseDTO {
    private FactDTO fact;
    private ForecastDTO forecast;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FactDTO {
        private Integer temp;
        private Integer humidity;
        private Integer pressure;
        private String precType;
        private String precStrength;
        private String windDirection;
        private Float windSpeed;
        private String condition;
        private String cloudiness;
        private String daytime;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ForecastDTO {
        private List<PartDTO> parts;

        @Getter @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class PartDTO {
            private String partName;
            private Integer tempMin;
            private Integer tempMax;
            private String condition;
            private String daytime;
        }
    }
}
