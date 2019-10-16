package com.eau.codered.coderedshell.config;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import com.eau.codered.coderedshell.entities.HashtagRankingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        modelMapper.typeMap(HashtagRankingEntity.class, DraftingRoomEntity.class)
                .addMappings(mapper -> {
                    mapper.skip(DraftingRoomEntity::setId);
                    mapper.skip(DraftingRoomEntity::setLeague);
                    mapper.skip(DraftingRoomEntity::setEspnRank);
                    mapper.skip(DraftingRoomEntity::setEspnAdp);
                    mapper.map(HashtagRankingEntity::getRank, DraftingRoomEntity::setHashtagRank);
                });

        return modelMapper;
    }
}
