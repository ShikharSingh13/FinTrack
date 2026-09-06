package com.shikhar;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.shikhar.entity.Budget;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;

public class PdfGenerator {

	// =========================================================
	// BRAND COLORS
	// =========================================================

	private static final Color NAVY = new Color(25, 42, 86);

	private static final Color BLUE = new Color(37, 99, 235);

	private static final Color LIGHT_BLUE = new Color(239, 246, 255);

	private static final Color DARK = new Color(31, 41, 55);

	private static final Color GREY = new Color(107, 114, 128);

	private static final Color LIGHT_GREY = new Color(243, 244, 246);

	private static final Color WHITE = Color.WHITE;

	private static final Color GREEN = new Color(22, 163, 74);

	private static final Color RED = new Color(220, 38, 38);

	private static final Color GOLD = new Color(180, 130, 40);

	// =========================================================
	// MAIN PDF GENERATOR
	// =========================================================

	public static void generateReport(Document document, User user, int month, int year, List<Expense> expenses,
			Budget budget) throws Exception {

		// -----------------------------------------------------
		// PAGE SETTINGS
		// -----------------------------------------------------

		document.setPageSize(PageSize.A4);
		document.setMargins(36, 36, 50, 50);

		// -----------------------------------------------------
		// FONTS
		// -----------------------------------------------------

		Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, WHITE);

