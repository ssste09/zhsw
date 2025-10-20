package com.zhsw.auth.mapper;

import com.zhsw.auth.entity.Address;
import com.zhsw.auth.entity.User;
import com.zhsw.auth.utils.Gender;
import lombok.Data;
import org.openapitools.model.SignUpUserRequest;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserMapper {
    public User mapSignUpRequestToUser(SignUpUserRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhone())
                .gender(mapToEntityGenderEnum(request.getGender()))
                .password(request.getPassword())
                .birthDate(request.getBirthDate())
                .build();
        Address address = mapToAddressEntity(request.getAddress(), user);
        user.setAddress(address);

        return user;
    }

    public Address mapToAddressEntity(org.openapitools.model.Address addressesRequest, User user) {

        return new Address(
                addressesRequest.getId(),
                addressesRequest.getStreet(),
                addressesRequest.getStreetNumber(),
                addressesRequest.getPostalCode(),
                addressesRequest.getCity(),
                addressesRequest.getCountry(),
                user);
    }

    public org.openapitools.model.Address mapToAddressResponse(Address address) {
        return new org.openapitools.model.Address()
                .id(address.getAddressId())
                .city(address.getCity())
                .country(address.getCountry())
                .street(address.getAddress())
                .postalCode(address.getPostalCode())
                .streetNumber(address.getStreetNumber());
    }

    public Gender mapToEntityGenderEnum(SignUpUserRequest.GenderEnum genderReq) {
        return genderReq.equals(SignUpUserRequest.GenderEnum.FEMALE)
                ? Gender.FEMALE
                : genderReq.equals(SignUpUserRequest.GenderEnum.MALE) ? Gender.MALE : Gender.OTHER;
    }

    public org.openapitools.model.User.GenderEnum mapToResponseGenderEnum(Gender gender) {
        return gender.equals(Gender.FEMALE)
                ? org.openapitools.model.User.GenderEnum.FEMALE
                : gender.equals(Gender.MALE)
                        ? org.openapitools.model.User.GenderEnum.MALE
                        : org.openapitools.model.User.GenderEnum.OTHER;
    }

    public org.openapitools.model.User mapToUserResponse(User user) {
        return new org.openapitools.model.User()
                .id(user.getUserId())
                .email(user.getEmail())
                .gender(mapToResponseGenderEnum(user.getGender()))
                .name(user.getName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .phone(user.getPhoneNumber())
                .address(mapToAddressResponse(user.getAddress()));
    }
}
