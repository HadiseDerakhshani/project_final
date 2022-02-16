package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.UserDto;
import ir.maktab.project_final.data.entity.user.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component

public class UserMap {


    public User createUser(UserDto userDto) {
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .userStatus(userDto.getUserStatus())
                .userRole(userDto.getUserRole())
                .credit(userDto.getCredit())
                .dateUpdate(userDto.getDateUpdate())
                .dateRegister(userDto.getDateRegister()).build();
        return user;
    }


    public UserDto createUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .userStatus(user.getUserStatus())
                .userRole(user.getUserRole())
                .credit(user.getCredit())
                .dateUpdate(user.getDateUpdate())
                .dateRegister(user.getDateRegister()).build();
        return userDto;
    }
}
