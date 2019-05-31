package com.excilys.cdb.controller;

public enum ChoiceMenuEnum {
	LIST_COMPUTERS("List computers"),
	LIST_COMPANIES("List companies"),
	SHOW_COMPUTER_DETAIL("Show computer details"),
	CREATE_COMPUTER("Create computer details"),
	UPDATE_COMPUTER("Update a computer"),
	DELETE_COMPUTER("Delete a computer");
	
	private String menuMessage;
	
	ChoiceMenuEnum(String pMenuMessage) {
		this.menuMessage = pMenuMessage;
	}

	public String getMenuMessage() {
		return menuMessage;
	}
}
