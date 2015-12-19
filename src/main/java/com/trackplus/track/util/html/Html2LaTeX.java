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

package com.trackplus.track.util.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import com.aurel.track.beans.TAttachmentBean;
import com.aurel.track.dao.DAOFactory;


public class Html2LaTeX {

	private static final Logger LOGGER = LogManager.getLogger(Html2LaTeX.class);

	private static Pattern quotes = Pattern.compile("\"(.+?)\"");

	private Html2LaTeX() {

	}

	/**
	 * Get LaTeX text translated from HTML.
	 *
	 * @param bodyHtml
	 * @return the LaTeX representation of the bodyHtml
	 */
	public static String getLaTeX(String bodyHtml) {

		// Make sure verbatim is properly transformed, protect spaces and line
		// breaks
		Pattern patt = Pattern.compile("(?s)(.?)<div\\s*class=\\\"code\\-text\\\".+?>(.+?)</div>");
		Matcher m = patt.matcher(bodyHtml);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String verbatim = m.group(2);
			verbatim = verbatim.replace("\n", "<br>").replace(" ", "&nbsp;");
			String text = m.group(1) + "<div class=\"code-text\">" + verbatim + "</div>"; // +
																							// m.group(3);
			m.appendReplacement(sb, Matcher.quoteReplacement(text));
		}
		m.appendTail(sb);
		bodyHtml = sb.toString();

		// System.err.println(bodyHtml);

		Document doc = Jsoup.parse(bodyHtml);

		String latex = getLatexText(doc);
		sb = new StringBuffer();
		String[] lines = latex.split("\n");
		for (int i = 0; i < lines.length; ++i) {
			if ("\\\\".equals(lines[i].trim())) {
				lines[i] = "";
			}
			sb.append(lines[i] + "\n");
		}

