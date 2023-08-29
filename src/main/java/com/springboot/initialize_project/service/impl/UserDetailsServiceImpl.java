package com.springboot.initialize_project.service.impl;

import com.springboot.initialize_project.data.repository.UserRepository;
import com.springboot.initialize_project.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByAccount(String account) throws UsernameNotFoundException {

        LOGGER.info("[loadUserByUsername] user 객체 조회를 진행합니다. account : {}", account);

        UserDetails userDetails = userRepository.getByAccount(account);

        LOGGER.info("[loadUserByUsername] user 객체 조회가 완료되었습니다. account : {}", account);

        return userDetails;
    }
}
