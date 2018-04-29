package beans.util.converters;

import beans.models.Ticket;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class TicketMessageConverter extends AbstractHttpMessageConverter<Ticket> {

    public TicketMessageConverter() {
        super(new MediaType("application", "pdf"));
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return true;
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return Ticket.class.isAssignableFrom(aClass);
    }

    @Override
    protected Ticket readInternal(Class<? extends Ticket> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Ticket ticket, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            build(Arrays.asList(ticket));
            byte[] array = Files.readAllBytes(new File("report.pdf").toPath());
            httpOutputMessage.getBody().write(array, 0, array.length);
            httpOutputMessage.getBody().flush();
        } catch (DocumentException e) {

        }
    }

    private Document build(List<Ticket> tickets) throws DocumentException, FileNotFoundException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
        document.open();

        document.add(new Paragraph("Dear User, Following is the list of available tickets"));

        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(10);

        // define font for table header row
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        cell.setPhrase(new Phrase("Event Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("User Email", font));
        table.addCell(cell);

        for (Ticket ticket : tickets) {
            table.addCell(ticket.getEvent().getName());
            table.addCell(ticket.getUser().getEmail());
        }

        document.add(table);

        document.add(new Paragraph(
                "The end of documents with tickets information."));
        document.close();

        return document;
    }
}