		return sb.toString();
	}

	/**
	 * Format an Element to LaTeX
	 *
	 * @param element
	 *            the root element to format
	 * @return formatted text
	 */
	public static String getLatexText(Element element) {
		FormattingVisitor formatter = new FormattingVisitor();
		NodeTraversor traversor = new NodeTraversor(formatter);
		traversor.traverse(element); // walk the DOM, and call .head() and
										// .tail() for each node

		return formatter.toString();
	}

	// the formatting rules, implemented in a breadth-first DOM traverse
	private static class FormattingVisitor implements NodeVisitor {
		private static final int maxWidth = 80;
		private int width = 0;
		private StringBuilder sbuilder = new StringBuilder(); // holds the
															// accumulated text
		private boolean inCell = false;
		private boolean inHeader = false;
		private boolean inCaption = false;
		private boolean inTable = false;
		private boolean inFigure = false;
		private boolean inImage = false;
		private boolean isVerbatim = false;
		private boolean inUrl = false;
		private LaTeXFigure figure = null;
		private LaTeXTable table = null;
		private LaTeXTable.TableHeader header = null;
		private LaTeXTable.TableRow row = null;
		private LaTeXTable.TableCell cell = null;

		private StringBuilder cellbuilder = new StringBuilder();
		// hit when the node is first seen
		@Override
		public void head(Node node, int depth) {
			String name = node.nodeName();

			String ttext = null;
			if (node instanceof TextNode) { // TextNodes carry all user-readable
				ttext = ((TextNode) node).text();
				ttext = ttext.replace("\\", "\\\\");
				ttext = ttext.replace("$","\\$");
				ttext = ttext.replace("%","\\%");
				ttext = ttext.replace("#","\\#");
				ttext = ttext.replace("&","\\&");
//				if (inCell) {
//					cell.setText(ttext);
//				} else
				if (inTable && inCaption) {
					table.setCaption(ttext);
				} else if (inFigure && inCaption) {
					figure.setCaption(ttext);
				} else {
					append(ttext);
				}
			} else if (name.equals("ul"))
				append("\n \\begin{itemize} ");
			else if (name.equals("ol"))
				append("\n \\begin{enumerate} ");
			else if (name.equals("li"))
				append("\n \\item ");
			else if (name.equals("h1"))
				append("\n\\chapter{");
			else if (name.equals("h2"))
				append("\n\\section{");
			else if (name.equals("h3"))
				append("\n\\subsection{");
			else if (name.equals("h4"))
				append("\n\\subsubsection{");
			else if (name.equals("em"))
				append("\\emph{");
			else if (name.equals("b") || name.equals("strong"))
				append("\\textbf{");
			else if (name.equals("code"))
				append("\\texttt{");
			else if (name.equals("u"))
				append("\\underline{");
			else if (name.equals("i"))
				append("\\textit{");
			else if (name.equals("dl"))
				append("\n \\begin{description}");
			else if (name.equals("dt"))
				append("\n \\item[");
			else if (name.equals("dd"))
				append("");
			else if (name.equals("span")) {
				if (node.hasAttr("class")) {
					if ("inlineLink".equals(node.attr("class"))) {
						append("\\inlineLink{");
					}
				} else if(node.hasAttr("style")) {
					if ((node.attr("style").contains("color"))) {
						append("{\\color{tpred}");
					}
				}
				else {
					append("{");
				}
			} else if (name.equals("div")) {
				if (node.hasAttr("class")) {
					if ("code-text".equals(node.attr("class"))) {
						append("\n\\begin{verbatim}\n");
						isVerbatim = true;
					}
				}
			} else if (name.equals("img")) {
				if (node.hasAttr("src")) {
					String imgfile = node.attr("src");
					String figSuffix = ".png";

					try {
						imgfile = imgfile.substring(imgfile.lastIndexOf("attachKey="));
						imgfile = imgfile.replace("attachKey=", "");
						imgfile = imgfile.replace("\"", "");

						TAttachmentBean image = DAOFactory.getFactory().getAttachmentDAO().loadByIDWithDimensions(Integer.valueOf(imgfile));

						figure.setOriginalWidth(image.getHeight());
						figure.setOriginalHeight(image.getWidth());

						figSuffix = FilenameUtils.getExtension(image.getFileName());

						figure.setFileName("fig" + imgfile + "." + figSuffix);

					} catch (Exception e) {
						LOGGER.debug("Attachment not accessible: " + e.getMessage(), e);
						inFigure = false;
					}
					if (!inFigure) {
						figure = new LaTeXFigure();
						inFigure = true;
						inImage=true;
					}

					if (node.hasAttr("width")) {
						figure.setWidth(Integer.valueOf(node.attr("width")));
					}
					if (node.hasAttr("height")) {
						figure.setHeight(Integer.valueOf(node.attr("height")));
					}
				}
			} else if (name.equals("table")) {
				table = new LaTeXTable();
				inTable = true;
			} else if (name.equals("figure")) {
				figure = new LaTeXFigure();
				inFigure = true;
			} else if (name.equals("thead")) {
				header = table.getNewHeader();
				inHeader = true;
			} else if (name.equals("tr")) {
				row = table.getNewRow();
			} else if (name.equals("th")) {
				inCell = true;
				cell = table.getNewCell();
			} else if (name.equals("td")) {
				inCell = true;
				cellbuilder = new StringBuilder();
				cell = table.getNewCell();
			} else if (name.equals("caption")) {
				inCaption = true;
			} else if (name.equals("figcaption")) {
				inCaption = true;
			} else if (name.equals("a")) {
				inUrl = true;
				append(String.format("\\href{%s}{", node.absUrl("href")));
			}
			else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr"))
				append("\n");
		}

		// hit when all of the node's children (if any) have been visited
		@Override
		public void tail(Node node, int depth) {
			String name = node.nodeName();
			if (name.equals("ul"))
				append("\n \\end{itemize} ");
			else if (name.equals("ol"))
				append("\n \\end{enumerate} ");
			else if (name.equals("dl"))
				append("\n \\end{description} ");
			else if (name.equals("dt"))
				append("] ");
			else if (name.equals("div")) {
				if (node.hasAttr("class")) {
					if ("code-text".equals(node.attr("class"))) {
						append("\n\\end{verbatim}");
						isVerbatim = false;
					}
				}
			} else if (name.equals("span")) {
				if (node.hasAttr("class")) {
					if ("inlineLink".equals(node.attr("class"))) {
						append("} ");
					}
				} else if (node.hasAttr("style")) {
					if ((node.attr("style").contains("color"))) {
						append("}");
					}
				} else {
					append("}");
				}
			}

			else if (name.equals("img")) {
				if (inImage) { // could have been broken off
					append("\n" + figure.getLaTeX());
					inFigure = false;
					inImage = false;
				}
			}

			else if (name.equals("figure")) {
				if (inFigure) { // could have been broken off
					append("\n" + figure.getLaTeX());
					inFigure = false;
				}
			}

			else if (name.equals("table")) {
				append("\n" + table.getLaTeX());
				inTable = false;
			}

			else if (name.equals("thead")) {
				table.addHeader(header);
				inHeader = false;
			}

			else if (name.equals("tr")) {
				if (inHeader) {
					header.addRow(row);
				} else {
					table.addRow(row);
				}
			} else if (name.equals("th")) {
				inCell = false;
				row.addCell(cell);
			} else if (name.equals("td")) {
				inCell = false;
				cell.setText(cellbuilder.toString());
				row.addCell(cell);
			} else if (name.equals("caption")) {
				inCaption = false;
			} else if (name.equals("figcaption")) {
				inCaption = false;
			}

			else if (name.equals("a")) {
				inUrl = false;
				append("}");
			}

			if (StringUtil.in(name, "h1", "h2", "h3", "h4", "em", "u", "b", "strong", "i", "code")) {
				append("}");
			}
			if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5")) {
				append("\n");
			}
		}

		// appends text to the string builder with a simple word wrap method
		private void append(String text) {
			if (!inCell) {
				append(text, sbuilder);
			} else {
				append(text, cellbuilder);
			}
		}

		private void append(String text, StringBuilder accum) {
			if (isVerbatim || inUrl) {
				accum.append(text);
				return;
			} else {
				Matcher m = quotes.matcher(text);
				StringBuffer stringBuffer = new StringBuffer();

				while (m.find()) {
					m.appendReplacement(stringBuffer, "\"`" + m.group(1) + "\"'");
				}
				m.appendTail(stringBuffer);
				text = stringBuffer.toString();
				text = text.replace("_", "\\_");
			}
			text = text.replace("\u20ac", "\\euro");
			text = text.replace("\u00a0", " "); // &nbsp;

			if (text.startsWith("\n"))
				width = 0; // reset counter if starts with a newline. only from
							// formats above, not in natural text
			if (text.equals(" ") && (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
				return; // don't accumulate long runs of empty spaces

			if (text.length() + width > maxWidth) { // won't fit, needs to wrap
				String words[] = text.split("\\s+");
				if (text.endsWith(" ")) {
					words[words.length - 1] = words[words.length - 1] + " ";
				}
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					boolean last = i == words.length - 1;
					if (!last) // insert a space if not the last word
						word = word + " ";
					if (word.length() + width > maxWidth) { // wrap and reset										// counter
						accum.append("\n").append(word);
						width = word.length();
					} else {
						accum.append(word);
						width += word.length();
					}
				}
			} else { // fits as is, without need to wrap text
				accum.append(text);
				width += text.length();
			}
		}

		@Override
		public String toString() {
			return sbuilder.toString();
		}
	}

}
