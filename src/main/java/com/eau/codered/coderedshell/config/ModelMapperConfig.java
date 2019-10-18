package com.eau.codered.coderedshell.config;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import com.eau.codered.coderedshell.entities.HashtagRankingStatsEntity;
import com.eau.codered.coderedshell.entities.HashtagRankingZscoreEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        modelMapper.typeMap(HashtagRankingStatsEntity.class, DraftingRoomEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(DraftingRoomEntity::setId);
                    mapper.skip(DraftingRoomEntity::setLeague);
                    mapper.skip(DraftingRoomEntity::setEspnRank);
                    mapper.skip(DraftingRoomEntity::setEspnAdp);
                    mapper.map(HashtagRankingStatsEntity::getRank, DraftingRoomEntity::setHashtagRank);
                });

        modelMapper.typeMap(HashtagRankingZscoreEntity.class, DraftingRoomEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(DraftingRoomEntity::setId);
                    mapper.skip(DraftingRoomEntity::setLeague);
                    mapper.skip(DraftingRoomEntity::setEspnRank);
                    mapper.skip(DraftingRoomEntity::setEspnAdp);
                    mapper.skip(DraftingRoomEntity::setHashtagRank);
                    mapper.skip(DraftingRoomEntity::setPos);
                    mapper.skip(DraftingRoomEntity::setGp);
                    mapper.skip(DraftingRoomEntity::setMpg);
                    mapper.skip(DraftingRoomEntity::setLastModified);
                });

        return modelMapper;
    }
}
