/**
 * Parser.java parses a line of the virtual machine code
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    private Scanner inputFile;
    private String cleanLine;
    private String rawLine;
    private int lineNumber;
    private char commandType = C_EMPTY;

    private String command = "";
    private String segment = "";
    private int index = 0;

    public static final char C_ARITHMETIC = 'A';
    public static final char C_PUSH = 'U';
    public static final char C_POP = 'O';
    public static final char C_EMPTY = 'N';

    /**
     * Parser constructor
     * Opens the input file stream
     * @param inFileName
     */
    public Parser(String inFileName){

        try {
            File file = new File(inFileName);
            inputFile = new Scanner(file);
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
            System.out.println("File not found program is now ending");
            System.exit(0);
        }
    }

    /**
     * hasMoreCommands check to see if input file has more commands
     * @return
     */
    public boolean hasMoreCommands() {
        if(inputFile.hasNext())
        {
            return true;
        }
        else{
            inputFile.close();
            return false;
        }
    }

    /**
     * advance processes one line from the virtual machine file
     */
    public void advance() {
        rawLine = inputFile.nextLine();
        cleanLine();
        parse();
        parseCommandType();
        lineNumber++;
    }

    /**
     * parseCommandType() parses the command type
     */
    private void parseCommandType()
    {
                if( command.equals("add") ||
                    command.equals("sub") ||
                    command.equals("neg") ||
                    command.equals("and") ||
                    command.equals("or")  ||
                    command.equals("not") ||
                    command.equals("eq")  ||
                    command.equals("gt")  ||
                    command.equals("lt")) {

                    commandType = C_ARITHMETIC;
                }

                else if( command.equals("push")) {
                    commandType = C_PUSH;
                }

                else if(command.equals("pop")) {
                    commandType = C_POP;
                }

                else{
                    commandType = C_EMPTY;
                }
    }

    /**
     * parse breaks the input string into three segments
     */
    private void parse()
    {
        String[] arguments = cleanLine.split("\\s+");

        if(arguments.length == 1)
        {
            command = arguments[0];
        }
        else if(arguments.length == 2)
        {
            command = arguments[0];
            segment = arguments[1];
        }
        else if(arguments.length == 3)
        {
            command = arguments[0];
            segment = arguments[1];
            index = Integer.parseInt(arguments[2]);
        }

    }

    //GETTERS AND SETTERS
    public char getCommandType()
    {
        return this.commandType;
    }

    public String getCommand()
    {
        return this.command;
    }

    public String getSegment()
    {
        return  this.segment;
    }

    public int getIndex()
    {
        return this.index;
    }

    private int getLineNumber()
    {
        return lineNumber;
    }

    /**
     * cleanLine() is a helper method that removes comments
     */
    private void cleanLine() {
        String cleanLine = rawLine;
        int commentLocation = cleanLine.indexOf("//");

        if (commentLocation != -1) {
            cleanLine = cleanLine.substring(0, commentLocation);
        }
        this.cleanLine = cleanLine;
    }

}
