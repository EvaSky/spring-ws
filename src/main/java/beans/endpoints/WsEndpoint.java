package beans.endpoints;

import beans.models.Event;
import beans.models.User;
import beans.services.EventService;
import beans.services.UserService;
import beans.util.mappers.EventMapper;
import beans.util.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import spring_advance.wsmodel.*;

import java.util.List;
import java.util.stream.Collectors;


@Endpoint
public class WsEndpoint {
    private static final String NAMESPACE_URI = "http://spring-advance/wsmodel";

    private UserService userService;
    private EventService eventService;
    private UserMapper userMapper;
    private EventMapper eventMapper;

    @Autowired
    public WsEndpoint(UserService userServiceImpl,
                      EventService eventServiceImpl,
                      UserMapper userMapper,
                      EventMapper eventMapper) {
        this.userService = userServiceImpl;
        this.eventService = eventServiceImpl;
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByIdRequest")
    @ResponsePayload
    public GetUserByIdResponse getUserById(@RequestPayload GetUserByIdRequest request) {
        GetUserByIdResponse response = new GetUserByIdResponse();
        WsUser wsUser = userMapper.buildWsUser(userService.getById(request.getId()));
        response.setWsUser(wsUser);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByEmailRequest")
    @ResponsePayload
    public GetUserByEmailResponse getUserByEmail(@RequestPayload GetUserByEmailRequest request) {
        GetUserByEmailResponse response = new GetUserByEmailResponse();
        WsUser wsUser = userMapper.buildWsUser(userService.getUserByEmail(request.getEmail()));
        response.setWsUser(wsUser);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsersByNameRequest")
    @ResponsePayload
    public GetUsersByNameResponse getUsersByName(@RequestPayload GetUsersByNameRequest request) {
        GetUsersByNameResponse response = new GetUsersByNameResponse();
        List<User> users = userService.getUsersByName(request.getName());
        List<WsUser> wsUsers = users.stream().map(u -> userMapper.buildWsUser(u)).collect(Collectors.toList());
        response.getWsUserList().addAll(wsUsers);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveNewUserRequest")
    @ResponsePayload
    public SaveNewUserResponse saveNewUser(@RequestPayload SaveNewUserRequest request) {
        SaveNewUserResponse response = new SaveNewUserResponse();
        WsUser wsUser = new WsUser();
        wsUser.setEmail(request.getEmail());
        wsUser.setName(request.getName());
        wsUser.setBirthday(request.getBirthday());
        WsUser savedUser = userMapper.buildWsUser(userService.register(userMapper.buildUser(wsUser)));
        response.setWsUser(savedUser);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {
        DeleteUserResponse response = new DeleteUserResponse();
        int id = request.getId();
        User u = userService.getById(id);
        if(u == null) {
            response.setMessage("User not found");
            return response;
        }
        userService.remove(u);
        response.setMessage("User was deleted");
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllEventsRequest")
    @ResponsePayload
    public GetAllEventsResponse getAllEvents(@RequestPayload GetAllEventsRequest request) {
        GetAllEventsResponse response = new GetAllEventsResponse();
        List<Event> events = eventService.getAll();
        List<WsEvent> wsEvents = events.stream().map(e -> eventMapper.buildWsEvent(e)).collect(Collectors.toList());
        response.getWsEvent().addAll(wsEvents);
        return response;
    }

}
