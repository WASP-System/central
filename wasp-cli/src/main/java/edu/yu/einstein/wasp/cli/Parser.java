package edu.yu.einstein.wasp.cli;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

	private final static Logger logger = LoggerFactory.getLogger(Parser.class);

	private Options options = new Options();
	private CommandLine cl;
	
	private String user;
	private char[] password;

	public Parser(String[] args) {
		options.addOption(new Option("h", "help", false, "print this help message"));
		options.addOption(new Option("u", "user", true, "username"));
		options.addOption(new Option("T", "target", true, "message target"));
		options.addOption(new Option("t", "task", true, "task to run"));
		options.addOption(new Option("H", "host", true, "host (default localhost)"));
		options.addOption(new Option("l", "list", true, "possible values: plugins, builds, sampleSubtypes, workflows, fileTypes, cellLibraries, users or a combination e.g. -l plugins,builds"));
		options.addOption(new Option("m", "message", true, "message to send"));
		options.addOption(new Option("P", "port", true, "remote port (default 23532)"));
		options.addOption(new Option("p", "password", true, "password"));
		options.addOption(new Option("g", "template-cellLib", true, "<filePath> : generate template file to register files assuming cellLibraries exist in db"));
		options.addOption(new Option("G", "template-no-cellLib", true, "<filePath> : generate template file to register files assuming cellLibraries do not exist in db"));
		options.addOption(new Option("i", "template-import", true, "<filePath> : import template to register files"));
		parseCommandline(args);
		help();
		validate();
	}

	private void parseCommandline(String[] args) {
		BasicParser parser = new BasicParser();
		try {
			cl = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error("Unable to parse options: " + e.getMessage());
			formatHelp();
			System.exit(1);
		}
	}

	public void formatHelp() {
		HelpFormatter formatter = new HelpFormatter();
		String header = "-------------------------------------------------";
		String footer = "-------------------------------------------------\n" +
						"WASP command line tool\n" +
						"(c) 2012-2014 Albert Einstein College of Medicine";
		formatter.printHelp("wasp-cli", header, options, footer);
	}

	private void help() {
		if ((cl.hasOption("h") && ! cl.hasOption("T") ) || cl.getOptions().length == 0) {
			formatHelp();
			System.exit(0);
		}
	}
	
	private void validate() {
		if (!cl.hasOption("u")) {
			System.out.print("username: ");
			Scanner scan = new Scanner(System.in);
		    this.user = scan.next();
		} else {
			this.user = cl.getOptionValue("u");
		}
		if (!cl.hasOption("p")) {
			try {
				this.password = PasswordField.getPassword(System.in, "password: ");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(2);
			}
		} else {
			this.password = cl.getOptionValue("p").toCharArray();
		}
	}
	
	public CommandLine getCommandLine() {
		return this.cl;
	}
	
	protected String getUser() {
		return this.user;
	}
	
	protected char[] getPassword() {
		return this.password;
	}

}
