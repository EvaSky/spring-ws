package beans.util.mappers;

import beans.models.User;
import org.mapstruct.Mapper;
import spring_advance.wsmodel.WsUser;

@Mapper(componentModel = "spring", uses=DateTimeMapper.class)
public interface UserMapper {

    User buildUser(WsUser wsUser);

    WsUser buildWsUser(User user);
}
