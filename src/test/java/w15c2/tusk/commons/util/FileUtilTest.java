package w15c2.tusk.commons.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import w15c2.tusk.commons.util.FileUtil;
import w15c2.tusk.testutil.SerializableTestClass;
import w15c2.tusk.testutil.TestUtil;

//@@author A0139708W
/**
 * Test for the file util class.
 *
 */
public class FileUtilTest {
    private static final File SERIALIZATION_FILE = new File(TestUtil.getFilePathInSandboxFolder("serialize.json"));
    private static final File TEST_FILE = new File(TestUtil.getFilePathInSandboxFolder("test.json"));


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getPath_validFolder() {
        assertEquals("folder" + File.separator + "sub-folder", FileUtil.getPath("folder/sub-folder"));
    }
    
    @Test
    public void getPath_nullParameter_assertionError() {
        thrown.expect(AssertionError.class);
        FileUtil.getPath(null);
    }
    
    public void getPath_noForwardSlash_assertionError() {
        thrown.expect(AssertionError.class);
        FileUtil.getPath("folder");
    }

    @Test
    public void serializeObjectToJsonFile_validObject_noExceptionThrown() throws IOException {
        SerializableTestClass serializableTestClass = new SerializableTestClass();
        serializableTestClass.setTestValues();

        FileUtil.serializeObjectToJsonFile(SERIALIZATION_FILE, serializableTestClass);

        assertEquals(FileUtil.readFromFile(SERIALIZATION_FILE), SerializableTestClass.JSON_STRING_REPRESENTATION);
    }

    @Test
    public void deserializeObjectFromJsonFile_validObject_noExceptionThrown() throws IOException {
        FileUtil.writeToFile(SERIALIZATION_FILE, SerializableTestClass.JSON_STRING_REPRESENTATION);

        SerializableTestClass serializableTestClass = FileUtil
                .deserializeObjectFromJsonFile(SERIALIZATION_FILE, SerializableTestClass.class);

        assertEquals(serializableTestClass.getName(), SerializableTestClass.getNameTestValue());
        assertEquals(serializableTestClass.getListOfLocalDateTimes(), SerializableTestClass.getListTestValues());
        assertEquals(serializableTestClass.getMapOfIntegerToString(), SerializableTestClass.getHashMapTestValues());
    }
    
    @Test
    public void createIfMissing_missingFile_returnTrue() throws IOException {
        TEST_FILE.delete();
        FileUtil.createIfMissing(TEST_FILE);
        assertTrue(TEST_FILE.exists());
    }
      
    @Test
    public void createIfMissing_existingFile_returnTrue() throws IOException {
        FileUtil.createIfMissing(TEST_FILE);
        assertTrue(TEST_FILE.exists());
    }
    
    @Test
    public void createFile_existingFile_returnFalse() throws IOException {
        FileUtil.createFile(TEST_FILE);
        assertFalse(FileUtil.createFile(TEST_FILE));
    }
    
    @Test
    public void createFile_missingFile_returnFalse() throws IOException {
        TEST_FILE.delete();
        FileUtil.createFile(TEST_FILE);
        assertTrue(TEST_FILE.exists());
    }
    
    @Test
    public void createDirs_emptyFile_exceptionThrown() throws IOException {
        thrown.expect(IOException.class);
        FileUtil.createDirs(new File(""));
    }
    
    
}