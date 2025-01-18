package com.tinomaster.virtualdream.virtualdream.mappers;

import com.tinomaster.virtualdream.virtualdream.entities.User;
import lombok.Getter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class UserMapper {

    private final Converter<List<User>, List<Long>> userListToIdListConverter = context -> context.getSource() == null ? null
            : context.getSource().stream().map(User::getId).toList();

    private final Converter<User, Long> userToIdConverter = context -> context.getSource() == null ? null : context.getSource().getId();

    public void addMapping(ModelMapper modelMapper) {
        modelMapper.addConverter(userListToIdListConverter);
        modelMapper.addConverter(userToIdConverter);
    }

}
