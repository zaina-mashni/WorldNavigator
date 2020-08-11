package com.WorldNavigator.repositories;

import com.WorldNavigator.model.PlayerModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface PlayerRepository extends CrudRepository<PlayerModel, Integer> {

    Optional<PlayerModel> findByUsernameIgnoreCase(String username);
}