		Font subtitleFont = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(219, 234, 254));

		Font sectionFont = new Font(Font.HELVETICA, 11, Font.BOLD, NAVY);

		Font normalFont = new Font(Font.HELVETICA, 9, Font.NORMAL, DARK);

		Font smallFont = new Font(Font.HELVETICA, 8, Font.NORMAL, GREY);

		Font boldFont = new Font(Font.HELVETICA, 9, Font.BOLD, DARK);

		Font whiteBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD, WHITE);

		// =====================================================
		// HEADER / BRANDING
		// =====================================================

		PdfPTable header = new PdfPTable(2);

		header.setWidthPercentage(100);

		header.setWidths(new float[] { 1.2f, 5f });

		header.setSpacingAfter(18);

		// -----------------------------------------------------
		// LOGO
		// -----------------------------------------------------

		PdfPCell logoCell = new PdfPCell();

		logoCell.setBackgroundColor(NAVY);
		logoCell.setBorder(Rectangle.NO_BORDER);
		logoCell.setPadding(12);
		logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		Paragraph logo = new Paragraph("PE", new Font(Font.HELVETICA, 20, Font.BOLD, WHITE));

		logo.setAlignment(Element.ALIGN_CENTER);

		logoCell.addElement(logo);

		header.addCell(logoCell);

		// -----------------------------------------------------
		// HEADER TEXT
		// -----------------------------------------------------

		PdfPCell headerText = new PdfPCell();

		headerText.setBackgroundColor(NAVY);
		headerText.setBorder(Rectangle.NO_BORDER);
		headerText.setPaddingTop(12);
		headerText.setPaddingBottom(12);
		headerText.setPaddingLeft(15);

		Paragraph brandName = new Paragraph("PERSONAL EXPENSE TRACKER", titleFont);

		brandName.setSpacingAfter(3);

		headerText.addElement(brandName);

		Paragraph reportType = new Paragraph("PREMIUM FINANCIAL STATEMENT", subtitleFont);

		reportType.setSpacingAfter(2);

		headerText.addElement(reportType);

		String monthName = Month.of(month).getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);

		Paragraph period = new Paragraph(monthName.toUpperCase() + " " + year, subtitleFont);

		headerText.addElement(period);

		header.addCell(headerText);

		document.add(header);

		// =====================================================
		// REPORT INFORMATION
		// =====================================================

		PdfPTable reportInfo = new PdfPTable(2);

		reportInfo.setWidthPercentage(100);
		reportInfo.setSpacingAfter(15);

		addInfoRow(reportInfo, "Prepared For", user.getName(), boldFont, normalFont);

		addInfoRow(reportInfo, "Email", user.getEmail(), boldFont, normalFont);

		addInfoRow(reportInfo, "Statement Period", monthName + " " + year, boldFont, normalFont);

		addInfoRow(reportInfo, "Generated",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")), boldFont, normalFont);

		document.add(reportInfo);

		// =====================================================
		// CALCULATIONS
		// =====================================================

		double totalExpense = 0;

		double highestExpense = 0;

		double lowestExpense = expenses.isEmpty() ? 0 : Double.MAX_VALUE;

		Expense highestExpenseRecord = null;

		Expense lowestExpenseRecord = null;

		Map<String, Double> categoryTotals = new LinkedHashMap<>();

		for (Expense expense : expenses) {

			double price = expense.getPrice();

			totalExpense += price;

			if (price > highestExpense) {

				highestExpense = price;

				highestExpenseRecord = expense;
			}

			if (price < lowestExpense) {

				lowestExpense = price;

				lowestExpenseRecord = expense;
			}

			String categoryName = expense.getCategory().getName();

			categoryTotals.put(categoryName, categoryTotals.getOrDefault(categoryName, 0.0) + price);
		}

		double averageExpense = expenses.isEmpty() ? 0 : totalExpense / expenses.size();

		double budgetAmount = budget != null ? budget.getAmount() : 0;

		double remainingBudget = budgetAmount - totalExpense;

		double budgetUsage = budgetAmount > 0 ? (totalExpense / budgetAmount) * 100 : 0;

		// =====================================================
		// FINANCIAL SUMMARY
		// =====================================================

		addSectionTitle(document, "FINANCIAL SUMMARY", sectionFont);

		PdfPTable summaryTable = new PdfPTable(4);

		summaryTable.setWidthPercentage(100);

		summaryTable.setWidths(new float[] { 1, 1, 1, 1 });

		summaryTable.setSpacingAfter(18);

		addSummaryCard(summaryTable, "MONTHLY BUDGET", formatAmount(budgetAmount), BLUE);

		addSummaryCard(summaryTable, "TOTAL SPENT", formatAmount(totalExpense), NAVY);

		addSummaryCard(summaryTable, "REMAINING", formatAmount(remainingBudget), remainingBudget >= 0 ? GREEN : RED);

		addSummaryCard(summaryTable, "BUDGET USED", String.format("%.1f%%", budgetUsage), GOLD);

		document.add(summaryTable);

		// =====================================================
		// EXPENSE OVERVIEW
		// =====================================================

		addSectionTitle(document, "EXPENSE OVERVIEW", sectionFont);

		PdfPTable overviewTable = new PdfPTable(4);

		overviewTable.setWidthPercentage(100);

		overviewTable.setSpacingAfter(18);

		addOverviewCell(overviewTable, "TRANSACTIONS", String.valueOf(expenses.size()), boldFont, smallFont);

		addOverviewCell(overviewTable, "AVERAGE", formatAmount(averageExpense), boldFont, smallFont);

		addOverviewCell(overviewTable, "HIGHEST", highestExpenseRecord == null ? "N/A" : formatAmount(highestExpense),
				boldFont, smallFont);

		addOverviewCell(overviewTable, "LOWEST", lowestExpenseRecord == null ? "N/A" : formatAmount(lowestExpense),
				boldFont, smallFont);

		document.add(overviewTable);

		// =====================================================
		// CATEGORY BREAKDOWN
		// =====================================================

		addSectionTitle(document, "CATEGORY BREAKDOWN", sectionFont);

		PdfPTable categoryTable = new PdfPTable(3);

		categoryTable.setWidthPercentage(100);

		categoryTable.setWidths(new float[] { 2.2f, 1f, 3f });

		categoryTable.setSpacingAfter(18);

		addTableHeader(categoryTable, "CATEGORY", whiteBoldFont);

		addTableHeader(categoryTable, "AMOUNT", whiteBoldFont);

		addTableHeader(categoryTable, "SHARE", whiteBoldFont);

		String topCategory = "N/A";

		double topCategoryAmount = 0;

		for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

			String category = entry.getKey();

			double amount = entry.getValue();

			double percentage = totalExpense > 0 ? (amount / totalExpense) * 100 : 0;

			categoryTable.addCell(createCell(category, normalFont, WHITE));

			categoryTable.addCell(createCell(formatAmount(amount), boldFont, WHITE));

			PdfPCell barCell = new PdfPCell();

			barCell.setBorder(Rectangle.NO_BORDER);

			barCell.setPadding(5);

			PdfPTable barTable = new PdfPTable(1);

			barTable.setWidthPercentage(100);

			PdfPCell bar = new PdfPCell(new Phrase(String.format("%.1f%%", percentage), smallFont));

			bar.setBackgroundColor(LIGHT_BLUE);

			bar.setBorder(Rectangle.NO_BORDER);

			bar.setPadding(5);

			barTable.addCell(bar);

			barCell.addElement(barTable);

			categoryTable.addCell(barCell);

			if (amount > topCategoryAmount) {

				topCategoryAmount = amount;

				topCategory = category;
			}
		}

		if (categoryTotals.isEmpty()) {

			PdfPCell empty = new PdfPCell(new Phrase("No category expenses found.", smallFont));

			empty.setColspan(3);

			empty.setHorizontalAlignment(Element.ALIGN_CENTER);

			empty.setPadding(10);

			empty.setBorder(Rectangle.NO_BORDER);

			categoryTable.addCell(empty);
		}

		document.add(categoryTable);

		// =====================================================
		// TOP CATEGORY
		// =====================================================

		PdfPTable topCategoryTable = new PdfPTable(1);

		topCategoryTable.setWidthPercentage(100);

		topCategoryTable.setSpacingAfter(18);

		PdfPCell topCategoryCell = new PdfPCell(new Phrase("Top Spending Category    " + topCategory, boldFont));

		topCategoryCell.setBackgroundColor(LIGHT_BLUE);

		topCategoryCell.setBorder(Rectangle.NO_BORDER);

		topCategoryCell.setPadding(10);

		topCategoryTable.addCell(topCategoryCell);

		document.add(topCategoryTable);

		// =====================================================
		// EXPENSE DETAILS
		// =====================================================

		addSectionTitle(document, "TRANSACTION DETAILS", sectionFont);

		PdfPTable expenseTable = new PdfPTable(5);

		expenseTable.setWidthPercentage(100);

		expenseTable.setWidths(new float[] { 1.3f, 2.5f, 1.8f, 1.2f, 1.4f });

		addTableHeader(expenseTable, "DATE", whiteBoldFont);

		addTableHeader(expenseTable, "DESCRIPTION", whiteBoldFont);

		addTableHeader(expenseTable, "CATEGORY", whiteBoldFont);

		addTableHeader(expenseTable, "TIME", whiteBoldFont);

		addTableHeader(expenseTable, "AMOUNT", whiteBoldFont);

		int rowNumber = 0;

		for (Expense expense : expenses) {

			Color rowColor = rowNumber % 2 == 0 ? WHITE : LIGHT_GREY;

			expenseTable.addCell(createCell(expense.getDate().toString(), normalFont, rowColor));

			expenseTable.addCell(createCell(expense.getTitle(), normalFont, rowColor));

			expenseTable.addCell(createCell(expense.getCategory().getName(), normalFont, rowColor));

			expenseTable.addCell(createCell(expense.getTime().toString(), smallFont, rowColor));

			expenseTable.addCell(createCell(formatAmount(expense.getPrice()), boldFont, rowColor));

			rowNumber++;
		}

		// -----------------------------------------------------
		// TOTAL
		// -----------------------------------------------------

		PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL EXPENSE", whiteBoldFont));

		totalLabel.setColspan(4);

		totalLabel.setBackgroundColor(NAVY);

		totalLabel.setPadding(8);

		totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);

		totalLabel.setBorder(Rectangle.NO_BORDER);

		expenseTable.addCell(totalLabel);

		PdfPCell totalAmount = new PdfPCell(new Phrase(formatAmount(totalExpense), whiteBoldFont));

		totalAmount.setBackgroundColor(NAVY);

		totalAmount.setPadding(8);

		totalAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);

		totalAmount.setBorder(Rectangle.NO_BORDER);

		expenseTable.addCell(totalAmount);

		if (expenses.isEmpty()) {

			PdfPCell empty = new PdfPCell(new Phrase("No expenses recorded for this month.", smallFont));

			empty.setColspan(5);

			empty.setHorizontalAlignment(Element.ALIGN_CENTER);

			empty.setPadding(15);

			expenseTable.addCell(empty);
		}

		document.add(expenseTable);

		// =====================================================
		// FINANCIAL NOTE
		// =====================================================

		document.add(new Paragraph(" "));

		String budgetMessage;

		if (budgetAmount == 0) {

			budgetMessage = "No monthly budget has been configured " + "for this period.";

		} else if (remainingBudget >= 0) {

			budgetMessage = "You are within your monthly budget with " + formatAmount(remainingBudget) + " remaining.";

		} else {

			budgetMessage = "You have exceeded your monthly budget by " + formatAmount(Math.abs(remainingBudget)) + ".";
		}

		PdfPTable noteTable = new PdfPTable(1);

		noteTable.setWidthPercentage(100);

		PdfPCell noteCell = new PdfPCell(new Phrase(budgetMessage, normalFont));

		noteCell.setBackgroundColor(remainingBudget >= 0 ? new Color(240, 253, 244) : new Color(254, 242, 242));

		noteCell.setBorder(Rectangle.NO_BORDER);

		noteCell.setPadding(10);

		noteTable.addCell(noteCell);

		document.add(noteTable);

		// =====================================================
		// FOOTER
		// =====================================================

		document.add(new Paragraph(" "));

		Paragraph footer = new Paragraph("PERSONAL EXPENSE TRACKER  •  " + "Confidential Financial Statement",
				new Font(Font.HELVETICA, 8, Font.NORMAL, GREY));

		footer.setAlignment(Element.ALIGN_CENTER);

		document.add(footer);
	}

	// =========================================================
	// SECTION TITLE
	// =========================================================

	private static void addSectionTitle(Document document, String title, Font font) throws Exception {

		Paragraph section = new Paragraph(title, font);

		section.setSpacingBefore(4);

		section.setSpacingAfter(8);

		document.add(section);
	}

	// =========================================================
	// SUMMARY CARD
	// =========================================================

	private static void addSummaryCard(PdfPTable table, String label, String value, Color color) {

		PdfPCell cell = new PdfPCell();

		cell.setBackgroundColor(color);

		cell.setBorder(Rectangle.NO_BORDER);

		cell.setPadding(10);

		cell.setMinimumHeight(58);

		Paragraph labelParagraph = new Paragraph(label, new Font(Font.HELVETICA, 7, Font.BOLD, WHITE));

		labelParagraph.setSpacingAfter(5);

		cell.addElement(labelParagraph);

		Paragraph valueParagraph = new Paragraph(value, new Font(Font.HELVETICA, 13, Font.BOLD, WHITE));

		cell.addElement(valueParagraph);

		table.addCell(cell);
	}

	// =========================================================
	// OVERVIEW CELL
	// =========================================================

	private static void addOverviewCell(PdfPTable table, String label, String value, Font valueFont, Font labelFont) {

		PdfPCell cell = new PdfPCell();

		cell.setBackgroundColor(LIGHT_GREY);

		cell.setBorder(Rectangle.NO_BORDER);

		cell.setPadding(10);

		Paragraph valueParagraph = new Paragraph(value, valueFont);

		valueParagraph.setAlignment(Element.ALIGN_CENTER);

		cell.addElement(valueParagraph);

		Paragraph labelParagraph = new Paragraph(label, labelFont);

		labelParagraph.setAlignment(Element.ALIGN_CENTER);

		cell.addElement(labelParagraph);

		table.addCell(cell);
	}

	// =========================================================
	// TABLE HEADER
	// =========================================================

	private static void addTableHeader(PdfPTable table, String text, Font font) {

		PdfPCell cell = new PdfPCell(new Phrase(text, font));

		cell.setBackgroundColor(NAVY);

		cell.setPadding(7);

		cell.setBorder(Rectangle.NO_BORDER);

		table.addCell(cell);
	}

	// =========================================================
	// NORMAL CELL
	// =========================================================

	private static PdfPCell createCell(String text, Font font, Color background) {

		PdfPCell cell = new PdfPCell(new Phrase(text, font));

		cell.setBackgroundColor(background);

		cell.setPadding(6);

		cell.setBorder(Rectangle.BOTTOM);

		cell.setBorderColor(new Color(229, 231, 235));

		return cell;
	}

	// =========================================================
	// INFORMATION ROW
	// =========================================================

	private static void addInfoRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {

		PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));

		labelCell.setBackgroundColor(LIGHT_GREY);

		labelCell.setBorder(Rectangle.NO_BORDER);

		labelCell.setPadding(7);

		PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));

		valueCell.setBackgroundColor(WHITE);

		valueCell.setBorder(Rectangle.NO_BORDER);

		valueCell.setPadding(7);

		table.addCell(labelCell);

		table.addCell(valueCell);
	}

	// =========================================================
	// MONEY FORMAT
	// =========================================================

	private static String formatAmount(double amount) {

		return String.format("Rs. %,.2f", amount);
	}
}