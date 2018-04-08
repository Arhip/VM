/**
 * CodeWriter translates each line of the VM code into assembly
 */

public class CodeWriter {

    private String translatedCode = "";
    private String fileName = "";

    /**
     * setFileName setter for fileName
     * @param fileName
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * writeArithmetic translates arithmetic calls
     * @param command
     */
    public void writeArithmetic(String command)
    {
        if(command.equals("add")) {
            translatedCode =
                            arithmeticDoubleIn() +
                            System.getProperty("line.separator") +
                            "M=D+M";
        }
        else if(command.equals("sub")) {
            translatedCode =
                            arithmeticDoubleIn() +
                            System.getProperty("line.separator") +
                            "M=M-D";
        }
        else if(command.equals("or")) {
            translatedCode =
                            arithmeticDoubleIn() +
                            System.getProperty("line.separator") +
                            "M=D|M";
        }
        else if(command.equals("and")) {
            translatedCode =
                            arithmeticDoubleIn() +
                            System.getProperty("line.separator") +
                            "M=D&M";
        }
        else if(command.equals("not")) {
            translatedCode =
                            arithmeticSingleIn() +
                            System.getProperty("line.separator") +
                            "M=!M";
        }
        else if(command.equals("neg")) {
            translatedCode =
                            "D=0" +
                            System.getProperty("line.separator") +
                            arithmeticSingleIn() +
                            System.getProperty("line.separator") +
                            "M=D-M";
        }
        else if(command.equals("eq"))
        {
            translatedCode = arithmeticComparison("JEQ");
        }
        else if(command.equals("gt"))
        {
            translatedCode = arithmeticComparison("JGT");
        }
        else if(command.equals("lt"))
        {
            translatedCode = arithmeticComparison("JLT");
        }

    }

    /**
     * writePushPop translates push pop methods
     * @param command
     * @param segment
     * @param index
     */
    public void writePushPop(String command, String segment, int index)
    {
        if(command.equals("push"))
        {
            if(segment.equals("constant"))
            {
                translatedCode = "@" + index +
                                 System.getProperty("line.separator") +
                                 pushConstant();
            }
            else if(segment.equals("static"))
            {
                translatedCode = "@" + fileName + "." + index +
                                 System.getProperty("line.separator") +
                                 "D=M" +
                                 System.getProperty("line.separator") +
                                 writePushD();
            }
            else if(segment.equals("this"))
            {
                translatedCode = pushHelper("THIS", index, false);
            }
            else if(segment.equals("local"))
            {
                translatedCode = pushHelper("LCL", index, false);
            }
            else if(segment.equals("argument"))
            {
                translatedCode = pushHelper("ARG", index, false);
            }
            else if(segment.equals("that"))
            {
                translatedCode = popHelper("THAT", index, false);
            }
            else if(segment.equals("temp"))
            {
                translatedCode = pushHelper("R5", index + 5, false);
            }
            else if(segment.equals("pointer") && index == 0)
            {
                translatedCode = pushHelper("THIS", index, true);
            }
            else if(segment.equals("pointer") && index == 1)
            {
                translatedCode = pushHelper("THAT", index, true);
            }
        }
        else if(command.equals("pop"))
        {
            if(segment.equals("static"))
            {
                translatedCode = "@" + fileName + "." + index +
                                System.getProperty("line.separator") +
                                "D=A" +
                                System.getProperty("line.separator") +
                                writePopTemp();
            }
            else if(segment.equals("this"))
            {
                translatedCode = popHelper("THIS", index, false);
            }
            else if(segment.equals("local"))
            {
                translatedCode = popHelper("LCL", index, false);
            }
            else if(segment.equals("argument"))
            {
                translatedCode = popHelper("ARG", index, false);
            }
            else if(segment.equals("that"))
            {
                translatedCode = popHelper("THAT", index, false);
            }
            else if(segment.equals("temp"))
            {
                translatedCode = popHelper("R5", index + 5, false);
            }
            else if(segment.equals("pointer") && index == 0)
            {
                translatedCode = popHelper("THIS", index, true);
            }
            else if(segment.equals("pointer") && index == 1)
            {
                translatedCode = popHelper("THAT", index, true);
            }
        }
    }

    //------------HELPERS-------------

