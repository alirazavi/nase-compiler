// This code was generated by the Gardens Point Parser Generator
// Copyright (c) Wayne Kelly, QUT 2005-2010
// (see accompanying GPPGcopyright.rtf)

// GPPG version 1.4.5
// Machine:  DARKONE-L
// DateTime: 02.12.2011 01:07:37
// UserName: darkone
// Input file <NaseParser.y - 02.12.2011 01:07:36>

// options: lines

using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text;
using QUT.Gppg;
using Nase.Files;
using Nase.Syntax;

namespace Nase.GeneratedParser
{
public enum Symbol {
    error=1,EOF=2,NULL_SYMBOL=3,BEGIN_SYMBOL=4,END_SYMBOL=5,DELIMITER_SYMBOL=6,
    COMMA_SYMBOL=7,INT_TYPE_SYMBOL=8,BOOL_TYPE_SYMBOL=9,READ_SYMBOL=10,WRITE_SYMBOL=11,IF_SYMBOL=12,
    THEN_SYMBOL=13,ELSE_SYMBOL=14,WHILE_SYMBOL=15,DO_SYMBOL=16,ASSIGN_SYMBOL=17,OPEN_PARENTHESIS_SYMBOL=18,
    CLOSE_PARENTHESIS_SYMBOL=19,INLINE_IF_SYMBOL=20,INLINE_THEN_SYMBOL=21,INLINE_ELSE_SYMBOL=22,INLINE_FI_SYMBOL=23,PLUS_SYMBOL=24,
    MINUS_SYMBOL=25,TIMES_SYMBOL=26,DIVIDE_SYMBOL=27,MODULO_SYMBOL=28,LT_SYMBOL=29,LE_SYMBOL=30,
    EQ_SYMBOL=31,GE_SYMBOL=32,GT_SYMBOL=33,NE_SYMBOL=34,AND_SYMBOL=35,OR_SYMBOL=36,
    NOT_SYMBOL=37,TRUE_SYMBOL=38,FALSE_SYMBOL=39,IDENTIFIER_SYMBOL=40,INTEGER_LITERAL_SYMBOL=41};

public partial struct ValueType
#line 9 "NaseParser.y"
{ public Symbol symbol;
#line 10 "NaseParser.y"
         public SyntaxTreeNode node;
#line 11 "NaseParser.y"
         public List<Symbol> symbolList; }
public partial class NaseParser: ShiftReduceParser<ValueType, FilePosition>
{
#pragma warning disable 649
  private static Dictionary<int, string> aliasses;
#pragma warning restore 649
  private static Rule[] rules = new Rule[65];
  private static State[] states = new State[117];
  private static string[] nonTerms = new string[] {
      "program", "declarationSequence", "blockSequence", "block", "statementSequence", 
      "statement", "declaration", "declarationRec", "assignment", "read", "write", 
      "typeName", "intExpr", "intTerm", "intTermSecondary", "intFactor", "boolExpr", 
      "boolTerm", "boolTermSecondary", "boolFactor", "comparisonExpr", "inlineIfStatement", 
      "ifStatement", "whileStatement", "identifier", "integer", "$accept", };

