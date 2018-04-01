public class CodeWriter {

    private String translatedCode = "";

    private String arithmeticDoubleIn = "@SP" +
                                        System.getProperty("line.separator") +
                                        "AM=M-1" +
                                        System.getProperty("line.separator") +
                                        "D=M" +
                                        System.getProperty("line.separator") +
                                        "A=A-1";

    private String arithmeticSingleIn = "@SP" +
                                        System.getProperty("line.separator") +
                                        "A=M-1";

    private String arithmeticComparisonOne =  "@SP" +
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


    private String arithmeticComparisonTwo =    "@_2" +
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

    private String pushConstant = "D=A" +
                                  System.getProperty("line.separator") +
                                  "@SP" +
                                  System.getProperty("line.separator") +
                                  "AM=M+1" +
                                  System.getProperty("line.separator") +
                                  "A=A-1" +
                                  System.getProperty("line.separator") +
                                  "M=D";

    public void writeArithmetic(String command)
    {
        if(command.equals("add")) {
            translatedCode =
                            arithmeticDoubleIn +
                            System.getProperty("line.separator") +
                            "M=D+M";
        }
        else if(command.equals("sub")) {
            translatedCode =
                            arithmeticDoubleIn +
                            System.getProperty("line.separator") +
                            "M=M-D";
        }
        else if(command.equals("or")) {
            translatedCode =
                            arithmeticDoubleIn +
                            System.getProperty("line.separator") +
                            "M=D|M";
        }
        else if(command.equals("and")) {
            translatedCode =
                            arithmeticDoubleIn +
                            System.getProperty("line.separator") +
                            "M=D&M";
        }
        else if(command.equals("not")) {
            translatedCode =
                            arithmeticSingleIn +
                            System.getProperty("line.separator") +
                            "M=!M";
        }
        else if(command.equals("not")) {
            translatedCode =
                            "D=0" +
                            System.getProperty("line.separator") +
                            arithmeticSingleIn +
                            System.getProperty("line.separator") +
                            "M=D-M";
        }
        else if(command.equals("eq"))
        {
            translatedCode =
                            arithmeticComparisonOne +
                            System.getProperty("line.separator") +
                            "D;JEQ" +
                            System.getProperty("line.separator") +
                            arithmeticComparisonTwo;
        }
        else if(command.equals("gt"))
        {
            translatedCode =
                    arithmeticComparisonOne +
                            System.getProperty("line.separator") +
                            "D;JGT" +
                            System.getProperty("line.separator") +
                            arithmeticComparisonTwo;

        }
        else if(command.equals("lt"))
        {
            translatedCode =
                    arithmeticComparisonOne +
                            System.getProperty("line.separator") +
                            "D;JLT" +
                            System.getProperty("line.separator") +
                            arithmeticComparisonTwo;

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
                                 pushConstant;
            }
        }
        else if(command.equals("pull"))
        {

        }
    }

    public String getTranslatedCode()
    {
        return translatedCode;
    }

}
