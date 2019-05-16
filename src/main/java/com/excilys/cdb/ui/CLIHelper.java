package com.excilys.cdb.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.constant.Constant;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NotAChoiceException;
import com.excilys.cdb.exception.NotAnIntegerException;
import com.excilys.cdb.exception.WrongComputerArgumentException;

public class CLIHelper {

	private static Scanner in = new Scanner(System.in);
	private static final Logger logger = LoggerFactory.getLogger(CLIHelper.class);

	private CLIHelper() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Get the choice of the user and throw an exception if not correct
	 * @return the choice of the user
	 * @throws NotAChoiceException
	 */
	public static int choice(String[] choices) throws NotAChoiceException{
		String choice = in.nextLine();
		try {
			int intChoice = Integer.parseInt(choice); // If this doesn't fail then it's integer
			if(intChoice>=1 && intChoice<=choices.length) {
				return intChoice;
			} else {
				String message = String.format(Constant.CHOICE_NOT_POSSIBLE_FORMAT, choice);
				logger.warn(message);
				throw new NotAChoiceException(message);
			}
		} catch (NumberFormatException e) {
			String message = String.format(Constant.CHOICE_NOT_POSSIBLE_FORMAT, choice);
			logger.warn(message);
			throw new NotAChoiceException(message);
		}
	}

	/**
	 * Ask the user to enter an integer. Fail if it's not an integer
	 * @param message
	 * @return the integer the user entered
	 * @throws NotAnIntegerException 
	 * @throws NumberFormatException if not an integer
	 */
	public static int askIntegerHelper() throws NotAnIntegerException{
		String integer = in.nextLine();
		try {
			return Integer.parseInt(integer); // If this doesn't fail then it's integer
		} catch (NumberFormatException e) {
			String message = "Expected Integer, actual " + integer;
			logger.error(message);
			throw new NotAnIntegerException(message);
		}
	}

	/**
	 * Ask the user to enter a computer. Fail if wrong number of argument
	 * @return the DTOComputer that the user enter
	 * @throws IllegalArgumentException
	 */
	public static DTOComputer askComputer() throws WrongComputerArgumentException{
		String line = in.nextLine();
		String[] parsedLine = Stream.of(line.split(",")).map(x -> x.trim().equals("")?"NULL":x.trim()).toArray(String[]::new);
		if(parsedLine[0].equals("NULL")) {
			logger.error("Input : Empty computer");
			throw new WrongComputerArgumentException("Name is mandatory !");
		}
		if(parsedLine.length == 1) {
			return new DTOComputer.DTOComputerBuilder(parsedLine[0]).build();
		} else if (parsedLine.length == 4) {
			return new DTOComputer.DTOComputerBuilder(parsedLine[0])
					.withIntroduced(parsedLine[1])
					.withDiscontinued(parsedLine[2])
					.withCompany(parsedLine[3])
					.build();
		} else {
			String message = "Wrong number of argument !";
			logger.error("{} Input : {}",message,line);
			throw new WrongComputerArgumentException(message);
		}
	}

	/**
	 * Print a message in a box
	 * @param message
	 */
	public static void box(String message) {
		System.out.println("+-" + String.join("", Collections.nCopies(message.length(), "-")) + "-+\n"
				+ "| " + message + " |\n"
				+ "+-" + String.join("", Collections.nCopies(message.length(), "-")) + "-+");
	}

	/**
	 * Print a message on multiple line or a menu
	 * @param messages
	 * @param menu true if it's a menu
	 */
	public static void box(String[] messages, boolean menu) {
		List<String> messagesList = new ArrayList<>(Arrays.asList(messages));
		Optional<String> optionalMax = messagesList.stream().max(Comparator.comparingInt(String::length));
		if(optionalMax.isPresent()) {
			int max = optionalMax.get().length();
			String borderLine = menu?"+-" + String.join("", Collections.nCopies(max+3, "-")) + "-+":"+-" + String.join("", Collections.nCopies(max, "-")) + "-+";

			System.out.println(borderLine);
			for(int i=1;i<=messagesList.size();i++) {
				String line = "| ";
				if(menu) {
					line+= i + ". ";
				}
				line+= messagesList.get(i-1) + String.join("", Collections.nCopies(max-messagesList.get(i-1).length(), " ")) + " |";
				System.out.println(line);
			}
			System.out.println(borderLine);
		}
	}

