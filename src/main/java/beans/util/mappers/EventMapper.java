package beans.util.mappers;

import beans.models.Event;
import org.mapstruct.Mapper;
import spring_advance.wsmodel.WsEvent;

@Mapper(componentModel = "spring", uses={DateTimeMapper.class, AuditoriumMapper.class})
public interface EventMapper {

    WsEvent buildWsEvent(Event event);

    Event buildEvent(WsEvent wsEvent);
}
