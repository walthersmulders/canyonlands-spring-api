package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.users.book.UsersBook;
import com.walthersmulders.persistance.entity.UsersBookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersBookMapper {

    UsersBook entityToUsersBook(UsersBookEntity entity);
}
