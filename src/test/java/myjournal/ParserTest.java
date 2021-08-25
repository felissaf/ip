package myjournal;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    @Test
    public void testIsInteger() {
        assertEquals(true, Parser.isInteger("003"));
        assertEquals(true, Parser.isInteger("647"));
        assertEquals(false, Parser.isInteger("45.36"));
    }

    @Test
    public void testIsTime() {
        assertEquals(true, Parser.isTime("12:00"));
        assertEquals(false, Parser.isTime("hello"));
        assertEquals(false, Parser.isTime("1200"));
    }
}