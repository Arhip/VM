import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class VirtualMachine {

    public static void main(String[] args) {
        String inputFileName, outputFileName;
        PrintWriter outputFile = null; //keep compiler happy

        //get input file name from command line or console input
        if(args.length == 1) {
            System.out.println("command line arg = " + args[0]);
            inputFileName = args[0];
        }
        else
        {
            Scanner keyboard = new Scanner(System.in);

            System.out.println("Please enter assembly file name you would like to assemble.");
            System.out.println("Don't forget the .vm extension: ");
            inputFileName = keyboard.nextLine();

            keyboard.close();

        }

        outputFileName = inputFileName.substring(0,inputFileName.lastIndexOf('.')) + ".asm";

        try {
            outputFile = new PrintWriter(new FileOutputStream(outputFileName));

        } catch (FileNotFoundException ex) {
            System.err.println("Could not open output file " + outputFileName);
            System.err.println("Run program again, make sure you have write permissions, etc.");
            System.exit(0);
        }

        // TODO: finish driver as algorithm describes

        pass(inputFileName, outputFile);
        System.out.println("done");
        outputFile.close();
    }


    private static void pass(String inputFileName, PrintWriter outputFile) {
        Parser parser = new Parser(inputFileName);
        CodeWriter writer = new CodeWriter();
        writer.setFileName(inputFileName.substring(0, inputFileName.lastIndexOf('.')));
        while(parser.hasMoreCommands())
        {
            parser.advance();
            if(parser.getCommandType() == Parser.C_ARITHMETIC) {
                writer.writeArithmetic(parser.getCommand());
            }
            else if(parser.getCommandType() == Parser.C_POP || parser.getCommandType() == Parser.C_PUSH) {
                writer.writePushPop(parser.getCommand(), parser.getSegment(), parser.getIndex());
            }

            String output = writer.getTranslatedCode();
            outputFile.println(output);
        }

    }

}


