// arden2bytecode
// Copyright (c) 2010, Daniel Grunwald
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification, are
// permitted provided that the following conditions are met:
//
// - Redistributions of source code must retain the above copyright notice, this list
//   of conditions and the following disclaimer.
//
// - Redistributions in binary form must reproduce the above copyright notice, this list
//   of conditions and the following disclaimer in the documentation and/or other materials
//   provided with the distribution.
//
// - Neither the name of the owner nor the names of its contributors may be used to
//   endorse or promote products derived from this software without specific prior written
//   permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS &AS IS& AND ANY EXPRESS
// OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
// IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
// OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package arden.compiler;

import java.util.GregorianCalendar;

import arden.compiler.node.*;
import arden.runtime.ArdenValue;
import arden.runtime.UnaryOperator;

/**
 * Compiler for unary function operators (of_func_op, from_of_func_op and
 * related productions).
 * 
 * Every operator.apply(this) call will generate code that pushes the operator's
 * result value onto the evaluation stack. The parent compiler is used to
 * generate code for the specified argument. Every possible code path will emit
 * code that evaluates the argument exactly once.
 * 
 * @author Daniel Grunwald
 */
final class UnaryOperatorCompiler extends VisitorBase {
	private final ExpressionCompiler parent;
	private final Node argument;
	private final CompilerContext context;

	public UnaryOperatorCompiler(ExpressionCompiler parent, Node argument) {
		this.parent = parent;
		this.argument = argument;
		this.context = parent.getContext();
	}

	// of_func_op =
	// {ofr} of_read_func_op
	// | {ofnr} of_noread_func_op;
	@Override
	public void caseAOfrOfFuncOp(AOfrOfFuncOp node) {
		node.getOfReadFuncOp().apply(this);
	}

	@Override
	public void caseAOfnrOfFuncOp(AOfnrOfFuncOp node) {
		node.getOfNoreadFuncOp().apply(this);
	}

