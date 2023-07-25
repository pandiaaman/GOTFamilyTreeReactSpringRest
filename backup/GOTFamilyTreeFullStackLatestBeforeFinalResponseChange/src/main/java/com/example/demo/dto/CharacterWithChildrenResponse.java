package com.example.demo.dto;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CharacterWithChildrenResponse {

private String characterId;
	
	
	private String characterName;
	
	private String houseName;
	
	private List<String> parents;
	
	private List<String> siblings;
	
	private List<CharacterWithChildrenResponse> parentOf;
	
	private List<String> marriedEngaged;
	
	private boolean isFavourite;
	
	private String characterImageThumb;
	
	private String characterImageFull;
	
	private String characterLink;
	
	private String actorName;
	
	private String actorLink;
	
	private String nickname;
	
	@ColumnDefault("false")
	private boolean royal;
	
	private List<String> killed;
	
	private List<String> killedBy;
	
	
	//getter
	@JsonProperty("name")
	public String getCharacterName() {
		return characterName;
	}
	
	@JsonProperty("children")
	public List<CharacterWithChildrenResponse> getParentOf(){
		return parentOf;
	}
	
}
