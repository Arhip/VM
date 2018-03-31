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
    private String reference = "";
    private int index = 0;

    public static final char C_ARITHMETIC = 'A';
    public static final char C_PUSH = 'U';
    public static final char C_POP = 'O';
    public static final char C_EMPTY = 'N';

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

    public void advance() {
        rawLine = inputFile.nextLine();
        cleanLine();
        parse();
        parseCommandType();
        lineNumber++;
    }

    public void parseCommandType()
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

    public void parse()
    {
        String[] arguments = cleanLine.split("\\s+");

        if(arguments.length == 1)
        {
            command = arguments[0];
        }
        else if(arguments.length == 3)
        {
            command = arguments[0];
            reference = arguments[1];
            index = Integer.parseInt(arguments[2]);
        }

    }

    public char getCommandType()
    {
        return this.commandType;
    }

    private void cleanLine() {
        String cleanLine = rawLine;
        int commentLocation = cleanLine.indexOf("//");

        if (commentLocation != -1) {
            cleanLine = cleanLine.substring(0, commentLocation);
        }
        this.cleanLine = cleanLine;
    }

    private int getLineNumber()
    {
        return lineNumber;
    }
}
