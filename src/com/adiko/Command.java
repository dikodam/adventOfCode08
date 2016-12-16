package com.adiko;

import static com.adiko.CommandType.*;

/**
 * Created by Adam on 16.12.2016.
 */
public class Command {

    private CommandType commandType;
    private Integer x;
    private Integer y;
    private Integer value;

    public Command(String command) {
        String[] splitCommand = command.split(" ");
        if (splitCommand.length == 2) {
            // rect axb
            commandType = RECT;
            String[] arguments = splitCommand[1].split("x");
            x = Integer.parseInt(arguments[0]);
            y = Integer.parseInt(arguments[1]);

        } else {
            String[] split = splitCommand[2].split("=");
            if ("column".equals(splitCommand[1])) {
                commandType = ROTATE_COLUMN;
                x = Integer.parseInt(split[1]);
                value = Integer.parseInt(splitCommand[4]);
            } else {
                commandType = ROTATE_ROW;
                y = Integer.parseInt(split[1]);
                value = Integer.parseInt(splitCommand[4]);
            }
        }
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getValue() {
        return value;
    }
}
