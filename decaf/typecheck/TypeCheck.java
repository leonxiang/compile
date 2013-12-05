package decaf.typecheck;

import java.util.Stack;

import decaf.Driver;
import decaf.Location;
import decaf.tree.Tree;
import decaf.error.BadArgCountError;
import decaf.error.BadArgTypeError;
import decaf.error.BadArrElementError;
import decaf.error.BadLengthArgError;
import decaf.error.BadLengthError;
import decaf.error.BadNewArrayLength;
import decaf.error.BadPrintArgError;
import decaf.error.BadReturnTypeError;
import decaf.error.BadTestExpr;
import decaf.error.BreakOutOfLoopError;
import decaf.error.ClassNotFoundError;
import decaf.error.DecafError;
import decaf.error.FieldNotAccessError;
import decaf.error.FieldNotFoundError;
import decaf.error.IncompatBinOpError;
import decaf.error.IncompatUnOpError;
import decaf.error.NotArrayError;
import decaf.error.NotClassError;
import decaf.error.NotClassFieldError;
import decaf.error.NotClassMethodError;
import decaf.error.RefNonStaticError;
import decaf.error.SubNotIntError;
import decaf.error.ThisInStaticFuncError;
import decaf.error.UndeclVarError;
import decaf.frontend.Parser;
import decaf.scope.ClassScope;
import decaf.scope.FormalScope;
import decaf.scope.Scope;
import decaf.scope.ScopeStack;
import decaf.scope.Scope.Kind;
import decaf.symbol.Class;
import decaf.symbol.Function;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.type.*;

public class TypeCheck extends Tree.Visitor {

	private ScopeStack table;

	private Stack<Tree> breaks;

	private Function currentFunction;

	public TypeCheck(ScopeStack table) {
		this.table = table;
		breaks = new Stack<Tree>();
	}

