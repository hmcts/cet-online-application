package uk.gov.hmcts.reform.cet.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonUtilsTest {

    private  File testFile;

    private List<String> content;

    private Dummy dummy;

    @Before
    public void setUp() throws Exception {
        testFile = JsonUtils.getTestFile("util/dummy");
        content = Files.readAllLines(Paths.get(testFile.getPath()));
        dummy = JsonUtils.toObjectFromJson(content.get(0), Dummy.class);
    }

    @Test
    public void testGetTestFile() throws Exception {
        assertTrue(testFile.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() throws FileNotFoundException {
        JsonUtils.getTestFile("util/idontexist");
    }

    @Test
    public void testGetJsonInput() throws Exception  {
        String testContent = JsonUtils.getJsonInput("util/dummy");
        assertEquals(content.get(0), testContent);
    }

    @Test
    public void testToObjectFromTestName() throws Exception {
        Dummy dummy = JsonUtils.toObjectFromTestName("util/dummy", Dummy.class);
        assertEquals("case_123", dummy.getCaseId());
    }

    @Test
    public void testToObjectFromJson() throws Exception {
        assertEquals("case_123", dummy.getCaseId());
    }

    @Test
    public void testToJson() throws Exception {
        String serializedContent = JsonUtils.toJson(dummy);
        assertEquals(content.get(0), serializedContent);
    }
}