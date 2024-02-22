package ru.erma.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.erma.dto.SecurityDTO;
import ru.erma.model.Role;
import ru.erma.model.UserEntity;

/**
 * This interface is used to map between SecurityDTO objects and UserEntity entities.
 * It uses the MapStruct framework to generate the implementation code.
 * The @Mapper annotation specifies that the interface is a MapStruct mapper and the component model is "spring".
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * This method converts a SecurityDTO object to a UserEntity entity.
     * It uses the "hashPassword" method to hash the password and sets the default role to "USER".
     *
     * @param securityDTO the SecurityDTO object
     * @param passwordEncoder the PasswordEncoder used to hash the password
     * @return the UserEntity entity
     */
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role" , expression = "java(defaultRole().name())")
    UserEntity toUserEntity(SecurityDTO securityDTO, @Context PasswordEncoder passwordEncoder);

    /**
     * This method hashes a password using the PasswordEncoder.
     * It is named "hashPassword" so it can be referenced in the @Mapping annotation in the toUserEntity method.
     *
     * @param password the password to be hashed
     * @param passwordEncoder the PasswordEncoder used to hash the password
     * @return the hashed password
     */
    @Named("hashPassword")
    default String hashPassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

    /**
     * This method returns the default role for a user, which is "USER".
     * @return the default role
     */
    default Role defaultRole() {
        return Role.USER;
    }
}
