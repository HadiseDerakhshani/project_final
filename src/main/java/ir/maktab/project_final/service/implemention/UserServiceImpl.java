package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.UserCategoryDto;
import ir.maktab.project_final.data.dto.UserDto;
import ir.maktab.project_final.data.dto.mapper.UserMap;
import ir.maktab.project_final.data.entity.enums.UserRole;
import ir.maktab.project_final.data.entity.user.User;
import ir.maktab.project_final.data.repasitory.UserRepository;
import ir.maktab.project_final.data.repasitory.UserSpecifications;
import ir.maktab.project_final.exception.ObjectEntityNotFoundException;
import ir.maktab.project_final.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMap userMap;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, @Lazy UserMap userMap) {
        this.userRepository = userRepository;
        this.userMap = userMap;
    }


    @Override
    public UserRole findByEmail(String email, String pass) {
        if (!userRepository.findByEmail(email).isPresent())
            throw new ObjectEntityNotFoundException("--- user is not exit ---");
        else {
            User user = userRepository.findByEmail(email).get();
            if (checkPassword(user, pass)) ;
            return user.getUserRole();
        }
    }

    @Override
    public boolean checkPassword(User user, String pass) {
        if (user.getPassword().equals(pass))
            return true;

        return false;
    }

    @Override
    public List<UserDto> filtering(UserCategoryDto categoryDto) {
        Pageable pageable = PageRequest.of(categoryDto.getPageNumber(), categoryDto.getPageSize());
        Specification<User> specification = UserSpecifications.filterByCriteria(categoryDto);
        return userRepository
                .findAll(specification, pageable)
                .stream()
                .map(userMap::createUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findAll() {
        List<User> list = userRepository.findAll();
        if (list != null)
            return list.stream().map(userMap::createUserDto).collect(Collectors.toList());

        throw new ObjectEntityNotFoundException(" --- list of expert is null ---");
    }


}