	/**
	 * Print companies in a table
	 * @param headers of the table
	 * @param companies
	 */
	public static void listCompaniesHelper(String[] headers, List<DTOCompany> companies) {
		int maxIdLength = companies.stream()
				.map(DTOCompany::getId)
				.mapToInt(String::length)
				.max().orElse(headers[0].length());
		int maxNameLength = companies.stream()
				.map(DTOCompany::getName)
				.mapToInt(String::length)
				.max().orElse(headers[1].length());

		String borderLine = "+-" + String.join("", Collections.nCopies(maxIdLength, "-")) + "-+-"
				+ String.join("", Collections.nCopies(maxNameLength, "-")) + "-+";
		String companyFormater = "| %-"+maxIdLength+"s | %-" + maxNameLength + "s |%n";

		System.out.println(borderLine);
		System.out.format(companyFormater,headers[0], headers[1]);
		System.out.println(borderLine);
		for(DTOCompany company:companies) {
			System.out.format(companyFormater,company.getId(), company.getName());
		}
		System.out.println(borderLine);
	}

	/**
	 * Print computers in a table
	 * @param headers of the table
	 * @param computers
	 */
	public static void listComputersHelper(String[] headers, List<DTOComputer> computers) {
		if(headers.length == 0 || computers.isEmpty()) {
			return;
		}
		int maxIdLength = 			Math.max(headers[0].length(), computers.stream()
				.map(DTOComputer::getId)
				.mapToInt(String::length)
				.max().orElse(headers[0].length()));
		int maxNameLength =			Math.max(headers[1].length(),computers.stream()
				.map(DTOComputer::getName)
				.mapToInt(String::length)
				.max().orElse(headers[1].length()));
		int maxIntroducedLength = 	Math.max(headers[2].length(),computers.stream()
				.map(DTOComputer::getIntroduced)
				.mapToInt(String::length)
				.max().orElse(headers[2].length()));
		int maxDiscontinuedLength = Math.max(headers[3].length(),computers.stream()
				.map(DTOComputer::getDiscontinued)
				.mapToInt(String::length)
				.max().orElse(headers[3].length()));

		int maxCompanyIdLength = 	Math.max(headers[4].length(),computers.stream()
				.map(DTOComputer::getCompany)
				.mapToInt(String::length)
				.max().orElse(headers[4].length()));

		String borderLine = "+-" + String.join("", Collections.nCopies(maxIdLength, "-")) + "-+-"
				+ String.join("", Collections.nCopies(maxNameLength, "-")) + "-+-"
				+ String.join("", Collections.nCopies(maxIntroducedLength, "-")) + "-+-"
				+ String.join("", Collections.nCopies(maxDiscontinuedLength, "-")) + "-+-"
				+ String.join("", Collections.nCopies(maxCompanyIdLength, "-")) + "-+";
		String computerFormater = "| %-" + maxIdLength + "s | %-" + maxNameLength + "s | %-"
				+ maxIntroducedLength + "s | %-" + maxDiscontinuedLength + "s | %-"
				+ maxCompanyIdLength + "s |%n";

		System.out.println(borderLine);
		System.out.format(computerFormater,headers[0], headers[1],headers[2],headers[3],headers[4]);
		System.out.println(borderLine);
		for(DTOComputer computer:computers) {
			System.out.format(computerFormater,computer.getId(), computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompany());
		}
		System.out.println(borderLine);
	}
}