  static NaseParser() {
    states[0] = new State(new int[]{8,22,9,23,4,8},new int[]{-1,1,-2,3,-7,112,-12,16,-3,115,-4,6});
    states[1] = new State(new int[]{2,2});
    states[2] = new State(-1);
    states[3] = new State(new int[]{4,8},new int[]{-3,4,-4,6});
    states[4] = new State(new int[]{2,5});
    states[5] = new State(-2);
    states[6] = new State(new int[]{4,8,2,-7},new int[]{-3,7,-4,6});
    states[7] = new State(-6);
    states[8] = new State(new int[]{8,22,9,23,4,8,40,44,10,93,11,97,12,101,15,108},new int[]{-5,9,-6,12,-7,14,-12,16,-4,24,-9,25,-25,27,-10,91,-11,95,-23,100,-24,107});
    states[9] = new State(new int[]{5,10});
    states[10] = new State(new int[]{6,11});
    states[11] = new State(-8);
    states[12] = new State(new int[]{8,22,9,23,4,8,40,44,10,93,11,97,12,101,15,108,5,-10},new int[]{-5,13,-6,12,-7,14,-12,16,-4,24,-9,25,-25,27,-10,91,-11,95,-23,100,-24,107});
    states[13] = new State(-9);
    states[14] = new State(new int[]{6,15});
    states[15] = new State(-11);
    states[16] = new State(new int[]{40,17});
    states[17] = new State(new int[]{7,19,6,-19},new int[]{-8,18});
    states[18] = new State(-18);
    states[19] = new State(new int[]{40,20});
    states[20] = new State(new int[]{7,19,6,-21},new int[]{-8,21});
    states[21] = new State(-20);
    states[22] = new State(-22);
    states[23] = new State(-23);
    states[24] = new State(-12);
    states[25] = new State(new int[]{6,26});
    states[26] = new State(-13);
    states[27] = new State(new int[]{17,28});
    states[28] = new State(new int[]{25,35,41,42,40,44,18,79,20,49,37,59,38,64,39,65},new int[]{-13,29,-17,90,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48,-18,56,-19,83,-20,61,-21,66});
    states[29] = new State(new int[]{29,30,30,68,31,70,32,72,33,74,34,76,6,-24});
    states[30] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,31,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[31] = new State(-51);
    states[32] = new State(new int[]{24,33,25,88,29,-28,30,-28,31,-28,32,-28,33,-28,34,-28,6,-28,35,-28,36,-28,21,-28,19,-28,13,-28,16,-28,22,-28,23,-28});
    states[33] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,34,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[34] = new State(-26);
    states[35] = new State(new int[]{41,42,40,44,18,45,20,49},new int[]{-15,36,-16,37,-26,41,-25,43,-22,48});
    states[36] = new State(-29);
    states[37] = new State(new int[]{26,38,27,84,28,86,24,-34,25,-34,29,-34,30,-34,31,-34,32,-34,33,-34,34,-34,6,-34,35,-34,36,-34,21,-34,19,-34,13,-34,16,-34,22,-34,23,-34});
    states[38] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-14,39,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[39] = new State(-31);
    states[40] = new State(-30);
    states[41] = new State(-35);
    states[42] = new State(-58);
    states[43] = new State(-36);
    states[44] = new State(-57);
    states[45] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,46,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[46] = new State(new int[]{19,47});
    states[47] = new State(-37);
    states[48] = new State(-38);
    states[49] = new State(new int[]{37,59,38,64,39,65,25,35,41,42,40,44,18,79,20,49},new int[]{-17,50,-18,56,-19,83,-20,61,-21,66,-13,67,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48});
    states[50] = new State(new int[]{21,51});
    states[51] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,52,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[52] = new State(new int[]{22,53});
    states[53] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,54,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[54] = new State(new int[]{23,55});
    states[55] = new State(-39);
    states[56] = new State(new int[]{36,57,6,-41,21,-41,19,-41,13,-41,16,-41});
    states[57] = new State(new int[]{37,59,38,64,39,65,25,35,41,42,40,44,18,79,20,49},new int[]{-17,58,-18,56,-19,83,-20,61,-21,66,-13,67,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48});
    states[58] = new State(-40);
    states[59] = new State(new int[]{38,64,39,65,25,35,41,42,40,44,18,79,20,49},new int[]{-19,60,-20,61,-21,66,-13,67,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48});
    states[60] = new State(-42);
    states[61] = new State(new int[]{35,62,36,-45,6,-45,21,-45,19,-45,13,-45,16,-45});
    states[62] = new State(new int[]{38,64,39,65,25,35,41,42,40,44,18,79,20,49},new int[]{-19,63,-20,61,-21,66,-13,67,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48});
    states[63] = new State(-44);
    states[64] = new State(-46);
    states[65] = new State(-47);
    states[66] = new State(-48);
    states[67] = new State(new int[]{29,30,30,68,31,70,32,72,33,74,34,76});
    states[68] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,69,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[69] = new State(-52);
    states[70] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,71,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[71] = new State(-53);
    states[72] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,73,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[73] = new State(-54);
    states[74] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,75,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[75] = new State(-55);
    states[76] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,77,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[77] = new State(-56);
    states[78] = new State(new int[]{26,-36,27,-36,28,-36,24,-36,25,-36,29,-36,30,-36,31,-36,32,-36,33,-36,34,-36,6,-36,19,-36,35,-49,36,-49,21,-49,13,-49,16,-49});
    states[79] = new State(new int[]{25,35,41,42,40,44,18,79,20,49,37,59,38,64,39,65},new int[]{-13,80,-17,81,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48,-18,56,-19,83,-20,61,-21,66});
    states[80] = new State(new int[]{19,47,29,30,30,68,31,70,32,72,33,74,34,76});
    states[81] = new State(new int[]{19,82});
    states[82] = new State(-50);
    states[83] = new State(-43);
    states[84] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-14,85,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[85] = new State(-32);
    states[86] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-14,87,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[87] = new State(-33);
    states[88] = new State(new int[]{25,35,41,42,40,44,18,45,20,49},new int[]{-13,89,-14,32,-15,40,-16,37,-26,41,-25,43,-22,48});
    states[89] = new State(-27);
    states[90] = new State(-25);
    states[91] = new State(new int[]{6,92});
    states[92] = new State(-14);
    states[93] = new State(new int[]{40,44},new int[]{-25,94});
    states[94] = new State(-59);
    states[95] = new State(new int[]{6,96});
    states[96] = new State(-15);
    states[97] = new State(new int[]{25,35,41,42,40,44,18,79,20,49,37,59,38,64,39,65},new int[]{-13,98,-17,99,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48,-18,56,-19,83,-20,61,-21,66});
    states[98] = new State(new int[]{29,30,30,68,31,70,32,72,33,74,34,76,6,-60});
    states[99] = new State(-61);
    states[100] = new State(-16);
    states[101] = new State(new int[]{37,59,38,64,39,65,25,35,41,42,40,44,18,79,20,49},new int[]{-17,102,-18,56,-19,83,-20,61,-21,66,-13,67,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48});
    states[102] = new State(new int[]{13,103});
    states[103] = new State(new int[]{8,22,9,23,4,8,40,44,10,93,11,97,12,101,15,108},new int[]{-6,104,-7,14,-12,16,-4,24,-9,25,-25,27,-10,91,-11,95,-23,100,-24,107});
    states[104] = new State(new int[]{14,105,8,-63,9,-63,4,-63,40,-63,10,-63,11,-63,12,-63,15,-63,5,-63});
    states[105] = new State(new int[]{8,22,9,23,4,8,40,44,10,93,11,97,12,101,15,108},new int[]{-6,106,-7,14,-12,16,-4,24,-9,25,-25,27,-10,91,-11,95,-23,100,-24,107});
    states[106] = new State(-62);
    states[107] = new State(-17);
    states[108] = new State(new int[]{37,59,38,64,39,65,25,35,41,42,40,44,18,79,20,49},new int[]{-17,109,-18,56,-19,83,-20,61,-21,66,-13,67,-14,32,-15,40,-16,37,-26,41,-25,78,-22,48});
    states[109] = new State(new int[]{16,110});
    states[110] = new State(new int[]{8,22,9,23,4,8,40,44,10,93,11,97,12,101,15,108},new int[]{-6,111,-7,14,-12,16,-4,24,-9,25,-25,27,-10,91,-11,95,-23,100,-24,107});
    states[111] = new State(-64);
    states[112] = new State(new int[]{6,113});
    states[113] = new State(new int[]{8,22,9,23,4,-5},new int[]{-2,114,-7,112,-12,16});
    states[114] = new State(-4);
    states[115] = new State(new int[]{2,116});
    states[116] = new State(-3);

    for (int sNo = 0; sNo < states.Length; sNo++) states[sNo].number = sNo;

    rules[1] = new Rule(-27, new int[]{-1,2});
    rules[2] = new Rule(-1, new int[]{-2,-3,2});
    rules[3] = new Rule(-1, new int[]{-3,2});
    rules[4] = new Rule(-2, new int[]{-7,6,-2});
    rules[5] = new Rule(-2, new int[]{-7,6});
    rules[6] = new Rule(-3, new int[]{-4,-3});
    rules[7] = new Rule(-3, new int[]{-4});
    rules[8] = new Rule(-4, new int[]{4,-5,5,6});
    rules[9] = new Rule(-5, new int[]{-6,-5});
    rules[10] = new Rule(-5, new int[]{-6});
    rules[11] = new Rule(-6, new int[]{-7,6});
    rules[12] = new Rule(-6, new int[]{-4});
    rules[13] = new Rule(-6, new int[]{-9,6});
    rules[14] = new Rule(-6, new int[]{-10,6});
    rules[15] = new Rule(-6, new int[]{-11,6});
    rules[16] = new Rule(-6, new int[]{-23});
    rules[17] = new Rule(-6, new int[]{-24});
    rules[18] = new Rule(-7, new int[]{-12,40,-8});
    rules[19] = new Rule(-7, new int[]{-12,40});
    rules[20] = new Rule(-8, new int[]{7,40,-8});
    rules[21] = new Rule(-8, new int[]{7,40});
    rules[22] = new Rule(-12, new int[]{8});
    rules[23] = new Rule(-12, new int[]{9});
    rules[24] = new Rule(-9, new int[]{-25,17,-13});
    rules[25] = new Rule(-9, new int[]{-25,17,-17});
    rules[26] = new Rule(-13, new int[]{-14,24,-13});
    rules[27] = new Rule(-13, new int[]{-14,25,-13});
    rules[28] = new Rule(-13, new int[]{-14});
    rules[29] = new Rule(-14, new int[]{25,-15});
    rules[30] = new Rule(-14, new int[]{-15});
    rules[31] = new Rule(-15, new int[]{-16,26,-14});
    rules[32] = new Rule(-15, new int[]{-16,27,-14});
    rules[33] = new Rule(-15, new int[]{-16,28,-14});
    rules[34] = new Rule(-15, new int[]{-16});
    rules[35] = new Rule(-16, new int[]{-26});
    rules[36] = new Rule(-16, new int[]{-25});
    rules[37] = new Rule(-16, new int[]{18,-13,19});
    rules[38] = new Rule(-16, new int[]{-22});
    rules[39] = new Rule(-22, new int[]{20,-17,21,-13,22,-13,23});
    rules[40] = new Rule(-17, new int[]{-18,36,-17});
    rules[41] = new Rule(-17, new int[]{-18});
    rules[42] = new Rule(-18, new int[]{37,-19});
    rules[43] = new Rule(-18, new int[]{-19});
    rules[44] = new Rule(-19, new int[]{-20,35,-19});
    rules[45] = new Rule(-19, new int[]{-20});
    rules[46] = new Rule(-20, new int[]{38});
    rules[47] = new Rule(-20, new int[]{39});
    rules[48] = new Rule(-20, new int[]{-21});
    rules[49] = new Rule(-20, new int[]{-25});
    rules[50] = new Rule(-20, new int[]{18,-17,19});
    rules[51] = new Rule(-21, new int[]{-13,29,-13});
    rules[52] = new Rule(-21, new int[]{-13,30,-13});
    rules[53] = new Rule(-21, new int[]{-13,31,-13});
    rules[54] = new Rule(-21, new int[]{-13,32,-13});
    rules[55] = new Rule(-21, new int[]{-13,33,-13});
    rules[56] = new Rule(-21, new int[]{-13,34,-13});
    rules[57] = new Rule(-25, new int[]{40});
    rules[58] = new Rule(-26, new int[]{41});
    rules[59] = new Rule(-10, new int[]{10,-25});
    rules[60] = new Rule(-11, new int[]{11,-13});
    rules[61] = new Rule(-11, new int[]{11,-17});
    rules[62] = new Rule(-23, new int[]{12,-17,13,-6,14,-6});
    rules[63] = new Rule(-23, new int[]{12,-17,13,-6});
    rules[64] = new Rule(-24, new int[]{15,-17,16,-6});
  }

