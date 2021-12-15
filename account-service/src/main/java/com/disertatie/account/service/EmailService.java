package com.disertatie.account.service;

import com.disertatie.account.dto.ClientDTO;
import com.disertatie.account.model.Account;
import com.disertatie.account.model.Operation;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;


@Slf4j
@Service
@NoArgsConstructor
public class EmailService
{

	public static final String DEST = "results/logs/log.pdf";
	public static final String DATE_STRING = "Date: ";
	public static final String ACCOUNT_NAME_STRING = "Account name: ";
	public static final String CURRENT_BALLANCE_STRING = "Current balance: ";

	public void createPDF(Operation operation, ClientDTO client, Account acc) throws IOException
	{
		log.info("Creating PDF file...");
		File file = new File(DEST);
		file.getParentFile().mkdirs();

		LocalDate date = LocalDate.now();

		FileOutputStream fos = new FileOutputStream(DEST);
		PdfWriter writer = new PdfWriter(fos);

		PdfDocument pdf = new PdfDocument(writer);

		log.info("Setting up font...");
		PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

		Document document = new Document(pdf);
		Text text = new Text("ATM - Project");

		text.setFont(font);
		text.setFontColor(Color.RED);

		log.info("Generating paragraphs...");
		Paragraph paragraph1 = new Paragraph();
		paragraph1.add(text);
		document.add(paragraph1);

		Account account = operation.getAccount();

		document.add(new Paragraph("Transaction details"));

		Paragraph paragraph2 = new Paragraph();

		if (operation.getType().equalsIgnoreCase("deposit"))
		{
			paragraph2.add(DATE_STRING + date + "\n");
			paragraph2.add(ACCOUNT_NAME_STRING + account.getName() + "\n"); // add IBAN
			paragraph2.add("Amount deposited: " + operation.getAmount() + "\n");
			paragraph2.add(CURRENT_BALLANCE_STRING + account.getAmount() + "\n");
		}
		if (operation.getType().equalsIgnoreCase("withdraw"))
		{
			paragraph2.add(DATE_STRING + date + "\n");
			paragraph2.add(ACCOUNT_NAME_STRING + account.getName() + "\n"); // add IBAN
			paragraph2.add("Amount withdrawed: " + operation.getAmount() + "\n");
			paragraph2.add(CURRENT_BALLANCE_STRING + account.getAmount() + "\n");
		}
		if (operation.getType().equalsIgnoreCase("transfer"))
		{
			paragraph2.add(DATE_STRING + date + "\n");
			paragraph2.add("Receiver's account id: " + acc.getId() + "\n"); // add IBAN
			paragraph2.add("Amount transfered: " + operation.getAmount() + "\n");
			paragraph2.add(CURRENT_BALLANCE_STRING + account.getAmount() + "\n");
		}
		if (operation.getType().equalsIgnoreCase("payment"))
		{
			paragraph2.add(DATE_STRING + date + "\n");
			paragraph2.add(ACCOUNT_NAME_STRING + account.getName() + "\n"); //
			paragraph2.add("Paid amount: " + operation.getAmount() + "\n");
			paragraph2.add(CURRENT_BALLANCE_STRING + account.getAmount() + "\n");
		}

		document.add(paragraph2);
		document.close();
		log.info("Document is generated, starting sendMail(...) procedure...");
		SenderService.sendMail(DEST, client.getEmail(), client.getFirstName() + " " + client.getLastName());
	}
}
