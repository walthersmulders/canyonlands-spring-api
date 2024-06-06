package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.user.User;
import com.walthersmulders.mapstruct.dto.users.album.UsersAlbum;
import com.walthersmulders.mapstruct.dto.users.book.UsersBook;
import com.walthersmulders.mapstruct.dto.users.movie.UsersMovie;
import com.walthersmulders.mapstruct.dto.users.series.UsersSeries;
import com.walthersmulders.persistence.entity.book.BookEntity;
import com.walthersmulders.persistence.entity.movie.MovieEntity;
import com.walthersmulders.persistence.entity.music.AlbumEntity;
import com.walthersmulders.persistence.entity.series.SeriesEntity;
import com.walthersmulders.persistence.entity.user.UserEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserEntity userToEntity(User user);

    User entityToUser(UserEntity entity);

    @InheritConfiguration
    UserEntity userEntityUpdateMerge(
            @MappingTarget UserEntity entity,
            String emailAddress,
            String firstName,
            String lastName
    );

    UsersBook entityToUsersBook(BookEntity book, Integer rating, String review);

    UsersBook entityToUsersBook(BookEntity book, Integer rating);

    UsersMovie entityToUsersMovie(MovieEntity movie, Integer rating, String review);

    UsersMovie entityToUsersMovie(MovieEntity movie, Integer rating);

    UsersSeries entityToUsersSeries(SeriesEntity series, Integer rating, String review);

    UsersSeries entityToUsersSeries(SeriesEntity series, Integer rating);

    UsersAlbum entityToUsersAlbum(AlbumEntity album, Integer rating);

    UsersAlbum entityToUsersAlbum(AlbumEntity album, Integer rating, String review);
}
