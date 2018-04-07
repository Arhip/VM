public class CodeWriter {

    private String translatedCode = "";

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
        else if(command.equals("not")) {
            translatedCode =
                            "D=0" +
                            System.getProperty("line.separator") +
                            arithmeticSingleIn() +
                            System.getProperty("line.separator") +
                            "M=D-M";
        }
        else if(command.equals("eq"))
        {
            translatedCode =
                            arithmeticComparisonOne() +
                            System.getProperty("line.separator") +
                            "D;JEQ" +
                            System.getProperty("line.separator") +
                            arithmeticComparisonTwo();
        }
        else if(command.equals("gt"))
        {
            translatedCode =
                    arithmeticComparisonOne() +
                            System.getProperty("line.separator") +
                            "D;JGT" +
                            System.getProperty("line.separator") +
                            arithmeticComparisonTwo();

        }
        else if(command.equals("lt"))
        {
            translatedCode =
                    arithmeticComparisonOne() +
                            System.getProperty("line.separator") +
                            "D;JLT" +
                            System.getProperty("line.separator") +
                            arithmeticComparisonTwo();

        }

    }

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
                translatedCode =
            }
            else if(segment.equals("this"))
            {
                translatedCode = pushHelper("THIS", index);
            }
            else if(segment.equals("local"))
            {
                translatedCode = pushHelper("LCL", index);
            }
            else if(segment.equals("argument"))
            {
                translatedCode = pushHelper("ARG", index);
            }
            else if(segment.equals("that"))
            {
                translatedCode = popHelper("THAT", index);
            }
            else if(segment.equals("temp"))
            {
                translatedCode = pushHelper("R5", index + 5);
            }
            else if(segment.equals("pointer") && index == 0)
            {
                translatedCode = pushHelper("THIS", index);
            }
            else if(segment.equals("pointer") && index == 1)
            {
                translatedCode = pushHelper("THAT", index);
            }
        }
        else if(command.equals("pop"))
        {

        }
    }

    //------------HELPERS-------------
    private String pushHelper(String segment, int index)
    {
        String referenceCode = "";
        if(segment.equals("pointer"))
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

    private String popHelper(String segment, int index)
    {
        String referenceCode = "";
        if(segment.equals("pointer"))
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
                "@R13" +
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
    private String arithmeticDoubleIn() {
        return "@SP" +
                System.getProperty("line.separator") +
                "AM=M-1" +
                System.getProperty("line.separator") +
                "D=M" +
                System.getProperty("line.separator") +
                "A=A-1";
    }

    private String arithmeticSingleIn() {
        return "@SP" +
                System.getProperty("line.separator") +
                "A=M-1";
    }

    private String arithmeticComparisonOne() {
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
                "@_1";
    }


    private String arithmeticComparisonTwo() {
        return "@_2" +
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

    private String writePushD() {
        return "@SP" +
                System.getProperty("line.separator") +
                "AM=M+1" +
                System.getProperty("line.separator") +
                "A=A-1" +
                System.getProperty("line.separator") +
                "M=D";
    }

    private String writePopD() {
        return "@SP" +
                System.getProperty("line.separator") +
                "AM=M-1" +
                System.getProperty("line.separator") +
                "D=M";
    }

    private String pushConstant() {
        return "D=A" +
                System.getProperty("line.separator") +
                writePushD();
    }

    public String getTranslatedCode()
    {
        return translatedCode;
    }

}
