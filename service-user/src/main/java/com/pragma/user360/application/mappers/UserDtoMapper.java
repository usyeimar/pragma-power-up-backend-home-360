package com.pragma.user360.application.mappers;

import com.pragma.user360.application.dto.request.RegisterUserRequest;
import com.pragma.user360.application.dto.response.UserResponse;
import com.pragma.user360.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserDtoMapper {

    UserModel requestToModel(RegisterUserRequest request);



    UserResponse modelToResponse(UserModel userModel);

    List<UserResponse> modelListToResponseList(List<UserModel> users);
}