  protected override void Initialize() {
    this.InitSpecialTokens((int)Symbol.error, (int)Symbol.EOF);
    this.InitStates(states);
    this.InitRules(rules);
    this.InitNonTerminals(nonTerms);
  }

  protected override void DoAction(int action)
  {
    switch (action)
    {
      case 2: // program -> declarationSequence, blockSequence, EOF
#line 69 "NaseParser.y"
{
#line 70 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeProgramNode(CurrentLocationSpan, new SyntaxTreeSequenceNode(LocationStack[LocationStack.Depth-3], ValueStack[ValueStack.Depth-3].node, new SyntaxTreeSequenceNode(LocationStack[LocationStack.Depth-2], ValueStack[ValueStack.Depth-2].node, null)));
#line 71 "NaseParser.y"
                SyntaxTree = new SyntaxTree(CurrentSemanticValue.node);
#line 72 "NaseParser.y"
            }
        break;
      case 3: // program -> blockSequence, EOF
#line 74 "NaseParser.y"
{
#line 75 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeProgramNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-2].node);
#line 76 "NaseParser.y"
                SyntaxTree = new SyntaxTree(CurrentSemanticValue.node);
#line 77 "NaseParser.y"
            }
        break;
      case 4: // declarationSequence -> declaration, DELIMITER_SYMBOL, declarationSequence
#line 82 "NaseParser.y"
{
#line 83 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeSequenceNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 84 "NaseParser.y"
            }
        break;
      case 5: // declarationSequence -> declaration, DELIMITER_SYMBOL
#line 86 "NaseParser.y"
{
#line 87 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeSequenceNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-2].node, null);
#line 88 "NaseParser.y"
            }
        break;
      case 6: // blockSequence -> block, blockSequence
#line 93 "NaseParser.y"
{
#line 94 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeSequenceNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-2].node, ValueStack[ValueStack.Depth-1].node);
#line 95 "NaseParser.y"
            }
        break;
      case 7: // blockSequence -> block
#line 97 "NaseParser.y"
{
#line 98 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeSequenceNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-1].node, null);
#line 99 "NaseParser.y"
            }
        break;
      case 8: // block -> BEGIN_SYMBOL, statementSequence, END_SYMBOL, DELIMITER_SYMBOL
#line 104 "NaseParser.y"
{
#line 105 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-3].node;
#line 106 "NaseParser.y"
            }
        break;
      case 9: // statementSequence -> statement, statementSequence
#line 111 "NaseParser.y"
{
#line 112 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeSequenceNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-2].node, ValueStack[ValueStack.Depth-1].node);
#line 113 "NaseParser.y"
            }
        break;
      case 10: // statementSequence -> statement
#line 115 "NaseParser.y"
{
#line 116 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeSequenceNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-1].node, null);
#line 117 "NaseParser.y"
            }
        break;
      case 11: // statement -> declaration, DELIMITER_SYMBOL
#line 122 "NaseParser.y"
{
#line 123 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-2].node;
#line 124 "NaseParser.y"
            }
        break;
      case 12: // statement -> block
#line 126 "NaseParser.y"
{
#line 127 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 128 "NaseParser.y"
            }
        break;
      case 13: // statement -> assignment, DELIMITER_SYMBOL
#line 130 "NaseParser.y"
{
#line 131 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-2].node;
#line 132 "NaseParser.y"
            }
        break;
      case 14: // statement -> read, DELIMITER_SYMBOL
#line 134 "NaseParser.y"
{
#line 135 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-2].node;
#line 136 "NaseParser.y"
            }
        break;
      case 15: // statement -> write, DELIMITER_SYMBOL
#line 138 "NaseParser.y"
{
#line 139 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-2].node;
#line 140 "NaseParser.y"
            }
        break;
      case 16: // statement -> ifStatement
#line 142 "NaseParser.y"
{
#line 143 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 144 "NaseParser.y"
            }
        break;
      case 17: // statement -> whileStatement
#line 146 "NaseParser.y"
{
#line 147 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 148 "NaseParser.y"
            }
        break;
      case 18: // declaration -> typeName, IDENTIFIER_SYMBOL, declarationRec
#line 153 "NaseParser.y"
{
#line 154 "NaseParser.y"
                SyntaxTreeDeclarationNode declNode = null;
#line 155 "NaseParser.y"
                SyntaxTreeSequenceNode seqNode = null;
#line 156 "NaseParser.y"

#line 157 "NaseParser.y"
                var firstDeclNode = new SyntaxTreeDeclarationNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-2].symbol);
#line 158 "NaseParser.y"
                this._symbolTable.SetDeclarationNodeLinkToSymbol(ValueStack[ValueStack.Depth-2].symbol, firstDeclNode);
#line 159 "NaseParser.y"

#line 160 "NaseParser.y"
                foreach(Symbol s in ValueStack[ValueStack.Depth-1].symbolList)
#line 161 "NaseParser.y"
                {
#line 162 "NaseParser.y"
                    declNode = new SyntaxTreeDeclarationNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-3].node, s);
#line 163 "NaseParser.y"
                    this._symbolTable.SetDeclarationNodeLinkToSymbol(s, declNode);
#line 164 "NaseParser.y"
                    seqNode = new SyntaxTreeSequenceNode(CurrentLocationSpan, declNode, seqNode);
#line 165 "NaseParser.y"
                }
#line 166 "NaseParser.y"

#line 167 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeSequenceNode(CurrentLocationSpan, firstDeclNode, seqNode);
#line 168 "NaseParser.y"
            }
        break;
      case 19: // declaration -> typeName, IDENTIFIER_SYMBOL
