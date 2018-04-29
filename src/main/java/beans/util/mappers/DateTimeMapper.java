package beans.util.mappers;

import org.mapstruct.Mapper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

@Mapper(componentModel = "spring")
public class DateTimeMapper {

    LocalDate convertToLocalDate(XMLGregorianCalendar xcal) {
        return xcal.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }

    XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDate date) throws DatatypeConfigurationException {
        GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
    }

    LocalDateTime convertToLocalDateTime(XMLGregorianCalendar xcal) {
        return xcal.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime dateTime) throws DatatypeConfigurationException {
        GregorianCalendar gcal = GregorianCalendar.from(dateTime.atZone(ZoneId.systemDefault()));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
    }

}
