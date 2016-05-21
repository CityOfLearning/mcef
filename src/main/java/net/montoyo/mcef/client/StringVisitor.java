package net.montoyo.mcef.client;

import org.cef.callback.CefStringVisitor;

import net.montoyo.mcef.api.IStringVisitor;

public class StringVisitor implements CefStringVisitor {

	IStringVisitor isv;

	public StringVisitor(IStringVisitor isv) {
		this.isv = isv;
	}

	@Override
	public void visit(String string) {
		isv.visit(string);
	}

}