#line 170 "NaseParser.y"
{
#line 171 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDeclarationNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-2].node, ValueStack[ValueStack.Depth-1].symbol);
#line 172 "NaseParser.y"
                this._symbolTable.SetDeclarationNodeLinkToSymbol(ValueStack[ValueStack.Depth-1].symbol, CurrentSemanticValue.node);
#line 173 "NaseParser.y"
            }
        break;
      case 20: // declarationRec -> COMMA_SYMBOL, IDENTIFIER_SYMBOL, declarationRec
#line 178 "NaseParser.y"
{
#line 179 "NaseParser.y"
                CurrentSemanticValue.symbolList = ValueStack[ValueStack.Depth-1].symbolList;
#line 180 "NaseParser.y"
                CurrentSemanticValue.symbolList.Add(ValueStack[ValueStack.Depth-2].symbol);
#line 181 "NaseParser.y"
            }
        break;
      case 21: // declarationRec -> COMMA_SYMBOL, IDENTIFIER_SYMBOL
#line 183 "NaseParser.y"
{
#line 184 "NaseParser.y"
                CurrentSemanticValue.symbolList = new List<Symbol>();
#line 185 "NaseParser.y"
                CurrentSemanticValue.symbolList.Add(ValueStack[ValueStack.Depth-1].symbol);
#line 186 "NaseParser.y"
            }
        break;
      case 22: // typeName -> INT_TYPE_SYMBOL
