package com.antler4AC; 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ACParser}.
 */
public interface ACListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ACParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(ACParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(ACParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(ACParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(ACParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(ACParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(ACParser.StatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code processingDone}
	 * labeled alternative in {@link ACParser#done}.
	 * @param ctx the parse tree
	 */
	void enterProcessingDone(ACParser.ProcessingDoneContext ctx);
	/**
	 * Exit a parse tree produced by the {@code processingDone}
	 * labeled alternative in {@link ACParser#done}.
	 * @param ctx the parse tree
	 */
	void exitProcessingDone(ACParser.ProcessingDoneContext ctx);
	/**
	 * Enter a parse tree produced by the {@code normalAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterNormalAssignment(ACParser.NormalAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code normalAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitNormalAssignment(ACParser.NormalAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code basicAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterBasicAssignment(ACParser.BasicAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code basicAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitBasicAssignment(ACParser.BasicAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minusminusAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterMinusminusAssignment(ACParser.MinusminusAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minusminusAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitMinusminusAssignment(ACParser.MinusminusAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusplusAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterPlusplusAssignment(ACParser.PlusplusAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusplusAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitPlusplusAssignment(ACParser.PlusplusAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getNameAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterGetNameAssignment(ACParser.GetNameAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getNameAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitGetNameAssignment(ACParser.GetNameAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getTimerAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterGetTimerAssignment(ACParser.GetTimerAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getTimerAssignment}
	 * labeled alternative in {@link ACParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitGetTimerAssignment(ACParser.GetTimerAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreLine}
	 * labeled alternative in {@link ACParser#ignore_stat}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreLine(ACParser.IgnoreLineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreLine}
	 * labeled alternative in {@link ACParser#ignore_stat}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreLine(ACParser.IgnoreLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#if_stat}.
	 * @param ctx the parse tree
	 */
	void enterIf_stat(ACParser.If_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#if_stat}.
	 * @param ctx the parse tree
	 */
	void exitIf_stat(ACParser.If_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#condition_block}.
	 * @param ctx the parse tree
	 */
	void enterCondition_block(ACParser.Condition_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#condition_block}.
	 * @param ctx the parse tree
	 */
	void exitCondition_block(ACParser.Condition_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#stat_block}.
	 * @param ctx the parse tree
	 */
	void enterStat_block(ACParser.Stat_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#stat_block}.
	 * @param ctx the parse tree
	 */
	void exitStat_block(ACParser.Stat_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#while_stat}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stat(ACParser.While_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#while_stat}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stat(ACParser.While_statContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileLoop}
	 * labeled alternative in {@link ACParser#loop_stat}.
	 * @param ctx the parse tree
	 */
	void enterWhileLoop(ACParser.WhileLoopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileLoop}
	 * labeled alternative in {@link ACParser#loop_stat}.
	 * @param ctx the parse tree
	 */
	void exitWhileLoop(ACParser.WhileLoopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doWhileLoop}
	 * labeled alternative in {@link ACParser#loop_stat}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileLoop(ACParser.DoWhileLoopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doWhileLoop}
	 * labeled alternative in {@link ACParser#loop_stat}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileLoop(ACParser.DoWhileLoopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forLoop}
	 * labeled alternative in {@link ACParser#loop_stat}.
	 * @param ctx the parse tree
	 */
	void enterForLoop(ACParser.ForLoopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forLoop}
	 * labeled alternative in {@link ACParser#loop_stat}.
	 * @param ctx the parse tree
	 */
	void exitForLoop(ACParser.ForLoopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#sendat_stat}.
	 * @param ctx the parse tree
	 */
	void enterSendat_stat(ACParser.Sendat_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#sendat_stat}.
	 * @param ctx the parse tree
	 */
	void exitSendat_stat(ACParser.Sendat_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#send_stat}.
	 * @param ctx the parse tree
	 */
	void enterSend_stat(ACParser.Send_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#send_stat}.
	 * @param ctx the parse tree
	 */
	void exitSend_stat(ACParser.Send_statContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#timer_stat}.
	 * @param ctx the parse tree
	 */
	void enterTimer_stat(ACParser.Timer_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#timer_stat}.
	 * @param ctx the parse tree
	 */
	void exitTimer_stat(ACParser.Timer_statContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showHeapMem}
	 * labeled alternative in {@link ACParser#showContent_stat}.
	 * @param ctx the parse tree
	 */
	void enterShowHeapMem(ACParser.ShowHeapMemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showHeapMem}
	 * labeled alternative in {@link ACParser#showContent_stat}.
	 * @param ctx the parse tree
	 */
	void exitShowHeapMem(ACParser.ShowHeapMemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code showListSendMsg}
	 * labeled alternative in {@link ACParser#showContent_stat}.
	 * @param ctx the parse tree
	 */
	void enterShowListSendMsg(ACParser.ShowListSendMsgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code showListSendMsg}
	 * labeled alternative in {@link ACParser#showContent_stat}.
	 * @param ctx the parse tree
	 */
	void exitShowListSendMsg(ACParser.ShowListSendMsgContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#log}.
	 * @param ctx the parse tree
	 */
	void enterLog(ACParser.LogContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#log}.
	 * @param ctx the parse tree
	 */
	void exitLog(ACParser.LogContext ctx);
	/**
	 * Enter a parse tree produced by {@link ACParser#return_stat}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stat(ACParser.Return_statContext ctx);
	/**
	 * Exit a parse tree produced by {@link ACParser#return_stat}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stat(ACParser.Return_statContext ctx);
	/**
	 * Enter a parse tree produced by the {@code findFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFindFuncExpr(ACParser.FindFuncExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code findFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFindFuncExpr(ACParser.FindFuncExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(ACParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(ACParser.AtomExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(ACParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(ACParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusplusExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPlusplusExpr(ACParser.PlusplusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusplusExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPlusplusExpr(ACParser.PlusplusExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(ACParser.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code additiveExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(ACParser.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relationalExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpr(ACParser.RelationalExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relationalExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpr(ACParser.RelationalExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code backMsgExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBackMsgExpr(ACParser.BackMsgExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code backMsgExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBackMsgExpr(ACParser.BackMsgExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ignoreExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIgnoreExpr(ACParser.IgnoreExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ignoreExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIgnoreExpr(ACParser.IgnoreExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(ACParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(ACParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryMinusExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinusExpr(ACParser.UnaryMinusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryMinusExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinusExpr(ACParser.UnaryMinusExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multiplicationExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicationExpr(ACParser.MultiplicationExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multiplicationExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicationExpr(ACParser.MultiplicationExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minusminusExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMinusminusExpr(ACParser.MinusminusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minusminusExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMinusminusExpr(ACParser.MinusminusExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code strcmpFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStrcmpFuncExpr(ACParser.StrcmpFuncExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code strcmpFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStrcmpFuncExpr(ACParser.StrcmpFuncExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lengthFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLengthFuncExpr(ACParser.LengthFuncExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lengthFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLengthFuncExpr(ACParser.LengthFuncExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code randFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRandFuncExpr(ACParser.RandFuncExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code randFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRandFuncExpr(ACParser.RandFuncExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substrFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubstrFuncExpr(ACParser.SubstrFuncExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substrFuncExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubstrFuncExpr(ACParser.SubstrFuncExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code powExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPowExpr(ACParser.PowExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code powExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPowExpr(ACParser.PowExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code getNameExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGetNameExpr(ACParser.GetNameExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code getNameExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGetNameExpr(ACParser.GetNameExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code equalityExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(ACParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code equalityExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(ACParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(ACParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpr}
	 * labeled alternative in {@link ACParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(ACParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parExpr}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterParExpr(ACParser.ParExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parExpr}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitParExpr(ACParser.ParExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numberAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterNumberAtom(ACParser.NumberAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numberAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitNumberAtom(ACParser.NumberAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterBooleanAtom(ACParser.BooleanAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitBooleanAtom(ACParser.BooleanAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterIdAtom(ACParser.IdAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitIdAtom(ACParser.IdAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterStringAtom(ACParser.StringAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitStringAtom(ACParser.StringAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nilAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterNilAtom(ACParser.NilAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nilAtom}
	 * labeled alternative in {@link ACParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitNilAtom(ACParser.NilAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unknownsExpr}
	 * labeled alternative in {@link ACParser#unknowns}.
	 * @param ctx the parse tree
	 */
	void enterUnknownsExpr(ACParser.UnknownsExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unknownsExpr}
	 * labeled alternative in {@link ACParser#unknowns}.
	 * @param ctx the parse tree
	 */
	void exitUnknownsExpr(ACParser.UnknownsExprContext ctx);
}
