package com.howwelike.service;

import com.howwelike.dto.UserLoginDTO;
import com.howwelike.entity.User;

public interface UserService {

    User wxLogin(UserLoginDTO userLoginDTO);

}
