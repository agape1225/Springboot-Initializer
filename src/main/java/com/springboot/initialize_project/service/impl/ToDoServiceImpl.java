package com.springboot.initialize_project.service.impl;

import com.springboot.initialize_project.config.security.JwtTokenProvider;
import com.springboot.initialize_project.data.dto.RequestToDoDto;
import com.springboot.initialize_project.data.dto.ResponseListToDoDto;
import com.springboot.initialize_project.data.dto.ResponseToDoDto;
import com.springboot.initialize_project.data.dto.ToDoDto;
import com.springboot.initialize_project.data.entity.ToDo;
import com.springboot.initialize_project.data.entity.User;
import com.springboot.initialize_project.data.repository.ToDoRepository;
import com.springboot.initialize_project.data.repository.UserRepository;
import com.springboot.initialize_project.service.ToDoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ToDoRepository toDoRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    @Autowired
    public ToDoServiceImpl(JwtTokenProvider jwtTokenProvider,
                           UserRepository userRepository,
                           ToDoRepository toDoRepository){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.toDoRepository = toDoRepository;
    }

    @Override
    public void createToDo(RequestToDoDto requestToDoDto,
                           HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse) {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getAccount(token);

        LOGGER.info("[createToDo] to do 생성을 진행합니다. account : {}", account);

        User user = userRepository.getByAccount(account);

        ToDo toDo = new ToDo();
        toDo.setUser(user);
        toDo.setName(requestToDoDto.getName());
        toDo.setState(false);
        toDo.setDescription(requestToDoDto.getDescription());

        toDoRepository.save(toDo);

        LOGGER.info("[createToDo] to do 생성이 완료되었습니다. account : {}", account);

    }

    @Override
    public void deleteToDo(Long id, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getAccount(token);

        LOGGER.info("[deleteToDo] to do 삭재를 진행합니다. account : {}", account);

        if(jwtTokenProvider.validateToken(token)){

            User user = userRepository.getByAccount(account);
            ToDo toDo = toDoRepository.getById(id);

            if(user.getId().equals(toDo.getUser().getId()))
                toDoRepository.delete(toDo);

            LOGGER.info("[deleteToDo] to do 삭제가 완료되었습니다. account : {}", account);
        }



    }

    @Override
    public ToDoDto getToDo(Long id, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        String token = jwtTokenProvider.resolveToken(servletRequest);
        String account = jwtTokenProvider.getAccount(token);

        LOGGER.info("[getToDo] to do 조회를 진행합니다. account : {}", account);

        ModelMapper mapper = new ModelMapper();
        ToDoDto toDoDto = new ToDoDto();


        if(jwtTokenProvider.validateToken(token)){
            User user = userRepository.getByAccount(account);

            ToDo toDo = toDoRepository.getById(id);

            if(user.getId().equals(toDo.getUser().getId())){
                toDoDto = mapper.map(toDo, ToDoDto.class);
            }
        }

        LOGGER.info("[getToDo] to do 조회 완료되었습니다. account : {}", account);

        return toDoDto;
    }

    @Override
    public void updateToDo(ToDoDto toDoDto, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

        ModelMapper mapper = new ModelMapper();
        String token = jwtTokenProvider.resolveToken(servletRequest);

        if(jwtTokenProvider.validateToken(token) && toDoRepository.existsById(toDoDto.getId())) {

            String account = jwtTokenProvider.getAccount(token);

            LOGGER.info("[updateToDo] to do 수정을 진행합니다. account : {}", account);

            User user = userRepository.getByAccount(account);
            ToDo toDo = mapper.map(toDoDto, ToDo.class);
            toDo.setUser(user);

            toDoRepository.save(toDo);

            LOGGER.info("[updateToDo] to do 수정이 완료되었습니다. account : {}", account);
        }
    }

    @Override
    public ResponseListToDoDto getToDoList(
            String state, int page, int size,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {

        ModelMapper mapper = new ModelMapper();
        List<ResponseToDoDto> responseToDoDtoList = new ArrayList<>();
        ResponseListToDoDto responseListToDoDto = new ResponseListToDoDto();

        String token = jwtTokenProvider.resolveToken(servletRequest);

        if(jwtTokenProvider.validateToken(token)){
            String account = jwtTokenProvider.getAccount(token);
            User user = userRepository.getByAccount(jwtTokenProvider.getAccount(token));

            LOGGER.info("[getToDoList] to do 조회를 진행합니다. account : {}", account);

            Page<ToDo> toDoList = null;

            if(state.equals("ALL"))
                toDoList = toDoRepository.findAllByUser(user, PageRequest.of(page, size));

            else if(state.equals("COMPLETE"))
                toDoList = toDoRepository.findAllByUserAndState(user, true, PageRequest.of(page, size));

            else if(state.equals("INCOMPLETE"))
                toDoList = toDoRepository.findAllByUserAndState(user, false, PageRequest.of(page, size));

            for(ToDo toDo : toDoList.getContent()){
                ResponseToDoDto responseToDoDto = mapper.map(toDo, ResponseToDoDto.class);
                responseToDoDtoList.add(responseToDoDto);
            }
            responseListToDoDto.setItems(responseToDoDtoList);
            LOGGER.info("[getToDoList] to do 조회가 완료되었습니다. account : {}", account);
        }
        return responseListToDoDto;
    }
}