#line 191 "NaseParser.y"
{
#line 192 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeTypeNode(CurrentLocationSpan, Symbol.INT_TYPE_SYMBOL);
#line 193 "NaseParser.y"
            }
        break;
      case 23: // typeName -> BOOL_TYPE_SYMBOL
#line 195 "NaseParser.y"
{
#line 196 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeTypeNode(CurrentLocationSpan, Symbol.BOOL_TYPE_SYMBOL);
#line 197 "NaseParser.y"
            }
        break;
      case 24: // assignment -> identifier, ASSIGN_SYMBOL, intExpr
#line 202 "NaseParser.y"
{
#line 203 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeAssignmentNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 204 "NaseParser.y"
            }
        break;
      case 25: // assignment -> identifier, ASSIGN_SYMBOL, boolExpr
#line 206 "NaseParser.y"
{
#line 207 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeAssignmentNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 208 "NaseParser.y"
            }
        break;
      case 26: // intExpr -> intTerm, PLUS_SYMBOL, intExpr
#line 213 "NaseParser.y"
{
#line 214 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.PLUS_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 215 "NaseParser.y"
            }
        break;
      case 27: // intExpr -> intTerm, MINUS_SYMBOL, intExpr
#line 217 "NaseParser.y"
{
#line 218 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.MINUS_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 219 "NaseParser.y"
            }
        break;
      case 28: // intExpr -> intTerm
