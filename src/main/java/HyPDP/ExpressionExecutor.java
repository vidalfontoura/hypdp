/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package HyPDP;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 *
 * @author vfontoura
 */
public class ExpressionExecutor {

	public static double calculate(String function) {
		double value = 0;
		try {

			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");

			value = (double) engine.eval(function);

		} catch (ScriptException ex) {
			System.err.println(String.format("An error occurred while executing the expression: %s", function));
		}
		return value;
	}

	public static void main(String[] args) {
		String function = "(1.0 * 10.0) + (2.0 * 1)";
		double calculate = ExpressionExecutor.calculate(function);
		System.out.println(calculate);
	}
}
