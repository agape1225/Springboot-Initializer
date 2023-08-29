package com.springboot.initialize_project.service.impl;

import com.springboot.initialize_project.data.dto.UserResponseDto;
import com.springboot.initialize_project.data.dto.VerifyResponseDto;
import com.springboot.initialize_project.data.entity.User;
import com.springboot.initialize_project.data.repository.UserRepository;
import com.springboot.initialize_project.service.MaskingService;
import com.springboot.initialize_project.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final MaskingService maskingService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MaskingService maskingService){
        this.userRepository = userRepository;
        this.maskingService = maskingService;
    }

    @Override
    public UserResponseDto getUserByAccount(String account) {

        LOGGER.info("[getUserByAccount] user 정보 조회를 진행합니다. account : {}", account);

        ModelMapper mapper = new ModelMapper();
        User user = userRepository.getByAccount(account);
        UserResponseDto responseUser = mapper.map(user, UserResponseDto.class);

        LOGGER.info("[getUserByAccount] user 정보 조회가 완료되었습니다. account : {}", account);

        return responseUser;
    }

    @Override
    public void deleteUser(String account) {

        LOGGER.info("[deleteUser] user 정보 삭제를 진행합니다. account : {}", account);

        ModelMapper mapper = new ModelMapper();
        User user = userRepository.getByAccount(account);

        String maskedPhone = maskingService.phoneMasking(user.getPhone());
        String maskedCrn = maskingService.crnMasking(user.getCrn());

        User deletedUser = mapper.map(user, User.class);
        deletedUser.setPhone(maskedPhone);
        deletedUser.setCrn(maskedCrn);
        deletedUser.setPassword("****");
        deletedUser.setStatus(false);

        LOGGER.info("[deleteUser] user 정보 삭제가 완료되었습니다. account : {}", account);

        userRepository.save(deletedUser);
    }

    @Override
    public VerifyResponseDto checkAccountDuplicate(String account) {

        LOGGER.info("[checkAccountDuplicate] account 중복 정보 조회를 진행합니다. account : {}", account);

        VerifyResponseDto verifyResponseDto = new VerifyResponseDto();
        verifyResponseDto.setVerify(false);

        Optional<User> user = userRepository.findByAccount(account);

        if(user.isPresent())
            verifyResponseDto.setVerify(true);


        LOGGER.info("[checkAccountDuplicate] account 중복 정보 조회가 완료되었습니다. account : {}", account);

        return verifyResponseDto;
    }

    @Override
    public VerifyResponseDto checkNicknameDuplicate(String nickname) {

        LOGGER.info("[checkNicknameDuplicate] nickname 중복 정보 조회를 진행합니다. nickname : {}", nickname);

        VerifyResponseDto verifyResponseDto = new VerifyResponseDto();
        verifyResponseDto.setVerify(false);

        Optional<User> user = userRepository.findByNickname(nickname);

        if(user.isPresent())
            verifyResponseDto.setVerify(true);

        LOGGER.info("[checkNicknameDuplicate] nickname 중복 정보 조회를 진행합니다. nickname : {}", nickname);

        return verifyResponseDto;
    }
}
