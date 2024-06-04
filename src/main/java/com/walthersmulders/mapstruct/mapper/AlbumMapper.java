package com.walthersmulders.mapstruct.mapper;

import com.walthersmulders.mapstruct.dto.album.Album;
import com.walthersmulders.mapstruct.dto.album.AlbumUpsert;
import com.walthersmulders.mapstruct.dto.album.AlbumWithLinks;
import com.walthersmulders.mapstruct.dto.artist.Artist;
import com.walthersmulders.persistence.entity.music.AlbumEntity;
import com.walthersmulders.persistence.entity.music.ArtistAlbumEntity;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumMapper {
    @Mapping(target = "artists", source = "entity", qualifiedByName = "artistAlbumListToAlbumList")
    AlbumWithLinks entityToAlbumWithLinks(AlbumEntity entity);

    AlbumEntity albumUpsertToEntity(AlbumUpsert albumUpsert);

    Album entityToAlbum(AlbumEntity entity);

    @InheritConfiguration
    AlbumEntity albumEntityUpdateMerge(
            @MappingTarget AlbumEntity entity,
            AlbumUpsert albumUpsert
    );

    @Named(value = "artistAlbumListToAlbumList")
    default List<Artist> artistAlbumListToAlbumList(AlbumEntity entity) {
        ArtistMapperImpl artistMapper = new ArtistMapperImpl();

        List<Artist> list = new ArrayList<>();

        entity.getArtists()
              .stream()
              .map(ArtistAlbumEntity::getArtist)
              .forEach(artist -> list.add(artistMapper.entityToArtist(artist)));

        return list;
    }
}
