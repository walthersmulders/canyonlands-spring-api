package com.walthersmulders.persistence.repository.tvseries;

import com.walthersmulders.persistence.entity.tvseries.TvSeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TvSeriesRepository extends JpaRepository<TvSeriesEntity, UUID> {

}