	// of_read_func_op =
	// {avge} average
	// | {avg} avg
	// | {cnt} count
	// | {ex} exist
	// | {exs} exists
	// | {sum} sum
	// | {med} median;
	@Override
	public void caseAAvgeOfReadFuncOp(AAvgeOfReadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("average", ArdenValue.class));
	}

	@Override
	public void caseAAvgOfReadFuncOp(AAvgOfReadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("average", ArdenValue.class));
	}

	@Override
	public void caseACntOfReadFuncOp(ACntOfReadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("count", ArdenValue.class));
	}

	@Override
	public void caseAExOfReadFuncOp(AExOfReadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("exist", ArdenValue.class));
	}

	@Override
	public void caseAExsOfReadFuncOp(AExsOfReadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("exist", ArdenValue.class));
	}

	@Override
	public void caseASumOfReadFuncOp(ASumOfReadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("sum", ArdenValue.class));
	}

	@Override
	public void caseAMedOfReadFuncOp(AMedOfReadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("median", ArdenValue.class));
	}

	// of_noread_func_op =
	// {any} any
	// | {all} all
	// | {no} no
	// | {slp} slope
	// | {stdv} stddev
	// | {var} variance
	// | {inc} increase
	// | {peri} percent increase
	// | {modi} mod increase
	// | {dec} decrease
	// | {perd} percent decrease
	// | {modd} mod decrease
	// | {inter} interval
	// | {time} time
	// | {acos} arccos
	// | {asin} arcsin
	// | {atan} arctan
	// | {csin} cosine
	// | {cos} cos
	// | {sine} sine
	// | {sin} sin
	// | {tang} tangent
	// | {tan} tan
	// | {exp} exp
	// | {flr} floor
	// | {int} int
	// | {round} round
	// | {ceil} ceiling
	// | {trunc} truncate
	// | {log} log
	// | {logt} log10
	// | {abs} abs
	// | {sqrt} sqrt
	// | {exy} extract year
	// | {exmo} extract month
	// | {exd} extract day
	// | {exh} extract hour
	// | {exmi} extract minute
	// | {exs} extract second
	// | {str} T.string
	// | {exc} extract characters
	// | {rev} reverse
	// | {len} length
	// | {uc} uppercase
	// | {lc} lowercase
	// | {clone} clone
	// | {exattr} extract attribute names;

	@Override
	public void caseAAnyOfNoreadFuncOp(AAnyOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("any", ArdenValue.class));
	}

	@Override
	public void caseAAllOfNoreadFuncOp(AAllOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("all", ArdenValue.class));
	}

	@Override
	public void caseANoOfNoreadFuncOp(ANoOfNoreadFuncOp node) {
		parent.loadOperator(UnaryOperator.NOT);
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("any", ArdenValue.class));
		parent.invokeLoadedUnaryOperator();
	}

	@Override
	public void caseASlpOfNoreadFuncOp(ASlpOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("slope", ArdenValue.class));
	}

	@Override
	public void caseAStdvOfNoreadFuncOp(AStdvOfNoreadFuncOp node) {
		parent.loadOperator(UnaryOperator.SQRT);
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("variance", ArdenValue.class));
		parent.invokeLoadedUnaryOperator();
	}

	@Override
	public void caseAVarOfNoreadFuncOp(AVarOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("variance", ArdenValue.class));
	}

	@Override
	public void caseAIncOfNoreadFuncOp(AIncOfNoreadFuncOp node) {
		// increase
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("increase", ArdenValue.class));
	}

	@Override
	public void caseAPeriOfNoreadFuncOp(APeriOfNoreadFuncOp node) {
		// percent increase
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("percentIncrease", ArdenValue.class));
	}

	@Override
	public void caseAModiOfNoreadFuncOp(AModiOfNoreadFuncOp node) {
		// % increase
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("percentIncrease", ArdenValue.class));
	}

	@Override
	public void caseADecOfNoreadFuncOp(ADecOfNoreadFuncOp node) {
		// decrease
		parent.loadOperator(UnaryOperator.MINUS);
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("increase", ArdenValue.class));
		parent.invokeLoadedUnaryOperator();
	}

	@Override
	public void caseAPerdOfNoreadFuncOp(APerdOfNoreadFuncOp node) {
		// percent decrease
		parent.loadOperator(UnaryOperator.MINUS);
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("percentIncrease", ArdenValue.class));
		parent.invokeLoadedUnaryOperator();
	}

	@Override
	public void caseAModdOfNoreadFuncOp(AModdOfNoreadFuncOp node) {
		// % decrease
		parent.loadOperator(UnaryOperator.MINUS);
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("percentIncrease", ArdenValue.class));
		parent.invokeLoadedUnaryOperator();
	}

	@Override
	public void caseAInterOfNoreadFuncOp(AInterOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.TIME, argument);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("increase", ArdenValue.class));
	}

	@Override
	public void caseATimeOfNoreadFuncOp(ATimeOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.TIME, argument);
	}

	@Override
	public void caseAAcosOfNoreadFuncOp(AAcosOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.ARCCOS, argument);
	}

	@Override
	public void caseAAsinOfNoreadFuncOp(AAsinOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.ARCSIN, argument);
	}

	@Override
	public void caseAAtanOfNoreadFuncOp(AAtanOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.ARCTAN, argument);
	}

	@Override
	public void caseACsinOfNoreadFuncOp(ACsinOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.COS, argument);
	}

	@Override
	public void caseACosOfNoreadFuncOp(ACosOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.COS, argument);
	}

	@Override
	public void caseASineOfNoreadFuncOp(ASineOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.SIN, argument);
	}

	@Override
	public void caseASinOfNoreadFuncOp(ASinOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.SIN, argument);
	}

	@Override
	public void caseATangOfNoreadFuncOp(ATangOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.TAN, argument);
	}

	@Override
	public void caseATanOfNoreadFuncOp(ATanOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.TAN, argument);
	}

	@Override
	public void caseAExpOfNoreadFuncOp(AExpOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.EXP, argument);
	}

	@Override
	public void caseAFlrOfNoreadFuncOp(AFlrOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.FLOOR, argument);
	}

	@Override
	public void caseAIntOfNoreadFuncOp(AIntOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.FLOOR, argument);
	}

	@Override
	public void caseARoundOfNoreadFuncOp(ARoundOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.ROUND, argument);
	}

	@Override
	public void caseACeilOfNoreadFuncOp(ACeilOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.CEILING, argument);
	}

	@Override
	public void caseATruncOfNoreadFuncOp(ATruncOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.TRUNCATE, argument);
	}

	@Override
	public void caseALogOfNoreadFuncOp(ALogOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.LOG, argument);
	}

	@Override
	public void caseALogtOfNoreadFuncOp(ALogtOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.LOG10, argument);
	}

	@Override
	public void caseAAbsOfNoreadFuncOp(AAbsOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.ABS, argument);
	}

	@Override
	public void caseASqrtOfNoreadFuncOp(ASqrtOfNoreadFuncOp node) {
		parent.invokeOperator(UnaryOperator.SQRT, argument);
	}

	@Override
	public void caseAExyOfNoreadFuncOp(AExyOfNoreadFuncOp node) {
		extractTimeComponent(GregorianCalendar.YEAR);
	}

	@Override
	public void caseAExmoOfNoreadFuncOp(AExmoOfNoreadFuncOp node) {
		extractTimeComponent(GregorianCalendar.MONTH);
	}

	@Override
	public void caseAExdOfNoreadFuncOp(AExdOfNoreadFuncOp node) {
		extractTimeComponent(GregorianCalendar.DAY_OF_MONTH);
	}

	@Override
	public void caseAExhOfNoreadFuncOp(AExhOfNoreadFuncOp node) {
		extractTimeComponent(GregorianCalendar.HOUR_OF_DAY);
	}

	@Override
	public void caseAExmiOfNoreadFuncOp(AExmiOfNoreadFuncOp node) {
		extractTimeComponent(GregorianCalendar.MINUTE);
	}

	private void extractTimeComponent(int component) {
		argument.apply(parent);
		context.writer.loadIntegerConstant(component);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("extractTimeComponent", ArdenValue.class, int.class));
	}

	@Override
	public void caseAExsOfNoreadFuncOp(AExsOfNoreadFuncOp node) {
		// does not use extractTimeComponent because we need both seconds and
		// milliseconds
		parent.invokeOperator(UnaryOperator.EXTRACTSECOND, argument);
	}

	@Override
	public void caseAStrOfNoreadFuncOp(AStrOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("joinString", ArdenValue.class));
	}

	@Override
	public void caseAExcOfNoreadFuncOp(AExcOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("extractCharacters", ArdenValue.class));
	}

	@Override
	public void caseARevOfNoreadFuncOp(ARevOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("reverse", ArdenValue.class));
	}

	@Override
	public void caseALenOfNoreadFuncOp(ALenOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("length", ArdenValue.class));
	}

	@Override
	public void caseAUcOfNoreadFuncOp(AUcOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("toUpperCase", ArdenValue.class));
	}

	@Override
	public void caseALcOfNoreadFuncOp(ALcOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("toLowerCase", ArdenValue.class));
	}

	@Override
	public void caseACloneOfNoreadFuncOp(ACloneOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("cloneObjects", ArdenValue.class));
	}

	@Override
	public void caseAExattrOfNoreadFuncOp(AExattrOfNoreadFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("extractAttributeNames", ArdenValue.class));
	}

	// from_of_func_op =
	// {mini} minimum
	// | {min} min
	// | {maxi} maximum
	// | {max} max
	// | {last} last
	// | {fir} first
	// | {ear} earliest
	// | {lat} latest;
	@Override
	public void caseAMiniFromOfFuncOp(AMiniFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.dup();
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMinimum", ArdenValue.class));
		context.writer.invokeStatic(ExpressionCompiler.getMethod("elementAt", ArdenValue.class, ArdenValue.class));
	}

	@Override
	public void caseAMinFromOfFuncOp(AMinFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.dup();
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMinimum", ArdenValue.class));
		context.writer.invokeStatic(ExpressionCompiler.getMethod("elementAt", ArdenValue.class, ArdenValue.class));
	}

	@Override
	public void caseAMaxiFromOfFuncOp(AMaxiFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.dup();
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMaximum", ArdenValue.class));
		context.writer.invokeStatic(ExpressionCompiler.getMethod("elementAt", ArdenValue.class, ArdenValue.class));
	}

	@Override
	public void caseAMaxFromOfFuncOp(AMaxFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.dup();
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMaximum", ArdenValue.class));
		context.writer.invokeStatic(ExpressionCompiler.getMethod("elementAt", ArdenValue.class, ArdenValue.class));
	}

	@Override
	public void caseALastFromOfFuncOp(ALastFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("last", ArdenValue.class));
	}

	@Override
	public void caseAFirFromOfFuncOp(AFirFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("first", ArdenValue.class));
	}

	@Override
	public void caseAEarFromOfFuncOp(AEarFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.dup();
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexEarliest", ArdenValue.class));
		context.writer.invokeStatic(ExpressionCompiler.getMethod("elementAt", ArdenValue.class, ArdenValue.class));
	}

	@Override
	public void caseALatFromOfFuncOp(ALatFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.dup();
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexLatest", ArdenValue.class));
		context.writer.invokeStatic(ExpressionCompiler.getMethod("elementAt", ArdenValue.class, ArdenValue.class));
	}

	// index_from_of_func_op =
	// {minimum} index minimum
	// | {indexmin} index min
	// | {maximum} index maximum
	// | {indexmax} index max
	// | {earliest} index earliest
	// | {latest} index latest;
	@Override
	public void caseAMinimumIndexFromOfFuncOp(AMinimumIndexFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMinimum", ArdenValue.class));
	}

	@Override
	public void caseAIndexminIndexFromOfFuncOp(AIndexminIndexFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMinimum", ArdenValue.class));
	}

	@Override
	public void caseAMaximumIndexFromOfFuncOp(AMaximumIndexFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMaximum", ArdenValue.class));
	}

	@Override
	public void caseAIndexmaxIndexFromOfFuncOp(AIndexmaxIndexFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexMaximum", ArdenValue.class));
	}

	@Override
	public void caseAEarliestIndexFromOfFuncOp(AEarliestIndexFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexEarliest", ArdenValue.class));
	}

	@Override
	public void caseALatestIndexFromOfFuncOp(ALatestIndexFromOfFuncOp node) {
		argument.apply(parent);
		context.writer.invokeStatic(ExpressionCompiler.getMethod("indexLatest", ArdenValue.class));
	}

	// as_func_op = T.number;
	@Override
	public void caseAAsFuncOp(AAsFuncOp node) {
		parent.invokeOperator(UnaryOperator.ASNUMBER, argument);
	}
}
