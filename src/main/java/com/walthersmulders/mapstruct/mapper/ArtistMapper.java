package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.album.Album;
import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.mapstruct.dto.artist.ArtistUpsert;
import com.walthersmulders.mapstruct.dto.artist.ArtistWithAlbums;
import com.walthersmulders.persistence.entity.music.ArtistAlbumEntity;
import com.walthersmulders.persistence.entity.music.ArtistEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtistMapper {
    Artist entityToArtist(ArtistEntity entity);

    @Mapping(target = "albums", source = "entity", qualifiedByName = "artistAlbumListToAlbumList")
    ArtistWithAlbums entityToArtistWithAlbums(ArtistEntity entity);

    ArtistEntity artistUpsertToEntity(ArtistUpsert artistUpsert);

    @InheritConfiguration
    ArtistEntity artistEntityUpdateMerge(
            @MappingTarget ArtistEntity artistEntity,
            ArtistUpsert artistUpsert
    );

    @Named(value = "artistAlbumListToAlbumList")
    default List<Album> artistAlbumListToAlbumList(ArtistEntity entity) {
        AlbumMapperImpl albumMapper = new AlbumMapperImpl();

        List<Album> list = new ArrayList<>();

        entity.getAlbums()
              .stream()
              .map(ArtistAlbumEntity::getAlbum)
              .forEach(album -> list.add(albumMapper.entityToAlbum(album)));

        return list;
    }
}
