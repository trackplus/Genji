/**
 * Genji Scrum Tool and Issue Tracker
 * Copyright (C) 2015 Steinbeis GmbH & Co. KG Task Management Solutions

 * <a href="http://www.trackplus.com">Genji Scrum Tool</a>

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/* $Id:$ */

package com.aurel.track.admin.customize.treeConfig.screen.importScreen.parser;

import java.io.File;

import org.xml.sax.helpers.DefaultHandler;

import com.aurel.track.admin.customize.category.report.exportReport.ExportTemplateParser;

public class ParserFactory extends DefaultHandler
{

	private static ParserFactory instance;
	private FieldParser fieldParser;
	private FieldConfigParser fieldConfigParser;
	private GeneralSettingsParser generalSettingsParser;
	private TextBoxSettingsParser textBoxSettingsParser;  
	
	private ListParser listParser;
	private OptionParser optionParser;
	private OptionSettingsParser optionSettingsParser;
	
	private ScreenParser screenParser;
	private ScreenTabParser screenTabParser;
	private ScreenPanelParser screenPanelParser;
	private ScreenFieldParser screenFieldParser;
	
	private ExportTemplateParser exportTemplateParser;
	private PersonParser personParser;

	//TODO This could be a problem, one file for everybody?
	private static File file;
	
	public static void setFile(File aFile)
	{
		file = aFile;
	}
	
	public static File getFile()
	{
		return file;
	}
	
	public static ParserFactory getInstance()
	{
		if (instance == null)
			instance = new ParserFactory();
		return instance;
	}
	
	public FieldParser getFieldParser()
	{
		if (fieldParser == null)
			fieldParser = new FieldParser();
		return fieldParser;
	}
	
	public FieldConfigParser getFieldConfigParser()
	{
		if (fieldConfigParser == null)
			fieldConfigParser = new FieldConfigParser();
		return fieldConfigParser;
	}
	
	public GeneralSettingsParser getGeneralSettingsParser()
	{
		if (generalSettingsParser == null)
			generalSettingsParser = new GeneralSettingsParser();
		return generalSettingsParser;
	}
	
	public TextBoxSettingsParser getTextBoxSettingsParser()
	{
		if (textBoxSettingsParser == null)
			textBoxSettingsParser = new TextBoxSettingsParser();
		return textBoxSettingsParser;
	}
	
	public ListParser getListParser()
	{
		if (listParser == null)
			listParser = new ListParser();
		return listParser;
	}
	
	public OptionParser getOptionParser()
	{
		if (optionParser == null)
			optionParser = new OptionParser();
		return optionParser;
	}
	
	public OptionSettingsParser getOptionSettingsParser()
	{
		if (optionSettingsParser == null)
			optionSettingsParser = new OptionSettingsParser();
		return optionSettingsParser;
	}
	
	public ScreenParser getScreenParser()
	{
		if (screenParser == null)
			screenParser = new ScreenParser();
		return screenParser;
	}
	
	public ScreenTabParser getScreenTabParser()
	{
		if (screenTabParser == null)
			screenTabParser = new ScreenTabParser();
		return screenTabParser;
	}
	
	public ScreenPanelParser getScreenPanelParser()
	{
		if (screenPanelParser == null)
			screenPanelParser = new ScreenPanelParser();
		return screenPanelParser;
	}
	
	public ScreenFieldParser getScreenFieldParser()
	{
		if (screenFieldParser == null)
			screenFieldParser = new ScreenFieldParser();
		return screenFieldParser;
	}
	
	public ExportTemplateParser getExportTemplateParser()
	{
		if (exportTemplateParser == null)
			exportTemplateParser = new ExportTemplateParser();
		return exportTemplateParser;
	}

	
	public PersonParser getPersonParser()
	{
		if (personParser == null)
			personParser = new PersonParser();
		return personParser;
	}
	
}
