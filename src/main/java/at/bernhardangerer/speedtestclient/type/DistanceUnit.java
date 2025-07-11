package at.bernhardangerer.speedtestclient.type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DistanceUnit {
    MILE("mi"),
    KILOMETER("km"),
    NAUTICAL_MILE("nm");

    private final String abbreviation;

    DistanceUnit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static DistanceUnit fromAbbreviation(String abbreviation) {
        return Arrays.stream(values())
                .filter(enumValue -> enumValue.getAbbreviation().equals(abbreviation))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
