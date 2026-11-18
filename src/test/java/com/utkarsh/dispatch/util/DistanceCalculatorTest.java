package com.utkarsh.dispatch.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistanceCalculatorTest {

    @Test
    void shouldReturnZeroForSameCoordinates() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.6139, 77.2090
        );

        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void shouldCalculateDelhiToNoidaDistance() {

        double distance = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.5355, 77.3910
        );

        assertTrue(distance > 19 && distance < 21);
    }

    @Test
    void shouldReturnSameDistanceInReverseDirection() {

        double distance1 = DistanceCalculator.calculateDistance(
                28.6139, 77.2090,
                28.5355, 77.3910
        );

        double distance2 = DistanceCalculator.calculateDistance(
                28.5355, 77.3910,
                28.6139, 77.2090
        );

        assertEquals(distance1, distance2, 0.0001);
    }
}