#line 221 "NaseParser.y"
{
#line 222 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 223 "NaseParser.y"
            }
        break;
      case 29: // intTerm -> MINUS_SYMBOL, intTermSecondary
#line 228 "NaseParser.y"
{
#line 229 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeMonadicOpNode(CurrentLocationSpan, Symbol.MINUS_SYMBOL, ValueStack[ValueStack.Depth-1].node);
#line 230 "NaseParser.y"
            }
        break;
      case 30: // intTerm -> intTermSecondary
#line 232 "NaseParser.y"
{
#line 233 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 234 "NaseParser.y"
            }
        break;
      case 31: // intTermSecondary -> intFactor, TIMES_SYMBOL, intTerm
#line 239 "NaseParser.y"
{
#line 240 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.TIMES_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 241 "NaseParser.y"
            }
        break;
      case 32: // intTermSecondary -> intFactor, DIVIDE_SYMBOL, intTerm
#line 243 "NaseParser.y"
{
#line 244 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.DIVIDE_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 245 "NaseParser.y"
            }
        break;
      case 33: // intTermSecondary -> intFactor, MODULO_SYMBOL, intTerm
#line 247 "NaseParser.y"
{
#line 248 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.MODULO_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 249 "NaseParser.y"
            }
        break;
      case 34: // intTermSecondary -> intFactor
