/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

 
package com.trackplus.tools;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * <p>
 * This class implements a command-line test tool for checking connections
 * to database servers via JDBC. More details about the usage of this tool 
 * and its command-line options and parameters are available through 
 * the <code>--help</code> option.
 * </p>
 *
 * <code>$ java -jar DBConnectionTester.jar --help</code>
 *
 */

public class DBConnectionTester {
	
	private Options options = new Options ();
	private boolean verbose = false;
	private String user = null;
	private String jdbc = null;
	private String url = null;
	private String password = null;

	public static void main(String[] args) {
		DBConnectionTester dbtester = new DBConnectionTester();

		dbtester.parseCommandLineArguments (args);
		dbtester.checkCommandLineArguments ();
		dbtester.evaluateCommandLineArguments ();

	}
	
	private void parseCommandLineArguments (String[] args)
	{
		Option option = new Option ("h", "Print this help message.");
		option.setLongOpt ("help");
		options.addOption (option);

		option = new Option ("v", "Turn on verbose output.");
		option.setLongOpt ("verbose");
		options.addOption (option);

		options.addOption (newOption ("u", "Database user name.", "user", "database user"));
		options.addOption (newOption ("c", "JDBC connector class.", "jdbc", "class name"));
		options.addOption (newOption ("url", "Use this user name for login.", "url", "JDBC URL"));

		options.addOption (newOption ("p", "Database user password.", "password", "password"));
		option.setOptionalArg (true);
		options.addOption (option);

		CommandLineParser parser = new GnuParser ();
		CommandLine line = null;
		try
		{
			line = parser.parse (options, args);
		}
		catch (ParseException e)
		{
			System.err.println ("Error: Parsing failed. Reason: " + e.getMessage ());
			System.exit (1);
		}

		if (line != null) {
			if (line.hasOption ("h"))
			{
				printUsage ();
				System.exit (0);
			}

			if (line.hasOption ("v"))
			{
				verbose = true;
			}

			if (line.hasOption ("u"))
			{
				user = line.getOptionValue ("u", "");
			}

			if (line.hasOption ("c"))
			{
				jdbc = line.getOptionValue ("c", "");
			}

			if (line.hasOption ("url"))
			{
				url = line.getOptionValue ("url", "");
			}

			if (line.hasOption ("p"))
			{
				password = line.getOptionValue ("p");
				if (password == null)
				{
					try
					{
						System.out.print ("Password: ");
						System.out.flush ();
						password = new BufferedReader (new InputStreamReader (System.in)).readLine ();
					}
					catch (IOException e)
					{
						System.out.println ();
						System.exit (1);
					}
				}
			}
		}
		return;
	}


	private void checkCommandLineArguments ()
	{
		if (jdbc == null)
		{
			System.out.println ("Error: You must specify a JDBC class.");
			System.exit (1);
		}

		if (user == null || user.length() < 1)
		{
			System.out.println ("Error: You must specify a user.");
			System.exit (1);
		}

		if (password == null)
		{
			System.out.println ("Error: You must specify a password.");
			System.exit (1);
		}
	}


	private void evaluateCommandLineArguments ()
	{
		try
		{

		}
		catch (Exception e)
		{


			System.exit (1);
		}
	}

	private Option newOption (String shortOption, String description, String longOption, String argumentName)
	{
		Option option = new Option (shortOption, description);
		option.setLongOpt (longOption);
		option.setArgs (1);
		option.setArgName (argumentName);

		return option;
	}


	private void printUsage ()
	{
		HelpFormatter formatter = new HelpFormatter ();
		formatter.printHelp (new PrintWriter (System.out, true), 80, "java -jar DBConnectionTester.jar [options]", "\nOptions:", options, 2, 2, "");

		System.out.println ("\n\n" +
			"$ java -jar DBConnectionTester.jar --user joe --password xyz\\\n" +
			"      --jdbc org.gjt.mm.mysql.Driver --url jdbc:mysql://localhost:3306/track\\\n\n" +
			"$ java -jar DBConnectionTester.jar --user joe --password xyz\\\n" +
			"      --jdbc org.firebirdsql.jdbc.FBDriver --url jdbc:firebirdsql://localhost/C:/DB//track\\\n\n" +
			"$ java -jar DBConnectionTester.jar --user joe --password xyz\\\n" +
			"      --jdbc net.sourceforge.jtds.jdbc.Driver --url jdbc:jtds:sqlserver://localhost/track\\\n\n"
		);
	}



}
