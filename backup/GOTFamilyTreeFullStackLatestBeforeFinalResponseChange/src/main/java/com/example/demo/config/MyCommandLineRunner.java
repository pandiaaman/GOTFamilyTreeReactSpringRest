package com.example.demo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.example.demo.dto.CharacterRequest;
import com.example.demo.models.Characters;
import com.example.demo.repositories.CharacterRepository;
import com.example.demo.services.CharacterServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MyCommandLineRunner implements CommandLineRunner{

//	private final String JSON_FILE = "/json/characters.json";
	
	@Autowired
	private CharacterRepository charRepo;
	
	@Autowired
	private CharacterServiceImpl charService;
	
	@Override
	public void run(String... args) throws Exception {
		log.info("adding json file data...");

		if(this.charRepo.findAll().size() == 0) {
			log.info("Since currently NO data is present, we are adding json data ... ");
			
			try {
				TypeReference<List<CharacterRequest>> charactersRef = new TypeReference<List<CharacterRequest>>() {};
				
				InputStream inStream = TypeReference.class.getResourceAsStream("/json/characters.json");
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				
				List<CharacterRequest> charactersFromJSON = new ArrayList<>();
				
				try {
					charactersFromJSON = mapper.readValue(inStream, charactersRef);
				}catch(IOException e) {
					log.error("exception finding the file!!!");
					e.printStackTrace();
				}
				if(charactersFromJSON!=null && !charactersFromJSON.isEmpty()) {
					log.info("grabbed data from JSON file into a list of characters...");
					
					
//					List<Characters> charactersList = new ArrayList<Characters>(); 
//					
//					charactersFromJSON.forEach(character ->
//							charactersList.add(Characters.builder()
//							.characterName(character.getCharacterName())
//							.houseName(character.getHouseName())
//							.marriedEngaged(character.getMarriedEngaged())
//							.parentOf(character.getParentOf())
//							.parents(character.getParents())
//							.siblings(character.getSiblings())
//							.isFavourite(character.isFavourite())
//							.characterImageThumb(character.getCharacterImageThumb())
//							.characterImageFull(character.getCharacterImageFull())
//							.characterLink(character.getCharacterLink())
//							.actorName(character.getActorName())
//							.actorLink(character.getActorLink())
//							.nickname(character.getNickname())
//							.royal(character.isRoyal())
//							.killed(character.getKilled())
//							.killedBy(character.getKilledBy())
//							.build()));
//					
//					log.info("saving the data now...");
//					
//					this.charRepo.saveAll(charactersList);
					
					if(this.charService.addCharactersUsingJSONdata(charactersFromJSON)) {
						log.info("data saved");
					}
				}	
			}catch(Exception e) {
				log.error("exception while adding json data...");
				e.printStackTrace();
			}
		}
		else {
			log.warn("NO DATA FROM JSON HAS BEEN ADDED SINCE DATA IS ALREADY PRESENT IN DB");
		}
		
	}

}
