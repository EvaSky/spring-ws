package beans.util.mappers;

import beans.models.Auditorium;
import org.mapstruct.Mapper;
import spring_advance.wsmodel.WsAuditorium;

@Mapper(componentModel = "spring")
public interface AuditoriumMapper {

    WsAuditorium buildWsAuditorium(Auditorium auditorium);

    Auditorium buildAuditorium(WsAuditorium wsAuditorium);
}
