package com.springboot.initialize_project.controller;

import com.springboot.initialize_project.config.security.JwtTokenProvider;
import com.springboot.initialize_project.data.dto.RequestToDoDto;
import com.springboot.initialize_project.data.dto.ResponseListToDoDto;
import com.springboot.initialize_project.data.dto.ToDoDto;
import com.springboot.initialize_project.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/todo")
public class ToDoController {
    private final Logger LOGGER = LoggerFactory.getLogger(ToDoController.class);
    private final ToDoService toDoService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ToDoController(ToDoService toDoService, JwtTokenProvider jwtTokenProvider){
        this.toDoService = toDoService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping()
    public void createToDo(@RequestBody RequestToDoDto requestToDoDto,
                           HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse){

        String account = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));

        LOGGER.info("[createToDo] todo 생성을 시도하고 있습니다. account : {}", account);

        toDoService.createToDo(requestToDoDto, servletRequest, servletResponse);

        LOGGER.info("[createToDo] todo 생성을 완료하였습니다. account : {}", account);
    }

    @DeleteMapping()
    public void deleteToDo(@RequestParam(value = "id", required = true) Long id,
                           HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse){

        String account = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));

        LOGGER.info("[deleteToDo] todo 삭제를 시도하고 있습니다. account : {}, todo id: {}", account, id);

        toDoService.deleteToDo(id, servletRequest, servletResponse);

        LOGGER.info("[deleteToDo] todo 삭제를 완료하였습니다. account : {}, , todo id: {}", account, id);
    }

    @GetMapping(value = "/{todoId}")
    public ResponseEntity<ToDoDto> getToDo(@PathVariable Long todoId,
                        HttpServletRequest servletRequest,
                        HttpServletResponse servletResponse){

        String account = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));

        LOGGER.info("[getToDo] todo 조회를 시도하고 있습니다. account : {}, todo id: {}", account, todoId);

        ToDoDto toDoDto = toDoService.getToDo(todoId, servletRequest, servletResponse);

        LOGGER.info("[getToDo] todo 조회를 완료하였습니다. account : {}, , todo id: {}", account, todoId);

        return ResponseEntity.status(HttpStatus.OK).body(toDoDto);
    }

    @PutMapping()
    public void updateToDo(@RequestBody ToDoDto toDoDto,
                           HttpServletRequest servletRequest,
                           HttpServletResponse servletResponse){

        String account = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));

        LOGGER.info("[updateToDo] todo 수정을 시도하고 있습니다. account : {}, todo id: {}", account, toDoDto.getId());

        toDoService.updateToDo(toDoDto, servletRequest, servletResponse);

        LOGGER.info("[updateToDo] todo 수정을 완료하였습니다. account : {}, , todo id: {}", account, toDoDto.getId());
    }

    @GetMapping()
    public ResponseEntity<ResponseListToDoDto> getToDoList(
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "size", required = true) int size,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse){

        String account = jwtTokenProvider.getAccount(jwtTokenProvider.resolveToken(servletRequest));

        LOGGER.info("[getToDoList] todo 리스트 조회를 시도하고 있습니다. account : {}, state : {}, page : {}, size : {}",
                account, state, page, size);

        ResponseListToDoDto toDoList = toDoService.getToDoList(state, page, size, servletRequest, servletResponse);

        LOGGER.info("[getToDoList] todo 리스트 조회를 완료하였습니다. account : {}, state : {}, page : {}, size : {}",
                account, state, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(toDoList);
    }
}
