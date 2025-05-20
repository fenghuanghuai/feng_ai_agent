package com.qcdfz.fengaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TimeToolTest {
        TimeTool timeTool = new TimeTool();
    @Test
    void getCurrentTime() {
        Map<String, String> timeInfo = timeTool.getCurrentTime();
        assertNotNull(timeInfo);
        assertTrue(timeInfo.containsKey("date"));
        assertTrue(timeInfo.containsKey("time"));
        assertTrue(timeInfo.containsKey("datetime"));
        assertTrue(timeInfo.containsKey("timestamp"));
    }

    @Test
    void calculateDaysBetween() {
        long days = timeTool.calculateDaysBetween("2024-01-01", "2024-01-10");
        assertEquals(9, days);
    }

    @Test
    void formatTimestamp() {
        String formattedTime = timeTool.formatTimestamp(System.currentTimeMillis());
        assertNotNull(formattedTime);
        assertTrue(formattedTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void getDayStartEndTime() {
        Map<String, String> timeRange = timeTool.getDayStartEndTime("2024-01-01");
        assertNotNull(timeRange);
        assertEquals("2024-01-01 00:00:00", timeRange.get("startTime"));
        assertEquals("2024-01-01 23:59:59", timeRange.get("endTime"));
    }

    @Test
    void isDateInRange() {
        boolean inRange = timeTool.isDateInRange("2024-01-15", "2024-01-01", "2024-01-31");
        assertTrue(inRange);

        boolean outOfRange = timeTool.isDateInRange("2024-02-01", "2024-01-01", "2024-01-31");
        assertFalse(outOfRange);
    }
}