#line 251 "NaseParser.y"
{
#line 252 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 253 "NaseParser.y"
            }
        break;
      case 35: // intFactor -> integer
#line 258 "NaseParser.y"
{
#line 259 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 260 "NaseParser.y"
            }
        break;
      case 36: // intFactor -> identifier
#line 262 "NaseParser.y"
{
#line 263 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 264 "NaseParser.y"
            }
        break;
      case 37: // intFactor -> OPEN_PARENTHESIS_SYMBOL, intExpr, CLOSE_PARENTHESIS_SYMBOL
#line 266 "NaseParser.y"
{
#line 267 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-2].node;
#line 268 "NaseParser.y"
            }
        break;
      case 38: // intFactor -> inlineIfStatement
#line 270 "NaseParser.y"
{
#line 271 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 272 "NaseParser.y"
            }
        break;
      case 39: // inlineIfStatement -> INLINE_IF_SYMBOL, boolExpr, INLINE_THEN_SYMBOL, intExpr, 
               //                      INLINE_ELSE_SYMBOL, intExpr, INLINE_FI_SYMBOL
#line 277 "NaseParser.y"
{
#line 278 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeInlineIfNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-6].node, ValueStack[ValueStack.Depth-4].node, ValueStack[ValueStack.Depth-2].node);
#line 279 "NaseParser.y"
            }
        break;
      case 40: // boolExpr -> boolTerm, OR_SYMBOL, boolExpr
#line 284 "NaseParser.y"
{
#line 285 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.OR_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 286 "NaseParser.y"
            }
        break;
      case 41: // boolExpr -> boolTerm
#line 288 "NaseParser.y"
{
#line 289 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 290 "NaseParser.y"
            }
        break;
      case 42: // boolTerm -> NOT_SYMBOL, boolTermSecondary
#line 295 "NaseParser.y"
{
#line 296 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeMonadicOpNode(CurrentLocationSpan, Symbol.NOT_SYMBOL, ValueStack[ValueStack.Depth-1].node);
#line 297 "NaseParser.y"
            }
        break;
      case 43: // boolTerm -> boolTermSecondary
#line 299 "NaseParser.y"
{
#line 300 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 301 "NaseParser.y"
            }
        break;
      case 44: // boolTermSecondary -> boolFactor, AND_SYMBOL, boolTermSecondary
#line 306 "NaseParser.y"
{
#line 307 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.AND_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 308 "NaseParser.y"
            }
        break;
      case 45: // boolTermSecondary -> boolFactor
#line 310 "NaseParser.y"
{
#line 311 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 312 "NaseParser.y"
            }
        break;
      case 46: // boolFactor -> TRUE_SYMBOL
#line 317 "NaseParser.y"
{
#line 318 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeConstNode(CurrentLocationSpan, Symbol.TRUE_SYMBOL);
#line 319 "NaseParser.y"
            }
        break;
      case 47: // boolFactor -> FALSE_SYMBOL
#line 321 "NaseParser.y"
{
#line 322 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeConstNode(CurrentLocationSpan, Symbol.FALSE_SYMBOL);
#line 323 "NaseParser.y"
            }
        break;
      case 48: // boolFactor -> comparisonExpr
#line 325 "NaseParser.y"
{
#line 326 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 327 "NaseParser.y"
            }
        break;
      case 49: // boolFactor -> identifier
#line 329 "NaseParser.y"
{
#line 330 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-1].node;
#line 331 "NaseParser.y"
            }
        break;
      case 50: // boolFactor -> OPEN_PARENTHESIS_SYMBOL, boolExpr, CLOSE_PARENTHESIS_SYMBOL
