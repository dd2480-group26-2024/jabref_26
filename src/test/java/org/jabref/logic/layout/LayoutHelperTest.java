package org.jabref.logic.layout;

import java.io.IOException;
import java.io.StringReader;

import org.jabref.logic.journals.JournalAbbreviationRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LayoutHelperTest {

    private final LayoutFormatterPreferences layoutFormatterPreferences = mock(LayoutFormatterPreferences.class);
    private final JournalAbbreviationRepository abbreviationRepository = mock(JournalAbbreviationRepository.class);

    @Test
    public void backslashDoesNotTriggerException() {
        StringReader stringReader = new StringReader("\\");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        assertThrows(IOException.class, layoutHelper::getLayoutFromText);
    }

    @Test
    public void unbalancedBeginEndIsParsed() throws Exception {
        StringReader stringReader = new StringReader("\\begin{doi}, DOI: \\doi");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        Layout layout = layoutHelper.getLayoutFromText();
        assertNotNull(layout);
    }

    @Test
    public void minimalExampleWithDoiGetsParsed() throws Exception {
        StringReader stringReader = new StringReader("\\begin{doi}, DOI: \\doi\\end{doi}");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        Layout layout = layoutHelper.getLayoutFromText();
        assertNotNull(layout);
    }

    @Test
    public void testLastFiveEntriesAppendedToErrorMessage() throws IOException {
        StringReader stringReader = new StringReader("\\invalid");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        IOException exception = assertThrows(IOException.class, layoutHelper::getLayoutFromText);
        assertTrue(exception.getMessage().contains("Last five entries:"));
    }

    @Test
    public void testBegingroupCommandHandled() throws IOException {
        StringReader stringReader = new StringReader("\\begingroup");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        Layout layout = layoutHelper.getLayoutFromText();
        assertNotNull(layout);
    }

    @Test
    public void testFilenameCommandHandled() throws IOException {
        StringReader stringReader = new StringReader("\\filename{}");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        Layout layout = layoutHelper.getLayoutFromText();
        assertNotNull(layout);
    }

    @Test
    public void testFilepathCommandHandled() throws IOException {
        StringReader stringReader = new StringReader("\\filepath{}");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        Layout layout = layoutHelper.getLayoutFromText();
        assertNotNull(layout);
    }

    @Test
    public void testEndgroupCommandHandled() throws IOException {
        StringReader stringReader = new StringReader("\\endgroup");
        LayoutHelper layoutHelper = new LayoutHelper(stringReader, layoutFormatterPreferences, abbreviationRepository);
        Layout layout = layoutHelper.getLayoutFromText();
        assertNotNull(layout);
    }
}