	public static void checkType(Tree.TopLevel tree) {
		new TypeCheck(Driver.getDriver().getTable()).visitTopLevel(tree);
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.type = checkBinaryOp(expr.left, expr.right, expr.tag, expr.loc);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		if(expr.tag == Tree.NEG){
			if (expr.expr.type.equal(BaseType.ERROR)
					|| expr.expr.type.equal(BaseType.INT) || expr.expr.type.equal(BaseType.DOUBLE)) {
				expr.type = expr.expr.type;
			} else {
				issueError(new IncompatUnOpError(expr.getLocation(), "-",
						expr.expr.type.toString()));
				expr.type = BaseType.ERROR;
			}
		}
		else{
			if (!(expr.expr.type.equal(BaseType.BOOL) || expr.expr.type
					.equal(BaseType.ERROR))) {
				issueError(new IncompatUnOpError(expr.getLocation(), "!",
						expr.expr.type.toString()));
			}
			expr.type = BaseType.BOOL;
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.type = BaseType.INT;
			break;
		case Tree.BOOL:
			literal.type = BaseType.BOOL;
			break;
		case Tree.DOUBLE:
			literal.type = BaseType.DOUBLE;  //Leon
			break;
		case Tree.STRING:
			literal.type = BaseType.STRING;
			break;
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.type = BaseType.NULL;
	}

	@Override
	public void visitReadIntExpr(Tree.ReadIntExpr readIntExpr) {
		readIntExpr.type = BaseType.INT;
	}

	@Override
	public void visitReadLineExpr(Tree.ReadLineExpr readStringExpr) {
		readStringExpr.type = BaseType.STRING;
	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.lvKind = Tree.LValue.Kind.ARRAY_ELEMENT;
		indexed.array.accept(this);
		if (!indexed.array.type.isArrayType()) {
			issueError(new NotArrayError(indexed.array.getLocation()));
			indexed.type = BaseType.ERROR;
		} else {
			indexed.type = ((ArrayType) indexed.array.type)
					.getElementType();
		}
		indexed.index.accept(this);
		if (!indexed.index.type.equal(BaseType.INT)) {
			issueError(new SubNotIntError(indexed.getLocation()));
		}
	}

	private void checkCallExpr(Tree.CallExpr callExpr, Symbol f) {
		Type receiverType = callExpr.receiver == null ? ((ClassScope) table
				.lookForScope(Scope.Kind.CLASS)).getOwner().getType()
				: callExpr.receiver.type;
		if (f == null) {
			issueError(new FieldNotFoundError(callExpr.getLocation(),
					callExpr.method, receiverType.toString()));
			callExpr.type = BaseType.ERROR;
		} else if (!f.isFunction()) {
			issueError(new NotClassMethodError(callExpr.getLocation(),
					callExpr.method, receiverType.toString()));
			callExpr.type = BaseType.ERROR;
		} else {
			Function func = (Function) f;
			callExpr.symbol = func;
			callExpr.type = func.getReturnType();
			if (callExpr.receiver == null && currentFunction.isStatik()
					&& !func.isStatik()) {
				issueError(new RefNonStaticError(callExpr.getLocation(),
						currentFunction.getName(), func.getName()));
			}
			// TODO: Add code here.
			//Leon
			//非成员函数
			if (callExpr.receiver != null && callExpr.receiver.isClass && !func.isStatik()){
                issueError(new NotClassFieldError(
                        callExpr.getLocation(),callExpr.method,callExpr.receiver.type.toString()));
            }
			
			if(!currentFunction.isStatik()&&callExpr.receiver==null&&!func.isStatik()){
				callExpr.receiver=new Tree.ThisExpr(callExpr.getLocation());
				callExpr.receiver.accept(this);
			}
			
			/**
			 *       call.actuals.size是调用时参数数目
			 *   	 其对应symbol的参数数目规定如下，
			 *      若不是静态，则应比调用时多1
			 *      若是静态则应与调用时数目相等。
			 */


            int symArgCount=func.getType().getArgList().size();
            int argCount=callExpr.actuals.size();//调用时的参数数目
            int add=0;
            if(!func.isStatik())
            	add=1;
           if((argCount+add)!=func.getType().numOfParams()){
        	   issueError(new BadArgCountError(callExpr.getLocation(),
                       callExpr.method, symArgCount-add , callExpr.actuals.size()));
           }else{
        	   
        	   for(int i=0; i<argCount; i++){
        		   callExpr.actuals.get(i).accept(this);
        		   
        		  	Type arg = callExpr.actuals.get(i).type;
	               	Type symArg = func.getType().getArgList().get(i+add);
	               	
	               	if(!arg.compatible(symArg)&&!symArg.equal(BaseType.ERROR) ){
	               		 issueError(new BadArgTypeError(callExpr.actuals.get(i)
	                                .getLocation(), i + 1, arg.toString(),
	                                symArg.toString()));
	               	}
               	
                }
        	   
           }
          	
		}
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		if (callExpr.receiver == null) {
			ClassScope cs = (ClassScope) table.lookForScope(Kind.CLASS);
			checkCallExpr(callExpr, cs.lookupVisible(callExpr.method));
			return;
		}
		callExpr.receiver.usedForRef = true;
		callExpr.receiver.accept(this);
		if (callExpr.receiver.type.equal(BaseType.ERROR)) {
			callExpr.type = BaseType.ERROR;
			return;
		}
		if (callExpr.method.equals("length")) {
			if (callExpr.receiver.type.isArrayType()) {
				if (callExpr.actuals.size() > 0) {
					issueError(new BadLengthArgError(callExpr.getLocation(),
							callExpr.actuals.size()));
				}
				callExpr.type = BaseType.INT;
				callExpr.isArrayLength = true;
				return;
			} else if (!callExpr.receiver.type.isClassType()) {
				issueError(new BadLengthError(callExpr.getLocation()));
				callExpr.type = BaseType.ERROR;
				return;
			}
		}

		if (!callExpr.receiver.type.isClassType()) {
			issueError(new NotClassFieldError(callExpr.getLocation(),
					callExpr.method, callExpr.receiver.type.toString()));
			callExpr.type = BaseType.ERROR;
			return;
		}

		ClassScope cs = ((ClassType) callExpr.receiver.type)
				.getClassScope();
		checkCallExpr(callExpr, cs.lookupVisible(callExpr.method));
	}

	@Override
	public void visitExec(Tree.Exec exec){
		exec.expr.accept(this);
	}
	
	@Override
	public void visitNewArray(Tree.NewArray newArrayExpr) {
		// TODO
		//Leon
		newArrayExpr.elementType.accept(this);
        newArrayExpr.length.accept(this);

        if (newArrayExpr.elementType.type.equal(BaseType.VOID)
        		|| newArrayExpr.elementType.type.equal(BaseType.ERROR))
        {
            issueError(new BadArrElementError(
                    newArrayExpr.elementType.getLocation()));
            newArrayExpr.type = BaseType.ERROR;
        }  else {
            newArrayExpr.type = new ArrayType(newArrayExpr.elementType.type);
        }

        if (!newArrayExpr.length.type.equal(BaseType.INT)
                && !newArrayExpr.length.type.equal(BaseType.ERROR)){
            issueError(new BadNewArrayLength(newArrayExpr.length.getLocation()));
        }
		
	}

	@Override
	public void visitNewClass(Tree.NewClass newClass) {
		Class c = table.lookupClass(newClass.className);
		newClass.symbol = c;
		if (c == null) {
			issueError(new ClassNotFoundError(newClass.getLocation(),
					newClass.className));
			newClass.type = BaseType.ERROR;
		} else {
			newClass.type = c.getType();
		}
	}

	@Override
	public void visitThisExpr(Tree.ThisExpr thisExpr) {
		if (currentFunction.isStatik()) {
			issueError(new ThisInStaticFuncError(thisExpr.getLocation()));
			thisExpr.type = BaseType.ERROR;
		} else {
			thisExpr.type = ((ClassScope) table.lookForScope(Scope.Kind.CLASS))
					.getOwner().getType();
		}
	}
/**
 * Leon delete
 * 1
 * @param instanceofExpr
 */
	/*
	@Override
	public void visitTypeTest(Tree.TypeTest instanceofExpr) {
		instanceofExpr.instance.accept(this);
		if (!instanceofExpr.instance.type.isClassType()) {
			issueError(new NotClassError(instanceofExpr.instance.type
					.toString(), instanceofExpr.getLocation()));
		}
		Class c = table.lookupClass(instanceofExpr.className);
		instanceofExpr.symbol = c;
		instanceofExpr.type = BaseType.BOOL;
		if (c == null) {
			issueError(new ClassNotFoundError(instanceofExpr.getLocation(),
					instanceofExpr.className));
		}
	}
*/
	@Override
	public void visitTypeCast(Tree.TypeCast cast) {
		cast.expr.accept(this);
		if (!cast.expr.type.isClassType()) {
			issueError(new NotClassError(cast.expr.type.toString(),
					cast.getLocation()));
		}
		Class c = table.lookupClass(cast.className);
		cast.symbol = c;
		if (c == null) {
			issueError(new ClassNotFoundError(cast.getLocation(),
					cast.className));
			cast.type = BaseType.ERROR;
		} else {
			cast.type = c.getType();
		}
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
		if (ident.owner == null) {
			Symbol v = table.lookupBeforeLocation(ident.name, ident
					.getLocation());
			if (v == null) {
				issueError(new UndeclVarError(ident.getLocation(), ident.name));
				ident.type = BaseType.ERROR;
			} else if (v.isVariable()) {
				// TODO: Add code here
				//Leon 2
				ident.type=v.getType();
				ident.symbol=(Variable) v;
				if(ident.symbol.isLocalVar()){
					ident.lvKind = Tree.LValue.Kind.LOCAL_VAR;
				}else if(ident.symbol.isParam()){
					ident.lvKind = Tree.LValue.Kind.PARAM_VAR;
				}else{
	
					if(!currentFunction.isStatik()){
						ident.owner= new Tree.ThisExpr(ident.getLocation());
						ident.owner.accept(this);
					}else{
						issueError(new RefNonStaticError(ident.getLocation(), currentFunction.getName(), ident.name));
					}
					ident.lvKind = Tree.LValue.Kind.MEMBER_VAR;
				}
				ident.isDefined = true;
				
			} else {
				// TODO: Add code here
				//Leon
				ident.type=v.getType();
				if(v.isClass()){
					ident.isClass=true;
				}
				ident.isDefined=false;
			}
		} else {
			ident.owner.usedForRef = true;
			ident.owner.accept(this);
			if (!ident.owner.type.equal(BaseType.ERROR)) {
				if (ident.owner.isClass || !ident.owner.type.isClassType()) {
					issueError(new NotClassFieldError(ident.getLocation(),
							ident.name, ident.owner.type.toString()));
					ident.type = BaseType.ERROR;
				} else {
					ClassScope cs = ((ClassType) ident.owner.type)
							.getClassScope();
					Symbol v = cs.lookupVisible(ident.name);
					if (v == null) {
						issueError(new FieldNotFoundError(ident.getLocation(),
								ident.name, ident.owner.type.toString()));
						ident.type = BaseType.ERROR;
					} else if (v.isVariable()) {
						ClassType thisType = ((ClassScope) table
								.lookForScope(Scope.Kind.CLASS)).getOwner()
								.getType();
						ident.type = v.getType();
						if (!thisType.compatible(ident.owner.type)) {
							issueError(new FieldNotAccessError(ident
									.getLocation(), ident.name,
									ident.owner.type.toString()));
						} else {
							ident.symbol = (Variable) v;
							ident.lvKind = Tree.LValue.Kind.MEMBER_VAR;
						}
					} else {
						ident.type = v.getType();
					}
				}
			} else {
				ident.type = BaseType.ERROR;
			}
		}
	}

	@Override
	public void visitClassDef(Tree.ClassDef classDef) {
		table.open(classDef.symbol.getAssociatedScope());
		for (Tree f : classDef.fields) {
			f.accept(this);
		}
		table.close();
	}

	@Override
	public void visitMethodDef(Tree.MethodDef func) {
		this.currentFunction = func.symbol;
		table.open(func.symbol.getAssociatedScope());
		func.body.accept(this);
		table.close();
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		table.open(program.globalScope);
		for (Tree.ClassDef cd : program.classes) {
			cd.accept(this);
		}
		table.close();
	}

	@Override
	public void visitBlock(Tree.Block block) {
		table.open(block.associatedScope);
		for (Tree s : block.block) {
			s.accept(this);
		}
		table.close();
	}

	@Override
	public void visitAssign(Tree.Assign assign) {
		// TODO: Add code here.
		//Leon
		assign.left.accept(this);
		assign.expr.accept(this);
		
		if(!assign.left.type.equal(BaseType.ERROR)){
			if(!assign.expr.type.compatible(assign.left.type) || assign.left.type.isFuncType()){
				issueError(new IncompatBinOpError(assign.getLocation(),  assign.left.type.toString(), "=", assign.expr.type.toString()));		
				assign.type = BaseType.ERROR;
			}			
			assign.type=assign.left.type;
		}else{
			assign.type = BaseType.ERROR;
		}
		
	/*	assign.left.accept(this);
        assign.expr.accept(this);

        if ((assign.left.type.isFuncType() ||
                !assign.expr.type.compatible(assign.left.type))
                && (!assign.left.type.equal(BaseType.ERROR))){
            issueError(new IncompatBinOpError(assign.getLocation(),
                    assign.left.type.toString(),"=",
                    assign.expr.type.toString()));
            assign.type = BaseType.ERROR;
        } else {
            assign.type = assign.left.type;
        }
		*/
		
	}

	@Override
	public void visitBreak(Tree.Break breakStmt) {
		if (breaks.isEmpty()/* TODO: REPLACE THIS WITH A CORRECT CONDITION */) {
			issueError(new BreakOutOfLoopError(breakStmt.getLocation()));
		}
	}
	
	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		checkTestExpr(forLoop.condition);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		breaks.add(forLoop);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	@Override
	public void visitRepeatLoop(Tree.RepeatLoop repeatLoop) {
		// TODO
		// repeat循环。参考visitWhileLoop，自行修改Tree，
		//Leon
		breaks.add(repeatLoop);
		if (repeatLoop.loopBody != null) {
			repeatLoop.loopBody.accept(this);
		}
		checkTestExpr(repeatLoop.condition);
		breaks.pop();
		
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		checkTestExpr(ifStmt.condition);
		if (ifStmt.trueBranch != null) {
			ifStmt.trueBranch.accept(this);
		}
		if (ifStmt.falseBranch != null) {
			ifStmt.falseBranch.accept(this);
		}
	}

	@Override
	public void visitPrint(Tree.Print printStmt) {
		int i = 0;
		for (Tree.Expr e : printStmt.exprs) {
			e.accept(this);
			i++;
			if (!e.type.equal(BaseType.ERROR) && !e.type.equal(BaseType.BOOL)&& !e.type.equal(BaseType.DOUBLE)//leon
					&& !e.type.equal(BaseType.INT)
					&& !e.type.equal(BaseType.STRING)) {
				issueError(new BadPrintArgError(e.getLocation(), Integer
						.toString(i), e.type.toString()));
			}
		}
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		
		//System.out.println("check return type");
		
		Type returnType = ((FormalScope) table
				.lookForScope(Scope.Kind.FORMAL)).getOwner().getReturnType();
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
		}
		// TODO: 检查返回值类型
		//Leon
		
		if (returnStmt.expr != null && returnType.equal(BaseType.VOID)){
            issueError(new BadReturnTypeError(returnStmt.getLocation(),
                    returnType.toString(), returnStmt.expr.type.toString()));
        }else if (returnStmt.expr == null && !returnType.equal(BaseType.VOID)){
            issueError(new BadReturnTypeError(returnStmt.getLocation(),
                    returnType.toString(), "void"));
        }else if (!returnStmt.expr.type.equal(BaseType.ERROR)
                && !returnStmt.expr.type.compatible(returnType)){
            issueError(new BadReturnTypeError(returnStmt.getLocation(),
                    returnType.toString(), returnStmt.expr.type.toString()));
        }
		
//		if(returnStmt.expr.type.equal(returnType)==false)
//			issueError(new BadReturnTypeError(returnStmt.loc, returnStmt.expr.type.toString(), returnType.toString()));
	}

	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		checkTestExpr(whileLoop.condition);
		breaks.add(whileLoop);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	// visiting types
	@Override
	public void visitTypeIdent(Tree.TypeIdent type) {
		switch (type.typeTag) {
		case Tree.VOID:
			type.type = BaseType.VOID;
			break;
		case Tree.INT:
			type.type = BaseType.INT;
			break;
		case Tree.BOOL:
			type.type = BaseType.BOOL;
			break;
		case Tree.DOUBLE:
			type.type = BaseType.DOUBLE;
			break;
		default:
			type.type = BaseType.STRING;
		}
	}

