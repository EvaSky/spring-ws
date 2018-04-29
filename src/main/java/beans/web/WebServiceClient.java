package beans.web;

import beans.models.Event;
import beans.models.User;
import beans.util.mappers.EventMapper;
import beans.util.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.client.core.WebServiceTemplate;
import spring_advance.wsmodel.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/web-service")
public class WebServiceClient{
    @Autowired
    WebServiceTemplate webServiceTemplate;

    @Autowired
    UserMapper userMapper;
    @Autowired
    EventMapper eventMapper;

    @GetMapping(value = "/user/{id}")
    public User invokeWsGetUserById(@PathVariable("id") Integer id) {
        GetUserByIdRequest request = new GetUserByIdRequest();
        request.setId(id);
        GetUserByIdResponse response = (GetUserByIdResponse) webServiceTemplate.marshalSendAndReceive("http://localhost:8090/ws", request);
        WsUser wsUser = response.getWsUser();
        printUser(wsUser);

        return userMapper.buildUser(response.getWsUser());
    }

    @GetMapping(value = "/user")
    public User invokeWsGetUserByEmail(@RequestParam("email") String email) {
        GetUserByEmailRequest request = new GetUserByEmailRequest();
        request.setEmail(email);
        GetUserByEmailResponse response = (GetUserByEmailResponse) webServiceTemplate.marshalSendAndReceive("http://localhost:8090/ws", request);
        WsUser wsUser = response.getWsUser();
        printUser(wsUser);

        return userMapper.buildUser(response.getWsUser());
    }

    @GetMapping(value = "/users")
    public List<User> invokeWsGetUsersByName(@RequestParam("name") String name) {
        GetUsersByNameRequest request = new GetUsersByNameRequest();
        request.setName(name);
        GetUsersByNameResponse response = (GetUsersByNameResponse) webServiceTemplate.marshalSendAndReceive("http://localhost:8090/ws", request);
        List<WsUser> wsUsers = response.getWsUserList();
        wsUsers.forEach(this::printUser);

        return wsUsers.stream().map(u -> userMapper.buildUser(u)).collect(Collectors.toList());
    }

    @PostMapping(value = "/user")
    public User invokeWsSaveUser(@RequestBody User user) {
        WsUser wsUser = userMapper.buildWsUser(user);
        SaveNewUserRequest request = new SaveNewUserRequest();
        request.setName(wsUser.getName());
        request.setEmail(wsUser.getEmail());
        request.setBirthday(wsUser.getBirthday());

        SaveNewUserResponse response = (SaveNewUserResponse) webServiceTemplate.marshalSendAndReceive("http://localhost:8090/ws", request);
        WsUser wsUserSaved = response.getWsUser();
        printUser(wsUserSaved);
        return userMapper.buildUser(wsUserSaved);
    }

    @GetMapping(value = "/user/delete/{id}")
    public String invokeWsDeleteUser(@PathVariable("id") Integer id) {
        DeleteUserRequest request = new DeleteUserRequest();
        request.setId(id);
        DeleteUserResponse response = (DeleteUserResponse) webServiceTemplate.marshalSendAndReceive("http://localhost:8090/ws", request);
        String message = response.getMessage();
        System.out.println(message);
        return message;
    }

    @GetMapping(value = "/events")
    public List<Event> invokeWsGetAllEvents() {
        GetAllEventsResponse response = (GetAllEventsResponse) webServiceTemplate.marshalSendAndReceive("http://localhost:8090/ws",
                new GetAllEventsRequest());
        List<WsEvent> wsEvents = response.getWsEvent();
        List<Event> events = wsEvents.stream().map(wsev -> eventMapper.buildEvent(wsev)).collect(Collectors.toList());
        return events;
    }

    private void printUser(WsUser user) {
        System.out.println("User{" +
                "id=" + user.getId() + ", email='" + user.getEmail() + '\'' +
                ", name='" + user.getName() + '\'' +
                ", birthday=" + user.getBirthday() +
                '}');
    }
}