    /**
     * pushHelper is used for non-constant pushes
     * @param segment
     * @param index
     * @param isPointer
     * @return
     */
    private String pushHelper(String segment, int index, boolean isPointer)
    {
        String referenceCode = "";
        if(isPointer)
        {
            referenceCode = "@" + index +
                            System.getProperty("line.separator") +
                            "A=D+A" +
                            System.getProperty("line.separator") +
                            "D=M";
        }

        return "@" + segment +
                System.getProperty("line.separator") +
                "D=M" +
                System.getProperty("line.separator") +
                referenceCode +
                System.getProperty("line.separator") +
                referenceCode +
                System.getProperty("line.separator") +
                writePushD();
    }

    /**
     * popHelper is used to build the translation for pop calls
     * @param segment
     * @param index
     * @param isPointer
     * @return
     */
    private String popHelper(String segment, int index, boolean isPointer)
    {
        String referenceCode = "";
        if(isPointer)
        {
            referenceCode = "D=A" +
                    System.getProperty("line.separator") +
                    "D=M" +
                    System.getProperty("line.separator") +
                    "@" + index +
                    System.getProperty("line.separator") +
                    "D=D+A";
        }

        return "@" + segment +
                System.getProperty("line.separator") +
                referenceCode +
                System.getProperty("line.separator") +
                writePopTemp();
    }

    /**
     * arithmeticDoubleIn translation used for 2 input arithmetic calls
     * @return
     */
    private String arithmeticDoubleIn() {
        return "@SP" +
                System.getProperty("line.separator") +
                "AM=M-1" +
                System.getProperty("line.separator") +
                "D=M" +
                System.getProperty("line.separator") +
                "A=A-1";
    }

    /**
     * arithetmicSingleIn translation used for 1 input arithmetic calls
     * @return
     */
    private String arithmeticSingleIn() {
        return "@SP" +
                System.getProperty("line.separator") +
                "A=M-1";
    }

    /**
     * arithmeticComparison translation used for boolean arithetmic calls
     * @param jump
     * @return
     */
    private String arithmeticComparison(String jump) {
        return "@SP" +
                System.getProperty("line.separator") +
                "A=M-1" +
                System.getProperty("line.separator") +
                "A=A-1" +
                System.getProperty("line.separator") +
                "D=M" +
                System.getProperty("line.separator") +
                "A=A+1" +
                System.getProperty("line.separator") +
                "D=D-M" +
                System.getProperty("line.separator") +
                "@_1" +
                System.getProperty("line.separator") +
                "D;" + jump +
                System.getProperty("line.separator") +
                "@_2" +
                System.getProperty("line.separator") +
                "D=0" +
                System.getProperty("line.separator") +
                "0;JMP" +
                System.getProperty("line.separator") +
                "(_1)" +
                System.getProperty("line.separator") +
                "D=-1" +
                System.getProperty("line.separator") +
                "(_2)" +
                System.getProperty("line.separator") +
                "@SP" +
                System.getProperty("line.separator") +
                "AM=M-1" +
                System.getProperty("line.separator") +
                "A=A-1" +
                System.getProperty("line.separator") +
                "M=D";
    }

    /**
     * writePushD standard translation to Push D
     * @return
     */
    private String writePushD() {
        return "@SP" +
                System.getProperty("line.separator") +
                "AM=M+1" +
                System.getProperty("line.separator") +
                "A=A-1" +
                System.getProperty("line.separator") +
                "M=D";
    }

    /**
     * writePopD standard translation to Pop D
     * @return
     */
    private String writePopD() {
        return "@SP" +
                System.getProperty("line.separator") +
                "AM=M-1" +
                System.getProperty("line.separator") +
                "D=M";
    }

    /**
     * writePopTemp used if a temp variable needs to be stored before popping
     * @return
     */
    private String writePopTemp()
    {
        return "@R13" +
                System.getProperty("line.separator") +
                "M=D" +
                System.getProperty("line.separator") +
                writePopD() +
                System.getProperty("line.separator") +
                "@R13" +
                System.getProperty("line.separator") +
                "A=M" +
                System.getProperty("line.separator") +
                "M=D";
    }

    /**
     * pushConstant used for pushing constants
     * @return
     */
    private String pushConstant() {
        return "D=A" +
                System.getProperty("line.separator") +
                writePushD();
    }

    /**
     * getTranslatedCode returns the translated code
     * @return
     */
    public String getTranslatedCode()
    {
        return translatedCode;
    }

}