	@Override
	public void visitTypeClass(Tree.TypeClass typeClass) {
		Class c = table.lookupClass(typeClass.name);
		if (c == null) {
			issueError(new ClassNotFoundError(typeClass.getLocation(),
					typeClass.name));
			typeClass.type = BaseType.ERROR;
		} else {
			typeClass.type = c.getType();
		}
	}

	@Override
	public void visitTypeArray(Tree.TypeArray typeArray) {
		// TODO
		//Leon
		typeArray.elementType.accept(this);
		
        if (typeArray.elementType.type.equal(BaseType.VOID)) {
            issueError(new BadArrElementError(typeArray.getLocation()));
            typeArray.type = BaseType.ERROR;
        } else if (typeArray.elementType.type.equal(BaseType.ERROR))
            typeArray.type = BaseType.ERROR;
        else
            typeArray.type = new ArrayType(typeArray.elementType.type);
		
		
	}

	private void issueError(DecafError error) {
		Driver.getDriver().issueError(error);
	}

	private Type checkBinaryOp(Tree.Expr left, Tree.Expr right, int op, Location location) {
		left.accept(this);
		right.accept(this);

		if (left.type.equal(BaseType.ERROR) || right.type.equal(BaseType.ERROR)) {
			switch (op) {
			case Tree.PLUS:
			case Tree.MINUS:
			case Tree.MUL:
			case Tree.DIV:
				return left.type;
			case Tree.MOD:
				return BaseType.INT;
			default:
				return BaseType.BOOL;
			}
		}

		boolean compatible = false;
		Type returnType = BaseType.ERROR;
		switch (op) {
		case Tree.PLUS:
		case Tree.MINUS:
		case Tree.MUL:
		case Tree.DIV:
			compatible = ((left.type.equals(BaseType.INT)||left.type.equals(BaseType.DOUBLE))
					&& left.type.equal(right.type));
			if(compatible)
				returnType=left.type;
			else
				returnType=BaseType.ERROR;
			break;
			
		case Tree.GT:
		case Tree.GE:
		case Tree.LT:
		case Tree.LE:
			compatible = ((left.type.equals(BaseType.INT)||left.type.equals(BaseType.DOUBLE))
					&& left.type.equal(right.type));
			if(compatible)
				returnType = BaseType.BOOL;
			else
				returnType=BaseType.ERROR;
			break;
			
		case Tree.MOD:
			compatible = left.type.equal(BaseType.INT)
				&& right.type.equal(BaseType.INT);
			if(compatible)
				returnType = BaseType.INT;
			else
				returnType=BaseType.ERROR;
			break;
			
		case Tree.EQ:
		case Tree.NE:
			compatible = left.type.compatible(right.type)
            	|| right.type.compatible(left.type);
			if(compatible)
				returnType = BaseType.BOOL;
			else
				returnType=BaseType.ERROR;
			break;
		case Tree.AND:
		case Tree.OR:
			compatible = left.type.equal(BaseType.BOOL)
					&& left.type.equal(right.type);
			if(compatible)
				returnType = BaseType.BOOL;
			else
				returnType=BaseType.ERROR;
			break;
			
		// TODO: 为上面每个case添加代码
		default:
			break;
		}

		if (!compatible) {
			issueError(new IncompatBinOpError(location, left.type.toString(),
					Parser.opStr(op), right.type.toString()));
		}
		return returnType;
	}

	private void checkTestExpr(Tree.Expr expr) {
		expr.accept(this);
		if (!expr.type.equal(BaseType.ERROR) && !expr.type.equal(BaseType.BOOL)) {
			issueError(new BadTestExpr(expr.getLocation()));
		}
	}

}
