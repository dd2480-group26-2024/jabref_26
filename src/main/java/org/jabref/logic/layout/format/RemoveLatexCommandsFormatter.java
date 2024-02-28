package org.jabref.logic.layout.format;

import org.jabref.logic.layout.LayoutFormatter;
import org.jabref.model.strings.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class RemoveLatexCommandsFormatter implements LayoutFormatter {
    public static Map<Integer, Boolean> branchCoverage = new HashMap<>();

    private static class CommandStatus {
        boolean incommand;
        boolean escaped;

        CommandStatus(boolean incommand, boolean escaped) {
            this.incommand = incommand;
            this.escaped = escaped;
        }
    }

    private final StringBuilder cleanedField = new StringBuilder();
    private StringBuilder currentCommand = null;
    private char currentCharacter;
    private int currentFieldPosition;
    @Override
    public String format(String field) {

        CommandStatus status = new CommandStatus(false, false);

        for (currentFieldPosition = 0; currentFieldPosition < field.length(); currentFieldPosition++) {
            branchCoverage.put(1, true);
            currentCharacter = field.charAt(currentFieldPosition);
            if (status.escaped && (currentCharacter == '\\')) {
                branchCoverage.put(2, true);
                branchCoverage.put(3, true);
                cleanedField.append('\\');
                status.escaped = false;
                status.incommand = false;
            } else if (currentCharacter == '\\') {
                branchCoverage.put(4, true);
                status.escaped = true;
                status.incommand = true;
                currentCommand = new StringBuilder();
            } else if (!status.incommand && ((currentCharacter == '{') || (currentCharacter == '}'))) {
                branchCoverage.put(5, true);
                branchCoverage.put(6, true);
                branchCoverage.put(7, true);

            } else {
                branchCoverage.put(8, true);
                processLatexCommandCharacter( status, field);
            }
        }

        return cleanedField.toString();
    }

    private void processLatexCommandCharacter(CommandStatus status, String field) {
        if (Character.isLetter(currentCharacter) || StringUtil.SPECIAL_COMMAND_CHARS.contains(String.valueOf(currentCharacter))) {
            status.escaped = false;
            if (status.incommand) {
                currentCommand.append(currentCharacter);
                if ((currentCommand.length() == 1)
                        && StringUtil.SPECIAL_COMMAND_CHARS.contains(currentCommand.toString())) {
                    status.incommand = false;
                }
            } else {
                cleanedField.append(currentCharacter);
            }
        }
        else {
            if (!status.incommand || (!Character.isWhitespace(currentCharacter) && (currentCharacter != '{'))) {
                cleanedField.append(currentCharacter);
            } else {
                if (!Character.isWhitespace(currentCharacter) && (currentCharacter != '{')) {
                    cleanedField.append(currentCharacter);
                }
                while (currentFieldPosition + 1 < field.length() && Character.isWhitespace(field.charAt(currentFieldPosition + 1))) {
                    currentFieldPosition++;
                }
            }
            status.incommand = false;
            status.escaped = false;
        }
    }
}
