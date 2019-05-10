package com.excilys.cdb.controller;

public enum PageMenuEnum {
	PREVIOUS_PAGE("\u2190 Previous Page"),
	NEXT_PAGE("\u2192 Next Page"),
	SET_PAGE("Set Page"),
	FIRST_PAGE("First Page"),
	LAST_PAGE("Last Page"),
	QUIT("Quit");
	
	private String menuMessage;
	
	PageMenuEnum(String pMenuMessage) {
		this.menuMessage = pMenuMessage;
	}

	public String getMenuMessage() {
		return menuMessage;
	}
}
