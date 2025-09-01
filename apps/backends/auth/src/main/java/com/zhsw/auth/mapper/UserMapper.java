package com.zhsw.auth.mapper;

import com.zhsw.auth.entity.Address;
import com.zhsw.auth.entity.User;
import com.zhsw.auth.utils.Gender;
import lombok.Data;
import org.openapitools.model.SignUpUserRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class UserMapper {
    public User mapToSignUpRequestToUser(SignUpUserRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhone())
                .gender(mapToEntityGenderEnum(request.getGender()))
                .password(request.getPassword())
                .birthDate(request.getBirthDate())
                .build();
        List<Address> addresses = mapToAddressEntity(request.getAddresses(), user);
        user.setAddresses(addresses);

        return user;
    }

    public List<Address> mapToAddressEntity(List<org.openapitools.model.Address> addressesRequest, User user) {

        return addressesRequest.stream()
                .map(reqAddress -> Address.builder()
                        .address(reqAddress.getStreet())
                        .city(reqAddress.getCity())
                        .postalCode(reqAddress.getPostalCode())
                        .user(user)
                        .country(reqAddress.getCountry())
                        .build())
                .toList();
    }

    public List<org.openapitools.model.Address> mapToAddressResponse(List<Address> addresses) {
        return addresses.stream()
                .map(address -> new org.openapitools.model.Address()
                        .id(address.getAddressId())
                        .city(address.getCity())
                        .country(address.getCountry())
                        .street(address.getAddress())
                        .postalCode(address.getPostalCode()))
                .toList();
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
                .addresses(mapToAddressResponse(user.getAddresses()));
    }
}
