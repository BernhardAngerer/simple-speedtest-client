package at.bernhardangerer.speedtestclient.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CsvUtilTest {

    @Test
    void testNullValue() {
        assertEquals("", CsvUtil.formatCsvValue(null));
    }

    @Test
    void testSimpleString() {
        assertEquals("hello", CsvUtil.formatCsvValue("hello"));
    }

    @Test
    void testStringWithComma() {
        assertEquals("\"hello,world\"", CsvUtil.formatCsvValue("hello,world"));
    }

    @Test
    void testStringWithQuote() {
        assertEquals("\"he\"\"llo\"\"\"", CsvUtil.formatCsvValue("he\"llo\""));
    }

    @Test
    void testStringWithNewline() {
        assertEquals("\"hello\nworld\"", CsvUtil.formatCsvValue("hello\nworld"));
    }

    @Test
    void testIntegerValue() {
        assertEquals("42", CsvUtil.formatCsvValue(42));
    }

    @Test
    void testDoubleValue() {
        assertEquals("3.140000", CsvUtil.formatCsvValue(3.14));
    }

    @Test
    void testFloatValue() {
        assertEquals("2.720000", CsvUtil.formatCsvValue(2.72f));
    }

    @Test
    void testBigDecimalValue() {
        assertEquals("1234.567800", CsvUtil.formatCsvValue(new BigDecimal("1234.5678")));
    }

    @Test
    void testNumberWithCommaAfterFormatting() {
        // 1000.5 becomes "1000.500000" – no comma, so no quotes
        assertEquals("1000.500000", CsvUtil.formatCsvValue(1000.5));
    }

    @Test
    void testStringWithCarriageReturn() {
        assertEquals("\"line1\rline2\"", CsvUtil.formatCsvValue("line1\rline2"));
    }

    @Test
    void testStringStartingWithSpace() {
        assertEquals("\" hello\"", CsvUtil.formatCsvValue(" hello"));
    }

    @Test
    void testStringEndingWithSpace() {
        assertEquals("\"hello \"", CsvUtil.formatCsvValue("hello "));
    }

    @Test
    void testStringWithBothLeadingAndTrailingSpaces() {
        assertEquals("\" hello world \"", CsvUtil.formatCsvValue(" hello world "));
    }
}