#line 333 "NaseParser.y"
{
#line 334 "NaseParser.y"
                CurrentSemanticValue.node = ValueStack[ValueStack.Depth-2].node;
#line 335 "NaseParser.y"
            }
        break;
      case 51: // comparisonExpr -> intExpr, LT_SYMBOL, intExpr
#line 340 "NaseParser.y"
{
#line 341 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.LT_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 342 "NaseParser.y"
            }
        break;
      case 52: // comparisonExpr -> intExpr, LE_SYMBOL, intExpr
#line 344 "NaseParser.y"
{
#line 345 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.LE_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 346 "NaseParser.y"
            }
        break;
      case 53: // comparisonExpr -> intExpr, EQ_SYMBOL, intExpr
#line 348 "NaseParser.y"
{
#line 349 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.EQ_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 350 "NaseParser.y"
            }
        break;
      case 54: // comparisonExpr -> intExpr, GE_SYMBOL, intExpr
#line 352 "NaseParser.y"
{
#line 353 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.GE_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 354 "NaseParser.y"
            }
        break;
      case 55: // comparisonExpr -> intExpr, GT_SYMBOL, intExpr
#line 356 "NaseParser.y"
{
#line 357 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.GT_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 358 "NaseParser.y"
            }
        break;
      case 56: // comparisonExpr -> intExpr, NE_SYMBOL, intExpr
#line 360 "NaseParser.y"
{
#line 361 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeDyadicOpNode(CurrentLocationSpan, Symbol.NE_SYMBOL, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 362 "NaseParser.y"
            }
        break;
      case 57: // identifier -> IDENTIFIER_SYMBOL
#line 367 "NaseParser.y"
{
#line 368 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeIdentNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-1].symbol);
#line 369 "NaseParser.y"
            }
        break;
      case 58: // integer -> INTEGER_LITERAL_SYMBOL
#line 374 "NaseParser.y"
{
#line 375 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeConstNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-1].symbol);
#line 376 "NaseParser.y"
                this._symbolTable.SetDeclarationNodeLinkToSymbol(ValueStack[ValueStack.Depth-1].symbol, CurrentSemanticValue.node);
#line 377 "NaseParser.y"
            }
        break;
      case 59: // read -> READ_SYMBOL, identifier
#line 382 "NaseParser.y"
{
#line 383 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeReadNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-1].node);
#line 384 "NaseParser.y"
            }
        break;
      case 60: // write -> WRITE_SYMBOL, intExpr
#line 389 "NaseParser.y"
{
#line 390 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeWriteNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-1].node);
#line 391 "NaseParser.y"
            }
        break;
      case 61: // write -> WRITE_SYMBOL, boolExpr
#line 393 "NaseParser.y"
{
#line 394 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeWriteNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-1].node);
#line 395 "NaseParser.y"
            }
        break;
      case 62: // ifStatement -> IF_SYMBOL, boolExpr, THEN_SYMBOL, statement, ELSE_SYMBOL, 
               //                statement
#line 400 "NaseParser.y"
{
#line 401 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeIfStatementNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-5].node, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 402 "NaseParser.y"
            }
        break;
      case 63: // ifStatement -> IF_SYMBOL, boolExpr, THEN_SYMBOL, statement
#line 404 "NaseParser.y"
{
#line 405 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeIfStatementNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node, null);
#line 406 "NaseParser.y"
            }
        break;
      case 64: // whileStatement -> WHILE_SYMBOL, boolExpr, DO_SYMBOL, statement
#line 411 "NaseParser.y"
{
#line 412 "NaseParser.y"
                CurrentSemanticValue.node = new SyntaxTreeWhileStatementNode(CurrentLocationSpan, ValueStack[ValueStack.Depth-3].node, ValueStack[ValueStack.Depth-1].node);
#line 413 "NaseParser.y"
            }
        break;
    }
  }

  protected override string TerminalToString(int terminal)
  {
    if (aliasses != null && aliasses.ContainsKey(terminal))
        return aliasses[terminal];
    else if (((Symbol)terminal).ToString() != terminal.ToString(CultureInfo.InvariantCulture))
        return ((Symbol)terminal).ToString();
    else
        return CharToString((char)terminal);
  }

}
}
