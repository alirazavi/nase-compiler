using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Nase
{
    public static class Macro
    {
        public static string Environment()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO environment");

            b.AppendLine("PROGEP  IS @");
            b.AppendLine("% -----------------------------------------------------------------------");
            b.AppendLine("% And here we go....");
            b.AppendLine("% -----------------------------------------------------------------------");


            b.AppendLine("ACCU      GREG 0 %Accumulator");
            b.AppendLine("OP2       GREG 0 %Register for 2nd operand");
            b.AppendLine("FLAG      GREG 0 %Flag reg.");
            b.AppendLine("MINUSONE  GREG -1 %Reg. just holding -1; the value of TRUE");
            b.AppendLine("OFFSET    GREG 0 %Offset register");

            b.AppendLine("          LOC Data_Segment %Switch to Data_Segment by default");

            b.AppendLine("PROMPT    GREG @ %Pointer register to prompt string");
            b.AppendLine("          BYTE  \"?\",0,0,0,0,0,0,0");

            b.AppendLine("NEWLINE   GREG @ %Pointer register to newline string");
            b.AppendLine("          BYTE  #a,0,0,0,0,0,0,0");

            b.AppendLine();
            return b.ToString();
        }

        public static string OpenConstDefinitionTable()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO openConstDefinitionTable");
            b.AppendLine("CONST     GREG @ %Pointer register to const definition table");
            b.AppendLine();
            return b.ToString();
        }

        public static string DefineConstValue(int value)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO defineConstValue");
            b.AppendFormat("          OCTA {0} %Value of constant", value); b.AppendLine();
            b.AppendLine();
            return b.ToString();
        }

        public static string AllocateStaticMemory(ulong dataSize)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO allocateStaticMemory");
            b.AppendLine("ENVDSA    GREG @ %Base pointer register to that area");
            b.AppendFormat("          LOC @+{0}*8 %Set location pointer to end of data area", dataSize); b.AppendLine();
            b.AppendLine("");
            return b.ToString();
        }

        public static string ProgramStart()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO programStart");
            b.AppendLine("          LOC PROGEP %Switch to code segment; prog starts at end of prolog code");
            b.AppendLine("Main      SWYM 1,2,3 %Main program starts here with a noop instruction");
            b.AppendLine();
            return b.ToString();
        }

        public static string ProgramEnd()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO programEnd");
            b.AppendLine("End       TRAP 0,Halt,0 %Program end");
            b.AppendLine();
            return b.ToString();
        }

        public static string Label(string labelText)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO label");
            b.AppendFormat("{0} SWYM 1,2,3 %Jump destination filled with a noop instruction for syntactic reasons", labelText); b.AppendLine();
            b.AppendLine();
            return b.ToString();
        }

        public static string Jump(string target)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO jump");
            b.AppendFormat("          JMP {0} %Jump to destination", target); b.AppendLine();
            b.AppendLine();
            return b.ToString();
        }

        public static string JumpOnFalse(string target)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO jumpOnFalse");
            b.AppendFormat("          BNN ACCU,{0} %Jump to destination if ACCU >= 0", target); b.AppendLine();
            b.AppendLine();
            return b.ToString();
        }

        public static string DyadicOperator(Symbol opcode, ulong operand2)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO dyadicOperator");
            ComputeOffset(b, operand2);
            b.AppendLine("          LDO OP2,ENVDSA,OFFSET %%Load value of mem location based by ENVDSA + OFFSET into reg. for 2nd operand");

            string cmpString = "          CMP FLAG,ACCU,OP2 %Compare ACCU and Comp reg. Result in FLAG reg.";

            switch (opcode)
            {
                case Symbol.EQ_SYMBOL:
                    b.AppendLine(cmpString);
                    b.AppendLine("          ZSZ ACCU,FLAG,MINUSONE %Set ACCU to -1, if CMP ACCU,pchOperand2 yields equality");
                    break;
                case Symbol.NE_SYMBOL:
                    b.AppendLine(cmpString);
                    b.AppendLine("          ZSNZ ACCU,FLAG,MINUSONE %Set ACCU to -1, if CMP ACCU,pchOperand2 yields no equality");
                    break;
                case Symbol.LT_SYMBOL:
                    b.AppendLine(cmpString);
                    b.AppendLine("          ZSP ACCU,FLAG,MINUSONE %Set ACCU to -1, if CMP ACCU,pchOperand2 yields ACCU < pchOperand2");
                    break;
                case Symbol.LE_SYMBOL:
                    b.AppendLine(cmpString);
                    b.AppendLine("          ZSNN ACCU,FLAG,MINUSONE %Set ACCU to -1, if CMP ACCU,pchOperand2 yields ACCU <= pchOperand2");
                    break;
                case Symbol.GT_SYMBOL:
                    b.AppendLine(cmpString);
                    b.AppendLine("          ZSN ACCU,FLAG,MINUSONE %Set ACCU to -1, if CMP ACCU,pchOperand2 yields ACCU > pchOperand2");
                    break;
                case Symbol.GE_SYMBOL:
                    b.AppendLine(cmpString);
                    b.AppendLine("          ZSNP ACCU,FLAG,MINUSONE %Set ACCU to -1, if CMP ACCU,pchOperand2 yields ACCU >= pchOperand2");
                    break;
                case Symbol.AND_SYMBOL:
                    b.AppendLine("          AND ACCU,ACCU,OP2 %ACCU := ACCU && pchOperand2");
                    break;
                case Symbol.OR_SYMBOL:
                    b.AppendLine("          OR ACCU,ACCU,OP2 %ACCU := ACCU || pchOperand2");
                    break;
                case Symbol.PLUS_SYMBOL:
                    b.AppendLine("          ADD ACCU,ACCU,OP2 %ACCU := ACCU + pchOperand2");
                    break;
                case Symbol.MINUS_SYMBOL:
                    b.AppendLine("          SUB ACCU,OP2,ACCU %ACCU := ACCU - pchOperand2");
                    break;
                case Symbol.TIMES_SYMBOL:
                    b.AppendLine("          MUL ACCU,ACCU,OP2 %ACCU := ACCU * pchOperand2");
                    break;
                case Symbol.DIVIDE_SYMBOL:
                    b.AppendLine("          DIV ACCU,OP2,ACCU %ACCU := ACCU / pchOperand2");
                    break;
                case Symbol.MODULO_SYMBOL:
                    b.AppendLine("          DIV ACCU,OP2,ACCU %ACCU := ACCU / pchOperand2");
                    b.AppendLine("          GET ACCU,rR %ACCU := $rR (So ACCU modulo pchOperand2 is performed)");
                    break;
                default:
                    throw new Exception("Unknown opcode as DyadicOperator");
            }
            b.AppendLine();
            return b.ToString();
        }

        public static string MonadicOperator(Symbol opcode)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO monadicOperator");
            switch (opcode)
            {
                case Symbol.MINUS_SYMBOL:
                    b.AppendLine("          NEG ACCU,0,ACCU %ACCU := -ACCU");
                    break;
                default:
                    throw new Exception("Unknown opcode as MonadicOperator");
            }
            b.AppendLine();
            return b.ToString();
        }

        public static string LoadAccu(ulong addr)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO loadAccu");
            ComputeOffset(b, addr);
            b.AppendLine("          LDO ACCU,ENVDSA,OFFSET %Load value of mem location ENVDSA+OFFSET into ACCU");
            b.AppendLine();
            return b.ToString();
        }

        public static string StoreAccu(ulong addr)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO storeAccu");
            ComputeOffset(b, addr);
            b.AppendLine("          STO ACCU,ENVDSA,OFFSET %%Store value of ACCU into mem location ENVDSA+OFFSET");
            b.AppendLine();
            return b.ToString();
        }

        public static string LoadAccuImmed(ulong constAddr)
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO moveImmed");
            ComputeOffset(b, constAddr);
            b.AppendLine("          LDO ACCU,CONST,OFFSET %%Load value of const from mem location CONST+OFFSET into ACCU");
            b.AppendLine();
            return b.ToString();
        }

        public static string ReadValueIntoAccu()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO readIntoAccu");
            b.AppendLine("          LDA $255,PROMPT,0 %Adress of prompt string");
            b.AppendLine("          TRAP 0,Fputs,StdOut %Print out prompt string");
            b.AppendLine("          TRIP LsZahl,ACCU,0 %Integer einlesen");
            b.AppendLine();
            return b.ToString();
        }

        public static string WriteOutAccuValue()
        {
            StringBuilder b = new StringBuilder();
            b.AppendLine("% MACRO writeOutAccu");
            b.AppendLine("          TRIP DrZahl,ACCU,0 %Integer ausgeben");
            b.AppendLine("          LDA $255,NEWLINE,0 %Adress of newline string");
            b.AppendLine("          TRAP 0,Fputs,StdOut %Print out newline string");
            return b.ToString();
        }

        static void ComputeOffset(StringBuilder b, ulong offset)
        {
            b.AppendFormat("          SETL OFFSET,{0} %Load value of mem location into offset reg. for 2nd operand", offset); b.AppendLine();
            b.AppendLine("          8ADDU OFFSET,OFFSET,0 %Multiply offset by 8 to convert OCTA Adr. into Byte Adr.");
        }

        #region default prolog
        public static string DefaultProlog()
        {
            StringBuilder b = new StringBuilder();

            b.AppendLine("% Program prolog for NASE Compiler into destination maschine MMIX.");
            b.AppendLine("");
            b.AppendLine("% You do not have to understand the hand coded routines, just belive in them!");
            b.AppendLine("");
            b.AppendLine("% Currently only readInt and writeInt are supported");
            b.AppendLine("% Writing of a signed octabyte integer:");
            b.AppendLine("% Code: TRIP DrZahl,$n,0 %Where $n is the register containing the integer");
            b.AppendLine("% Reading of a signed octabyte integer:");
            b.AppendLine("% Code: TRIP LsZahl,$n,0 %Read a signed integer from the keyboard and assigns it to register $n");
            b.AppendLine("% These routines changes the content of register $255");
            b.AppendLine("");
            b.AppendLine("% Version 1.0 is based on an internet source from 8.9.2001 of ab@cs.fhm.edu");
            b.AppendLine("");
            b.AppendLine("");
            b.AppendLine("DrZahl    IS 0");
            b.AppendLine("LsZahl    IS 1");
            b.AppendLine("");
            b.AppendLine("          LOC 0");
            b.AppendLine("          JMP IO %Einsprung für TRIP Verzweigung nach IO");
            b.AppendLine("          LOC #100");
            b.AppendLine("");
            b.AppendLine("% GetInt, PutInt Routinen");
            b.AppendLine("% -----------------------------------------------------------------------");
            b.AppendLine("% Ausgeben");
            b.AppendLine("          PREFIX Prolog:");
            b.AppendLine("r         GREG 0 %Rest");
            b.AppendLine("Zahl      GREG 0 %zu behandelnde Zahl");
            b.AppendLine("Stellen   IS 23 %max. 23 Stellen");
            b.AppendLine("BUFFER    OCTA 0");
            b.AppendLine("          LOC (BUFFER+Stellen+4)&-4");
            b.AppendLine("Arg       OCTA BUFFER,Stellen");
            b.AppendLine("");
            b.AppendLine(":IO       GET $255,:rX %X der TRIP-instruction extrahieren");
            b.AppendLine("          SETML r,1");
            b.AppendLine("          AND r,r,$255");
            b.AppendLine("          BNZ r,LsZahl1");
            b.AppendLine("");
            b.AppendLine("          GETA $255,BUFFER+Stellen+1 %positioniern");
            b.AppendLine("          GET Zahl,:rY");
            b.AppendLine("          PBNN Zahl,2F");
            b.AppendLine("          NEG Zahl,Zahl %2-Kompl. berechnen");
            b.AppendLine("2H        SUB $255,$255,1 %Zahl ohne VZ ausgeben");
            b.AppendLine("          DIVU Zahl,Zahl,10");
            b.AppendLine("          GET r,:rR");
            b.AppendLine("          INCL r,'0'");
            b.AppendLine("          STBU r,$255,0");
            b.AppendLine("          PBNZ Zahl,2B");
            b.AppendLine("          GET Zahl,:rY");
            b.AppendLine("          PBNN Zahl,3F");
            b.AppendLine("          SUB $255,$255,1 %neg. VZ schreiben");
            b.AppendLine("          SETL r,'-'");
            b.AppendLine("          STBU r,$255,0");
            b.AppendLine("3H        TRAP 0,:Fputs,:StdOut");
            b.AppendLine("          RESUME 0");
            b.AppendLine("% -----------------------------------------------------------------------");
            b.AppendLine("% Einlesen");
            b.AppendLine("Count     GREG 0 %Bytes im Eingabepuffer zählen");
            b.AppendLine("Str       GREG 0 %Adr. Eingegebener String");
            b.AppendLine("Vorz      GREG 0 %Vorzeichen");
            b.AppendLine("Num       IS $255 %eing. dez.-Zahl");
            b.AppendLine("LsZahl1   GETA $255,Arg %Lesen");
            b.AppendLine("          TRAP 0,:Fgets,:StdIn");
            b.AppendLine("          GETA Str,BUFFER");
            b.AppendLine("          SET Count,0 %Vorbelegen");
            b.AppendLine("          SET Num,0");
            b.AppendLine("          LDB Zahl,Str,Count %Vorzeichen prüfen");
            b.AppendLine("          CMP Vorz,Zahl,'-'");
            b.AppendLine("          PBNZ Vorz,1F");
            b.AppendLine("          INCL Count,1");
            b.AppendLine("1H        LDB Zahl,Str,Count");
            b.AppendLine("          CMP r,Zahl,'0' %Hier kann die Basis");
            b.AppendLine("          BN r,2F %veraendert werden");
            b.AppendLine("          CMP r,Zahl,'9'");
            b.AppendLine("          BP r,2F");
            b.AppendLine("          MUL Num,Num,10 %Mult. mit Basis");
            b.AppendLine("          SUB Zahl,Zahl,'0'");
            b.AppendLine("          ADD Num,Num,Zahl");
            b.AppendLine("          INCL Count,1");
            b.AppendLine("          JMP 1B");
            b.AppendLine("2H        PBNZ Vorz,3F");
            b.AppendLine("          NEG Num,Num");
            b.AppendLine("3H        SETML Vorz,#2100 %gelesenen Wert bereit stellen:");
            b.AppendLine("          INCL Vorz,#FF00 %ADDI $0,$255,$0} nach Vorz");
            b.AppendLine("          GET Zahl,:rX %Zahl < Zielregister");
            b.AppendLine("          SET Count,#FF00");
            b.AppendLine("          AND Zahl,Zahl,Count");
            b.AppendLine("          SL Zahl,Zahl,8 %Als 1. Operand in ADDI einbauen");
            b.AppendLine("          OR Vorz,Vorz,Zahl");
            b.AppendLine("          PUT :rX,Vorz");
            b.AppendLine("          RESUME 0");
            b.AppendLine("");
            b.AppendLine("          PREFIX :");

            return b.ToString();
        }
        #endregion
    }
}
