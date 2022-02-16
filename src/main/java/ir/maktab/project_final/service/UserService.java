package ir.maktab.project_final.service;

import ir.maktab.project_final.data.dto.UserCategoryDto;
import ir.maktab.project_final.data.dto.UserDto;
import ir.maktab.project_final.data.entity.enums.UserRole;
import ir.maktab.project_final.data.entity.user.User;

import java.util.List;


public interface UserService {


    UserRole findByEmail(String email, String pass);

    boolean checkPassword(User user, String pass);

    List<UserDto> filtering(UserCategoryDto categoryDto);

    List<UserDto> findAll();

}
