package pl.olekky.projekt1.controller;

import org.springframework.web.bind.annotation.RestController;

import pl.olekky.projekt1.model.Player;
import pl.olekky.projekt1.repository.PlayerRepository;
import pl.olekky.projekt1.repository.exception.DatabaseException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

	@Autowired
	PlayerRepository playerRepository;
	
    @GetMapping("/")
    public List<Player> index() throws DatabaseException {
        //playerRepository.intializedPlayerTable();
    	List<Player> playerList = new ArrayList<Player>();
    	playerList = playerRepository.getPlayers();

        return playerList;
    }

}